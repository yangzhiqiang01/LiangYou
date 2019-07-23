package com.inhim.pj.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.inhim.pj.adapter.provider.BannerItemProvider;
import com.inhim.pj.adapter.provider.ChapterItemProvider;
import com.inhim.pj.adapter.provider.CourseOnerItemProvider;
import com.inhim.pj.adapter.provider.CourseOtherItemProvider;
import com.inhim.pj.adapter.provider.ListenReaderTypeProvider;
import com.inhim.pj.adapter.provider.ReaderTypeProvider;
import com.inhim.pj.adapter.provider.StyleItemProvider;
import com.inhim.pj.adapter.provider.StyleTitleProvider;
import com.inhim.pj.entity.BannerList;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderStyle;
import com.inhim.pj.entity.ReaderTypeList;
import com.inhim.pj.entity.TitleAndSize;

import java.util.List;

/**
 * Created by jianghejie on 15/11/26.
 */
public class ListenAdapter<T> extends MultipleItemRvAdapter<T, BaseViewHolder> {
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
    //每日推荐
    public static final int STYLE = 500;
    //每日推荐标题
    public static final int STYLE_TITLE = 600;
    public static final int CHAPTER_ITEM = 700;
    private Context context;

    public ListenAdapter(@Nullable List<T> data, Context context) {
        super(data);
        this.context = context;
        finishInitialize();//调用该方法告知MultipleItemRvAdapter1已初始化完构造函数参数的传递
    }

    @Override
    protected int getViewType(T news) {
        if (news instanceof BannerList) {
            return BANNER;
        } else if (news instanceof TitleAndSize) {
            return CHAPTER;
        } else if (news instanceof String) {
            return STYLE_TITLE;
        } else if (news instanceof ReaderStyle.List) {
            return STYLE;
        } else if (news instanceof ReaderTypeList.List) {
            return CHAPTER_ITEM;
        } else if (news instanceof ReaderList.List) {
            return COURSE_OTHER;
        } else {
            return 0;
        }
    }

    @Override
    public void registerItemProvider() {
        //注册itemProvider
        mProviderDelegate.registerProvider(new BannerItemProvider());
        mProviderDelegate.registerProvider(new StyleTitleProvider());
        mProviderDelegate.registerProvider(new StyleItemProvider(context));
        //mProviderDelegate.registerProvider(new CourseOnerItemProvider(context));
        mProviderDelegate.registerProvider(new CourseOtherItemProvider(context));
        mProviderDelegate.registerProvider(new ListenReaderTypeProvider());
        mProviderDelegate.registerProvider(new ChapterItemProvider(context));
    }
}





















