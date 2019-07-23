package com.inhim.pj.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.inhim.pj.R;
import com.inhim.pj.app.MyApplication;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

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
        switch (baseResp.errCode) {
            // 正确返回
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    // ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX是微信分享，api自带
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        // 只是做了简单的finish操作
                        finish();
                        break;
                    case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                        break;
                    case BaseResp.ErrCode.ERR_OK:
                        String code = ((SendAuth.Resp) baseResp).code;
                        //获取用户信息
                        //getAccessToken(code);
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                        finish();
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
                        Log.i("WXEntryActivity" , ">>>errCode = " + baseResp.errCode);
                        finish();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

   /* private void getAccessToken(String code) {
        //获取授权
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=")
                .append(MyApplication.APP_ID)
                .append("&secret=")
                .append(Constant.SECRET)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String access = null;
                String openId = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    access = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //获取个人信息
                String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId;
                OkHttpUtils.ResultCallback<String> reCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String responses) {

                        String nickName = null;
                        String sex = null;
                        String city = null;
                        String province = null;
                        String country = null;
                        String headimgurl = null;
                        try {
                            JSONObject jsonObject = new JSONObject(responses);

                            openid = jsonObject.getString("openid");
                            nickName = jsonObject.getString("nickname");
                            sex = jsonObject.getString("sex");
                            city = jsonObject.getString("city");
                            province = jsonObject.getString("province");
                            country = jsonObject.getString("country");
                            headimgurl = jsonObject.getString("headimgurl");
                            unionid = jsonObject.getString("unionid");
                            loadNetData(1, openid, nickName, sex, province,
                                    city, country, headimgurl, unionid);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                };
                OkHttpUtils.get(getUserInfo, reCallback);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        OkHttpUtils.get(loginUrl.toString(), resultCallback);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }*/
}
