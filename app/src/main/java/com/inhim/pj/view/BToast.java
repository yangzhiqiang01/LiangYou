package com.inhim.pj.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inhim.pj.R;
import com.inhim.pj.app.MyApplication;

public class BToast extends Toast {
    private static ImageView toast_img;
    /**
     * Toast单例
     */
    private static BToast toast;

    /**
     * 构造
     *
     * @param context
     */
    public BToast(Context context) {
        super(context);
    }
    /**
     * 隐藏当前Toast
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {

        }
    }


    /**
     * 初始化Toast
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    private static void initToast(Context context, CharSequence text) {
        try {
            cancelToast();

            toast = new BToast(context);

            // 获取LayoutInflater对象
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 由layout文件创建一个View对象
            View layout = inflater.inflate(R.layout.toast_layout, null);

            // 吐司上的图片
            toast_img = layout.findViewById(R.id.toast_img);

            // 吐司上的文字
            TextView toast_text = layout.findViewById(R.id.toast_text);
            toast_text.setText(text);
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 70);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图标状态 不显示图标
     */
    private static final int TYPE_HIDE = -1;
    /**
     * 图标状态 显示√
     */
    private static final int TYPE_TRUE = 0;
    /**
     * 图标状态 显示×
     */
    private static final int TYPE_FALSE = 1;

    /**
     * 显示Toast
     *
     * @param text    显示的文本
     * @param time    显示时长
     * @param imgType 图标状态
     */
    private static void showToast( CharSequence text, int time, int imgType) {
        // 初始化一个新的Toast对象
        initToast(MyApplication.getInstance(), text);

        // 设置显示时长
        if (time == Toast.LENGTH_LONG) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        // 判断图标是否该显示，显示√还是×
        if (imgType == TYPE_HIDE) {
            toast_img.setVisibility(View.GONE);
        } else {
            if (imgType == TYPE_TRUE) {
                toast_img.setBackgroundResource(R.mipmap.toast_prompt);
            } else {
                toast_img.setBackgroundResource(R.mipmap.toast_error_prompt);
            }
            toast_img.setVisibility(View.VISIBLE);

            // 动画
            //ObjectAnimator.ofFloat(toast_img, "rotationY", 0, 360).setDuration(1700).start();
        }

        // 显示Toast
        toast.show();
    }

    /**
     * 显示一个纯文本吐司
     *
     * @param text    显示的文本
     */
    public static void showText( CharSequence text) {
        showToast( text, Toast.LENGTH_SHORT, TYPE_HIDE);
    }

    /**
     * 显示一个带图标的吐司
     *
     * @param text      显示的文本
     * @param isSucceed 显示【对号图标】还是【叉号图标】
     */
    public static void showText( CharSequence text, boolean isSucceed) {
        showToast( text, Toast.LENGTH_SHORT, isSucceed ? TYPE_TRUE : TYPE_FALSE);
    }

    /**
     * 显示一个纯文本吐司
     *
     * @param text    显示的文本
     * @param time    持续的时间
     */
    public static void showText( CharSequence text, int time) {
        showToast( text, time, TYPE_HIDE);
    }

    /**
     * 显示一个带图标的吐司
     *
     * @param text      显示的文本
     * @param time      持续的时间
     * @param isSucceed 显示【对号图标】还是【叉号图标】
     */
    public static void showText( CharSequence text, int time, boolean isSucceed) {
        showToast( text, time, isSucceed ? TYPE_TRUE : TYPE_FALSE);
    }
}