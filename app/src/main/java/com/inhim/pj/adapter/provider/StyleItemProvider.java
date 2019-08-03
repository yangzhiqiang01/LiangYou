package com.inhim.pj.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.VideoActivity;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.utils.GlideUtils;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author ChayChan
 * @description:
 */

public class StyleItemProvider extends BaseItemProvider<ReaderStyle.List, BaseViewHolder> {

    private Context context;
    public StyleItemProvider(Context context) {
        this.context=context;
    }

    @Override
    public void convert(BaseViewHolder helper, final ReaderStyle.List news, int i) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            ImageView iv_title = helper.getView(R.id.iv_title1);
            TextView tv_title = helper.getView(R.id.tv_title1);
            TextView tv_num = helper.getView(R.id.tv_num1);
            TextView tv_time = helper.getView(R.id.tv_time1);
            final ConstraintLayout constran=helper.getView(R.id.constran);
            constran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,VideoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("ReaderStyleId",news.getReaderStyleValue().getReaderStyleId());
                    context.startActivity(intent);
                }
            });
            ImageLoaderUtils.setImage(news.getReaderStyleValue().getCover(),iv_title);
            tv_num.setText(String.valueOf(news.getTotal()));
            tv_title.setText(news.getReaderStyleValue().getValue());
            tv_time.setText(news.getReaderStyleValue().getUpdateTime());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int viewType() {
        return ReadingTwoAdapter.STYLE;
    }

    @Override
    public int layout() {
        return R.layout.item_reading_two;
    }

}
