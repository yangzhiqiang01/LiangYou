package com.inhim.pj.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.activity.CollectionActivity;
import com.inhim.pj.activity.HistoryActivity;
import com.inhim.pj.activity.LoginActivity;
import com.inhim.pj.activity.MyInfoActivity;
import com.inhim.pj.activity.SettingActivity;
import com.inhim.pj.activity.WebViewActivity;
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.dowloadvedio.ListActivity;
import com.inhim.pj.entity.UserInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.GlideCircleUtils;
import com.inhim.pj.utils.PermissionUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.utils.Util;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.WXShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
    private LinearLayout lin_1,lin_2,lin_3,lin_7,lin_4,lin_5,lin_6,lin_8;
    private Button btn_back_login;
    private Context mContext;
    private TextView tv_setting,mycollection,dowload_list,tv_code;

    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private int mTargetScene1 = SendMessageToWX.Req.WXSceneTimeline;
    private IWXAPI api;
    private WXShareDialog wxShareDialog;
    private UserInfo.User userInfo;
    private ImageView iv_photo;
    private TextView tv_name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        api = WXAPIFactory.createWXAPI(mContext, MyApplication.appID,false);
        api.registerApp(MyApplication.appID);
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
        String examUrl = Urls.getUserInfo;
        MyOkHttpClient.getInstance().asyncGet(examUrl, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Request request, String results) {
                Gson gson=new Gson();
                UserInfo smsResult = gson.fromJson(results, UserInfo.class);
                if (smsResult.getCode()==0) {
                    userInfo=smsResult.getUser();
                    if(isStart){
                        Intent intent1=new Intent(mContext, MyInfoActivity.class);
                        intent1.putExtra("result",userInfo);
                        startActivity(intent1);
                    }
                    tv_name.setText(userInfo.getRealname());
                    ImageLoader.getInstance().displayImage(userInfo.getHeadimgurl(),iv_photo);
                    //GlideCircleUtils.displayFromUrl(userInfo.getHeadimgurl(),iv_photo,mContext);
                } else {
                    BToast.showText(smsResult.getMsg(), Toast.LENGTH_LONG, false);
                }
            }
        });
    }

    private void initView(View view) {
        tv_name=view.findViewById(R.id.tv_name);
        iv_photo=view.findViewById(R.id.iv_photo);
        tv_code=view.findViewById(R.id.tv_code);
        tv_code.setText("当前版本V."+MyApplication.getVersion());
        dowload_list=view.findViewById(R.id.dowload_list);
        dowload_list.setOnClickListener(this);
        lin_1=view.findViewById(R.id.lin_1);
        lin_2=view.findViewById(R.id.lin_2);
        lin_3=view.findViewById(R.id.lin_3);
        lin_4=view.findViewById(R.id.lin_4);
        lin_5=view.findViewById(R.id.lin_5);
        lin_6=view.findViewById(R.id.lin_6);
        lin_7=view.findViewById(R.id.lin_7);
        lin_8=view.findViewById(R.id.lin_8);
        lin_1.setOnClickListener(this);
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
            case R.id.lin_1:
                if(userInfo==null){
                    getInfo(true);
                }else{
                    Intent intent1=new Intent(mContext, MyInfoActivity.class);
                    intent1.putExtra("result",userInfo);
                    startActivity(intent1);
                }
                break;
            case R.id.lin_2:
                Intent intent2=new Intent(mContext, CollectionActivity.class);
                startActivity(intent2);
                break;
            case R.id.lin_3:
                Intent intent3=new Intent(mContext, HistoryActivity.class);
                startActivity(intent3);
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
                Intent intent5=new Intent(getActivity(), ListActivity.class);
                startActivity(intent5);
                break;
            case R.id.lin_6:
                /***** 检查更新 *****/
                /***** 获取升级信息 *****/
                UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

                if (upgradeInfo == null) {
                    BToast.showText("已是最新版本");
                    return;
                }else if(upgradeInfo.versionCode> MyApplication.getVersionCode()){
                    Beta.checkUpgrade();
                }else{
                    BToast.showText("已是最新版本");
                }
                break;
            case R.id.lin_8:
                setDiaglog();
                break;
        }
    }

    private void setDiaglog(){
        View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wx_share, null);
        TextView tv_cancel=outerView.findViewById(R.id.tv_cancel);
        TextView tv_dete=outerView.findViewById(R.id.tv_dete);

        //点击确定
        tv_dete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendShare(1);
                wxShareDialog.dismiss();
            }
        });
        //点击取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendShare(0);
                wxShareDialog.dismiss();
            }
        });
        //防止弹出两个窗口
        if (wxShareDialog !=null && wxShareDialog.isShowing()) {
            return;
        }

        wxShareDialog = new WXShareDialog(getActivity(), R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        wxShareDialog.setContentView(outerView);
        wxShareDialog.show();//显示对话框
    }
    private void sendShare(int type){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://ly.bible.ac.cn/upload/android/app-release.apk";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "良友学院下载页";
        msg.description = "请点击网页进入并点击右上角\"···\"按钮,在浏览器打开，下载。";
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if(type==0){
            req.scene = mTargetScene;
        }else{
            req.scene = mTargetScene1;
        }
        api.sendReq(req);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    break;
                default:
                    break;
            }
        }
    };

    private void outLogin() {
        MyOkHttpClient myOkHttpClient=MyOkHttpClient.getInstance();
        myOkHttpClient.asyncJsonPost(Urls.onLogout, new HashMap(), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Request request, String result) {
                PrefUtils.remove("expire");
                PrefUtils.remove("token");
                PrefUtils.remove("isLogin");
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("msg").equals("success")){
                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
