package com.inhim.pj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.CollectionAdapter;
import com.inhim.pj.adapter.RadioGridViewAdapter;
import com.inhim.pj.entity.CollectionList;
import com.inhim.pj.entity.CollectionTypeList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;
import org.yczbj.ycrefreshviewlib.inter.OnItemChildClickListener;
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

public class CollectionActivity extends AppCompatActivity {
    /*收藏页面*/
    Gson gson=new Gson();
    private CollectionAdapter mAdapter;
    private YCRefreshView mRecyclerView;
    private int mPageNum=1;
    private Boolean refresh=true;
    private List<CollectionList.List> colleList;
    private int totalPage;
    private CheckBox cb_doload;
    private TextView tv_editor;
    private boolean isCheck;
    private LinearLayout lin_caozuo;
    private TextView textview1,textview2;
    private Map vipCollectionIdsMap;
    private List<CollectionTypeList.TypeList> typeList;
    private int readerTypeId;
    private PopupWindow popupwindow;
    private RadioGridViewAdapter popupwindowAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        mRecyclerView=findViewById(R.id.onceTask_member_ycView);
        cb_doload=findViewById(R.id.cb_doload);
        tv_editor=findViewById(R.id.tv_editor);
        lin_caozuo=findViewById(R.id.lin_caozuo);
        textview1=findViewById(R.id.textview1);
        textview2=findViewById(R.id.textview2);
        colleList=new ArrayList<>();
        vipCollectionIdsMap=new HashMap();
        typeList=new ArrayList<>();
        handleRecyclerViewInfo();
        collectionTypeList();
        readerTypeId=0;
        getCollectionList(readerTypeId);
        cb_doload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showPopuwindow(CollectionActivity.this,cb_doload);
                }
            }
        });
        textview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setCheck(true,true);
                mAdapter.notifyDataSetChanged();
                for(int i=0;i<colleList.size();i++){
                    vipCollectionIdsMap.put(i,colleList.get(i).getVipCollectionId());
                }
            }
        });
        textview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCollection();
            }
        });
        tv_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCheck){
                    isCheck=true;
                    mAdapter.setCheck(isCheck,false);
                    mAdapter.notifyDataSetChanged();
                    lin_caozuo.setVisibility(View.VISIBLE);
                }else{
                    isCheck=false;
                    mAdapter.setCheck(isCheck,false);
                    mAdapter.notifyDataSetChanged();
                    lin_caozuo.setVisibility(View.GONE);
                }
            }
        });
        ImageView iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void deleteCollection() {
        FormBody.Builder formBody=new FormBody.Builder();

        StringBuilder vipCollectionIds=new StringBuilder();
        for(int i=0;i<colleList.size();i++){
            try{
                vipCollectionIds.append(vipCollectionIdsMap.get(i));
                if(i!=colleList.size()-1){
                    vipCollectionIds.append(",");
                }else{
                    vipCollectionIds.append("}");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        formBody.add("vipCollectionIds",vipCollectionIds.toString());
        MyOkHttpClient.getInstance().doDelete(Urls.collectionDelete, formBody.build(), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                JSONObject jsonObject=new JSONObject();
                try {
                    if(jsonObject.getInt("code")==0){
                        getCollectionList(readerTypeId);
                    }else{
                        BToast.showText(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void collectionTypeList(){
        MyOkHttpClient.getInstance().asyncGet(Urls.collectionTypeList, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                Log.e("result",result);
                CollectionTypeList collectionTypeList=gson.fromJson(result,CollectionTypeList.class);
                CollectionTypeList.TypeList type=new CollectionTypeList().new TypeList();
                type.setName("全部");
                typeList.add(type);
                if(collectionTypeList.getTypeList()!=null&&collectionTypeList.getTypeList().size()>0){
                    typeList.addAll(collectionTypeList.getTypeList());
                }
            }
        });
    }
    private void getCollectionList(int readerTypeId) {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        HashMap postMap = new HashMap();
        if(readerTypeId!=0){
           postMap.put("readerTypeId",readerTypeId);
        }
        MyOkHttpClient.getInstance().asyncJsonPost(Urls.getCollectionList(mPageNum, 10,"1"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                CollectionList collectionList = gson.fromJson(result, CollectionList.class);
                if (collectionList.getCode() == 0) {
                    totalPage=collectionList.getPage().getTotalPage();
                    if(refresh){
                        mAdapter.clear();
                        colleList.clear();
                    }
                    colleList.addAll(collectionList.getPage().getList());
                    for(int i=0;i<colleList.size();i++){
                        mAdapter.add(colleList.get(i));
                    }
                    if(mPageNum>=totalPage){
                        mAdapter.pauseMore();
                    }
                } else {
                    BToast.showText(collectionList.getMsg(), false);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    private void handleRecyclerViewInfo() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CollectionAdapter(this);
        mRecyclerView.setEmptyView(R.layout.empty_view_layout);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum= 1;
                refresh=true;
                getCollectionList(readerTypeId);
            }
        });

        //设置上拉加载更多时布局，以及监听事件
        mAdapter.setMore(R.layout.view_more, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //可以做请求下一页操作
                mPageNum++;
                refresh=false;
                getCollectionList(readerTypeId);
            }
        });
       /* mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CollectionList.List collection=mAdapter.getAllData().get(position);
                Intent intent=new Intent(CollectionActivity.this, VideoActivity.class);
                intent.putExtra("ReaderId",collection.getReaderId());
                startActivity(intent);
            }
        });*/

        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                CollectionList.List collection=mAdapter.getAllData().get(position);
                switch (view.getId()){
                    case R.id.checkbox:
                        CheckBox checkBox=(CheckBox)view;
                        if(checkBox.isChecked()){
                            vipCollectionIdsMap.put(position,collection.getVipCollectionId());
                        }else{
                            try{
                                vipCollectionIdsMap.remove(position);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }
        });

    }
    public void showPopuwindow(Context context, CheckBox view1) {
        View popupwindow_view = ((Activity) context).getLayoutInflater().inflate(R.layout.listvie_popuwindow, null, false);
        LinearLayout lin=popupwindow_view.findViewById(R.id.lin);
        lin.getBackground().setAlpha(100);//0~255透明度值
        popupwindow = new PopupWindow(popupwindow_view, getLayout(), getLayout()-view1.getHeight(), true);
        popupwindow.setFocusable(true);
        popupwindow.setOutsideTouchable(true);
        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        popupwindow.setTouchable(true);
        // 这里是位置显示方式,在屏幕的右侧
        popupwindow.showAsDropDown(view1, 0, 0);
        //消失监听听
        popupwindow.setOnDismissListener(mDismissListener);
        MyGridView gridView=popupwindow_view.findViewById(R.id.gridView);
        popupwindowAdapter = new RadioGridViewAdapter(context);
        popupwindowAdapter.setData(typeList);//传数组, 并指定默认值
        gridView.setAdapter(popupwindowAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                popupwindowAdapter.setSeclection(position);//传值更新
                popupwindowAdapter.notifyDataSetChanged();
                CollectionTypeList.TypeList type=typeList.get(position);
                readerTypeId=type.getReaderTypeId();
                getCollectionList(readerTypeId);
            }
        });
    }
    private PopupWindow.OnDismissListener mDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            cb_doload.setChecked(false);
        }
    };
    /**
     * 获取listview的高度，在TaskFragment中设置popupwindow中调用
     *
     * @return
     */
    public static int getLayout() {

        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

}
