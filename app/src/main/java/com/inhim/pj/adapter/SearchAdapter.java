package com.inhim.pj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.utils.StringFormatUtil;

import java.util.List;

/**
 * Created by hsmacmini on 2018/4/8.
 */

public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<ReaderList.List> list;
    private String keyword;
    public SearchAdapter(Context context, List<ReaderList.List> list, String keyword){
        this.list=list;
        this.context=context;
        this.keyword=keyword;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bean_list1, viewGroup, false);
            holder = new ViewHolder();
            assert view != null;
            holder.tv_name = view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String wholeStr = list.get(position).getTitle();
        StringFormatUtil spanStr = new StringFormatUtil(context, wholeStr,
                keyword, R.color.baseColor).fillColor();
        try {
            holder.tv_name.setText(spanStr.getResult());
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
    class ViewHolder {
        TextView tv_name;
    }
}
