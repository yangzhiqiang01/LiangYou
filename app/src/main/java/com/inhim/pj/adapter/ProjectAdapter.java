package com.inhim.pj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inhim.pj.R;
import com.inhim.pj.entity.CollectionList;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.utils.GlideCircleUtils;
import com.inhim.pj.utils.GlideUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

public class ProjectAdapter extends RecyclerArrayAdapter<ReaderStyle.List> {

    private Context context;
    public ProjectAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonViewHolder(parent);
    }
    public class PersonViewHolder extends BaseViewHolder<ReaderStyle.List> {

        ImageView iv_title;
        TextView tv_title;
        TextView tv_num;
        TextView tv_content;
        PersonViewHolder(ViewGroup parent){
            super(parent, R.layout.item_project);
            iv_title = getView(R.id.iv_title);
            tv_title = getView(R.id.tv_title);
            tv_num = getView(R.id.tv_num);
            tv_content = getView(R.id.tv_content);
        }
        @Override
        public void setData(ReaderStyle.List data) {
            super.setData(data);
            Glide.with(context).load(data.getReaderStyleValue().getCover()).into(iv_title);
            tv_num.setText("共"+data.getTotal()+"篇文章");
            tv_title.setText(data.getReaderStyleValue().getValue());
            tv_content.setText(data.getReaderStyleValue().getSynopsis());
        }
    }
}
