package com.inhim.pj.adapter.provider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.activity.VideoActivity;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.utils.GlideUtils;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author ChayChan
 * @description: 头部banner
 * @date 2018/3/22  14:48
 * 未使用
 */

public class CourseOnerItemProvider extends BaseItemProvider<ReaderList.List, BaseViewHolder> {

    private Context context;
    public CourseOnerItemProvider(Context context) {
        this.context=context;
    }

    @Override
    public void convert(BaseViewHolder helper, final ReaderList.List news, int i) {
        if (news == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            ImageView iv_title = helper.getView(R.id.iv_title);
            TextView tv_title = helper.getView(R.id.tv_title);
            TextView tv_num = helper.getView(R.id.tv_num);
            TextView tv_time = helper.getView(R.id.tv_time);
            ImageLoaderUtils.setImage(news.getCover(),iv_title);
            tv_num.setText(news.getReadAmount());
            tv_title.setText(news.getTitle());
            tv_time.setText(news.getCreateTime());
            LinearLayout item_lin=helper.getView(R.id.item_lin);
            item_lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("ReaderId",news.getReaderId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                }
            });
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
        return R.layout.item_reading_one;
    }

}
