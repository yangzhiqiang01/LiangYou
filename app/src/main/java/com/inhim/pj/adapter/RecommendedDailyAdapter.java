package com.inhim.pj.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.utils.GlideUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

public class RecommendedDailyAdapter extends RecyclerArrayAdapter<ReaderList.List> {

    Context context;

    public RecommendedDailyAdapter(Context context){
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonViewHolder(parent);
    }

    public class PersonViewHolder extends BaseViewHolder<ReaderList.List> {

        ImageView iv_title;
        TextView tv_title;
        TextView tv_num;
        TextView tv_content;
        PersonViewHolder(ViewGroup parent){
            super(parent, R.layout.item_chapter);
            iv_title = getView(R.id.iv_title1);
            tv_title = getView(R.id.tv_title1);
            tv_num = getView(R.id.tv_num);
            tv_content = getView(R.id.tv_content);
        }
        @Override
        public void setData(ReaderList.List data) {
            super.setData(data);
            GlideUtils.displayFromUrl(data.getCover(),iv_title);
            tv_num.setText(data.getReadAmount());
            tv_title.setText(data.getTitle());
            tv_content.setText(data.getContent());

        }
    }

}
