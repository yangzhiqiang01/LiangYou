package com.inhim.pj.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.AllCategoriesAdapter;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.base.ClassicsHeader;
import com.inhim.pj.base.ModeType;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Request;

public class AllCategoriesActivity extends BaseActivity implements AllCategoriesAdapter.onClickLinear {
    RecyclerView mRecyclerView;
    private AllCategoriesAdapter mAdapter;
    private Gson gson;
    private ArrayList categoriesList1, categoriesList2;
    private ArrayList categoriesList3, categoriesList4;
    SmartRefreshLayout home_SwipeRefreshLayout;
    private int mPageNum = 1;
    private Boolean refresh = true;


    @Override
    public Object offerLayout() {
        return R.layout.activity_all_categories;
    }
    @Override
    public void onBindView() {
        categoriesList1 = new ArrayList();
        categoriesList2 = new ArrayList();
        categoriesList3 = new ArrayList();
        categoriesList4 = new ArrayList();
        gson = new Gson();
        initView();
        initAdapter();
        getReaderTypeList();
    }

    @Override
    public void destory() {

    }

    private void initAdapter() {
        categoriesList1.add("全部分类");
        //categoriesList3.add("其他分类");
        mAdapter = new AllCategoriesAdapter(AllCategoriesActivity.this);
        mAdapter.setOnClickLinear(AllCategoriesActivity.this);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //得到每个type的值
                int type = mRecyclerView.getAdapter().getItemViewType(position);
                if (type == ModeType.TYPE_ONE || type == ModeType.TYPE_THREE) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        home_SwipeRefreshLayout = findViewById(R.id.home_SwipeRefreshLayout);
        home_SwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPageNum= 1;
                refresh=true;
                //请求数据
                home_SwipeRefreshLayout.finishRefresh();  //刷新完成
                getReaderTypeList();
            }
        });
        home_SwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPageNum++;
                refresh=false;
                //加载
                home_SwipeRefreshLayout.finishLoadmore();      //加载完成

                getReaderTypeList();
            }
        });
        /**
         * 设置不同的头部、底部样式
         */
        home_SwipeRefreshLayout.setRefreshHeader( new ClassicsHeader(this));
        home_SwipeRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
    }
    private void getReaderTypeList() {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        if (refresh){
            showLoading("加载中");
        }
        HashMap postMap = new HashMap();
        MyOkHttpClient.getInstance().asyncJsonPost(Urls.getReaderTypeList(mPageNum, 10, ""), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                hideLoading();
                ReaderTypeList readerTypeList = gson.fromJson(result, ReaderTypeList.class);
                if (readerTypeList.getCode() == 0) {
                    if (refresh) {
                        categoriesList2.clear();
                        //categoriesList4.clear();
                        categoriesList2.addAll(readerTypeList.getPage().getList());
                        //categoriesList4.addAll(readerTypeList.getPage().getList());
                    } else {
                        categoriesList2.addAll(readerTypeList.getPage().getList());
                        //categoriesList4.addAll(readerTypeList.getPage().getList());
                    }
                    mAdapter.addList(categoriesList1, categoriesList2, categoriesList3, categoriesList4);
                    mAdapter.notifyDataSetChanged();
                } else {
                    BToast.showText(readerTypeList.getMsg(), false);
                }
            }
        });
    }

    @Override
    public void callResult() {
        finish();
    }
}
