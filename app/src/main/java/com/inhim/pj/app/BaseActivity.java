package com.inhim.pj.app;

import android.support.v7.app.AppCompatActivity;

import com.inhim.pj.view.TransparentProgressDialog;

public class BaseActivity extends AppCompatActivity {
    private TransparentProgressDialog mProgressDialog;
    public void showLoading(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new TransparentProgressDialog(this,text);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        try{
            mProgressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try{
                mProgressDialog.dismiss();
                mProgressDialog=null;
            }catch (Exception e){
                e.printStackTrace();
            }
            mProgressDialog=null;
        }
    }

}
