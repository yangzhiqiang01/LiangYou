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
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;

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
        String url=Urls.getReaderTypeList(1, 10, "2");
        MyOkHttpClient.getInstance().asyncJsonPost(url, postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

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
        String url=Urls.getReaderList(1, 10,"desc");
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(url,postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                Gson gson = new Gson();
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    homeList.add("视频推荐");
                    homeList.addAll(readerList.getPage().getList());
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
                mAdapter.notifyDataSetChanged();
                //getReaderStyle();
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
                getReaderTypeList();

            }
        });
    }
}