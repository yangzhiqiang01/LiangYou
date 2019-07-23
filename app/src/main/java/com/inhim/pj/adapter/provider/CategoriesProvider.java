package com.inhim.pj.adapter.provider;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.entity.BannerList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChayChan
 * @description: 头部banner
 * @date 2018/3/22  14:48
 */

public class CategoriesProvider extends BaseItemProvider<BannerList, BaseViewHolder> {

    public CategoriesProvider() {
    }

    @Override
    public void convert(BaseViewHolder helper, BannerList news, int i) {
        if (news.getData() == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            //处理相关业务逻辑
            ConvenientBanner bannerView = helper.getView(R.id.bannerView);
            // 设置数据
            List bannlist1 = new ArrayList<>();
            for (int j = 0; j < news.getData().size(); j++) {
                bannlist1.add(news.getData().get(j).getImgUrl());
            }
            bannerView.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new LocalImageHolderView();
                }
            }, bannlist1) //mList是图片地址的集合
                    .setPointViewVisible(true)    //设置指示器是否可见
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                    //设置指示器位置（左、中、右）
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .startTurning(4000)     //设置自动切换（同时设置了切换时间间隔）
                    .setManualPageable(true);  //设置手动影响（设置了该项无法手动切换）
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int viewType() {
        return ReadingTwoAdapter.BANNER;
    }

    @Override
    public int layout() {
        return R.layout.recyclerview_header;
    }


    class LocalImageHolderView implements Holder<String> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            //glide加载出图片，data是传过来的图片地址，
            Glide.with(context).load(data).into(imageView);
        }
    }
}
