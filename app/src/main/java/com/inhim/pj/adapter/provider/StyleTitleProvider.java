package com.inhim.pj.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.ProjectListActivity;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.utils.GlideUtils;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.inhim.pj.view.CustomRoundAngleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author ChayChan
 * @description: 章节标题
 * @date 2018/3/22  14:48
 */

public class StyleTitleProvider extends BaseItemProvider<ReaderStyle.List, BaseViewHolder> {

    private Context context;
    public StyleTitleProvider(Context context) {
        this.context=context;
    }

    @Override
    public void convert(BaseViewHolder helper, final ReaderStyle.List news, int i) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            //处理相关业务逻辑
            CustomRoundAngleImageView imageview = helper.getView(R.id.imageview);
            ImageLoaderUtils.setImage(news.getReaderStyleValue().getCover(),imageview);
            ConstraintLayout constran = helper.getView(R.id.constran);
            constran.getBackground().setAlpha(100);//0~255透明度值
            TextView textview1 = helper.getView(R.id.textview1);
            textview1.setText(news.getReaderStyleValue().getValue());
            TextView textview2 = helper.getView(R.id.textview2);
            textview2.setText("共"+news.getTotal()+"篇文章");
            constran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, ProjectListActivity.class);
                    intent.putExtra("ReaderStyle",news);
                    mContext.startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int viewType() {
        return ReadingTwoAdapter.CHAPTER;
    }

    @Override
    public int layout() {
        return R.layout.item_style_title;
    }
}
