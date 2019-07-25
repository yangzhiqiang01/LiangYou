package com.inhim.pj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.inhim.pj.R;


/**
 * Created by hsmacmini on 2018/4/16.
 */

public class JIangyiFragment extends Fragment {
    WebView webView;
    String content;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    private RefreshReceiver refreshReceiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_jiangyi,null);
        content=getArguments().getString("content");
        initView(view);
        IntentFilter filter=new IntentFilter();
        filter.addAction("refresh.fragment.content");
        //注册广播接收
        refreshReceiver=new RefreshReceiver();
        if(getActivity()!=null){
            getActivity().registerReceiver(refreshReceiver,filter);
        }
        return view;

    }

    private void initView(View view){
        webView = view.findViewById(R.id.webView1);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshReceiver);
    }

    class RefreshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            content=intent.getStringExtra("content");
            if (content == null || content.length() == 0) {
                content = "暂无内容";
            }
            webView.loadDataWithBaseURL(null, content, mimeType, encoding, null);
        }
    }
}
