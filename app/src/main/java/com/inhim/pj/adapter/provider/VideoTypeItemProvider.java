package com.inhim.pj.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.ProjectListActivity;
import com.inhim.pj.activity.VideoActivity;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.utils.GlideUtils;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author ChayChan
 * @description: 头部banner
 * @date 2018/3/22  14:48
 */

public class VideoTypeItemProvider extends BaseItemProvider<ReaderTypeList.List, BaseViewHolder> {

    private Context context;
    public VideoTypeItemProvider(Context context) {
        this.context=context;
    }

    @Override
    public void convert(BaseViewHolder helper, final ReaderTypeList.List news, int i) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            ImageView iv_title = helper.getView(R.id.iv_title);
            TextView tv_title = helper.getView(R.id.tv_title);
            TextView tv_time = helper.getView(R.id.tv_time);
            ImageLoaderUtils.setImage(news.getIcon(),iv_title);
            tv_title.setText(news.getName());
            tv_time.setText(news.getCreateTime());
            ConstraintLayout constran=helper.getView(R.id.constran);
            constran.setOnClickListener(v -> {
                Intent intent=new Intent(context, ProjectListActivity.class);
                intent.putExtra("ReaderTypeList",news);
                context.startActivity(intent);
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
        return R.layout.item_video_type;
    }

}
