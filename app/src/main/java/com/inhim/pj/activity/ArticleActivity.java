package com.inhim.pj.activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.ReaderInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.GlideUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.CustomRoundAngleImageView;

import java.io.IOException;

import okhttp3.Request;

public class ArticleActivity extends BaseActivity {
    private TextView textview1,textview2,textview3,textview4;
    private CustomRoundAngleImageView custImageview;
    private ImageView iv_back,iv_share;
    private CheckBox checkbox;
    private ReaderInfo.Reader readerInfo;
    private Gson gson;
    private WebView webView;
    String content;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        gson=new Gson();
        initView();
        getReaderInfo(getIntent().getIntExtra("ReaderId", 0));
    }

    private void initView() {
        checkbox = findViewById(R.id.checkbox);
        textview1 = findViewById(R.id.textview1);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        textview4 = findViewById(R.id.textview4);
        custImageview = findViewById(R.id.custImageview);
        webView=findViewById(R.id.webView);
        iv_share = findViewById(R.id.iv_share);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getReaderInfo(int readerId) {
        MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
        myOkHttpClient.asyncGet(Urls.getReaderInfo(readerId, PrefUtils.getString("token", "")), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderInfo readerInfos = gson.fromJson(result, ReaderInfo.class);
                readerInfo=readerInfos.getReader();
                GlideUtils.displayFromUrl(readerInfo.getCover(),custImageview);
                checkbox.setChecked(readerInfo.getCollectionStatus());
                textview1.setText(readerInfo.getTitle());
                textview2.setText(readerInfo.getTagName());
                textview3.setText(String.valueOf(readerInfo.getReadAmount()));
                textview4.setText(readerInfo.getTimeText());
                content=readerInfo.getContent();
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
        });
    }
}
