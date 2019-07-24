package com.inhim.pj.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.utils.GlideCircleUtils;
import com.inhim.pj.view.CircleImageView;

import java.util.List;

public class ChapterItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ReaderTypeList.List> datas;
    private Context context;
    public ChapterItemAdapter(ReaderTypeList datas, Context context) {
        this.datas = datas.getPage().getList();
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chapter_list, viewGroup, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PersonViewHolder) {
            try{
                GlideCircleUtils.displayFromUrl(datas.get(position).getIcon(),
                        ((PersonViewHolder) viewHolder).circleImageView,context);
                ((PersonViewHolder) viewHolder).textview.setText(datas.get(position).getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView textview;

        PersonViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            textview = itemView.findViewById(R.id.textview);
        }

    }
}
