package com.inhim.pj.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.utils.GlideUtils;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

public class ProjectListAdapter extends RecyclerArrayAdapter<ReaderList.List> {

    private ReaderStyle.List readerStyle;
    private ReaderTypeList.List readerType;
    private Context context;
    public ProjectListAdapter(Context context) {
        super(context);
        this.context=context;
    }
    public void  setReaderStyle(ReaderStyle.List readerStyle){
        this.readerStyle=readerStyle;
    }
    public void  setReaderType(ReaderTypeList.List readerType){
        this.readerType=readerType;
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        PersonViewHolder personViewHolder =new PersonViewHolder(parent,viewType);
        if(viewType==0&&readerStyle!=null){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_list_title,parent,false);
            TitleHolder titleHolder=new TitleHolder(view);
            ViewGroup viewGroup= (ViewGroup) personViewHolder.itemView;
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
            );
            ImageLoaderUtils.setImage(readerStyle.getReaderStyleValue().getCover(),titleHolder.iv_title);
            titleHolder.tv_num.setText("共"+readerStyle.getTotal()+"篇文章");
            titleHolder.tv_content.setText(readerStyle.getReaderStyleValue().getSynopsis());
            titleHolder.tv_title.setText(readerStyle.getReaderStyleValue().getValue());
            viewGroup.addView(view,0,layoutParams);
            return personViewHolder;
        }
        if(viewType==0&&readerType!=null){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_type_list_title,parent,false);
            TitleTypeHolder titleHolder=new TitleTypeHolder(view);
            ViewGroup viewGroup= (ViewGroup) personViewHolder.itemView;
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
            );
            ImageLoaderUtils.setImage(readerType.getIcon(),titleHolder.iv_title);
            titleHolder.tv_title.setText(readerType.getName());
            viewGroup.addView(view,0,layoutParams);
            return personViewHolder;
        }
        return personViewHolder;
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

    class TitleTypeHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public ImageView iv_title;
        public TitleTypeHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            iv_title=itemView.findViewById(R.id.iv_title);
        }
    }

    public class PersonViewHolder extends BaseViewHolder<ReaderList.List> {

        ImageView iv_title;
        TextView tv_title;
        TextView tv_num;
        TextView tv_content;
        ImageView iv_icon;
        LinearLayout lin_1;
        int position;
        PersonViewHolder(ViewGroup parent,int position){
            super(parent, R.layout.item_project_list);
            iv_title = getView(R.id.iv_title);
            tv_title = getView(R.id.tv_title);
            tv_num = getView(R.id.tv_num);
            tv_content = getView(R.id.tv_content);
            iv_icon=getView(R.id.iv_icon);
            lin_1=getView(R.id.lin_1);
            position=position;
        }
        @Override
        public void setData(ReaderList.List data) {
            super.setData(data);
            if(position==getItemCount()-1){
                lin_1.setVisibility(View.GONE);
            }else{
                lin_1.setVisibility(View.VISIBLE);
            }
            if(data.getReadAmount()!=null){
                tv_title.setTextColor(Color.parseColor("#999999"));
                tv_num.setTextColor(Color.parseColor("#999999"));
                tv_content.setTextColor(Color.parseColor("#999999"));
                tv_content.setText(data.getReadAmount());
            }else{
                tv_title.setTextColor(Color.parseColor("#333333"));
                tv_num.setTextColor(Color.parseColor("#666666"));
                tv_content.setTextColor(Color.parseColor("#666666"));
                tv_content.setText("0");
            }
            ImageLoaderUtils.setImage(data.getCover(),iv_title);
            tv_title.setText(data.getTitle());
            if(data.getTimeText()!=null){
                tv_num.setText(data.getTimeText());
            }
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
