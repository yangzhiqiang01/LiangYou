package com.inhim.pj.adapter.provider;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.ChapterActivity;
import com.inhim.pj.activity.ProjectListActivity;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderTypeList;

/**
 * @author ChayChan
 * @description: 章节标题
 * @date 2018/3/22  14:48
 */

public class ListenReaderTypeProvider extends BaseItemProvider<ReaderTypeList.List, BaseViewHolder> {


    public ListenReaderTypeProvider() {
    }

    @Override
    public void convert(BaseViewHolder helper, final ReaderTypeList.List news, int i) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            //处理相关业务逻辑
            TextView tv_title = helper.getView(R.id.tv_title);
            tv_title.setText(news.getName());
            TextView tv_lookall=helper.getView(R.id.tv_lookall);
            tv_lookall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, ProjectListActivity.class);
                    intent.putExtra("ReaderTypeList",news);
                    mContext.startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int viewType() {
        return ReadingTwoAdapter.CHAPTER_ITEM;
    }

    @Override
    public int layout() {
        return R.layout.item_title;
    }
}
