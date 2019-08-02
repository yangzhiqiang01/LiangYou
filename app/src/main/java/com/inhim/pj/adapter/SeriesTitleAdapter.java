package com.inhim.pj.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.utils.ScreenUtils;
import com.inhim.pj.view.CustomRoundAngleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SeriesTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ReaderStyle.List> datas;
    private Context context;
    public SeriesTitleAdapter(ReaderStyle readerStyle, Context context) {
        this.datas = readerStyle.getList();
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_style_title, viewGroup, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof PersonViewHolder) {
            try{
                ImageLoader.getInstance().displayImage(datas.get(position).getReaderStyleValue().getCover(),
                        ((PersonViewHolder) viewHolder).imageview);
                ((PersonViewHolder) viewHolder).textview1.setText(datas.get(position).getReaderStyleValue().getValue());
                ((PersonViewHolder) viewHolder).textview2.setText("共"+datas.get(position).getTotal()+"篇文章");
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

        public CustomRoundAngleImageView imageview;
        public TextView textview1,textview2;
        public ConstraintLayout constran,black_constran;
        PersonViewHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            textview1 = itemView.findViewById(R.id.textview1);
            textview2 = itemView.findViewById(R.id.textview2);
            constran=itemView.findViewById(R.id.constran);
            black_constran=itemView.findViewById(R.id.black_constran);
            black_constran.getBackground().setAlpha(100);//0~255透明度值
            //动态计算屏幕宽度 一行显示四个item
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ((context.getResources().getDisplayMetrics().
                    widthPixels - ScreenUtils.dip2px(context, 5)) / 1.1f),

                    ViewGroup.LayoutParams.MATCH_PARENT);
            constran.setLayoutParams(params);
        }

    }
}
