package com.inhim.pj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.base.ClassicsHeader;
import com.inhim.pj.entity.BannerList;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.LoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ReadingTwoAdapter mAdapter;
    private BannerList bannerList;
    private Gson gson;
    private ReaderStyle.ReaderStyleValue readerStyleValue;
    private List homeList;
    private SmartRefreshLayout home_SwipeRefreshLayout;
    private int mPageNum = 1;
    private Boolean refresh = true;
    private ReaderStyle.List rederType;
    private LoadingView loadingView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reading_two, container, false);
        Bundle bundle = getArguments();
        readerStyleValue = (ReaderStyle.ReaderStyleValue) bundle.getSerializable("result");
        gson = new Gson();
        initView(view);
        homeList = new ArrayList();
        initAdapter();
        getBannerList();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        home_SwipeRefreshLayout = view.findViewById(R.id.home_SwipeRefreshLayout);
        home_SwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPageNum = 1;
                refresh = true;
                //请求数据
                home_SwipeRefreshLayout.finishRefresh();  //刷新完成
                getBannerList();
            }
        });
        home_SwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //加载
                home_SwipeRefreshLayout.finishLoadmore();      //加载完成
                getReaderList();
            }
        });
        /**
         * 设置不同的头部、底部样式
         */
        home_SwipeRefreshLayout.setRefreshHeader( new ClassicsHeader(getActivity()));
        home_SwipeRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
    }

    private void initAdapter() {
        mAdapter = new ReadingTwoAdapter(homeList, getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getReaderTypeList() {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        HashMap postMap = new HashMap();
        String url=Urls.getReaderTypeList(mPageNum, 5, "2");
        MyOkHttpClient.getInstance().asyncJsonPost(url, postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                getReaderList();
            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderTypeList readerTypeList = gson.fromJson(result, ReaderTypeList.class);
                if (readerTypeList.getCode() == 0) {
                    homeList.add(readerTypeList);
                } else {
                    BToast.showText(readerTypeList.getMsg(), false);
                }
                getReaderList();
            }
        });
    }

    private void getReaderList() {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        HashMap postMap = new HashMap();
        postMap.put("readerStyleValueId", readerStyleValue.getReaderStyleValueId());
        postMap.put("readerStyleId", readerStyleValue.getReaderStyleId());
        //postMap.put("type", readerStyleValue.getType());
        //1: 推荐 0：不推荐
        postMap.put("intRecommend",1);
        String url=Urls.getReaderList(mPageNum, 10,"desc");
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(url,postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                loadingView.hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                mPageNum++;
                loadingView.hideLoading();
                Gson gson = new Gson();
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    if(refresh){
                        homeList.add("视频推荐");
                    }
                    homeList.addAll(readerList.getPage().getList());
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getBannerList() {
        loadingView=new LoadingView();
        loadingView.showLoading("加载中",getActivity());
        MyOkHttpClient.getInstance().asyncGet(Urls.getBannerList(3), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                getReaderTypeList();
            }

            @Override
            public void onSuccess(Request request, String result) {
                bannerList = gson.fromJson(result, BannerList.class);
                if(refresh){
                    refresh=false;
                    homeList.clear();
                    mPageNum=1;
                }
                if (bannerList.getCode() == 0) {
                    homeList.add(bannerList);
                } else {
                    BToast.showText(bannerList.getMsg(), false);
                }
                getReaderTypeList();

            }
        });
    }
}