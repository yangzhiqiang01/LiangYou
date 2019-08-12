package com.inhim.pj.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.app.BaseActivity;
import com.inhim.pj.dowloadfile.download.DownloadInfo;
import com.inhim.pj.dowloadfile.download.MyBusinessInfoDid;
import com.inhim.pj.http.Urls;
import com.inhim.pj.utils.FileUtils;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;
import com.inhim.pj.view.CenterDialog;

import org.litepal.LitePal;

import java.io.File;

public class SettingActivity extends BaseActivity {
    RelativeLayout rela_clean;
    TextView tv_size;
    CheckBox checkbox;
    private CenterDialog centerDialog;

    @Override
    public Object offerLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void onBindView() {
        initView();
    }

    @Override
    public void destory() {

    }

    private void initView() {
        rela_clean = findViewById(R.id.rela_clean);
        tv_size = findViewById(R.id.tv_size);
        tv_size.setText(FileUtils.getAutoFileOrFilesSize(Urls.getFilePath()));
        checkbox = findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> PrefUtils.putBoolean("isPlay", isChecked));
        rela_clean.setOnClickListener(v -> setDiaglog());
    }

    private void setDiaglog() {
        View outerView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_deletes, null);
        Button btn_ok = outerView.findViewById(R.id.btn_ok);
        Button btn_cancel = outerView.findViewById(R.id.btn_cancel);
        TextView tvTitle=outerView.findViewById(R.id.tv_title);
        tvTitle.setText("确定要清除缓存吗？");
        btn_ok.setOnClickListener(v -> {
            showLoading("删除中");
            try{
                FileUtils.DeleteFile(new File(Urls.getFilePath()));
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                LitePal.deleteAll(DownloadInfo.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                LitePal.deleteAll(MyBusinessInfoDid.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            //删除下载的信息
            hideLoading();
            tv_size.setText("0B");
            BToast.showText("已删除缓存文件");
        });
        btn_cancel.setOnClickListener(v -> centerDialog.dismiss());
        //防止弹出两个窗口
        if (centerDialog != null && centerDialog.isShowing()) {
            return;
        }

        centerDialog = new CenterDialog(SettingActivity.this, R.style.ActionSheetDialogBotoomStyle);
        //将布局设置给Dialog
        centerDialog.setContentView(outerView);
        centerDialog.show();//显示对话框
    }
}
