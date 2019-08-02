package com.inhim.pj.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.inhim.pj.R;
import com.inhim.pj.adapter.RadioGridViewAdapter;
import com.inhim.pj.entity.CollectionTypeList;

import java.util.List;

public class MyPopuwindow {
    private PopupWindow popupwindow;
    private RadioGridViewAdapter popupwindowAdapter;
    private onClickLinear ClickLinear;
    public void setOnClickLinear(onClickLinear ClickLinear) {
        this.ClickLinear = ClickLinear;
    }

    public void ShowPopuwindow(Context context,  List<CollectionTypeList.TypeList> typeList, CheckBox view1) {
        View popupwindow_view = ((Activity) context).getLayoutInflater().inflate(R.layout.listvie_popuwindow, null, false);
        LinearLayout lin=popupwindow_view.findViewById(R.id.lin);
        lin.getBackground().setAlpha(100);//0~255透明度值
        popupwindow = new PopupWindow(popupwindow_view, getLayout(), getLayout()-view1.getHeight(), true);
        popupwindow.setFocusable(true);
        popupwindow.setOutsideTouchable(true);
        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        popupwindow.setTouchable(true);
        // 这里是位置显示方式,在屏幕的右侧
        popupwindow.showAsDropDown(view1, 0, 0);
        MyGridView gridView=popupwindow_view.findViewById(R.id.gridView);
        popupwindowAdapter = new RadioGridViewAdapter(context);
        popupwindowAdapter.setData(typeList);//传数组, 并指定默认值
        gridView.setAdapter(popupwindowAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                ClickLinear.callResult(position);
                popupwindowAdapter.setSeclection(position);//传值更新
                popupwindowAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取listview的高度，在TaskFragment中设置popupwindow中调用
     *
     * @return
     */
    public static int getLayout() {

        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public interface onClickLinear {
        void callResult(int position);
    }
}
