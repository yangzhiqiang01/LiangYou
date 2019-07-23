package com.inhim.pj.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.ProjectActivity;
import com.inhim.pj.activity.ProjectListActivity;
import com.inhim.pj.adapter.ChapterItemAdapter;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.listener.RecyclerViewClickListener;

/**
 * @author ChayChan
 * @description: 章节标题
 * @date 2018/3/22  14:48
 */

public class ChapterItemProvider extends BaseItemProvider<ReaderTypeList, BaseViewHolder> {

    private Context context;
    public ChapterItemProvider(Context context) {
        this.context=context;
    }

    @Override
    public void convert(BaseViewHolder helper, final ReaderTypeList news, int i) {
        if (news == null) {
            //如果没有，则直接跳过
            return;
        }
        try{
            //处理相关业务逻辑
            RecyclerView recyclerView = helper.getView(R.id.recyclerView);
            ChapterItemAdapter adapter = new ChapterItemAdapter(news);
            recyclerView.setAdapter(adapter);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            /**
             * 用于实现列表布局
             */
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(context,
                    recyclerView, new RecyclerViewClickListener.OnItem2ClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                Intent intent=new Intent(context, ProjectListActivity.class);
                intent.putExtra("readerTypeId",news.getPage().getList().get(position).getReaderTypeId());
                context.startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));
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
        return R.layout.item_chapter_provider;
    }
}
