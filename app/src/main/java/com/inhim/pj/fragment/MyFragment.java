package com.inhim.pj.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.activity.CollectionActivity;
import com.inhim.pj.activity.HistoryActivity;
import com.inhim.pj.activity.HomeActivity;
import com.inhim.pj.activity.LoginActivity;
import com.inhim.pj.activity.MyInfoActivity;
import com.inhim.pj.activity.SettingActivity;
import com.inhim.pj.activity.WebViewActivity;
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.dowloadfile.ui.ListActivity;
import com.inhim.pj.entity.UserInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.utils.WXShareUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.LoadingView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private LinearLayout lin_2,lin_3,lin_7,lin_4,lin_5,lin_6,lin_8;
    private Button btn_back_login;
    private Context mContext;
    private TextView tv_setting,mycollection,dowload_list,tv_code;

    private UserInfo.User userInfo;
    private ImageView iv_photo;
    private TextView tv_name;
    private RelativeLayout rela1;
    private String token;
    private String webpageUrl="http://ly.bible.ac.cn/upload/android/app-release.apk";
    private String title = "良友学院下载页";
    private String description = "请点击网页进入并点击右上角\"···\"按钮,在浏览器打开，下载。";
    private LoadingView loadingView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getInfo(false);
    }

    private void getInfo(final boolean isStart) {
        token=PrefUtils.getString("token", "");
        if(!token.equals("")){
            loadingView=new LoadingView();
            loadingView.showLoading("加载中",getActivity());
            String examUrl = Urls.getUserInfo;
            MyOkHttpClient.getInstance().asyncGet(examUrl, new MyOkHttpClient.HttpCallBack() {
                @Override
                public void onError(Request request, IOException e) {
                    loadingView.hideLoading();
                }

                @Override
                public void onSuccess(Request request, String results) {
                    Gson gson=new Gson();
                    loadingView.hideLoading();
                    UserInfo smsResult = gson.fromJson(results, UserInfo.class);
                    if (smsResult.getCode()==0) {
                        userInfo=smsResult.getUser();
                        if(isStart){
                            Intent intent1=new Intent(mContext, MyInfoActivity.class);
                            intent1.putExtra("result",userInfo);
                            startActivity(intent1);
                        }
                        if(userInfo.getUsername()==null||"".equals(userInfo.getUsername())){
                            tv_name.setText(userInfo.getWechatUser().getNickname());
                        }else{
                            tv_name.setText(userInfo.getUsername());
                        }
                        if(userInfo.getHeadimgurl()==null||"".equals(userInfo.getHeadimgurl())){
                            ImageLoaderUtils.setImage(userInfo.getWechatUser().getHeadimgurl(),iv_photo);
                        }else{
                            ImageLoaderUtils.setImage(userInfo.getHeadimgurl(),iv_photo);
                        }
                    }else if(smsResult.getCode()==500){
                        PrefUtils.remove("expire");
                        PrefUtils.remove("token");
                        PrefUtils.remove("isLogin");
                        tv_name.setText("未登录");
                        iv_photo.setImageResource(R.mipmap.user_photo);
                        btn_back_login.setVisibility(View.GONE);
                        BToast.showText(smsResult.getMsg(), Toast.LENGTH_LONG, false);
                    } else {
                        BToast.showText(smsResult.getMsg(), Toast.LENGTH_LONG, false);
                    }
                }
            });
        }
    }

    private void initView(View view) {
        rela1=view.findViewById(R.id.rela1);
        rela1.setOnClickListener(this);
        tv_name=view.findViewById(R.id.tv_name);
        iv_photo=view.findViewById(R.id.iv_photo);
        tv_code=view.findViewById(R.id.tv_code);
        tv_code.setText("当前版本V."+MyApplication.getVersion());
        dowload_list=view.findViewById(R.id.dowload_list);
        dowload_list.setOnClickListener(this);
        lin_2=view.findViewById(R.id.lin_2);
        lin_3=view.findViewById(R.id.lin_3);
        lin_4=view.findViewById(R.id.lin_4);
        lin_5=view.findViewById(R.id.lin_5);
        lin_6=view.findViewById(R.id.lin_6);
        lin_7=view.findViewById(R.id.lin_7);
        lin_8=view.findViewById(R.id.lin_8);
        lin_2.setOnClickListener(this);
        lin_3.setOnClickListener(this);
        lin_4.setOnClickListener(this);
        lin_5.setOnClickListener(this);
        lin_6.setOnClickListener(this);
        lin_7.setOnClickListener(this);
        lin_8.setOnClickListener(this);
        tv_setting=view.findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(this);
        btn_back_login=view.findViewById(R.id.btn_back_login);
        btn_back_login.setOnClickListener(this);
        if(!PrefUtils.getBoolean("isLogin",false)){
            btn_back_login.setVisibility(View.GONE);
        }
        mycollection=view.findViewById(R.id.mycollection);
        mycollection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_2:
                if(token.equals("")){
                    Intent login=new Intent(mContext,LoginActivity.class);
                    startActivity(login);
                }else {
                    Intent intent2=new Intent(mContext, CollectionActivity.class);
                    startActivity(intent2);
                }

                break;
            case R.id.lin_3:
                if(token.equals("")){
                    Intent login=new Intent(mContext,LoginActivity.class);
                    startActivity(login);
                }else{
                    Intent intent3=new Intent(mContext, HistoryActivity.class);
                    startActivity(intent3);
                }

                break;
            case R.id.mycollection:
                Intent intent7=new Intent(getActivity(), WebViewActivity.class);
                intent7.putExtra("Type","about");
                startActivity(intent7);
                break;
            case R.id.btn_back_login:
                outLogin();
                break;
            case R.id.tv_setting:
                Intent intent4=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent4);
                break;
            case R.id.dowload_list:
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
                    if (granted) {
                        Intent intent5=new Intent(getActivity(), ListActivity.class);
                        startActivity(intent5);
                    } else {
                        BToast.showText("读写权限被拒绝",true);
                    }
                });
                break;
            case R.id.lin_6:
                /***** 检查更新 *****/
                /***** 获取升级信息 *****/
                UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

                if (upgradeInfo == null) {
                    BToast.showText("已是最新版本",true);
                    return;
                }else if(upgradeInfo.versionCode> MyApplication.getVersionCode()){
                    Beta.checkUpgrade();
                }else{
                    BToast.showText("已是最新版本",true);
                }
                break;
            case R.id.lin_8:
                WXShareUtils.show(getActivity(),webpageUrl,title,description);
                break;
            case R.id.rela1:
                if(token.equals("")){
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }else{
                    if(userInfo==null){
                        getInfo(true);
                    }else{
                        Intent intent1=new Intent(mContext, MyInfoActivity.class);
                        intent1.putExtra("result",userInfo);
                        startActivity(intent1);
                    }
                }

                break;
        }
    }

    private void outLogin() {
        loadingView=new LoadingView();
        loadingView.showLoading("退出中",getActivity());
        MyOkHttpClient myOkHttpClient=MyOkHttpClient.getInstance();
        myOkHttpClient.asyncJsonPost(Urls.onLogout, new HashMap(), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                loadingView.hideLoading();
            }

            @Override
            public void onSuccess(Request request, String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    loadingView.hideLoading();
                    if(jsonObject.getString("msg").equals("success")){
                        /*Intent intent=new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();*/
                        PrefUtils.remove("expire");
                        PrefUtils.remove("token");
                        PrefUtils.remove("isLogin");
                        token="";
                        tv_name.setText("点击登录");
                        iv_photo.setImageResource(R.mipmap.user_photo);
                        btn_back_login.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
