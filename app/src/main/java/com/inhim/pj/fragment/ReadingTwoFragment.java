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
import com.inhim.pj.entity.BannerList;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.entity.TitleAndSize;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;

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
    }

    private void initAdapter() {
        mAdapter = new ReadingTwoAdapter(homeList,getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getReaderStyle(){
        MyOkHttpClient.getInstance().asyncGet(Urls.getReaderStyle("special"), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderStyle readerStyle=gson.fromJson(result,ReaderStyle.class);
                if(readerStyle.getCode()==0){
                    homeList.add("系列专题");
                    homeList.add(readerStyle.getList().get(0));
                    getReaderList(readerStyle.getList().get(0));
                }
                mAdapter.notifyDataSetChanged();
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
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(Urls.getReaderList(0, 20,"desc"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    homeList.addAll(readerList.getPage().getList());
                    mAdapter.notifyDataSetChanged();
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
            }
        });
    }

    private void getReaderList() {
        /** {
         获取每日推荐文章
         }*/
        HashMap postMap = new HashMap();
        postMap.put("readerStyleId", readerStyleValue.getReaderStyleId());
        postMap.put("readerStyleValueId", readerStyleValue.getReaderStyleValueId());
        //1: 推荐 0：不推荐
        postMap.put("intRecommend",1);
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(Urls.getReaderList(1, 10,"desc"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                Gson gson = new Gson();
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    homeList.add("每日推荐");
                    homeList.addAll(readerList.getPage().getList());
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
                getReaderStyle();
            }
        });
    }

    private void getBannerList() {
        MyOkHttpClient.getInstance().asyncGet(Urls.getBannerList(1), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                bannerList = gson.fromJson(result, BannerList.class);
                if (bannerList.getCode() == 0) {
                    homeList.add(bannerList);
                } else {
                    BToast.showText(bannerList.getMsg(), false);
                }
                getReaderList();
            }
        });
    }

}
