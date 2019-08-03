package com.inhim.pj.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.ArticleActivity;
import com.inhim.pj.activity.RadioActivity;
import com.inhim.pj.activity.VideoActivity;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author ChayChan
 * @description:
 * @date 2018/3/22  14:48
 */

public class CourseOtherItemProvider extends BaseItemProvider<ReaderList.List, BaseViewHolder> {

    private Context context;
    private int size;
    private String TAG;
    public CourseOtherItemProvider(Context context,int size,String TAG) {
        this.context=context;
        this.size=size;
        this.TAG=TAG;
    }

    @Override
    public void convert(BaseViewHolder helper, final ReaderList.List news, int position) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            ImageView iv_title = helper.getView(R.id.iv_title1);
            TextView tv_title = helper.getView(R.id.tv_title1);
            TextView tv_num = helper.getView(R.id.tv_num1);
            TextView tv_time = helper.getView(R.id.tv_time1);
            ImageView iv_icon=helper.getView(R.id.iv_icon);
            ImageLoaderUtils.setImage(news.getCover(),iv_title);
            LinearLayout lin_1=helper.getView(R.id.lin_1);
          /*  Log.e("position11",position+"");
            Log.e("position11",size-1+"");*/
            /*if(position==size-1){
                lin_1.setVisibility(View.GONE);
            }else{
                lin_1.setVisibility(View.VISIBLE);
            }*/
            //1文章 2视频 3音频
            if(news.getType().equals("2")){
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(R.mipmap.icon_video);
            }else if(news.getType().equals("3")){
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(R.mipmap.yinyue_icon);
            }else{
                iv_icon.setVisibility(View.GONE);
            }
            if(news.getReadAmount()!=null){
                tv_title.setTextColor(Color.parseColor("#999999"));
                tv_num.setTextColor(Color.parseColor("#999999"));
                tv_time.setTextColor(Color.parseColor("#999999"));
                tv_num.setText(news.getReadAmount());
            }else{
                tv_title.setTextColor(Color.parseColor("#333333"));
                tv_num.setTextColor(Color.parseColor("#666666"));
                tv_time.setTextColor(Color.parseColor("#666666"));
                tv_num.setText("0");
            }
            tv_title.setText(news.getTitle());
            if(news.getTimeText()!=null){
                tv_time.setText(news.getTimeText());
            }
            ConstraintLayout constran=helper.getView(R.id.constran);
            constran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if(news.getType().equals("2")){
                        intent=new Intent(context, VideoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.putExtra("TAG",TAG);
                    }else if(news.getType().equals("3")){
                        intent=new Intent(context, RadioActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    }else{
                        intent=new Intent(context, ArticleActivity.class);
                    }
                    intent.putExtra("ReaderId",news.getReaderId());
                    context.startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int viewType() {
        return ReadingTwoAdapter.COURSE_OTHER;
    }

    @Override
    public int layout() {
        return R.layout.item_reading_two;
    }

}
