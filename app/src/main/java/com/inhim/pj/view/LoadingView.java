package com.inhim.pj.view;

import android.content.Context;

public class LoadingView {
    private TransparentProgressDialog mProgressDialog;
    public void showLoading(String text, Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new TransparentProgressDialog(context,text);
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
