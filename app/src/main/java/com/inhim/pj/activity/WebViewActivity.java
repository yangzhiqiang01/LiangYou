package com.inhim.pj.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.AboutEntity;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;

import java.io.IOException;

import okhttp3.Request;

public class WebViewActivity extends BaseActivity {
    WebView webView;
    String content;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    private String Type;
    private ImageView iv_back;
    private TextView tvCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Type=getIntent().getStringExtra("Type");
        initView();
        getAppAbout();
    }

    private void getAppAbout() {
        showLoading("加载中");
        MyOkHttpClient myOkHttpClient=MyOkHttpClient.getInstance();
        myOkHttpClient.asyncGetNoToken(Urls.appAbout(Type), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                hideLoading();
                Gson gson=new Gson();
                AboutEntity aboutEntity=gson.fromJson(result,AboutEntity.class);
                if(aboutEntity.getCode()==0){
                    tvCourse.setText(aboutEntity.getAppAbout().getName());
                    content=aboutEntity.getAppAbout().getContent();
                }else{
                    BToast.showText(aboutEntity.getMsg(),false);
                }
            }
        });
    }

    private void initView(){
        tvCourse=findViewById(R.id.tvCourse);
        if(Type.equals("about")){
            tvCourse.setText("关于我们");
        }else if(Type.equals("agreement")) {
            tvCourse.setText("用户协议");
        }
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        if (content == null || content.length() == 0) {
            content = "暂无内容";
        }
        webView.loadDataWithBaseURL(null, content, mimeType, encoding, null);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }
}
