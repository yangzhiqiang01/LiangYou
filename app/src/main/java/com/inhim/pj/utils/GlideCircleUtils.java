package com.inhim.pj.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

public class GlideCircleUtils {
    public static void displayFromUrl(String url, final ImageView imageView, final Context context){
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = imageView.getWidth();
                int imageHeight = imageView.getHeight();
                ViewGroup.LayoutParams para = imageView.getLayoutParams();
                para.height = imageWidth;
                para.width = imageHeight;
                imageView.setImageBitmap(resource);
            }
        });
    }
}
