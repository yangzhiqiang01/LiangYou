package com.inhim.pj.utils;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.inhim.pj.app.MyApplication;

public class GlideUtils {
    public static void displayFromUrl(String url, final ImageView imageView){
        Glide.with(MyApplication.getContext()).load(url
        ).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                int width = resource.getWidth();
                int height = resource.getHeight();
                //获取imageView的宽
                int imageViewWidth= imageView.getWidth();
                //计算缩放比例
                float sy= (float) (imageViewWidth* 0.1)/(float) (width * 0.1);
                //计算图片等比例放大后的高
                int imageViewHeight= (int) (height * sy);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = imageViewHeight;
                imageView.setLayoutParams(params);
            }
        });
    }
}
