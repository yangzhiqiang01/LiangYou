package com.inhim.pj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.LoginEntity;
import com.inhim.pj.entity.SMSResult;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Request;

public class ReplacePhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_mobile, ed_password;
    private TextView tv_encounter_problem;
    private Button btn_login, btn_gecode;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_phone);
        gson = new Gson();
        initView();
    }

    private void initView() {
        ed_mobile = findViewById(R.id.ed_mobile);
        ed_password = findViewById(R.id.ed_password);
        tv_encounter_problem = findViewById(R.id.tv_encounter_problem);
        btn_login = findViewById(R.id.btn_login);
        tv_encounter_problem.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_gecode = findViewById(R.id.btn_gecode);
        btn_gecode.setOnClickListener(this);
    }

    private void sendSMS() {
        if (ed_mobile.getText().toString().equals("")) {
            BToast.showText("请输入手机号码 ", Toast.LENGTH_LONG, false);
            return;
        }
        timer.start();
        String examUrl = Urls.sendSMS(ed_mobile.getText().toString());
        MyOkHttpClient.getInstance().asyncGet(examUrl, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(Request request, String results) {
                SMSResult smsResult = gson.fromJson(results, SMSResult.class);
                if (smsResult.getMsg().equals("success")) {
                } else {
                    btn_gecode.setEnabled(true);
                    btn_gecode.setTextAppearance(R.style.btn_getcode);
                    btn_gecode.setText("获取验证码");
                    BToast.showText(smsResult.getMsg(), Toast.LENGTH_LONG, false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    CountDownTimer timer = new CountDownTimer(50000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_gecode.setEnabled(false);
            btn_gecode.setBackgroundResource(R.drawable.backgroud_button_getcode2);
            SpannableString spannableString = new SpannableString(
                    millisUntilFinished / 1000 + "秒后再获取");
            //设置颜色
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0079D7")),
                    0, spannableString.length() - 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置字体大小，true表示前面的字体大小20单位为dip
            //spannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置链接
            //spannableString.setSpan(new URLSpan("www.baidu.com"), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置字体，BOLD为粗体
            btn_gecode.setText(spannableString);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onFinish() {
            btn_gecode.setEnabled(true);
            btn_gecode.setTextAppearance(R.style.btn_getcode);
            btn_gecode.setText("获取验证码");
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_encounter_problem:
                break;
            case R.id.btn_login:
                updateMobile();
                break;
            case R.id.btn_gecode:
                sendSMS();
                break;
            case R.id.tv_agreement:
                Intent intent = new Intent(ReplacePhoneActivity.this, AgreementActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void updateMobile() {
        if (ed_password.getText().toString().equals("")) {
            BToast.showText("请输入验证码 ", Toast.LENGTH_LONG, false);
            return;
        }
        HashMap build = new HashMap();
        MyOkHttpClient.getInstance().asyncPut(Urls.updateMobile(ed_mobile.getText().toString(), ed_password.getText().toString()),

                build, new MyOkHttpClient.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {

                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 0) {
                                BToast.showText("更改成功", true);
                                finish();
                                /*Intent intent=new Intent(ReplacePhoneActivity.this,LoginActivity.class);
                                startActivity(intent);*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
