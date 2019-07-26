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
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Request;

//单个章节
@SuppressLint("SetJavaScriptEnabled")
public class RadioActivity extends BaseActivity implements
        JCVideoPlayerStandard.DowloadVedioListener, MuluFragment.OnVideoLinear {

    ImageView iv_back;
    String name;
    public static String contents;
    private String photoUrl;

    JCVideoPlayerStandard mJcVideoPlayerStandard;
    private boolean mIsLiveStreaming;
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
    WebView webView;
    String content;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        instance = this;
        setContentView(R.layout.activity_radio);
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
                    loadContent(readerInfos);
                }
            }
        });
    }

    private void loadDownloadContent(MyBusinessInfoDid businessInfoDid) {
        checkbox.setChecked(Boolean.valueOf(businessInfoDid.getCollectionStatus()));
        name = businessInfoDid.getTitle();
        contents = businessInfoDid.getContent();
        photoUrl = businessInfoDid.getCover();
        videoPath = businessInfoDid.getFilePath();
        videoUrl = businessInfoDid.getUrl();
        videoFile = new File(videoPath);
        setVideo();
    }

    private void loadContent(ReaderInfo readerInfos) {
        readerInfo = readerInfos.getReader();
        checkbox.setChecked(readerInfo.getCollectionStatus());
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
    }

    @Override
    public void dowload() {
        if (videoFile.exists()) {
            BToast.showText("您已下载该视频");
        } else {
            try {
                JSONObject jsonObject = new JSONObject(results);
                MyBusinessInfo businessInfo = gson.fromJson(jsonObject.getJSONObject("reader").toString(), MyBusinessInfo.class);
                businessInfo.setFilePath(videoPath);
                businessInfo.setProgress("0");
                boolean istrue = businessInfo.save();
                if (istrue) {
                    Intent intent = new Intent(RadioActivity.this, ListActivity.class);
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
    protected void onDestroy() {
        super.onDestroy();
        //当前播放进度
        if(businessInfoDid!=null){
            int position = mJcVideoPlayerStandard.getCurrentPositionWhenPlaying();
            //获取当前播放进度
            int duration = getDuration();
            Log.e("position","==="+duration);
            float num= (float)position/duration;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String progressTime = df.format(num);//返回的是String类型
            //格式化fat,保留两位小数, 得到一个string字符串
            if(progressTime!=null&&!progressTime.equals("NaN")){
                MyBusinessInfoDid infoDid=businessInfoDid;
                DecimalFormat decimalFormat=new DecimalFormat(".00");
                String proess=decimalFormat.format(Float.valueOf(progressTime) * 100);
                infoDid.setProgress(proess+"%");
                infoDid.save();
                businessInfoDid.delete();
            }
        }
        unregisterReceiver(srearchreceiver);
        JCVideoPlayer.releaseAllVideos();
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
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkbox = findViewById(R.id.checkbox);
        mJcVideoPlayerStandard = findViewById(R.id.jc_video);
        mJcVideoPlayerStandard.setListener(RadioActivity.this);
        webView = findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
        if (content == null || content.length() == 0) {
            content = "暂无内容";
        }
        webView.loadDataWithBaseURL(null, contents, mimeType, encoding, null);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (videoFile.exists()) {
            mJcVideoPlayerStandard.setUp(videoPath
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, name);
        } else {
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


    private RadioActivity instance;
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

}
