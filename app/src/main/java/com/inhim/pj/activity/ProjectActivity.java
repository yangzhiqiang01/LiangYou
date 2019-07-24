package com.inhim.pj.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ProjectAdapter;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;

import org.yczbj.ycrefreshviewlib.inter.OnItemClickListener;
import org.yczbj.ycrefreshviewlib.view.YCRefreshView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class ProjectActivity extends AppCompatActivity {
    private YCRefreshView ycRefreshView;
    private List<ReaderStyle.List> typeList;
    private ProjectAdapter mAdapter;
    private TextView tvCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ycRefreshView=findViewById(R.id.ycRefreshView);
        tvCourse=findViewById(R.id.tvCourse);
        tvCourse.setText("系列专题");
        ImageView iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        typeList=new ArrayList();
        initAdapter();
        getReaderStyle();
    }
    private void initAdapter() {
        typeList=new ArrayList();
        mAdapter = new ProjectAdapter(ProjectActivity.this);
        ycRefreshView.setLayoutManager(new LinearLayoutManager(ProjectActivity.this));
        ycRefreshView.setEmptyView(R.layout.empty_view_layout);
        ycRefreshView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(ProjectActivity.this, ProjectListActivity.class);
                intent.putExtra("ReaderStyle",typeList.get(position));
                startActivity(intent);
            }
        });
    }

    private void getReaderStyle(){
        MyOkHttpClient.getInstance().asyncGet(Urls.getReaderStyle("special"), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                Gson gson=new Gson();
                ReaderStyle readerStyle=gson.fromJson(result,ReaderStyle.class);
                if(readerStyle.getCode()==0){
                    typeList.addAll(readerStyle.getList());
                    for(int i=0;i<readerStyle.getList().size();i++){
                        mAdapter.add(readerStyle.getList().get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
