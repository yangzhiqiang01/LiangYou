package com.inhim.pj.activity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.utils.StatusBarUtils;

public class BannerWebViewActivity extends BaseActivity {
    WebView webView;
    String content;
    private String url;
    private ImageView iv_back;
    private TextView tvCourse;


    @Override
    public Object offerLayout() {
        return R.layout.activity_web_view;
    }
    @Override
    public void onBindView() {
        hideActionBar();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        url=getIntent().getStringExtra("url");
        initView();
    }

    @Override
    public void destory() {

    }

    private void initView(){
        tvCourse=findViewById(R.id.tvCourse);
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
        webView.loadUrl(url);
        //webView.loadDataWithBaseURL(null, content, mimeType, encoding, null);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }
}
