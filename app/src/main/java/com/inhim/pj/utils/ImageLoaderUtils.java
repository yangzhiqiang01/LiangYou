package com.inhim.pj.utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageLoaderUtils {
    public static void setImage(String url, ImageView imageView){
        if(url!=null&&!"".equals(url)){
            ImageLoader.getInstance().displayImage(url,imageView);
        }
    }
}
