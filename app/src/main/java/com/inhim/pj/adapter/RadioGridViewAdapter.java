package com.inhim.pj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.CollectionTypeList;

import java.util.List;

public class RadioGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private int lastPosition;//定义一个标记为最后选择的位置
    private List<CollectionTypeList.TypeList> str = null;

    public void setData(List<CollectionTypeList.TypeList> typeList) {
        this.str = typeList;
    }

    public void setSeclection(int position) {
        lastPosition = position;
    }

    public RadioGridViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return str.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View mView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (mView == null) {
            holder = new ViewHolder();
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_gridview, null);
            holder.idGridviewTextview = mView.findViewById(R.id.idGridviewTextview);
            holder.idGridviewTextview1 = mView.findViewById(R.id.idGridviewTextview1);
            mView.setTag(holder);
        } else {
            holder = (ViewHolder) mView.getTag();
        }
        holder.idGridviewTextview.setText(str.get(position).getName());
        holder.idGridviewTextview1.setText(str.get(position).getName());
        if (lastPosition == position) {//最后选择的位置
            holder.idGridviewTextview.setVisibility(View.VISIBLE);
            holder.idGridviewTextview1.setVisibility(View.GONE);
        } else {
            holder.idGridviewTextview1.setVisibility(View.VISIBLE);
            holder.idGridviewTextview.setVisibility(View.GONE);
        }
        return mView;
    }

    class ViewHolder {
        private TextView idGridviewTextview,idGridviewTextview1;
    }
}