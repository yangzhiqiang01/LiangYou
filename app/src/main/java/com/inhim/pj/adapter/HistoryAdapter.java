package com.inhim.pj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.HistoryEntity;
import com.inhim.pj.utils.GlideUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

public class HistoryAdapter extends RecyclerArrayAdapter<HistoryEntity.List> {

    public HistoryAdapter(Context context) {
        super(context);
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reading_two, parent, false);
        return new PersonViewHolder(view);
    }

    public class PersonViewHolder extends BaseViewHolder<HistoryEntity.List> {

        public ImageView iv_title1;
        public TextView tv_title1, tv_num1, tv_time1;

        PersonViewHolder(View itemView) {
            super(itemView);
            iv_title1 = itemView.findViewById(R.id.iv_title1);
            tv_title1 = itemView.findViewById(R.id.tv_title1);
            tv_num1 = itemView.findViewById(R.id.tv_num1);
            tv_time1 = itemView.findViewById(R.id.tv_time1);
        }

        @Override
        public void setData(HistoryEntity.List data) {
            super.setData(data);
            try{
                GlideUtils.displayFromUrl(data.getReaderEntity().getCover(),iv_title1);
                tv_title1.setText(data.getReaderEntity().getTitle());
                tv_num1.setText(data.getReaderEntity().getSynopsis());
                tv_time1.setText( data.getTimeText());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
