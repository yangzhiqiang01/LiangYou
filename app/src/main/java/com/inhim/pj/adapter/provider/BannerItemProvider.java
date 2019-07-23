package com.inhim.pj.adapter.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.inhim.pj.R;
import com.inhim.pj.adapter.ReadingTwoAdapter;
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.entity.BannerList;
import com.inhim.pj.view.CustomRoundAngleImageView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.List;

/**
 * @author ChayChan
 * @description: 头部banner
 * @date 2018/3/22  14:48
 */

public class BannerItemProvider extends BaseItemProvider<BannerList, BaseViewHolder> {

    public BannerItemProvider() {
    }

    @Override
    public void convert(BaseViewHolder helper, BannerList news, int i) {
        if (news.getData() == null) {
            //如果没有标题，则直接跳过
            return;
        }
        try{
            //处理相关业务逻辑
            MZBannerView mMZBanner = helper.getView(R.id.bannerView);
            // 设置页面点击事件
            mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
                @Override
                public void onPageClick(View view, int position) {

                }
            });
            // 设置数据
            List<BannerList.Data> bannlist1 =news.getData();
            // 设置数据
            mMZBanner.setPages(bannlist1, new MZHolderCreator<LocalImageHolderView>() {
                @Override
                public LocalImageHolderView createViewHolder() {
                    return new LocalImageHolderView();
                }
            });

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


    class LocalImageHolderView implements MZViewHolder<BannerList.Data> {

        private CustomRoundAngleImageView imageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            imageView = view.findViewById(R.id.banner_image);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            return view;
        }
        @Override
        public void onBind(Context context, int position, BannerList.Data data) {
            // 数据绑定
            Glide.with(MyApplication.getContext()).load(data.getLink()).into(imageView);
            //imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
