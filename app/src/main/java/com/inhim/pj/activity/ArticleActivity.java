package com.inhim.pj.activity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.ReaderInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.utils.WXShareUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.CustomRoundAngleImageView;
import com.inhim.pj.view.MyScrollView;
import com.inhim.pj.view.NoScrollWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

public class ArticleActivity extends BaseActivity {
    private TextView textview1,textview2,textview3,textview4,textview;
    private CustomRoundAngleImageView custImageview;
    private ImageView iv_back,iv_share;
    private CheckBox checkbox;
    private ReaderInfo.Reader readerInfo;
    private Gson gson;
    private NoScrollWebView webView;
    String content;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    private String title ;
    private String description;

    @Override
    public Object offerLayout() {
        return R.layout.activity_article;
    }
    @Override
    public void onBindView() {
        hideActionBar();
        gson=new Gson();
        initView();
        getReaderInfo(getIntent().getIntExtra("ReaderId", 0));
    }

    @Override
    public void destory() {

    }

    private void initView() {
        checkbox = findViewById(R.id.checkbox);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading("收藏中");
                MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
                myOkHttpClient.asyncJsonPost(Urls.collectionReader(readerInfo.getReaderId()), new HashMap(),
                        new MyOkHttpClient.HttpCallBack() {
                            @Override
                            public void onError(Request request, IOException e) {
                                hideLoading();
                                if(checkbox.isChecked()){
                                    checkbox.setChecked(false);
                                }else{
                                    checkbox.setChecked(true);
                                }
                            }

                            @Override
                            public void onSuccess(Request request, String result) {
                                hideLoading();
                                try {
                                    JSONObject jsonObject=new JSONObject(result);
                                    if(jsonObject.getInt("code")!=0){
                                        BToast.showText(jsonObject.getString("msg"),false);
                                        //收藏失败时将按钮状态还原
                                        if(checkbox.isChecked()){
                                            checkbox.setChecked(false);
                                        }else{
                                            checkbox.setChecked(true);
                                        }
                                    }else{
                                        if(checkbox.isChecked()){
                                            BToast.showText("收藏成功",true);
                                        }else{
                                            BToast.showText("已取消收藏",true);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        });
        textview1 = findViewById(R.id.textview1);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        textview4 = findViewById(R.id.textview4);
        custImageview = findViewById(R.id.custImageview);
        webView=findViewById(R.id.webView);
        initWebview();
        /*webView.getSettings().setJavaScriptEnabled(true);
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);*/
        iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXShareUtils.show(ArticleActivity.this,Urls.shareH5(readerInfo.getReaderId()),title,description);
            }
        });
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textview=findViewById(R.id.textview);
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebview() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //启用应用缓存
        webSettings.setAppCacheEnabled(false);
        webSettings.setDatabaseEnabled(false);
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        webSettings.setDomStorageEnabled(true);
        //适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //解决android7.0以后版本加载异常问题
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                // 这个方法在6.0才出现
                int statusCode = errorResponse.getStatusCode();
                if (404 == statusCode || 500 == statusCode) {
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //接受证书
                handler.proceed();
            }
        });
    }
    private void getReaderInfo(int readerId) {
        showLoading("加载中");
        MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
        myOkHttpClient.asyncGet(Urls.getReaderInfo(readerId, PrefUtils.getString("token", "")), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                hideLoading();
                BToast.showText("网络错误",false);
            }

            @Override
            public void onSuccess(Request request, String result) {
                hideLoading();
                ReaderInfo readerInfos = gson.fromJson(result, ReaderInfo.class);
                if(readerInfos.getCode()==0){
                    readerInfo=readerInfos.getReader();
                    Glide.with(ArticleActivity.this).load(readerInfo.getCover()).into(custImageview);
                    title=readerInfo.getTitle() ;
                    description=readerInfo.getSynopsis();
                    if(description!=null&&"".equals(description)){
                        textview.setVisibility(View.VISIBLE);
                        textview.setText(description);
                    }
                    checkbox.setChecked(readerInfo.getCollectionStatus());
                    textview1.setText(readerInfo.getTitle());
                    textview2.setText("类别："+readerInfo.getReaderTypeText());
                    textview3.setText(String.valueOf(readerInfo.getReadAmount()));
                    textview4.setText(readerInfo.getTimeText());
                    content=readerInfo.getContent();
                    if (content == null || content.length() == 0) {
                        content = "暂无内容";
                    }
                    webView.loadDataWithBaseURL(null, content, mimeType, encoding, null);
                    webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                }else{
                    BToast.showText(readerInfos.getMsg(),false);
                }
            }
        });
    }
}
