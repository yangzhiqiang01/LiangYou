package com.inhim.pj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ProjectListAdapter;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;

import org.yczbj.ycrefreshviewlib.inter.OnItemClickListener;
import org.yczbj.ycrefreshviewlib.inter.OnLoadMoreListener;
import org.yczbj.ycrefreshviewlib.view.YCRefreshView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Request;

public class ChapterActivity extends BaseActivity {
    private YCRefreshView ycRefreshView;
    private ProjectListAdapter mAdapter;
    private int readerTypeId;
    private int mPageNum = 1;
    private Boolean refresh = true;
    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ycRefreshView = findViewById(R.id.ycRefreshView);
        readerTypeId = getIntent().getIntExtra("readerTypeId", 0);
        initAdapter();
        getReaderList();
    }

    private void initAdapter() {
        mAdapter = new ProjectListAdapter(ChapterActivity.this);
        ycRefreshView.setLayoutManager(new LinearLayoutManager(ChapterActivity.this));
        ycRefreshView.setAdapter(mAdapter);
        //设置上拉加载更多时布局，以及监听事件
        mAdapter.setMore(R.layout.view_more, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //可以做请求下一页操作
                mPageNum++;
                refresh=false;
                getReaderList();
            }
        });
        ycRefreshView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum= 1;
                refresh=true;
                getReaderList();
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ReaderList.List reader = mAdapter.getAllData().get(position);
                Intent intent;
                if (reader.getType().equals("2") || reader.getType().equals("3")) {
                    intent = new Intent(ChapterActivity.this, VideoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                } else {
                    intent = new Intent(ChapterActivity.this, ArticleActivity.class);
                }
                intent.putExtra("ReaderId", reader.getReaderId());
                startActivity(intent);
            }
        });
    }

    private void getReaderList() {
        showLoading("加载中");
        HashMap postMap= new HashMap<>();
        postMap.put("readerTypeId", String.valueOf(readerTypeId));
        String url = Urls.getReaderList(0, 20, "desc");
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(url, postMap,
                new MyOkHttpClient.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        hideLoading();
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        hideLoading();
                        Gson gson = new Gson();
                        ReaderList readerLists = gson.fromJson(result, ReaderList.class);
                        if (readerLists.getCode() == 0) {
                            totalPage=readerLists.getPage().getTotalPage();
                            if(refresh){
                                mAdapter.clear();
                            }
                            for (int i = 0; i < readerLists.getPage().getList().size(); i++) {
                                mAdapter.add(readerLists.getPage().getList().get(i));
                            }
                            if(mPageNum>=totalPage){
                                mAdapter.pauseMore();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}
