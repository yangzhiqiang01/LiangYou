package com.inhim.pj.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.dowloadvedio.db.DBController;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfo;
import com.inhim.pj.dowloadvedio.domain.MyBusinessInfoDid;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.FileUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.CenterDialog;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

public class SettingActivity extends BaseActivity {
    RelativeLayout rela_clean;
    TextView tv_size;
    CheckBox checkbox;
    private CenterDialog centerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        rela_clean = findViewById(R.id.rela_clean);
        tv_size=findViewById(R.id.tv_size);
        tv_size.setText(FileUtils.getAutoFileOrFilesSize(Urls.getFilePath()));
        checkbox=findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefUtils.putBoolean("isPlay",isChecked);
            }
        });
        rela_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiaglog();
            }
        });
    }

    private void setDiaglog(){
        View outerView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_deletes, null);
        Button btn_ok=outerView.findViewById(R.id.btn_ok);
        Button btn_cancel=outerView.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading("删除中");
                FileUtils.DeleteFile(new File(Urls.getFilePath()));
                List<MyBusinessInfo> myBusinessInfos = DataSupport.findAll(MyBusinessInfo.class);
                for (int j = 0; j < myBusinessInfos.size(); j++) {
                    myBusinessInfos.get(j).delete();
                }
                List<MyBusinessInfoDid> myBusinessInfoDids = DataSupport.findAll(MyBusinessInfoDid.class);
                for (int j = 0; j < myBusinessInfoDids.size(); j++) {
                    myBusinessInfoDids.get(j).delete();
                }
                FileUtils.DeleteFile(new File(Urls.getFilePath()));
                //删除下载的信息
                try {

                    DBController instance = DBController.getInstance(SettingActivity.this);
                    for (int i = 0; i < instance.findAllDownloaded().size(); i++) {
                        instance.delete(instance.findAllDownloaded().get(i));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                hideLoading();
                tv_size.setText("0B");
                BToast.showText("已删除缓存文件");
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerDialog.dismiss();
            }
        });
        //防止弹出两个窗口
        if (centerDialog !=null && centerDialog.isShowing()) {
            return;
        }

        centerDialog = new CenterDialog(SettingActivity.this, R.style.ActionSheetDialogBotoomStyle);
        //将布局设置给Dialog
        centerDialog.setContentView(outerView);
        centerDialog.show();//显示对话框
    }
}
