package com.inhim.pj.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.inhim.pj.MainActivity;
import com.inhim.pj.R;
import com.inhim.pj.http.Urls;
import com.inhim.pj.view.BToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends LitePalApplication {
    private static Context context;
    private static Context mContext;
    private ImageLoader imageLoader;
    public static String appID = "wxd48ba30205b8827a";
    public static final String APP_ID = "c60234e621"; // TODO 替换成bugly上注册的appid
    // TODO 自定义渠道
    public static final String APP_CHANNEL = "lyxy";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getBaseContext();
        context = this;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        setBugly();
        isExist(Urls.getFilePath());
    }

    /**
     * @param path 文件夹路径
     */
    public static void isExist(String path) {
        File file = new File(path);
//判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private void setBugly() {
/***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.ic_launcher;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher;

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        /***** Bugly高级设置 *****/
        BuglyStrategy strategy = new BuglyStrategy();
        /**
         * 设置app渠道号
         */
        strategy.setAppChannel(APP_CHANNEL);

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(this, APP_ID, true, strategy);
    }

    public static Context getInstance() {
        return context;
    }

    public static Context getContext() {
        return mContext;
    }

    public boolean isMobile() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            BToast.showText("没有联网！", false);
            return false;
        }
        if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            //连接的网络是移动数据流量
            return false;
        } else {
            //不是移动数据流量
            return true;
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static int getVersionCode() {
        try {
            PackageManager manager = MyApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            int version = info.versionCode;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static String getVersion() {
        try {
            PackageManager manager = MyApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            String versionName = info.versionName;
            return  versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }
}
