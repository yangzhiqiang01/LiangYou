package com.inhim.pj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.entity.LoginEntity;
import com.inhim.pj.entity.SMSResult;
import com.inhim.pj.entity.WeChatEntity;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_mobile, ed_password;
    private ImageView iv_back;
    private Button btn_login, btn_gecode;
    private Gson gson;
    private String openId;
    /**
     * 微信登录相关
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        openId=getIntent().getStringExtra("openId");
        MyApplication.instance.addActivity(this);
        gson = new Gson();
        initView();
    }

    private void initView() {
        ed_mobile = findViewById(R.id.ed_mobile);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_gecode = findViewById(R.id.btn_gecode);
        btn_gecode.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        btn_login.setEnabled(false);
        btn_gecode.setEnabled(false);
        ed_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>=11){
                    btn_gecode.setEnabled(true);
                    btn_gecode.setBackgroundResource(R.drawable.backgroud_button_getcode2);
                    btn_gecode.setTextColor(Color.parseColor("#0079D7"));
                }else{
                    btn_gecode.setEnabled(false);
                    btn_gecode.setBackgroundResource(R.drawable.backgroud_button_getcode1);
                    btn_gecode.setTextAppearance(R.style.btn_getcode);
                    btn_gecode.setTextColor(Color.parseColor("#999999"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>=6){
                    btn_login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loGin(String openId) {
        String examUrl = Urls.onLogin;
        HashMap map = new HashMap();
        if (ed_mobile.getText().toString().equals("")) {
            BToast.showText("请输入手机号码 ", Toast.LENGTH_LONG, false);
            return;
        }
        if (ed_password.getText().toString().equals("")) {
            BToast.showText("请输入验证码 ", Toast.LENGTH_LONG, false);
            return;
        }
        map.put("openId", openId);
        map.put("mobile", ed_mobile.getText().toString());
        map.put("validate", ed_password.getText().toString());
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
                    Intent intent = new Intent(BindPhoneActivity.this, HomeActivity.class);
                    startActivity(intent);
                    MyApplication.instance.finishAllActivity();
                } else {
                    BToast.showText(loginEntity.getMsg(), Toast.LENGTH_LONG, false);
                }
            }
        });
    }

    private void sendSMS() {
        if (ed_mobile.getText().toString().equals("")) {
            BToast.showText("请输入手机号码 ", Toast.LENGTH_LONG, false);
            return;
        }
        timer.start();
        String examUrl = Urls.sendSMS(ed_mobile.getText().toString());
        MyOkHttpClient.getInstance().asyncGetNoToken(examUrl, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Request request, String results) {
                SMSResult smsResult = gson.fromJson(results, SMSResult.class);
                if (!smsResult.getMsg().equals("success")) {
                    BToast.showText(smsResult.getMsg(), Toast.LENGTH_LONG, false);
                    timer.onFinish();
                    timer.cancel();
                }else{
                    BToast.showText(String.valueOf(smsResult.getCode()), Toast.LENGTH_LONG, true);
                }
            }
        });
    }


    CountDownTimer timer = new CountDownTimer(50000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_gecode.setEnabled(false);
            btn_gecode.setBackgroundResource(R.drawable.backgroud_button_getcode2);
            SpannableString spannableString = new SpannableString(
                    millisUntilFinished / 1000 + "秒后再获取");
            //设置颜色
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")),
                    spannableString.length() - 4, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置字体大小，true表示前面的字体大小20单位为dip
            //spannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置链接
            //spannableString.setSpan(new URLSpan("www.baidu.com"), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置字体，BOLD为粗体
            btn_gecode.setText(spannableString);
        }

        @Override
        public void onFinish() {
            btn_gecode.setEnabled(true);
            btn_gecode.setText("获取验证码");
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loGin(openId);
                break;
            case R.id.btn_gecode:
                sendSMS();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

}
