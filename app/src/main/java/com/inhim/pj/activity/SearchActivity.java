package com.inhim.pj.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.SearchAdapter;
import com.inhim.pj.adapter.SearchTwoAdapter;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.HistoricalRecordEntity;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.StatusBarUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.SearchView;

import org.litepal.LitePal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

/**
 * Created by hsmacmini on 2018/4/4.
 */

public class SearchActivity extends BaseActivity implements SearchView.SearchViewListener {

    /**
     * 搜索结果列表view
     */
    private ListView lvResults;

    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;


    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;

    /**
     * 搜索过程中自动补全数据
     */

    /**
     * 搜索结果的数据
     */
    private List<ReaderList.List> resultData1;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 10;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    private ImageView search_iv;
    private  Gson gson ;

    @Override
    public Object offerLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void onBindView() {
        gson = new Gson();
        initViews();
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        //getAutoCompleteData(null);

        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
    }

    @Override
    public void destory() {

    }

    private void getReaderList(final String keyword) {
        /** {
         "readerStyleId": "string",
         "readerStyleValueId": "string",
         "readerTypeId": "string",
         "title": "string"
         }*/
        HashMap postMap = new HashMap();
        postMap.put("title",keyword);
        MyOkHttpClient.getInstance().asyncJsonPostNoToken(Urls.getReaderList(1, 100,"desc"), postMap, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderList readerList = gson.fromJson(result, ReaderList.class);
                if (readerList.getCode() == 0) {
                    resultData1=readerList.getPage().getList();
                    if(null==resultData1||resultData1.size()==0){
                        BToast.showText("未搜索到相关内容", false);
                        return;
                    }
                    lvResults.setVisibility(View.VISIBLE);
                    resultAdapter = new SearchAdapter(SearchActivity.this, resultData1,keyword);
                    lvResults.setAdapter(resultAdapter);
                    lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Intent intent;
                            if(resultData1.get(position).getType().equals("2")){
                                intent=new Intent(SearchActivity.this, VideoActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            }else if(resultData1.get(position).getType().equals("3")){
                                intent=new Intent(SearchActivity.this, RadioActivity.class);
                            }else{
                                intent=new Intent(SearchActivity.this, ArticleActivity.class);
                            }
                            intent.putExtra("ReaderId",resultData1.get(position).getReaderId());
                            startActivity(intent);
                        }
                    });
                } else {
                    BToast.showText(readerList.getMsg(), false);
                }
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        lvResults = findViewById(R.id.main_lv_search_results);
        searchView = findViewById(R.id.main_search_layout);
        //设置监听
        searchView.setSearchViewListener(SearchActivity.this);
        /*search_iv=findViewById(R.id.search_iv);
        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getListCourses(searchView.getText());
            }
        });*/
    }


    private void getHintData() {
        List<HistoricalRecordEntity> historicalRecordEntityList= LitePal.findAll(HistoricalRecordEntity.class);
        hintAdapter = new SearchTwoAdapter(this, R.layout.listvie_popuwindow, historicalRecordEntityList);
    }


    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        //getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {
        //如果当前搜索字段已存在数据库中则不再重新加入
        List<HistoricalRecordEntity>  person = LitePal.where("text = ?", text).find(HistoricalRecordEntity.class);
        if((person==null||person.size()==0)){
            List<HistoricalRecordEntity> historicalRecordEntityList=new ArrayList<>();
            HistoricalRecordEntity historicalRecordEntity=new HistoricalRecordEntity();
            historicalRecordEntity.setText(text);
            historicalRecordEntityList.add(historicalRecordEntity);
            historicalRecordEntity.save();
            List<HistoricalRecordEntity> historList= LitePal.findAll(HistoricalRecordEntity.class);
            hintAdapter = new SearchTwoAdapter(this, R.layout.listvie_popuwindow, historList);
            searchView.setAutoCompleteAdapter(hintAdapter);
        }
        getReaderList(text);
        lvResults.setVisibility(View.GONE);
    }

    @Override
    public void onTvSearch(String text) {
        getReaderList(text);
    }

    @Override
    public void onGoneListview() {
        lvResults.setVisibility(View.GONE);
    }

}
