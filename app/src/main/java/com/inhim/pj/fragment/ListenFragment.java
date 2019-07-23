package com.inhim.pj.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ListenAdapter;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.adapter.provider.ListenReaderTypeProvider;
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

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListenFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ListenAdapter mAdapter;
    private BannerList bannerList;
    private Gson gson;
    private ReaderStyle.ReaderStyleValue readerStyleValue;
    private List homeList;
    private int size;
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
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

    private void initView (View view){
        mRecyclerView = view.findViewById(R.id.recyclerview);
    }

    private void initAdapter () {
        mAdapter = new ListenAdapter(homeList,getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getReaderTypeList () {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        HashMap postMap = new HashMap();
        String url=Urls.getReaderTypeList(1, 10, "3");
        MyOkHttpClient.getInstance().asyncJsonPost(url, postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderTypeList readerTypeList = gson.fromJson(result, ReaderTypeList.class);
                if (readerTypeList.getCode() == 0) {
                    size=readerTypeList.getPage().getList().size();
                    for (int i=0;i<size;i++){
                        getReaderList(readerTypeList.getPage().getList().get(i),i);
                    }
                } else {
                    BToast.showText(readerTypeList.getMsg(), false);
                }
            }
        });
    }

    private void getReaderList (final ReaderTypeList.List readerTypeList, final int position) {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        HashMap postMap = new HashMap();
        postMap.put("readerTypeId", readerTypeList.getReaderTypeId());
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(Urls.getReaderList(1, 10,"desc"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                Gson gson = new Gson();
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    homeList.add(readerTypeList);
                    homeList.addAll(readerList.getPage().getList());
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
                if(position==size-1){
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getBannerList () {
        MyOkHttpClient.getInstance().asyncGet(Urls.getBannerList(4), new MyOkHttpClient.HttpCallBack() {
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
