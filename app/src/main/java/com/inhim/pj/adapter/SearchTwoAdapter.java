package com.inhim.pj.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.entity.HistoricalRecordEntity;

import org.litepal.LitePal;

import java.util.List;


public class SearchTwoAdapter extends ArrayAdapter{
	private List<HistoricalRecordEntity>list;
	private LayoutInflater inflater;

	public SearchTwoAdapter(@NonNull Context context, int resource, List<HistoricalRecordEntity> list) {
		super(context, resource);
		this.list=list;
		this.inflater=LayoutInflater.from(context);
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
		ViewHolder holder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_search_list, null);
			holder = new ViewHolder();
			holder.name= convertView.findViewById(R.id.tv_name);
			holder.lin_1= convertView.findViewById(R.id.lin_1);
			convertView.setTag(holder);
			if(position==0){
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_title, parent, false);
				TitleHolder normalHolder = new TitleHolder();
				normalHolder.iv_delete=view.findViewById(R.id.iv_delete);
				holder.lin_1.addView(view,0);
				normalHolder.iv_delete.setOnClickListener(v -> {
					LitePal.deleteAll(HistoricalRecordEntity.class);
					list.clear();
					notifyDataSetChanged();
				});
			}

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getText());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		LinearLayout lin_1;
	}
	class TitleHolder {
		ImageView iv_delete;

	}
}
