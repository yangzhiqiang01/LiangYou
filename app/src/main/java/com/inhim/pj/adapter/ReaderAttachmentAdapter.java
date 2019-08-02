package com.inhim.pj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.ReaderInfo;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderStyle;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

import java.util.ArrayList;

public class ReaderAttachmentAdapter extends RecyclerArrayAdapter<ReaderList.List> {

    Context context;
    private int lastPosition=-1;//定义一个标记为最后选择的位置
    private  int ReaderId;
    public ReaderAttachmentAdapter(Context context,int ReaderId) {
        super(context);
        this.ReaderId=ReaderId;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponsViewHolder(parent,viewType);
    }

    public class CouponsViewHolder extends BaseViewHolder<ReaderList.List> {

        public ImageView imageview;
        public TextView textview;
        public int position;
        CouponsViewHolder(ViewGroup parent,int position){
            super(parent, R.layout.item_reader_attachment);
            imageview = itemView.findViewById(R.id.imageview);
            textview = itemView.findViewById(R.id.textview);
            this.position=position;
        }

        @Override
        public void setData(ReaderList.List data) {
            super.setData(data);
            textview.setText(data.getTitle());
            if(lastPosition==-1){
                if(ReaderId==data.getReaderId()){
                    imageview.setImageResource(R.mipmap.attachment1);
                    textview.setTextColor(Color.parseColor("#0079D7"));
                }
            }else{
                if (lastPosition == position) {
                    imageview.setImageResource(R.mipmap.attachment1);
                    textview.setTextColor(Color.parseColor("#0079D7"));
                } else {
                    imageview.setImageResource(R.mipmap.attachment2);
                    textview.setTextColor(Color.parseColor("#333333"));
                }
            }
        }
    }

    public void setSeclection(int position) {
        lastPosition = position;
    }
}




