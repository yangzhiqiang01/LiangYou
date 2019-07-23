package com.inhim.pj.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.inhim.pj.adapter.provider.BannerItemProvider;
import com.inhim.pj.adapter.provider.ChapterItemProvider;
import com.inhim.pj.adapter.provider.ChapterProvider;
import com.inhim.pj.adapter.provider.CourseOnerItemProvider;
import com.inhim.pj.adapter.provider.CourseOtherItemProvider;
import com.inhim.pj.entity.BannerList;
import com.inhim.pj.entity.ReaderList;
import com.inhim.pj.entity.ReaderTypeList;

import java.util.List;

/**
 * Created by jianghejie on 15/11/26.
 */
public class ChapterAdapter<T> extends MultipleItemRvAdapter<T, BaseViewHolder> {
    private List<T> data;
    /**
     * 课程第一节布局区别于其他
     */
    public static final int COURSE_ONE = 300;
    public ChapterAdapter(@Nullable List<T> data) {
        super(data);
        this.data = data;
        finishInitialize();//调用该方法告知MultipleItemRvAdapter1已初始化完构造函数参数的传递
    }

    @Override
    protected int getViewType(T news) {
        return COURSE_ONE;
    }

    @Override
    public void registerItemProvider() {
        //注册itemProvider
        mProviderDelegate.registerProvider(new ChapterProvider());
    }
}





















