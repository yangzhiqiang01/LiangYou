package com.inhim.pj.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Movie;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.dowloadfile.download.DownloadInfo;
import com.inhim.pj.dowloadfile.download.MyBusinessInfoDid;
import com.inhim.pj.dowloadfile.ui.ListActivity;
import com.inhim.pj.entity.ReaderInfo;
import com.inhim.pj.fragment.JIangyiFragment;
import com.inhim.pj.fragment.MuluFragment;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.utils.StatusBarUtils;
import com.inhim.pj.utils.WXShareUtils;
import com.inhim.pj.view.BToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pili.pldroid.player.AVOptions;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Request;

//单个章节
@SuppressLint("SetJavaScriptEnabled")
public class VideoActivity extends BaseActivity implements OnClickListener,
        JCVideoPlayerStandard.DowloadVedioListener, MuluFragment.OnVideoLinear {

    ImageView iv_back, iv_share;
    String name;
    public static String contents;
    private String photoUrl;

    JCVideoPlayerStandard mJcVideoPlayerStandard;
    private boolean mIsLiveStreaming = false;
    private ViewPager viewPager;
    private ArrayList<Fragment> fs;
    private RadioButton jiangyi, mulu;
    Long id;
    String videoUrl, videoPath;
    File videoFile;
    private String results;
    private FullScreenReceiver srearchreceiver;
    private String vedioName;
    private ReaderInfo.Reader readerInfo;
    private CheckBox checkbox;
    private Gson gson;
    //下载得视频 课程
    private MyBusinessInfoDid businessInfoDid;
    private String title;
    private String description;
    private String TAG;

    @Override
    public Object offerLayout() {
        return R.layout.activity_video;
    }

    @Override
    public void onBindView() {
        hideActionBar();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        gson = new Gson();
        instance = this;
        TAG=getIntent().getStringExtra("TAG");
        initView();
        srearchreceiver = new FullScreenReceiver();
        IntentFilter finishfilter1 = new IntentFilter();
        finishfilter1.addAction("the.search.data");
        registerReceiver(srearchreceiver, finishfilter1);
        businessInfoDid = (MyBusinessInfoDid) getIntent().getSerializableExtra("result");
        //判断 是已下载视频 且内存中视频未被删除
        if (businessInfoDid != null && (new File(businessInfoDid.getFilePath())).exists()) {
            loadDownloadContent(businessInfoDid);
        } else {
            getReaderInfo(getIntent().getIntExtra("ReaderId", 0), true);
        }
    }

    @Override
    public void destory() {
        unregisterReceiver(srearchreceiver);
        JCVideoPlayer.releaseAllVideos();
    }

    private void getReaderInfo(int readerId, final boolean isOne) {
        showLoading("加载中");
        MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
        myOkHttpClient.asyncGet(Urls.getReaderInfo(readerId, PrefUtils.getString("token", "")), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                hideLoading();
                ReaderInfo readerInfos = gson.fromJson(result, ReaderInfo.class);
                if (readerInfos.getCode() == 000) {
                    results = result;
                    loadContent(readerInfos, isOne);
                }
            }
        });
    }

    private void loadDownloadContent(MyBusinessInfoDid businessInfoDid) {
        title = businessInfoDid.getTitle();
        description = businessInfoDid.getSynopsis();
        name = businessInfoDid.getTitle();
        contents = businessInfoDid.getContent();
        photoUrl = businessInfoDid.getCover();
        videoPath = businessInfoDid.getFilePath();
        videoUrl = businessInfoDid.getUrl();
        videoFile = new File(videoPath);
        checkbox.setChecked(Boolean.valueOf(businessInfoDid.getCollectionStatus()));
        setVideo();
        Bundle jiangyiBl = new Bundle();
        jiangyiBl.putString("content", contents);
        Bundle muluBl = new Bundle();
        muluBl.putSerializable("ReaderTypeId", businessInfoDid.getReaderTypeId());
        fs = new ArrayList<>();
        JIangyiFragment jiangyiFm = new JIangyiFragment();
        MuluFragment muluFragment = new MuluFragment();
        muluFragment.setvedioLinear(VideoActivity.this);
        jiangyiFm.setArguments(jiangyiBl);
        muluFragment.setArguments(muluBl);
        fs.add(jiangyiFm);
        fs.add(muluFragment);
        FragmentManager fm = getSupportFragmentManager();
        MyPagerAdapter adapter = new MyPagerAdapter(fm);
        viewPager.setAdapter(adapter);
        setListener();
    }

    private void loadContent(ReaderInfo readerInfos, boolean isOne) {
        readerInfo = readerInfos.getReader();
        title = readerInfo.getTitle();
        description = readerInfo.getSynopsis();
        checkbox.setChecked(readerInfo.getCollectionStatus());
        name = readerInfo.getTitle();
        if (readerInfo.getUrl() != null) {
            videoUrl = readerInfo.getUrl();
            vedioName = readerInfo.getUrl().substring(readerInfo.getUrl().lastIndexOf("/"));
        }
        contents = readerInfo.getContent();
        photoUrl = readerInfo.getCover();
        videoPath = Urls.getFilePath() + vedioName;
        videoFile = new File(videoPath);
        if (isOne) {
            Bundle jiangyiBl = new Bundle();
            jiangyiBl.putString("content", contents);
            Bundle muluBl = new Bundle();
            muluBl.putSerializable("ReaderTypeId", readerInfo.getReaderTypeId());
            muluBl.putSerializable("ReaderId", readerInfo.getReaderId());
            fs = new ArrayList<>();
            JIangyiFragment jiangyiFm = new JIangyiFragment();
            jiangyiFm.setArguments(jiangyiBl);
            fs.add(jiangyiFm);
            //只有从首页的视频页面进入视频才显示目录
            if(TAG.equals("VideoFragment")){
                MuluFragment muluFragment = new MuluFragment();
                muluFragment.setvedioLinear(VideoActivity.this);
                muluFragment.setArguments(muluBl);
                fs.add(muluFragment);
                mulu.setVisibility(View.VISIBLE);
                jiangyi.setVisibility(View.VISIBLE);
            }
           /* if(!readerInfo.getReaderStyleIdLink().contains("#3")){

            }*/
            FragmentManager fm = getSupportFragmentManager();
            MyPagerAdapter adapter = new MyPagerAdapter(fm);
            viewPager.setAdapter(adapter);
            setListener();
        } else {
            JCVideoPlayer.releaseAllVideos();
            Intent intent1 = new Intent();
            intent1.setAction("refresh.fragment.content");
            intent1.putExtra("content", contents);
            sendBroadcast(intent1);
        }
        setVideo();
    }

    @Override
    public void dowload() {
        //查找所有年龄小于25岁的人
        List<DownloadInfo> person = null;
        if(businessInfoDid!=null){
            person = LitePal.where("url = ?", businessInfoDid.getUrl()).find(DownloadInfo.class);
        }else{
            try{
                person = LitePal.where("url = ?", readerInfo.getUrl()).find(DownloadInfo.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (videoFile.exists()||(person!=null&&person.size()>0)) {
            BToast.showText("您已下载该视频");
        } else {
            try {
                JSONObject jsonObject = new JSONObject(results);
                DownloadInfo businessInfo = gson.fromJson(jsonObject.getJSONObject("reader").toString(), DownloadInfo.class);
                businessInfo.setFilePath(videoPath);
                businessInfo.setProgress(0);
                businessInfo.setProgressText("0");
                businessInfo.setFileName(businessInfo.getUrl().substring(businessInfo.getUrl().lastIndexOf("/")));
                businessInfo.setDownloadStatus("wait");
                boolean istrue = businessInfo.save();
                if (istrue) {
                    Intent intent = new Intent(VideoActivity.this, ListActivity.class);
                    startActivity(intent);
                } else {
                    BToast.showText("下载失败");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void fullScreen() {
        changeToFullScreen(true);
    }

    @Override
    public void chapterPay() {
        //进入页面第一次点击播放按钮记录播放次数的回调
    }

    @Override
    public void setVidoUrl(int ReaderId) {
        getReaderInfo(ReaderId, false);
    }

    class FullScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            changeToFullScreen(true);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jiangyi:
                viewPager.setCurrentItem(0);
                break;
            case R.id.mulu:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    private void setListener() {
        // TODO Auto-generated method stub
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        jiangyi.setChecked(true);
                        break;
                    case 1:
                        mulu.setChecked(true);
                        break;
                    default:
                        break;
                }

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当前播放进度
        if (businessInfoDid != null) {
            int position = mJcVideoPlayerStandard.getCurrentPositionWhenPlaying();
            //获取当前播放进度
            int duration = getDuration();
            float num = (float) position / duration;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String progressTime = df.format(num);//返回的是String类型
            //格式化fat,保留两位小数, 得到一个string字符串
            if (progressTime != null && !progressTime.equals("NaN")) {
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                String proess = decimalFormat.format(Float.valueOf(progressTime) * 100);
                MyBusinessInfoDid myBusinessInfoDid = new MyBusinessInfoDid();
                //第二步，改变某个字段的值
                myBusinessInfoDid.setProgressText("已观看"+proess + "%");
                //第三步，保存数据
                //更新所有readerId为readerId的记录,记录观看进度
                myBusinessInfoDid.updateAll("readerId = ?", businessInfoDid.getReaderId());
            }
        }
    }

    public int getDuration() {
        //视频总时间
        int duration = 0;
        if (JCMediaManager.instance().mediaPlayer == null) return duration;
        try {
            duration = JCMediaManager.instance().mediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    private void initView() {
        iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessInfoDid != null) {
                    WXShareUtils.show(VideoActivity.this, Urls.shareH5(Integer.valueOf(businessInfoDid.getReaderId())), title, description);
                } else {
                    WXShareUtils.show(VideoActivity.this, Urls.shareH5(readerInfo.getReaderId()), title, description);
                }
            }
        });
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkbox = findViewById(R.id.checkbox);
        mulu = findViewById(R.id.mulu);
        jiangyi = findViewById(R.id.jiangyi);
        viewPager = findViewById(R.id.viewpager);
        mJcVideoPlayerStandard = findViewById(R.id.jc_video);
        mJcVideoPlayerStandard.setListener(VideoActivity.this);
        checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
                myOkHttpClient.asyncJsonPost(Urls.collectionReader(readerInfo.getReaderId()), new HashMap(),
                        new MyOkHttpClient.HttpCallBack() {
                            @Override
                            public void onError(Request request, IOException e) {
                                if (checkbox.isChecked()) {
                                    checkbox.setChecked(false);
                                } else {
                                    checkbox.setChecked(true);
                                }
                            }

                            @Override
                            public void onSuccess(Request request, String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getInt("code") != 0) {
                                        BToast.showText(jsonObject.getString("msg"), false);
                                        //收藏失败时将按钮状态还原
                                        if(checkbox.isChecked()){
                                            checkbox.setChecked(false);
                                        }else{
                                            checkbox.setChecked(true);
                                        }
                                    }else{
                                        if(checkbox.isChecked()){
                                            BToast.showText("收藏成功",true);
                                        }else{
                                            BToast.showText("已取消收藏",true);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }


    private void setVideo() {
        if (videoFile.exists()) {
            mJcVideoPlayerStandard.setUp(videoPath
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, name);
        } else {
            mJcVideoPlayerStandard.setUp(videoUrl
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, name);
        }
        /*Picasso.with(this)
                .load(photoUrl)
                .into(mJcVideoPlayerStandard.thumbImageView);*/
        ImageLoaderUtils.setImage(photoUrl, mJcVideoPlayerStandard.thumbImageView);
        //屏幕保持常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, mIsLiveStreaming ? 1 : 0);
        boolean disableLog = getIntent().getBooleanExtra("disable-log", false);
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        boolean cache = getIntent().getBooleanExtra("cache", false);
        if (!mIsLiveStreaming && cache) {
            options.setString(AVOptions.KEY_CACHE_DIR, Urls.getFilePath());
        }
        boolean vcallback = getIntent().getBooleanExtra("video-data-callback", false);
        if (vcallback) {
            options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);
        }

    }


    private VideoActivity instance;
    private boolean isFullScreen;

    /**
     * 横屏
     * isLeft参数代表是向左横屏还是向右横屏
     *
     * @param
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (isFullScreen == true)) {
            fullScreenToNormal();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    // 竖屏
    private void fullScreenToNormal() {
        isFullScreen = false;
        instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private void changeToFullScreen(boolean isLeft) {
        isFullScreen = true;
        if (isLeft) {
            // 向左横屏
            instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            // 向右横屏
            instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    /**
     * 自定义适配器  FragmentPagerAdapter
     */
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fs.get(arg0);
        }

        @Override
        public int getCount() {
            return fs.size();
        }
    }

}
