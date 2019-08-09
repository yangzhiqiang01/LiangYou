package com.inhim.pj.activity;


import android.content.Intent;
import android.os.Handler;

import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.utils.Utils;

public class StartActivity extends BaseActivity {
    final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟3秒
    @Override
    public Object offerLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void onBindView() {
        if (Utils.isExistNetwork(this)) {
            new Handler().postDelayed(() -> {
                Intent intent=new Intent(StartActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }, SPLASH_DISPLAY_LENGHT);
        } else {
            Utils.displayToast(this, "请检查网络……");
        }
    }

    @Override
    public void destory() {

    }
}
