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
import android.view.LayoutInflater;
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
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.entity.LoginEntity;
import com.inhim.pj.entity.SMSResult;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.CenterDialog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_mobile, ed_password;
    private TextView tv_encounter_problem, tv_agreement;
    private ImageView iv_weixin, iv_back;
    private Button btn_login, btn_gecode;
    private Gson gson;
    private CheckBox cb_save_pw;
    private boolean isOk;
    private CenterDialog centerDialog;
    /**
     * 微信登录相关
     */
    private IWXAPI api;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setImmersionStatusBar();
        MyApplication.instance.addActivity(this);
        gson = new Gson();
        if (PrefUtils.getBoolean("isLogin", false)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        initView();
    }

    private void initView() {
        ed_mobile = findViewById(R.id.ed_mobile);
        ed_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>=11){
                    btn_gecode.setEnabled(true);
                    //btn_gecode.setBackgroundResource(R.drawable.backgroud_button_getcode2);
                    btn_gecode.setTextColor(Color.parseColor("#0079D7"));
                }else{
                    btn_gecode.setEnabled(false);
                    //btn_gecode.setBackgroundResource(R.drawable.backgroud_button_getcode1);
                    btn_gecode.setTextAppearance(R.style.btn_getcode);
                    btn_gecode.setTextColor(Color.parseColor("#999999"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ed_password = findViewById(R.id.ed_password);
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
        tv_encounter_problem = findViewById(R.id.tv_encounter_problem);
        iv_weixin = findViewById(R.id.iv_weixin);
        btn_login = findViewById(R.id.btn_login);
        tv_encounter_problem.setOnClickListener(this);
        iv_weixin.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_login.setEnabled(false);
        btn_gecode = findViewById(R.id.btn_gecode);
        btn_gecode.setOnClickListener(this);
        btn_gecode.setEnabled(false);
        tv_agreement = findViewById(R.id.tv_agreement);
        tv_agreement.setOnClickListener(this);
        cb_save_pw = findViewById(R.id.cb_save_pw);
        cb_save_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOk = isChecked;
            }
        });
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    private void loGin() {
        String examUrl = Urls.onLogin;
        HashMap map = new HashMap();
        if (!isOk) {
            BToast.showText("请阅读并同意用户协议 ", Toast.LENGTH_LONG, false);
            return;
        }
        if (ed_mobile.getText().toString().equals("")) {
            BToast.showText("请输入手机号码 ", Toast.LENGTH_LONG, false);
            return;
        }
        if (ed_password.getText().toString().equals("")) {
            BToast.showText("请输入验证码 ", Toast.LENGTH_LONG, false);
            return;
        }
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
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (loginEntity.getCode() == 500) {
                    Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                    startActivity(intent);
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
                timer.onFinish();
                timer.cancel();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    CountDownTimer timer = new CountDownTimer(50000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_gecode.setEnabled(false);
            //btn_gecode.setBackgroundResource(R.drawable.backgroud_button_getcode2);
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
            case R.id.tv_encounter_problem:
                setCenterDiaolog();
                break;
            case R.id.iv_weixin:
                if (!isOk) {
                    BToast.showText("请阅读并同意用户协议 ", Toast.LENGTH_LONG, false);
                    return;
                }
                //通过WXAPIFactory工厂获取IWXApI的示例
                api = WXAPIFactory.createWXAPI(this, MyApplication.appID, true);
                //将应用的appid注册到微信
                api.registerApp(MyApplication.appID);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                req.state = "wechat_sdk_微信登录";
                api.sendReq(req);
                break;
            case R.id.btn_login:
                loGin();
                break;
            case R.id.btn_gecode:
                sendSMS();
                break;
            case R.id.tv_agreement:
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("Type", "agreement");
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            String code = data.getStringExtra("code");
            MyOkHttpClient.getInstance().asyncGet(Urls.wechatCallback(code), new MyOkHttpClient.HttpCallBack() {
                @Override
                public void onError(Request request, IOException e) {

                }

                @Override
                public void onSuccess(Request request, String result) {
                    Log.e("result", result);
                }
            });
        }
    }
*/
    private void setCenterDiaolog() {
        View outerView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_about, null);
        Button btn_ok = outerView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerDialog.dismiss();
            }
        });
        //防止弹出两个窗口
        if (centerDialog != null && centerDialog.isShowing()) {
            return;
        }

        centerDialog = new CenterDialog(LoginActivity.this, R.style.ActionSheetDialogBotoomStyle);
        //将布局设置给Dialog
        centerDialog.setContentView(outerView);
        centerDialog.show();//显示对话框
    }
}
