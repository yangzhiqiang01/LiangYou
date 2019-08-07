package com.inhim.pj.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.dowloadfile.download.DownloadInfo;
import com.inhim.pj.dowloadfile.download.MyBusinessInfoDid;
import com.inhim.pj.dowloadfile.ui.ListActivity;
import com.inhim.pj.entity.ReaderInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.utils.StatusBarUtils;
import com.inhim.pj.utils.WXShareUtils;
import com.inhim.pj.view.BToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pili.pldroid.player.AVOptions;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Request;

//单个章节
@SuppressLint("SetJavaScriptEnabled")
public class RadioActivity extends BaseActivity implements
        JCVideoPlayerStandard.DowloadVedioListener{

    ImageView iv_back,iv_share;
    String name;
    public static String contents;
    private String photoUrl;

    JCVideoPlayerStandard mJcVideoPlayerStandard;
    private boolean mIsLiveStreaming=false;
    Long id;
    String videoUrl, videoPath;
    File videoFile;
    private String results;
    private String vedioName;
    private ReaderInfo.Reader readerInfo;
    private CheckBox checkbox;
    private Gson gson;
    //下载得视频 课程
    private DownloadInfo businessInfoDid;
    WebView webView;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    private String title ;
    private String description;

    @Override
    public Object offerLayout() {
        return R.layout.activity_radio;
    }
    @Override
    public void onBindView() {
        hideActionBar();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        gson = new Gson();
        initView();
        businessInfoDid = (DownloadInfo) getIntent().getSerializableExtra("result");
        //判断 是已下载视频 且内存中视频未被删除
        if (businessInfoDid != null && (new File(businessInfoDid.getFilePath())).exists()) {
            loadDownloadContent(businessInfoDid);
        } else {
            getReaderInfo(getIntent().getIntExtra("ReaderId", 0));
        }
    }

    @Override
    public void destory() {
        JCVideoPlayer.releaseAllVideos();
    }

    private void getReaderInfo(int readerId) {
        MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
        myOkHttpClient.asyncGet(Urls.getReaderInfo(readerId, PrefUtils.getString("token", "")), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Request request, String result) {
                ReaderInfo readerInfos = gson.fromJson(result, ReaderInfo.class);
                if (readerInfos.getCode() == 000) {
                    results = result;
                    loadContent(readerInfos);
                }
            }
        });
    }

    private void loadDownloadContent(DownloadInfo businessInfoDid) {
        title=businessInfoDid.getTitle() ;
        description=businessInfoDid.getSynopsis();
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
        title=readerInfo.getTitle() ;
        description=readerInfo.getSynopsis();
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
        RxPermissions rxPermissions = new RxPermissions(RadioActivity.this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                List<DownloadInfo> person = null;
                //查找当前url文件是否正在下载中
                if(businessInfoDid!=null){
                    person = LitePal.where("url = ?", businessInfoDid.getUrl()).find(DownloadInfo.class);
                }else{
                    try{
                        person = LitePal.where("url = ?", readerInfo.getUrl()).find(DownloadInfo.class);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (videoFile.exists()) {
                    BToast.showText("您已下载该视频");
                } else if((person!=null&&person.size()>0)){
                    Intent intent = new Intent(RadioActivity.this, ListActivity.class);
                    startActivity(intent);
                } else{
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
                            Intent intent = new Intent(RadioActivity.this, ListActivity.class);
                            startActivity(intent);
                        } else {
                            BToast.showText("下载失败");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                BToast.showText("读写权限被拒绝",true);
            }
        });

    }

    @Override
    public void fullScreen() {

    }

    @Override
    public void chapterPay() {

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
        iv_share.setOnClickListener(v -> {
            if(businessInfoDid!=null){
                WXShareUtils.show(RadioActivity.this,Urls.shareH5(Integer.valueOf(businessInfoDid.getReaderId())),title,description);
            }else{
                WXShareUtils.show(RadioActivity.this,Urls.shareH5(readerInfo.getReaderId()),title,description);
            }
        });
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> finish());
        checkbox = findViewById(R.id.checkbox);
        mJcVideoPlayerStandard = findViewById(R.id.jc_video);
        mJcVideoPlayerStandard.setListener(RadioActivity.this);
        webView = findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        checkbox.setOnClickListener(v -> {
            MyOkHttpClient myOkHttpClient = MyOkHttpClient.getInstance();
            myOkHttpClient.asyncJsonPost(Urls.collectionReader(readerInfo.getReaderId()), new HashMap(),
                    new MyOkHttpClient.HttpCallBack() {
                        @Override
                        public void onError(Request request, IOException e) {
                            if(checkbox.isChecked()){
                                checkbox.setChecked(false);
                            }else{
                                checkbox.setChecked(true);
                            }
                        }

                        @Override
                        public void onSuccess(Request request, String result) {
                            hideLoading();
                            try {
                                JSONObject jsonObject=new JSONObject(result);
                                if(jsonObject.getInt("code")!=0){
                                    BToast.showText(jsonObject.getString("msg"),false);
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
        if (contents == null || contents.length() == 0) {
            contents = "暂无内容";
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
        ImageLoaderUtils.setImage(photoUrl,mJcVideoPlayerStandard.thumbImageView);

        //屏幕保持常亮
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

}
