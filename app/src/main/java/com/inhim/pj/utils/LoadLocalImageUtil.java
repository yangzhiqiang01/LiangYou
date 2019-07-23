package com.inhim.pj.utils;

import android.content.Context;
import android.widget.ImageView;

import com.inhim.pj.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/9/19 0019.
 * 异步加载本地图片工具类
 */
public class LoadLocalImageUtil {
        public LoadLocalImageUtil() {
        }

        private static LoadLocalImageUtil instance = null;

        public static synchronized LoadLocalImageUtil getInstance() {
            if (instance == null) {
                instance = new LoadLocalImageUtil();
            }
            return instance;
        }

        /**
         * 从内存卡中异步加载本地图片
         *
         * @param uri
         * @param imageView
         */
        public void displayFromSDCard(String uri, ImageView imageView) {
            // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
            ImageLoader.getInstance().displayImage("file://" + uri, imageView);
        }

        /**
         * 从assets文件夹中异步加载图片
         *
         * @param imageName
         *            图片名称，带后缀的，例如：1.png
         * @param imageView
         */
        public void dispalyFromAssets(String imageName, ImageView imageView) {
            // String imageUri = "assets://image.png"; // from assets
            ImageLoader.getInstance().displayImage("assets://" + imageName,
                    imageView);
        }

        /**
         * 从drawable中异步加载本地图片
         *
         * @param imageId
         * @param imageView
         */
        public void displayFromDrawable(int imageId, ImageView imageView) {
            // String imageUri = "drawable://" + R.drawable.image; // from drawables
            // (only images, non-9patch)
            ImageLoader.getInstance().displayImage("drawable://" + imageId,
                    imageView);
        }

        public void displayImage(Object path, ImageView imageView) {
            ImageLoader.getInstance().displayImage((String) path,imageView);
        }
        /**
         * 从内容提提供者中抓取图片
         */
        public void displayFromContent(String uri, ImageView imageView) {
            // String imageUri = "content://media/external/audio/albumart/13"; //
            // from content provider
            ImageLoader.getInstance().displayImage("content://" + uri, imageView);
        }
    /**
     * 从网络加载图片
     */
    public void displayFromUrl(String url, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//让图片进行内存缓存
                .cacheOnDisk(true)
                .build();//让图片进行sdcard缓存
                /*.showImageForEmptyUri(R.mipmap.pic_default)//图片地址有误
                .showImageOnFail(R.mipmap.pic_default)//当图片加载出现错误的时候显示的图片
                .showImageOnLoading(R.mipmap.pic_default)//图片正在加载的时候显示的图片*/

        ImageLoader.getInstance().displayImage(url,imageView,options);
    }

}
