package com.inhim.pj.adapter.provider;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.ProjectActivity;
import com.inhim.pj.adapter.ReadingTwoAdapter;

/**
 * @author ChayChan
 * @description: 章节标题
 * @date 2018/3/22  14:48
 */

public class ReaderTypeProvider extends BaseItemProvider<String, BaseViewHolder> {


    public ReaderTypeProvider() {
    }

    @Override
    public void convert(BaseViewHolder helper, final String news, int i) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try {
            //处理相关业务逻辑
            TextView tv_title = helper.getView(R.id.tv_title);
            TextView tv_lookall = helper.getView(R.id.tv_lookall);
            ImageView iv_arrow = helper.getView(R.id.iv_arrow);
            tv_title.setText(news);
            if ("每日推荐".equals(news) || "视频推荐".equals(news)) {
                tv_lookall.setVisibility(View.INVISIBLE);
                iv_arrow.setVisibility(View.INVISIBLE);
            } else {
                tv_lookall.setVisibility(View.VISIBLE);
                iv_arrow.setVisibility(View.VISIBLE);
            }
            tv_lookall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProjectActivity.class);
                    intent.putExtra("Type", "special");
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int viewType() {
        return ReadingTwoAdapter.STYLE_TITLE;
    }

    @Override
    public int layout() {
        return R.layout.item_title;
    }
}
