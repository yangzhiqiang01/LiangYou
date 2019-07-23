package com.inhim.pj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.utils.GlideUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

public class ProjectListAdapter extends RecyclerArrayAdapter<ReaderList.List> {

    private ReaderStyle.List readerStyle;
    public ProjectListAdapter(Context context) {
        super(context);

    }
    public void  setReaderStyle(ReaderStyle.List readerStyle){
        this.readerStyle=readerStyle;
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        PersonViewHolder personViewHolder =new PersonViewHolder(parent);
        if(viewType==0&&readerStyle!=null){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_list_title,parent,false);
            TitleHolder titleHolder=new TitleHolder(view);
            ViewGroup viewGroup= (ViewGroup) personViewHolder.itemView;
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
            );
            GlideUtils.displayFromUrl(readerStyle.getReaderStyleValue().getCover(),titleHolder.iv_title);
            titleHolder.tv_num.setText("共"+readerStyle.getTotal()+"篇文章");
            titleHolder.tv_content.setText(readerStyle.getReaderStyleValue().getValue());
            viewGroup.addView(view,0,layoutParams);
            return personViewHolder;
        }
        return new PersonViewHolder(parent);
    }
    class TitleHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_content;
        public TextView tv_num;
        public ImageView iv_title;
        public TitleHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_content=itemView.findViewById(R.id.tv_content);
            tv_num=itemView.findViewById(R.id.tv_num);
            iv_title=itemView.findViewById(R.id.iv_title);
        }
    }

    public class PersonViewHolder extends BaseViewHolder<ReaderList.List> {

        ImageView iv_title;
        TextView tv_title;
        TextView tv_num;
        TextView tv_content;
        ImageView iv_icon;
        PersonViewHolder(ViewGroup parent){
            super(parent, R.layout.item_project_list);
            iv_title = getView(R.id.iv_title);
            tv_title = getView(R.id.tv_title);
            tv_num = getView(R.id.tv_num);
            tv_content = getView(R.id.tv_content);
            iv_icon=getView(R.id.iv_icon);
        }
        @Override
        public void setData(ReaderList.List data) {
            super.setData(data);
            GlideUtils.displayFromUrl(data.getCover(),iv_title);
            tv_title.setText(data.getTitle());
            tv_content.setText(String.valueOf(data.getReadAmount()));
            tv_num.setText(data.getCreateTime().substring(0,10));
            //1文章 2视频 3音频
            if(data.getType().equals("2")){
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(R.mipmap.icon_video);
            }else if(data.getType().equals("3")){
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(R.mipmap.yinyue_icon);
            }else{
                iv_icon.setVisibility(View.GONE);
            }
        }
    }
}
