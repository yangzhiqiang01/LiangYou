package com.inhim.pj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.inhim.pj.R;

import java.util.List;


public class PopuwindowAdapter extends BaseAdapter {
	private List<String> list;
	private LayoutInflater inflater;
	public PopuwindowAdapter(Context context, List<String> list) {
		// TODO Auto-generated constructor stub
		this.list=list;
		this.inflater= LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_list, null);
			holder = new ViewHolder();
			holder.name=convertView.findViewById(R.id.file_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView name;
	}
}
