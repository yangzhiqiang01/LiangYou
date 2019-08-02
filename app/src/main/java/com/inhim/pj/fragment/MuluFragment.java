package com.inhim.pj.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ReaderAttachmentAdapter;
import com.inhim.pj.entity.ReaderInfo;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderStyle;
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

import okhttp3.Request;


/**
 * Created by hsmacmini on 2018/4/16.
 */

public class MuluFragment extends Fragment{
    private YCRefreshView rcyclerView;
    private OnVideoLinear onVideoLinear;
    private int mPageNum=1;
    private Boolean refresh=true;
    private int totalPage;
    private ReaderAttachmentAdapter readerAttachmentAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mulu,null);
        //id= Variable.codeId;
        // 初始化控件
        rcyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcyclerView.setLayoutManager(linearLayoutManager);
        final int ReaderTypeId=getArguments().getInt("ReaderTypeId");
        int ReaderId=getArguments().getInt("ReaderId");
        readerAttachmentAdapter = new ReaderAttachmentAdapter(getActivity(),ReaderId);
        rcyclerView.setAdapter(readerAttachmentAdapter);
        readerAttachmentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ReaderList.List reader=readerAttachmentAdapter.getAllData().get(position);
                readerAttachmentAdapter.setSeclection(position);
                readerAttachmentAdapter.notifyDataSetChanged();
                onVideoLinear.setVidoUrl(reader.getReaderId());
            }
        });
        //设置上拉加载更多时布局，以及监听事件
        readerAttachmentAdapter.setMore(R.layout.view_more, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //可以做请求下一页操作
                mPageNum++;
                refresh=false;
                getReaderList(ReaderTypeId);
            }
        });
        rcyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum= 1;
                refresh=true;
                getReaderList(ReaderTypeId);
            }
        });
        getReaderList(ReaderTypeId);
        return view;
    }

    private void getReaderList(int ReaderTypeId) {
        /** {
         * 获取系列专题文章
         }*/
        HashMap postMap = new HashMap();
        postMap.put("readerTypeId", ReaderTypeId);
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(Urls.getReaderList(mPageNum, 10,"desc"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                Gson gson=new Gson();
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    totalPage=readerList.getPage().getTotalPage();
                    if(refresh){
                        readerAttachmentAdapter.clear();
                    }
                    for(int i=0;i<readerList.getPage().getList().size();i++){
                        readerAttachmentAdapter.add(readerList.getPage().getList().get(i));
                    }
                    if(mPageNum>=totalPage){
                        readerAttachmentAdapter.pauseMore();
                    }
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
                readerAttachmentAdapter.notifyDataSetChanged();
            }
        });
    }


    public void setvedioLinear(OnVideoLinear onVideoLinear){
        this.onVideoLinear=onVideoLinear;
    }
    public interface OnVideoLinear{
        void setVidoUrl(int ReaderId);
    }
}
