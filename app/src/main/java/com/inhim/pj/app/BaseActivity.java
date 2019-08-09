package com.inhim.pj.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.activity.LoginActivity;
import com.inhim.pj.utils.FontUtils;
import com.inhim.pj.utils.StatusBarUtils;
import com.inhim.pj.view.TransparentProgressDialog;
@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 沉浸式状态栏
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setImmersionStatusBar() {
//        getSupportActionBar().hide();
//        View decorView = getWindow().getDecorView();
//        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        decorView.setSystemUiVisibility(option);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

    }

    private TransparentProgressDialog mProgressDialog;


    private View mConvertView;
    public AlertDialog.Builder alertDialog;


    /**
     * 加 final 不让子类 重写 onCreate，让其实现我们想要让他实现的方法
     */
    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConvertView = convertView();
        if (mConvertView == null) {
            // 传其他资源id 时的处理（只能传布局资源id ）
            throw new ClassCastException("view convert fail，check your resource id  be layout resource");
        } else {
            setContentView(mConvertView);
//            StatusBarUtils.setWindowStatusBarColor(this,R.color.color9999);
            onBindView();
            StatusBarUtils.setLightStatusBar(this, MyApplication.dark);
        }
//        EventBus.getDefault().register(this);
        alertDialog = new AlertDialog.Builder(this);

    }


    public void showLoading(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new TransparentProgressDialog(this,text);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        try{
            mProgressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideLoading() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                try{
                    mProgressDialog.dismiss();
                    mProgressDialog=null;
                }catch (Exception e){
                    e.printStackTrace();
                }
                mProgressDialog=null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    /**
     * 提供布局
     * 可以为 资源id
     * 可以为 view
     */
    public abstract Object offerLayout();

    /**
     * 处理view。 用户做自己的工作
     */
    public abstract void onBindView();

    public abstract void destory();

    /**
     * view的处理
     * 实现类 传布局资源ID 或者传view
     * 此处进行统一处理
     */
    private View convertView() {
        View view = null;
        if (offerLayout() instanceof Integer) {
            view = LayoutInflater.from(this).inflate((Integer) offerLayout(), null, false);
        } else if (offerLayout() instanceof View) {
            view = (View) offerLayout();
        } else {
            throw new IllegalArgumentException("offerLayout only be View or be Resource Id");
        }
        return view;
    }

    /**
     * ButterKnife的解绑
     */
    @Override
    public void onDestroy() {
        mConvertView = null; // call gc
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        destory();
    }

    /**
     * 全屏 且隐藏标题栏
     * （子类需要直接使用）
     */
    public void setNoTitleBarAndFullScreen() {
        // requestWindowFeature(Window.FEATURE_NO_TITLE); 此句必须在setContent之前
        getSupportActionBar().hide();// 标题栏的隐藏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //全屏处理
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 隐藏actionbar
     * */
    public void hideActionBar(){
        getSupportActionBar().hide();
    }


}
