package com.inhim.pj.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


public class CenterDialog extends Dialog {

    public CenterDialog(Context context) {
        super(context);
        init();
    }

    public CenterDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected CenterDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        //获取当前Activity所在的窗体
        Window dialogWindow = this.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);

    }

    @Override
    public void show() {
        super.show();
    }
}
