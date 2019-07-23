package com.inhim.pj.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.dowloadvedio.ListActivity;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfo;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfoDid;
import com.inhim.pj.dowloadvedio.util.Config;
import com.inhim.pj.entity.ReaderInfo;
import com.inhim.pj.fragment.JIangyiFragment;
import com.inhim.pj.fragment.MuluFragment;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;
import com.pili.pldroid.player.AVOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Request;

//单个章节
@SuppressLint("SetJavaScriptEnabled")
public class VideoActivity extends BaseActivity implements OnClickListener,
        JCVideoPlayerStandard.DowloadVedioListener, MuluFragment.OnVideoLinear {

    ImageView iv_back;
    String name;
    public static String contents;
    private String photoUrl;

    JCVideoPlayerStandard mJcVideoPlayerStandard;
    private boolean mIsLiveStreaming;
    private ViewPager viewPager;
    private ArrayList<Fragment> fs;
    private RadioButton jiangyi, mulu;
    Long id;
    String videoUrl, videoPath;
    File videoFile;
    private String results;
    private FullScreenReceiver srearchreceiver;
    private String vedioName;
    //下载得视频 课程
    //private MyBusinessInfoDid resultDid;
    private ReaderInfo.Reader readerInfo;
    private CheckBox checkbox;
    private Gson gson;
    private MyBusinessInfoDid businessInfoDid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        instance = this;
        setContentView(R.layout.activity_video);
        initView();
        srearchreceiver = new FullScreenReceiver();
        IntentFilter finishfilter1 = new IntentFilter();
        finishfilter1.addAction("the.search.data");
        registerReceiver(srearchreceiver, finishfilter1);
        businessInfoDid = (MyBusinessInfoDid) getIntent().getSerializableExtra("result");
        //判断 是已下载视频 且内存中视频未被删除
        if (businessInfoDid != null&& (new File(businessInfoDid.getFilePath())).exists()) {
            loadDownloadContent(businessInfoDid);
        }else{
            getReaderInfo(getIntent().getIntExtra("ReaderId", 0), true);
        }
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
                    results=result;
                    loadContent(readerInfos, isOne);
                }
            }
        });
    }
    private void loadDownloadContent(MyBusinessInfoDid businessInfoDid){
        name = businessInfoDid.getTitle();
        contents = businessInfoDid.getContent();
        photoUrl = businessInfoDid.getCover();
        videoPath = businessInfoDid.getFilePath();
        videoUrl=businessInfoDid.getUrl();
        videoFile = new File(videoPath);
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
        initListener();
    }
    private void loadContent(ReaderInfo readerInfos, boolean isOne) {
        readerInfo = readerInfos.getReader();
        name = readerInfo.getTitle();
        if (readerInfo.getUrl() != null) {
            videoUrl = readerInfo.getUrl();
            vedioName = name + videoUrl.substring(videoUrl.length() - 4);
        }
        contents = readerInfo.getContent();
        photoUrl = readerInfo.getCover();
        videoPath = Urls.getFilePath() + vedioName;
        videoFile = new File(videoPath);
        JCVideoPlayer.releaseAllVideos();
        setVideo();
        if (isOne) {
            Bundle jiangyiBl = new Bundle();
            jiangyiBl.putString("content", contents);
            Bundle muluBl = new Bundle();
            muluBl.putSerializable("ReaderTypeId", readerInfo.getReaderTypeId());
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
            initListener();
        } else {
            Intent intent1 = new Intent();
            intent1.setAction("refresh.fragment.content");
            intent1.putExtra("content", contents);
            sendBroadcast(intent1);
        }

    }

    @Override
    public void dowload() {
        if (videoFile.exists()) {
            BToast.showText("您已下载该视频");
        } else {
            try {
                JSONObject jsonObject=new JSONObject(results);
                MyBusinessInfo businessInfo = gson.fromJson(jsonObject.getJSONObject("reader").toString(), MyBusinessInfo.class);
                businessInfo.setFilePath(videoPath);
                businessInfo.setProgress("0");
                boolean istrue=businessInfo.save();
                if(istrue){
                    Intent intent = new Intent(VideoActivity.this, ListActivity.class);
                    startActivity(intent);
                }else{
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
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(srearchreceiver);
        JCVideoPlayer.releaseAllVideos();
    }

    class MyUserActionStandard implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_ERROR:
                    Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_PAUSE:
                    Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_RESUME:
                    Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;

                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    Log.i("USER_EVENT", "unknow");
                    break;
            }
        }
    }

    private void initView() {
        checkbox = findViewById(R.id.checkbox);
        mulu = findViewById(R.id.mulu);
        jiangyi = findViewById(R.id.jiangyi);
        viewPager = findViewById(R.id.viewpager);
        mJcVideoPlayerStandard = findViewById(R.id.jc_video);
        mJcVideoPlayerStandard.setListener(VideoActivity.this);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
                myOkHttpClient.asyncJsonPost(Urls.collectionReader(readerInfo.getReaderId()), new HashMap(),
                        new MyOkHttpClient.HttpCallBack() {
                            @Override
                            public void onError(Request request, IOException e) {

                            }

                            @Override
                            public void onSuccess(Request request, String result) {
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
        if(videoFile.exists()){
            mJcVideoPlayerStandard.setUp(videoPath
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, name);
        }else{
            mJcVideoPlayerStandard.setUp(videoUrl
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, name);
        }
        Picasso.with(this)
                .load(photoUrl)
                .into(mJcVideoPlayerStandard.thumbImageView);

        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
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
            options.setString(AVOptions.KEY_CACHE_DIR, Config.DEFAULT_CACHE_DIR);
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

    private void initListener() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
