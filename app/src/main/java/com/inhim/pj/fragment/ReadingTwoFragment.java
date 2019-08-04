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
import com.inhim.pj.entity.TitleAndSize;
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

import okhttp3.Request;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ReadingTwoFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ReadingTwoAdapter mAdapter;
    private BannerList bannerList;
    private Gson gson;
    private List homeList;
    private ReaderStyle.ReaderStyleValue readerStyleValue;
    private SmartRefreshLayout home_SwipeRefreshLayout;
    private int mPageNum = 1;
    private Boolean refresh = true;
    private ReaderStyle rederType;
    private LoadingView loadingView;
    private String TAG="ReadingTwoFragment";
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
                mPageNum++;
                refresh=false;
                getReaderList(rederType.getList().get(0));
            }
        });
        /**
         * 设置不同的头部、底部样式
         */
        home_SwipeRefreshLayout.setRefreshHeader( new ClassicsHeader(getActivity()));
        home_SwipeRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
    }

    private void initAdapter() {
        mAdapter = new ReadingTwoAdapter(homeList,getActivity(),TAG);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getReaderStyle(){
        MyOkHttpClient.getInstance().asyncGetNoToken(Urls.getReaderStyle("special"), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                loadingView.hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                rederType=gson.fromJson(result,ReaderStyle.class);
                if(rederType.getCode()==0){
                    if(rederType.getList().size()>0){
                        homeList.add("系列专题");
                        homeList.add(rederType);
                        getReaderList(rederType.getList().get(0));
                    }else{
                        loadingView.hideLoading();
                    }
                }else{
                    loadingView.hideLoading();
                }
            }
        });
    }

    private void getReaderList(final ReaderStyle.List rederType) {
        /** {
         * 获取系列专题文章
         }*/
        HashMap postMap = new HashMap();
        postMap.put("readerStyleValueId", rederType.getReaderStyleValue().getReaderStyleValueId());
        postMap.put("readerStyleId", rederType.getReaderStyleValue().getReaderStyleId());
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(Urls.getReaderList(mPageNum, 10,"desc"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                loadingView.hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                loadingView.hideLoading();
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0&&readerList.getPage().getList().size()>0) {
                    homeList.addAll(readerList.getPage().getList());
                } else if(readerList.getCode() != 0){
                    BToast.showText(readerList.getMsg(), false);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getReaderList() {
        /** {
         获取每日推荐文章
         }*/
        HashMap postMap = new HashMap();
         postMap.put("readerStyleValueId", readerStyleValue.getReaderStyleValueId());
        postMap.put("readerStyleId", readerStyleValue.getReaderStyleId());
        //postMap.put("type", readerStyleValue.getType());
        //1: 推荐 0：不推荐
        postMap.put("intRecommend",1);
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(Urls.getReaderList(1, 10,"desc"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                getReaderStyle();
            }

            @Override
            public void onSuccess(Request request, String result) {
                Gson gson = new Gson();
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0&&readerList.getPage().getList().size()>0) {
                    homeList.add("每日推荐");
                    homeList.addAll(readerList.getPage().getList());
                } else if(readerList.getCode() != 0){
                    BToast.showText(readerList.getMsg(), false);
                }
                getReaderStyle();
            }
        });
    }

    private void getBannerList() {
        loadingView=new LoadingView();
        loadingView.showLoading("加载中",getActivity());
        MyOkHttpClient.getInstance().asyncGetNoToken(Urls.getBannerList(2), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                getReaderList();
            }

            @Override
            public void onSuccess(Request request, String result) {
                bannerList = gson.fromJson(result, BannerList.class);
                if(refresh){
                    homeList.clear();
                }
                if (bannerList.getCode() == 0&&bannerList.getData().size()>0) {
                    homeList.add(bannerList);
                } else if(bannerList.getCode() != 0){
                    BToast.showText(bannerList.getMsg(), false);
                }
                getReaderList();
            }
        });
    }

}
