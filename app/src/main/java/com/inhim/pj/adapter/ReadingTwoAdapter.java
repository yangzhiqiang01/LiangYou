package com.inhim.pj.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.inhim.pj.adapter.provider.BannerItemProvider;
import com.inhim.pj.adapter.provider.ChapterItemProvider;
import com.inhim.pj.adapter.provider.CourseOtherItemProvider;
import com.inhim.pj.adapter.provider.ReaderTypeProvider;
import com.inhim.pj.adapter.provider.SeriesProvider;
import com.inhim.pj.entity.BannerList;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.entity.ReaderTypeList;

import java.util.List;

/**
 * Created by jianghejie on 15/11/26.
 */
public class ReadingTwoAdapter<T> extends MultipleItemRvAdapter<T, BaseViewHolder> {
    /**
     * banner(广告)
     */
    public static final int BANNER = 100;
    /**
     * 章节标题
     */
    public static final int CHAPTER = 200;
    /**
     * 课程第一节布局区别于其他
     */
    public static final int COURSE_ONE = 300;
    /**
     * 其余课程布局
     */
    public static final int COURSE_OTHER = 400;
    public static final int STYLE = 500;
    //每日推荐标题
    public static final int STYLE_TITLE = 600;
    public static final int CHAPTER_ITEM = 700;
    //执行一次其他数据再次执行此数据就说明是当前数据的第一条数据
    private Context context;
    private int size=0;
    public ReadingTwoAdapter(@Nullable List<T> data, Context context) {
        super(data);
        this.context = context;
        finishInitialize();//调用该方法告知MultipleItemRvAdapter1已初始化完构造函数参数的传递
    }

    @Override
    protected int getViewType(T news) {
        //纪录隐藏最后一个课程数据item的横线
        /*if(news instanceof ReaderList.List){
            size++;
            Log.e("position11",size+"");
        }else{
            size=0;
        }*/
        if (news instanceof BannerList) {
            return BANNER;
        } else if (news instanceof ReaderStyle) {
            return CHAPTER;
        } else if (news instanceof String) {
            return STYLE_TITLE;
        } else if (news instanceof ReaderTypeList) {
            return CHAPTER_ITEM;
        } else if (news instanceof ReaderList.List) {
            return COURSE_OTHER;
        }/*else if (news instanceof ReaderTypeList.List) {
            return CHAPTER_ITEM;
        } */else {
            return 0;
        }
    }

    @Override
    public void registerItemProvider() {
        //注册itemProvider
        mProviderDelegate.registerProvider(new BannerItemProvider());
        mProviderDelegate.registerProvider(new SeriesProvider(context));
        //mProviderDelegate.registerProvider(new CourseOnerItemProvider(context));
        mProviderDelegate.registerProvider(new CourseOtherItemProvider(context,size));
        mProviderDelegate.registerProvider(new ReaderTypeProvider());
        //圆形类别 如牧师讲道
        mProviderDelegate.registerProvider(new ChapterItemProvider(context));
        //mProviderDelegate.registerProvider(new VideoTypeItemProvider(context));
    }
}





















