package com.inhim.pj.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.activity.BindPhoneActivity;
import com.inhim.pj.activity.HomeActivity;
import com.inhim.pj.activity.LoginActivity;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.entity.LoginEntity;
import com.inhim.pj.entity.WXAccessTokenEntity;
import com.inhim.pj.entity.WXBaseRespEntity;
import com.inhim.pj.entity.WXUserInfo;
import com.inhim.pj.entity.WeChatEntity;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.Request;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI wxapi;
    private Gson gson;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
    }

    @Override
    public Object offerLayout() {
        return R.layout.activity_wxentry;
    }

    @Override
    public void onBindView() {
        hideActionBar();
        MyApplication.instance.addActivity(this);
        gson=new Gson();
        wxapi = WXAPIFactory.createWXAPI(this, MyApplication.appID);
        wxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void destory() {

    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     */
    @Override
    public void onReq(BaseReq baseReq) {
        // 这里不作深究
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     * app发送消息给微信，处理返回消息的回调
     */
    @Override
    public void onResp(BaseResp baseResp) {
        WXBaseRespEntity entity = JSON.parseObject(JSON.toJSONString(baseResp), WXBaseRespEntity.class);
        String result = "";
        switch (baseResp.errCode) {
            // 正确返回
            case BaseResp.ErrCode.ERR_OK:
                Log.e("getType",baseResp.getType()+"");
                switch (baseResp.getType()) {
                    // ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX是微信分享，api自带
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        // 只是做了简单的finish操作
                        finish();
                        break;
                    case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                        break;
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        // 跳转首页或者其他操作
                        String wxUrl = Urls.wechatCallback(entity.getCode());
                        MyOkHttpClient.getInstance().asyncGet(wxUrl, new MyOkHttpClient.HttpCallBack() {
                            @Override
                            public void onError(Request request, IOException e) {

                            }

                            @Override
                            public void onSuccess(Request request, String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getJSONObject("map").getBoolean("bindVipUser")) {
                                        JSONObject data=jsonObject.getJSONObject("map").getJSONObject("data");
                                        PrefUtils.putLong("expire", data.getLong("expire"));
                                        PrefUtils.putString("token", data.getString("token"));
                                        PrefUtils.putBoolean("isLogin", true);
                                        Intent intent = new Intent(WXEntryActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        MyApplication.instance.finishAllActivity();
                                    }else{
                                        WeChatEntity weChatEntity = gson.fromJson(result, WeChatEntity.class);
                                        loGin(weChatEntity.getMap().getData().getOpenId());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_BAN:
                        /*result = "签名错误";
                        ViseLog.d("签名错误");*/
                        break;
                    default:
                        finish();
                        break;
                }
                break;
            default:
                // 错误返回
                switch (baseResp.getType()) {
                    // 微信分享
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        Log.i("WXEntryActivity", ">>>errCode = " + baseResp.errCode);
                        finish();
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    private void loGin(final String openId) {
        String examUrl = Urls.onLogin;
        HashMap map = new HashMap();
        map.put("openId", openId);
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(examUrl, map, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Request request, String results) {
                LoginEntity loginEntity = gson.fromJson(results, LoginEntity.class);
                if (loginEntity.getCode() == 0) {
                    PrefUtils.putLong("expire", loginEntity.getExpire());
                    PrefUtils.putString("token", loginEntity.getToken());
                    PrefUtils.putBoolean("isLogin", true);
                    Intent intent = new Intent(WXEntryActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (loginEntity.getCode() == 500) {
                    Intent intent = new Intent(WXEntryActivity.this, BindPhoneActivity.class);
                    intent.putExtra("openId",openId);
                    startActivity(intent);
                } else {
                    BToast.showText(loginEntity.getMsg(), Toast.LENGTH_LONG, false);
                }
            }
        });
    }

    /**
     * 获取个人信息
     *
     * @param accessTokenEntity
     */
    private void getUserInfo(WXAccessTokenEntity accessTokenEntity) {
        Headers.Builder headers = new Headers.Builder();
        headers.add("access_token", accessTokenEntity.getAccess_token());
        headers.add("openid", accessTokenEntity.getOpenid());
        MyOkHttpClient.getInstance().asyncGetAddHeader("https://api.weixin.qq.com/sns/userinfo",
                headers, new MyOkHttpClient.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {

                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        WXUserInfo wxResponse = JSON.parseObject(result, WXUserInfo.class);
                        String headUrl = wxResponse.getHeadimgurl();
                        //ViseLog.d("头像Url:"+headUrl);
                        //App.getShared().putString("headUrl",headUrl);
                        Intent intent = getIntent();
                        intent.putExtra("WXUserInfo", wxResponse);
                        WXEntryActivity.this.setResult(0, intent);
                        finish();
                    }
                });
    }

}
