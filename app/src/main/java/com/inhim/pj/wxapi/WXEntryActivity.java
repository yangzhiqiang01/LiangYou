package com.inhim.pj.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.inhim.pj.R;
import com.inhim.pj.activity.LoginActivity;
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.entity.WXAccessTokenEntity;
import com.inhim.pj.entity.WXBaseRespEntity;
import com.inhim.pj.entity.WXUserInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI wxapi;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        wxapi = WXAPIFactory.createWXAPI(this, MyApplication.appID);
        wxapi.handleIntent(getIntent(), this);
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
                        Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
                        intent.putExtra("code", entity.getCode());
                        WXEntryActivity.this.setResult(0, intent);
                        startActivity(intent);
                        finish();
                        /*OkHttpUtils.get().url("https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code")
                                .addParams("appid",MyApplication.appID)
                                .addParams("secret",MyApplication.AppSecret)
                                .addParams("code",entity.getCode())
                                .addParams("grant_type","authorization_code")
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(okhttp3.Call call, Exception e, int id) {
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        WXAccessTokenEntity accessTokenEntity = JSON.parseObject(response,WXAccessTokenEntity.class);
                                        if(accessTokenEntity!=null){
                                            getUserInfo(accessTokenEntity);
                                        }else {
                                        }
                                    }
                                });*/
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
