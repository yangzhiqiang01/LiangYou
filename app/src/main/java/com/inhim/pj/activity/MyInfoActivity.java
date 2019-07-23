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
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.entity.UserInfo;
import com.inhim.pj.http.MyOkHttpClient;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.DateUtils;
import com.inhim.pj.utils.OkhttpUploadUtils;
import com.inhim.pj.utils.PermissionUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.utils.ViewShowUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.ChooseDialog;
import com.inhim.pj.view.MonPickerDialog;
import com.inhim.pj.wheelview.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by Administrator on 2017-11-29.
 */

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_name, ed_ethnic,
            ed_email, user_name;
    private TextView tv_belief, tv_marriage, tv_kiddo, tv_education, ed_believing_year, ed_bebaptized_year, tv_telephone;
    private String name, ethnic, believing_year, bebaptized_year, others, telephone, email, address,
            postcode, belief, marriage, kiddo, education;
    private List beliefList, marriageList, kiddoList, educationList;
    private LinearLayout lin;
    private ChooseDialog chooseDialog;
    private ImageView iv_photo;

    private String takePicOri, cropPicTmp;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_PICTURE = 2;
    public static final int CHOICE_PICTURE = 0;
    private EditText ed_zhiye;
    private TextView tvCourse;
    private Button btn_submit;
    private Calendar calendar;
    Gson gson;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_myinfo);
        gson = new Gson();
        initView();
        setPopuWindows();
        calendar = Calendar.getInstance();
        setLinear();
        getInfo();
    }

    private void setPopuWindows() {
        beliefList = new ArrayList();
        beliefList.add("基督教");
        beliefList.add("基督教-有参与教会侍奉");
        beliefList.add("基督教-教牧同工");
        beliefList.add("非基督教-有兴趣认识基督教");
        beliefList.add("非基督教-天主教");
        beliefList.add("非基督教-佛教");
        beliefList.add("非基督教-道教");
        beliefList.add("非基督教-伊斯兰教");
        beliefList.add("非基督教-印度教");
        beliefList.add("非基督教-无神论");
        beliefList.add("非基督教-其他");


        marriageList = new ArrayList();
        marriageList.add("未婚");
        marriageList.add("已婚");
        marriageList.add("离婚");
        marriageList.add("丧偶");
        marriageList.add("再婚");


        kiddoList = new ArrayList();
        kiddoList.add("有");
        kiddoList.add("没有");


        educationList = new ArrayList();
        educationList.add("未有入学");
        educationList.add("小学");
        educationList.add("初中");
        educationList.add("高中");
        educationList.add("大专以上");

    }

    private void getInfo() {
        String examUrl = Urls.getUserInfo;
        MyOkHttpClient.getInstance().asyncGet(examUrl, new MyOkHttpClient.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Request request, String results) {
                UserInfo smsResult = gson.fromJson(results, UserInfo.class);
                if (smsResult.getMsg().equals("success")) {
                    setView(smsResult.getUser());
                } else {
                    BToast.showText(smsResult.getMsg(), Toast.LENGTH_LONG, false);
                }
            }
        });
    }

    private void initView() {
        btn_submit=findViewById(R.id.btn_submit);
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
        lin = findViewById(R.id.lin);
        iv_photo = findViewById(R.id.iv_photo);
        ed_zhiye = findViewById(R.id.ed_zhiye);
        tvCourse = findViewById(R.id.tvCourse);
        tvCourse.setText("我的信息");
        if (PrefUtils.getString("photo", "") != null && !PrefUtils.getString("photo", "").equals("")) {
            String filename = PrefUtils.getString("photo", "");
            File file = new File(filename);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(filename);
                iv_photo.setImageBitmap(bm);
            }
        }
    }

    private void setView(UserInfo.User userInfoEntity) {
        ViewShowUtils.show(user_name, userInfoEntity.getUsername());
        ViewShowUtils.show(ed_zhiye, userInfoEntity.getUsername());
        ViewShowUtils.show(ed_name, userInfoEntity.getUsername());
        ViewShowUtils.show(ed_ethnic, userInfoEntity.getUsername());
        ViewShowUtils.show(ed_believing_year, userInfoEntity.getUsername());
        ViewShowUtils.show(ed_bebaptized_year, userInfoEntity.getUsername());
        ViewShowUtils.show(tv_telephone, userInfoEntity.getMobile());
        ViewShowUtils.show(ed_email, userInfoEntity.getMail());
        ViewShowUtils.show(tv_belief, userInfoEntity.getOccupation());
        ViewShowUtils.show(tv_marriage, userInfoEntity.getUsername());
        ViewShowUtils.show(tv_kiddo, userInfoEntity.getChildrenStatus());
        ViewShowUtils.show(tv_education, userInfoEntity.getEducationLevel());
    }

    private void setLinear() {

        tv_belief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDiaglog(beliefList, tv_belief);
            }
        });
        tv_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDiaglog(educationList, tv_education);
            }
        });
        tv_kiddo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDiaglog(kiddoList, tv_kiddo);
            }
        });
        tv_marriage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDiaglog(marriageList, tv_marriage);
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
        }
    }

    private void upLoad(File uploadFile) {
        showLoading("上传中");
        OkhttpUploadUtils okhttpUploadUtils = new OkhttpUploadUtils(new OkhttpUploadUtils.HttpCallBack() {
            @Override
            public void onError(okhttp3.Request request, IOException e) {
                hideLoading();
            }

            @Override
            public void onSuccess(okhttp3.Request request, String result) {
                hideLoading();
                //iv_photo.setImageBitmap(bm);
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
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
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
                //tv_belief.setText(item);
            }
        });

        //点击确定
        tv_dete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                chooseDialog.dismiss();
                textView.setText(wv.getSelectedItem());
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
                startActivity(intent);
                break;
            case R.id.btn_submit:
                updateInfo();
                break;
            default:
                break;
        }
    }

    private void updateInfo() {
        HashMap build = getFormBody();
        MyOkHttpClient.getInstance().asyncPut(Urls.updateUserInfo,

                build, new MyOkHttpClient.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {

                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 0) {
                                BToast.showText("更改成功", true);
                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private HashMap getFormBody() {

/* {
            "baptismTime": "2019-07-15T09:13:53.481Z",
                "childrenStatus": 0,
                "createTime": "2019-07-15T09:13:53.481Z",
                "educationLevel": "string",
                "faithStatus": 0,
                "faithTime": "2019-07-15T09:13:53.481Z",
                "headimgurl": "string",
                "mail": "string",
                "maritalStatus": 0,
                "mobile": "string",
                "nation": "string",
                "occupation": "string",
                "password": "string",
                "realname": "string",
                "username": "string",
                "vipUserId": 0,
                "wechatUser": {
            "city": "string",
                    "country": "string",
                    "createTime": "2019-07-15T09:13:53.481Z",
                    "headimgurl": "string",
                    "language": "string",
                    "nickname": "string",
                    "openId": "string",
                    "province": "string",
                    "sex": 0,
                    "updateTime": "2019-07-15T09:13:53.481Z",
                    "vipUserId": 0,
                    "wechatUserId": 0
        },
            "wechatUserId": 0
        }*/
        others = ed_zhiye.getText().toString();
        name = ed_name.getText().toString();
        ethnic = ed_ethnic.getText().toString();
        believing_year = ed_believing_year.getText().toString();

        bebaptized_year = ed_bebaptized_year.getText().toString();
        telephone = tv_telephone.getText().toString();
        email = ed_email.getText().toString();
        belief = tv_belief.getText().toString();
        marriage = tv_marriage.getText().toString();
        kiddo = tv_kiddo.getText().toString();
        education = tv_education.getText().toString();
        HashMap jsonObject=new HashMap();
        HashMap build = new HashMap();
            try {
                if (!bebaptized_year.equals("") && bebaptized_year != null) {
                jsonObject.put("baptismTime", bebaptized_year);
                }
                if (!kiddo.equals("") && kiddo != null) {
                    jsonObject.put("childrenStatus", kiddo);
                }
                if (!education.equals("") && education != null) {
                    jsonObject.put("educationLevel", education);
                }
                if (!email.equals("") && email != null) {
                    jsonObject.put("mail", email);
                }
                if (!belief.equals("") && belief != null) {
                    jsonObject.put("faithStatus", belief);
                }
                //不确定是否正确
                if (!name.equals("") && name != null) {
                    jsonObject.put("realname", name);
                }
                if (!believing_year.equals("") && believing_year != null) {
                    jsonObject.put("faithTime", believing_year);
                }
                if (!marriage.equals("") && marriage != null) {
                    jsonObject.put("maritalStatus", marriage);
                }
                if (!others.equals("") && others != null) {
                    jsonObject.put("occupation", others);
                }
                if (!telephone.equals("") && telephone != null) {
                    jsonObject.put("mobile", telephone);
                }
                jsonObject.put("userCode", PrefUtils.getString("userCode", ""));
                jsonObject.put("userName", ed_name.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        build.put("user",jsonObject);

        return build;
    }
}
