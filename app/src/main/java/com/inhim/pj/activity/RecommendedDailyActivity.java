package com.inhim.pj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ChapterAdapter;
import com.inhim.pj.adapter.RecommendedDailyAdapter;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.listener.RecyclerViewClickListener;
import com.inhim.pj.view.BToast;

import org.yczbj.ycrefreshviewlib.inter.OnItemClickListener;
import org.yczbj.ycrefreshviewlib.inter.OnLoadMoreListener;
import org.yczbj.ycrefreshviewlib.view.YCRefreshView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Request;

public class RecommendedDailyActivity extends BaseActivity {
    private YCRefreshView mRecyclerView;
    private Gson gson;
    private List<ReaderList.List> typeList;
    private RecommendedDailyAdapter mAdapter;
    int mPageNum=1;
    Boolean refresh=true;
    private int ReaderStyleId,ReaderStyleValueId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_daily);
        typeList=new ArrayList<>();
        ReaderStyleValueId=getIntent().getIntExtra("ReaderStyleValueId",0);
        ReaderStyleId=getIntent().getIntExtra("ReaderStyleId",0);
        gson=new Gson();
        initView();
        initAdapter();
        getReaderList();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerview);
    }

    private void getReaderList() {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        Headers.Builder headers=new Headers.Builder();
        headers.add("readerTypeId",String.valueOf(ReaderStyleId));
        headers.add("readerStyleValueId",String.valueOf(ReaderStyleValueId));
        MyOkHttpClient.getInstance().asyncPost(Urls.getReaderList(mPageNum, 10,"desc"), headers, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    if(refresh){
                        mAdapter.clear();
                        typeList.clear();
                    }
                    typeList.addAll(readerList.getPage().getList());
                    for(int i=0;i<readerList.getPage().getList().size();i++){
                        mAdapter.add(readerList.getPage().getList().get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
            }
        });
    }

    private void initAdapter() {
        typeList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RecommendedDailyAdapter(this);
        mRecyclerView.setEmptyView(R.layout.empty_view_layout);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum= 1;
                refresh=true;
                getReaderList();
            }
        });

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
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(RecommendedDailyActivity.this,VideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("ReaderId",typeList.get(position).getReaderId());
                startActivity(intent);
            }
        });
    }
}
