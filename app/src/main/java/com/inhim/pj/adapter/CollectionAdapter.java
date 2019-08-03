package com.inhim.pj.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.activity.ArticleActivity;
import com.inhim.pj.activity.RadioActivity;
import com.inhim.pj.activity.VideoActivity;
import com.inhim.pj.entity.CollectionList;
import com.inhim.pj.utils.GlideUtils;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;


public class CollectionAdapter extends RecyclerArrayAdapter<CollectionList.List> {

    Context context;
    private boolean isCheck,isAll;
    public CollectionAdapter(Context context){
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonViewHolder(parent);
    }
    public void setCheck(boolean isCheck,boolean isAll){
        this.isCheck=isCheck;
        this.isAll=isAll;
    }
    public class PersonViewHolder extends BaseViewHolder<CollectionList.List> {

        ImageView iv_title,iv_icon,iv_glasses1;
        TextView tv_title;
        TextView tv_num1;
        TextView tv_time1;
        CheckBox checkbox;
        PersonViewHolder(ViewGroup parent){
            super(parent, R.layout.item_collection);
            iv_title = getView(R.id.iv_title1);
            tv_title = getView(R.id.tv_title1);
            tv_num1 = getView(R.id.tv_num1);
            tv_time1 = getView(R.id.tv_time1);
            checkbox=getView(R.id.checkbox);
            iv_icon=getView(R.id.iv_icon);
            iv_glasses1=getView(R.id.iv_glasses1);
            //设置子View的点击事件
            addOnClickListener(R.id.checkbox);
        }
        @Override
        public void setData(final CollectionList.List data) {
            super.setData(data);
            if(data.getStatus()!=null&&data.getStatus().equals("0")){
                iv_icon.setVisibility(View.GONE);
                iv_glasses1.setVisibility(View.GONE);
                tv_title.setText("此数据已被管理员删除");
                iv_title.setImageResource(R.mipmap.readerinfo_delete);
            }else{
                ImageLoaderUtils.setImage(data.getReaderEntity().getCover(), iv_title);
                tv_num1.setText(data.getReaderEntity().getReadAmount());
                tv_title.setText(data.getReaderEntity().getTitle());
                if(data.getTimeText()!=null){
                    tv_time1.setText(data.getTimeText());
                }
                //1文章 2视频 3音频
                if(data.getReaderEntity().getType().equals("2")){
                    iv_icon.setVisibility(View.VISIBLE);
                    iv_icon.setImageResource(R.mipmap.icon_video);
                }else if(data.getReaderEntity().getType().equals("3")){
                    iv_icon.setVisibility(View.VISIBLE);
                    iv_icon.setImageResource(R.mipmap.yinyue_icon);
                }else{
                    iv_icon.setVisibility(View.GONE);
                }
                if(isCheck){
                    checkbox.setVisibility(View.VISIBLE);
                }else{
                    checkbox.setVisibility(View.GONE);
                }
                if(isAll){
                    checkbox.setChecked(true);
                }else{
                    checkbox.setChecked(false);
                }

            }

        }
    }
}
