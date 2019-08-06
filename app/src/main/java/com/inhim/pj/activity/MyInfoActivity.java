package com.inhim.pj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.CommonDictEntity;
import com.inhim.pj.entity.UserInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.DateUtils;
import com.inhim.pj.utils.GlideCircleUtils;
import com.inhim.pj.utils.ImageLoaderUtils;
import com.inhim.pj.utils.OkhttpUploadUtils;
import com.inhim.pj.utils.PermissionUtils;
import com.inhim.pj.utils.StatusBarUtils;
import com.inhim.pj.utils.ViewShowUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.ChooseDialog;
import com.inhim.pj.view.MonPickerDialog;
import com.inhim.pj.wheelview.WheelView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import okhttp3.Request;

/**
 * Created by Administrator on 2017-11-29.
 */

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_name, ed_ethnic,
            ed_email, user_name;
    private TextView tv_belief, tv_marriage, tv_kiddo, tv_education, ed_believing_year, ed_bebaptized_year, tv_telephone;
    private List beliefStringList, kiddoList, marriageStringList, educationStringList;
    private ChooseDialog chooseDialog;
    private ImageView iv_photo;
    private String headimgurl;
    private String takePicOri, cropPicTmp;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_PICTURE = 2;
    public static final int CHOICE_PICTURE = 0;
    private EditText ed_zhiye;
    private TextView tvCourse;
    private Button btn_submit;
    private Calendar calendar;
    private Gson gson;
    private int resultCode = 100;
    private UserInfo.User userInfo;

    @Override
    public Object offerLayout() {
        return R.layout.activity_myinfo;
    }

    @Override
    public void onBindView() {
        hideActionBar();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        gson = new Gson();
        initView();
        userInfo = (UserInfo.User) getIntent().getSerializableExtra("result");
        setView(userInfo);
        beliefStringList = new ArrayList();
        marriageStringList = new ArrayList();
        educationStringList = new ArrayList();
        kiddoList = new ArrayList();
        kiddoList.add("没有");
        kiddoList.add("有");
        calendar = Calendar.getInstance();
        setLinear();
    }

    @Override
    public void destory() {

    }

    private void initView() {
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        user_name = findViewById(R.id.user_name);
        ed_name = findViewById(R.id.ed_name);
        ed_ethnic = findViewById(R.id.ed_ethnic);
        ed_believing_year = findViewById(R.id.ed_believing_year);
        ed_bebaptized_year = findViewById(R.id.ed_bebaptized_year);
        tv_telephone = findViewById(R.id.tv_telephone);
        tv_telephone.setOnClickListener(this);
        ed_email = findViewById(R.id.ed_email);
        tv_belief = findViewById(R.id.tv_belief);
        tv_marriage = findViewById(R.id.tv_marriage);
        tv_kiddo = findViewById(R.id.tv_kiddo);
        tv_education = findViewById(R.id.tv_education);
        iv_photo = findViewById(R.id.iv_photo);
        ed_zhiye = findViewById(R.id.ed_zhiye);
        tvCourse = findViewById(R.id.tvCourse);
        tvCourse.setText("我的信息");
    }

    private void setView(UserInfo.User userInfoEntity) {
        ViewShowUtils.show(user_name, userInfoEntity.getUsername());
        ViewShowUtils.show(ed_zhiye, userInfoEntity.getOccupation());
        ViewShowUtils.show(ed_name, userInfoEntity.getRealname());
        ViewShowUtils.show(ed_ethnic, userInfoEntity.getNation());
        ViewShowUtils.show(ed_believing_year, userInfoEntity.getFaithTime());
        ViewShowUtils.show(ed_bebaptized_year, userInfoEntity.getBaptismTime());
        ViewShowUtils.show(tv_telephone, userInfoEntity.getMobile());
        ViewShowUtils.show(ed_email, userInfoEntity.getMail());
        ViewShowUtils.show(tv_belief, userInfoEntity.getFaithStatus());
        ViewShowUtils.show(tv_marriage, userInfoEntity.getMaritalStatus());
        ViewShowUtils.show(tv_kiddo, userInfoEntity.getChildrenStatus());
        ViewShowUtils.show(tv_education, userInfoEntity.getEducationLevel());
        if (userInfo.getHeadimgurl() == null || "".equals(userInfo.getHeadimgurl())) {
            ImageLoaderUtils.setImage(userInfo.getWechatUser().getHeadimgurl(), iv_photo);
        } else {
            ImageLoaderUtils.setImage(userInfo.getHeadimgurl(), iv_photo);
        }
        //GlideCircleUtils.displayFromUrl(userInfoEntity.getHeadimgurl(),iv_photo,MyInfoActivity.this);
    }

    private void getCommonDict(final String TAGs, String code) {
        showLoading("加载中");
        MyOkHttpClient.getInstance().asyncGet(Urls.getCommonDict(code), new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                hideLoading();
                BToast.showText("请求失败", false);
            }

            @Override
            public void onSuccess(Request request, String result) {
                hideLoading();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 0) {
                        CommonDictEntity commonDictEntity = gson.fromJson(result, CommonDictEntity.class);
                        if (TAGs.equals("belief")) {
                            for (int i = 0; i < commonDictEntity.getList().size(); i++) {
                                beliefStringList.add(commonDictEntity.getList().get(i).getName());
                            }
                            setDiaglog(beliefStringList, tv_belief);
                        } else if (TAGs.equals("educationLevel")) {
                            for (int i = 0; i < commonDictEntity.getList().size(); i++) {
                                educationStringList.add(commonDictEntity.getList().get(i).getName());
                            }
                            setDiaglog(educationStringList, tv_education);
                        } else if (TAGs.equals("marital")) {
                            for (int i = 0; i < commonDictEntity.getList().size(); i++) {
                                marriageStringList.add(commonDictEntity.getList().get(i).getName());
                            }
                            setDiaglog(marriageStringList, tv_marriage);
                        }

                    } else {
                        BToast.showText(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setLinear() {
        //信仰状况 vip.faith
        tv_belief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (beliefStringList.size() == 0) {
                    getCommonDict("belief", "vip.faith");
                } else {
                    setDiaglog(beliefStringList, tv_belief);
                }
            }
        });
        //教育程度 vip.educationLevel
        tv_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (educationStringList.size() == 0) {
                    getCommonDict("educationLevel", "vip.educationLevel");
                } else {
                    setDiaglog(educationStringList, tv_education);
                }
            }
        });
        //子女 1 有 0没有
        tv_kiddo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDiaglog(kiddoList, tv_kiddo);
            }
        });
        //婚姻状况 vip.marital
        tv_marriage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marriageStringList.size() == 0) {
                    getCommonDict("marital", "vip.marital");
                } else {
                    setDiaglog(marriageStringList, tv_marriage);
                }
            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicturesDialog();
            }
        });

        ed_believing_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MonPickerDialog(MyInfoActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        //calendar.set(Calendar.MONTH, monthOfYear);
                        ed_believing_year.setText(DateUtils.clanderTodatetime(calendar, "yyyy"));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            }
        });
        ed_bebaptized_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MonPickerDialog(MyInfoActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        //calendar.set(Calendar.MONTH, monthOfYear);
                        ed_bebaptized_year.setText(DateUtils.clanderTodatetime(calendar, "yyyy"));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            }
        });

    }


    /**
     * 显示添加图片选择对话框
     */

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicOri = Urls.getFilePath() + "touxiang.jpg";
        File file = new File(takePicOri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(file));
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private Uri getUriForFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(getApplicationContext(), "com.inhim.pj.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_CAMERA:
                    takePicture();

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 权限申请结果
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }


    private void choicePicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOICE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {//照相
            if (resultCode == Activity.RESULT_OK && takePicOri != null) {
                startCropPicture(getUriForFile(new File(takePicOri)));
            }
            takePicOri = null;
        } else if (requestCode == CHOICE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                startCropPicture(uri);
            }
            //在这里上传图片
        } else if (requestCode == CROP_PICTURE) {
            if (cropPicTmp != null) {
                upLoad(new File(cropPicTmp));
            }
        } else if (requestCode == resultCode) {
            ViewShowUtils.show(tv_telephone, data.getStringExtra("phoneNumber"));
        }
    }

    private void upLoad(File uploadFile) {
        showLoading("上传中");
        OkhttpUploadUtils okhttpUploadUtils = new OkhttpUploadUtils(new OkhttpUploadUtils.HttpCallBack() {
            @Override
            public void onError(okhttp3.Request request, IOException e) {
                BToast.showText("上传失败", false);
                hideLoading();
            }

            @Override
            public void onSuccess(okhttp3.Request request, String result) {
                hideLoading();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 0) {
                        headimgurl = jsonObject.getString("url");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GlideCircleUtils.displayFromUrl(headimgurl, iv_photo, MyInfoActivity.this);
                                BToast.showText("上传成功", true);
                            }
                        });

                    } else {
                        BToast.showText(jsonObject.getString("msg"), false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /**"msg":"success",
                 "code":0,
                 "url":
                 "http://ad-yp-easylife-image.oss-cn-shenzhen.aliyuncs.com/20190720/fee699df575d4b55a030cf2001f38760.png"*/
            }
        });
        okhttpUploadUtils.upLoad(Urls.fileUpload, uploadFile);
    }

    /**
     * 裁剪图片
     */
    private void startCropPicture(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// 进行修剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //cropPicTmp = getPicPath(listPaths.size() + 1);
        cropPicTmp = Urls.getFilePath() + System.currentTimeMillis() + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cropPicTmp)));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// no face detection
        if (!TextUtils.isEmpty(cropPicTmp)) {
            startActivityForResult(intent, CROP_PICTURE);
        }

    }

    private void showPicturesDialog() {
        View outerView = LayoutInflater.from(MyInfoActivity.this).inflate(R.layout.dialog_pictures, null);
        TextView textView1 = outerView.findViewById(R.id.textView1);
        TextView textView2 = outerView.findViewById(R.id.textView2);
        TextView textView3 = outerView.findViewById(R.id.textView3);

        //点击确定
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseDialog.dismiss();
                if (Integer.parseInt(Build.VERSION.SDK) <= 23) {
                    takePicture();
                } else {
                    PermissionUtils.requestPermission(MyInfoActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
                }
            }
        });
        //点击取消
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseDialog.dismiss();
                choicePicture();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseDialog.dismiss();
            }
        });
        //防止弹出两个窗口
        if (chooseDialog != null && chooseDialog.isShowing()) {
            return;
        }

        chooseDialog = new ChooseDialog(MyInfoActivity.this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        chooseDialog.setContentView(outerView);
        chooseDialog.show();//显示对话框
    }

    private void setDiaglog(List list, final TextView textView) {
        View outerView = LayoutInflater.from(MyInfoActivity.this).inflate(R.layout.dialog_choose, null);
        final WheelView wv = outerView.findViewById(R.id.wheel_view_wv);
        TextView tv_cancel = outerView.findViewById(R.id.tv_cancel);
        TextView tv_dete = outerView.findViewById(R.id.tv_dete);
        wv.setItems(list, 0);
        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index, String item) {
                //选择是执行
            }
        });

        //点击确定
        tv_dete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textView.setText(wv.getSelectedItem());
                chooseDialog.dismiss();
            }
        });
        //点击取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseDialog.dismiss();
            }
        });
        //防止弹出两个窗口
        if (chooseDialog != null && chooseDialog.isShowing()) {
            return;
        }

        chooseDialog = new ChooseDialog(MyInfoActivity.this, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        chooseDialog.setContentView(outerView);
        chooseDialog.show();//显示对话框
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_telephone:
                Intent intent = new Intent(MyInfoActivity.this, ReplacePhoneActivity.class);
                startActivityForResult(intent, resultCode);
                break;
            case R.id.btn_submit:
                updateInfo();
                break;
            default:
                break;
        }
    }

    private void updateInfo() {
        showLoading("提交中");
        HashMap build = getFormBody();
        MyOkHttpClient.getInstance().asyncPut(Urls.updateUserInfo,

                build, new MyOkHttpClient.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        hideLoading();
                        BToast.showText("请求失败", false);
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        hideLoading();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 0) {
                                BToast.showText("更改成功", true);
                                finish();
                            } else {
                                BToast.showText(jsonObject.getString("msg"), true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private HashMap getFormBody() {
        HashMap jsonObject = new HashMap();
        try {
            jsonObject.put("nation", ed_ethnic.getText().toString());
            if (headimgurl != null && !headimgurl.equals("")) {
                jsonObject.put("headimgurl", headimgurl);
            }
            jsonObject.put("baptismTime", ed_bebaptized_year.getText().toString());
            jsonObject.put("childrenStatus", tv_kiddo.getText().toString());
            jsonObject.put("educationLevel", tv_education.getText().toString());
            jsonObject.put("mail", ed_email.getText().toString());
            jsonObject.put("faithStatus", tv_belief.getText().toString());
            //不确定是否正确
            jsonObject.put("realname", ed_name.getText().toString());
            jsonObject.put("faithTime", ed_believing_year.getText().toString());
            jsonObject.put("maritalStatus", tv_marriage.getText().toString());
            jsonObject.put("occupation", ed_zhiye.getText().toString());
            if (!tv_telephone.getText().toString().equals("")) {
                jsonObject.put("mobile", tv_telephone.getText().toString());
            }
            jsonObject.put("username", user_name.getText().toString());
            jsonObject.put("vipUserId", userInfo.getVipUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
