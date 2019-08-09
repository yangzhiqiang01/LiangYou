package com.inhim.pj.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.SMSResult;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.StatusBarUtils;
import com.inhim.pj.view.BToast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_mobile,ed_password;
    private TextView tv_forget_password,tv_geSMS;
    private ImageView iv_weixin,iv_close;
    private Button btn_login;
    private Gson gson;

    @Override
    public Object offerLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void onBindView() {
        gson=new Gson();
        initView();
    }

    @Override
    public void destory() {

    }

    private void initView(){
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_password=findViewById(R.id.ed_password);
        tv_forget_password=findViewById(R.id.tv_forget_password);
        iv_weixin=findViewById(R.id.iv_weixin);
        btn_login=findViewById(R.id.btn_login);
        tv_forget_password.setOnClickListener(this);
        iv_weixin.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_geSMS=findViewById(R.id.tv_geSMS);
        tv_geSMS.setOnClickListener(this);
    }
    private void onRegister() {
        String examUrl = Urls.onRegister;
        FormBody.Builder formBody=new FormBody.Builder();
        formBody.add("mobile",ed_mobile.getText().toString());
        //formBody.add("password",ed_password.getText().toString());
        //formBody.add("openId",ed_mobile.getText().toString());
        formBody.add("validate",ed_password.getText().toString());
        MyOkHttpClient.getInstance().asyncPost(examUrl, formBody.build(),new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Request request, String results) {
                SMSResult smsResult=gson.fromJson(results,SMSResult.class);
                if(smsResult.getMsg().equals("success")){
                    BToast.showText("验证码发送成功 ", Toast.LENGTH_LONG, true);
                }

            }
        });
    }

    private void sendSMS() {
        String examUrl = Urls.sendSMS(ed_mobile.getText().toString());
        MyOkHttpClient.getInstance().asyncGetNoToken(examUrl, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Request request, String results) {
                SMSResult smsResult=gson.fromJson(results,SMSResult.class);
                if(smsResult.getMsg().equals("success")){
                    BToast.showText("验证码发送成功 ", Toast.LENGTH_LONG, true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_forget_password:
                break;
            case R.id.iv_weixin:
                break;
            case R.id.btn_login:
                onRegister();
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_geSMS:
                sendSMS();
                break;
            default:
                break;
        }
    }
}
