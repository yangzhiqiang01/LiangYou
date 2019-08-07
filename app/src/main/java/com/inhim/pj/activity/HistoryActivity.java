package com.inhim.pj.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.CollectionAdapter;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.CollectionList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.StatusBarUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.CenterDialog;

import org.json.JSONException;
import org.json.JSONObject;
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

public class HistoryActivity extends BaseActivity {
    Gson gson=new Gson();
    private CollectionAdapter adapter;
    private YCRefreshView mRecyclerView;
    private int mPageNum=1;
    private Boolean refresh=true;
    private int totalPage;
    private TextView tv_clean;
    private ImageView iv_back;
    private List<CollectionList.List> historyList;
    private Map vipCollectionIdsMap;
    private CenterDialog centerDialog;

    @Override
    public Object offerLayout() {
        return R.layout.activity_history;
    }

    @Override
    public void onBindView() {
        hideActionBar();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        tv_clean=findViewById(R.id.tv_clean);
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiaglog();
            }
        });
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerView=findViewById(R.id.onceTask_member_ycView);
        historyList=new ArrayList<>();
        vipCollectionIdsMap=new HashMap();
        handleRecyclerViewInfo();
        getHistoryList();
    }

    @Override
    public void destory() {

    }

    private void setDiaglog(){
        View outerView = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.dialog_deletes, null);
        Button btn_ok=outerView.findViewById(R.id.btn_ok);
        Button btn_cancel=outerView.findViewById(R.id.btn_cancel);
        TextView tv_title=outerView.findViewById(R.id.tv_title);
        tv_title.setText("是否确认清空？");
        btn_ok.setOnClickListener(v -> {
            centerDialog.dismiss();
            deleteAllHistory();
        });
        btn_cancel.setOnClickListener(v -> centerDialog.dismiss());
        //防止弹出两个窗口
        if (centerDialog !=null && centerDialog.isShowing()) {
            return;
        }

        centerDialog = new CenterDialog(HistoryActivity.this, R.style.ActionSheetDialogBotoomStyle);
        //将布局设置给Dialog
        centerDialog.setContentView(outerView);
        centerDialog.show();//显示对话框
    }
    private void deleteAllHistory() {
        showLoading("删除中");
        FormBody.Builder formBody=new FormBody.Builder();
        MyOkHttpClient.getInstance().doDelete(Urls.deleteAllHistory, formBody.build(), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                BToast.showText("删除失败");
                hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 0) {
                        getHistoryList();
                    } else {
                        BToast.showText(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getHistoryList() {
        showLoading("加载中");
        MyOkHttpClient.getInstance().asyncGet(Urls.browsingHistory(mPageNum, 10), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                BToast.showText("请求失败");
                hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                hideLoading();
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
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CollectionList.List data=adapter.getAllData().get(position);
                Intent intent;
                if(data.getStatus()!=null&&data.getStatus().equals("0")){

                }else{
                    if(data.getReaderEntity().getType().equals("2")){
                        intent=new Intent(HistoryActivity.this, VideoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    }else if(data.getReaderEntity().getType().equals("3")){
                        intent=new Intent(HistoryActivity.this, RadioActivity.class);
                    }else{
                        intent=new Intent(HistoryActivity.this, ArticleActivity.class);
                    }
                    intent.putExtra("ReaderId",data.getReaderEntity().getReaderId());
                    startActivity(intent);
                }
            }
        });
    }
}
