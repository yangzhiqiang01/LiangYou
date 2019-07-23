package com.inhim.pj.adapter.provider;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.utils.GlideUtils;

/**
 * @author ChayChan
 * @description: 头部banner
 * @date 2018/3/22  14:48
 */

public class ChapterProvider extends BaseItemProvider<ReaderTypeList.List, BaseViewHolder> {


    public ChapterProvider() {
    }

    @Override
    public void convert(final BaseViewHolder helper, ReaderTypeList.List news, final int position) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            ImageView iv_title = helper.getView(R.id.iv_title1);
            TextView tv_title = helper.getView(R.id.tv_title1);
            TextView tv_num = helper.getView(R.id.tv_num);
            TextView tv_content = helper.getView(R.id.tv_content);
            GlideUtils.displayFromUrl(news.getIcon(),iv_title);
            tv_num.setText(news.getLevel());
            tv_title.setText(news.getName());
            tv_content.setText(news.getPyFull());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int viewType() {
        return ReadingTwoAdapter.COURSE_ONE;
    }

    @Override
    public int layout() {
        return R.layout.item_chapter;
    }

}
