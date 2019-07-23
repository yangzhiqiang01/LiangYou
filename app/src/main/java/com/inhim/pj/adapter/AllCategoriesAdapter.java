package com.inhim.pj.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.activity.ProjectActivity;
import com.inhim.pj.activity.ProjectListActivity;
import com.inhim.pj.base.ModeType;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.view.MyPopuwindow;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    Context context;
    public AllCategoriesAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context=context;
    }

    private ArrayList<String> list1;
    private ArrayList<ReaderTypeList.List> list2;
    private ArrayList<String> list3;
    private ArrayList<ReaderTypeList.List> list4;
    private ArrayList arrayList;
    private ArrayList<Integer> types;
    private Map<Integer, Integer> mPosition;
    /**
     * 创建一个方法供外面操作此数据
     */
    public void addList(ArrayList<String> list1s, ArrayList<ReaderTypeList.List> list2s,
                        ArrayList<String> list3s, ArrayList<ReaderTypeList.List> list4s) {
        types = new ArrayList<>();
        mPosition = new HashMap<>();
        this.list1 = list1s;
        this.list2 = list2s;
        this.list3 = list3s;
        this.list4 = list4s;
        arrayList=new ArrayList();
        arrayList.addAll(list1);
        arrayList.addAll(list2);
        arrayList.addAll(list3);
        arrayList.addAll(list4);
        addListByType(ModeType.TYPE_ONE, list1);
        addListByType(ModeType.TYPE_TWO, list2);
        addListByType(ModeType.TYPE_THREE, list3);
        addListByType(ModeType.TYPE_FOUR, list4);
    }
    private void addListByType(int type, ArrayList list) {
        mPosition.put(type, types.size());
        for (int i = 0; i < list.size(); i++) {
            types.add(type);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ModeType.TYPE_ONE:
                return new TitleHolder(mLayoutInflater.inflate(R.layout.item_all_categories_title, parent, false));
            case ModeType.TYPE_TWO:
                return new PersonViewHolder(mLayoutInflater.inflate(R.layout.item_categories, parent, false));
            case ModeType.TYPE_THREE:
                return new TitleHolder(mLayoutInflater.inflate(R.layout.item_all_categories_title, parent, false));
            case ModeType.TYPE_FOUR:
                return new PersonViewHolder(mLayoutInflater.inflate(R.layout.item_categories, parent, false));
        }
        return null;
    }
    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public TextView textview;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            textview=itemView.findViewById(R.id.textview);
        }
    }
    public class TitleHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public ImageView iv_close;
        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            iv_close=itemView.findViewById(R.id.iv_close);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        final int realPosition = position - mPosition.get(viewType);
        switch (viewType) {
            case ModeType.TYPE_ONE:
                TitleHolder titleHolder=(TitleHolder) holder;
                titleHolder.tv_title.setText(list1.get(realPosition));
                titleHolder.iv_close.setVisibility(View.VISIBLE);
                titleHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClickLinear.callResult();
                    }
                });
                break;
            case ModeType.TYPE_TWO:
                PersonViewHolder personViewHolder=(PersonViewHolder) holder;
                personViewHolder.textview.setText(list2.get(realPosition).getName());
                personViewHolder.textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, ProjectListActivity.class);
                        intent.putExtra("ReaderTypeList",list2.get(realPosition));
                        context.startActivity(intent);
                    }
                });
                break;
            case ModeType.TYPE_THREE:
                TitleHolder titleHolder2=(TitleHolder) holder;
                titleHolder2.tv_title.setText(list3.get(realPosition));
                titleHolder2.iv_close.setVisibility(View.GONE);
                break;
            case ModeType.TYPE_FOUR:
                PersonViewHolder personViewHolder2=(PersonViewHolder) holder;
                personViewHolder2.textview.setText(list4.get(realPosition).getName());
                personViewHolder2.textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, ProjectListActivity.class);
                        intent.putExtra("ReaderTypeList",list4.get(realPosition));
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }
    /**
     * 多种布局时候至关重要的方法
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //得到不同的布局类型
        if(types!=null){
            return types.get(position);
        }else{
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        if(types!=null){
            return types.size();
        }else{
            return 0;
        }
    }
    private onClickLinear ClickLinear;
    public void setOnClickLinear(onClickLinear ClickLinear) {
        this.ClickLinear = ClickLinear;
    }
    public interface onClickLinear {
        void callResult();
    }
}




