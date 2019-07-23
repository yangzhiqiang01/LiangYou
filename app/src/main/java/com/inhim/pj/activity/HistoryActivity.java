package com.inhim.pj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.CollectionAdapter;
import com.inhim.pj.entity.CollectionList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;

import org.yczbj.ycrefreshviewlib.inter.OnItemClickListener;
import org.yczbj.ycrefreshviewlib.inter.OnLoadMoreListener;
import org.yczbj.ycrefreshviewlib.view.YCRefreshView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

public class HistoryActivity extends AppCompatActivity {
    Gson gson=new Gson();
    private CollectionAdapter adapter;
    private YCRefreshView mRecyclerView;
    private int mPageNum=1;
    private Boolean refresh=true;
    private int totalPage;
    private TextView tv_clean;
    private List<CollectionList.List> historyList;
    private Map vipCollectionIdsMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tv_clean=findViewById(R.id.tv_clean);
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllHistory();
            }
        });
        mRecyclerView=findViewById(R.id.onceTask_member_ycView);
        historyList=new ArrayList<>();
        vipCollectionIdsMap=new HashMap();
        handleRecyclerViewInfo();
        getHistoryList();
    }
    private void deleteAllHistory() {
        FormBody.Builder formBody=new FormBody.Builder();
        MyOkHttpClient.getInstance().doDelete(Urls.deleteAllHistory, formBody.build(), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                Log.e("result",result);
            }
        });
    }
    private void getHistoryList() {
        MyOkHttpClient.getInstance().asyncGet(Urls.browsingHistory(mPageNum, 10), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                CollectionList historyEntity = gson.fromJson(result, CollectionList.class);
                if (historyEntity.getCode() == 0) {
                    totalPage=historyEntity.getPage().getTotalPage();
                    if(refresh){
                        adapter.clear();
                        historyList.clear();
                    }
                    if(mPageNum>=totalPage){
                        adapter.pauseMore();
                    }
                    for(int i=0;i<historyEntity.getPage().getList().size();i++){
                        adapter.add(historyEntity.getPage().getList().get(i));
                    }
                    historyList.addAll(historyEntity.getPage().getList());
                    adapter.notifyDataSetChanged();
                } else {
                    BToast.showText(historyEntity.getMsg(), false);
                }
            }
        });
    }
    private void handleRecyclerViewInfo() {
        adapter = new CollectionAdapter(HistoryActivity.this);
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setEmptyView(R.layout.empty_view_layout);
        mRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum= 1;
                refresh=true;
                getHistoryList();
            }
        });

        //设置上拉加载更多时布局，以及监听事件
        adapter.setMore(R.layout.view_more, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //可以做请求下一页操作
                mPageNum++;
                refresh=false;
                getHistoryList();
            }
        });
        /*adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CollectionList.List history=adapter.getAllData().get(position);
                Intent intent=new Intent(HistoryActivity.this, VideoActivity.class);
                intent.putExtra("ReaderId",history.getReaderId());
                startActivity(intent);
            }
        });*/
    }
}
