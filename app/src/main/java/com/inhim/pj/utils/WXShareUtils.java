package com.inhim.pj.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.inhim.pj.R;
import com.inhim.pj.app.MyApplication;
import com.inhim.pj.view.WXShareDialog;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

public class WXShareUtils {
    private static final int THUMB_SIZE = 150;
    private static int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private static int mTargetScene1 = SendMessageToWX.Req.WXSceneTimeline;
    public static void show(final Context mContext, final String webpageUrl, final String title, final String description){
        View outerView = LayoutInflater.from(mContext).inflate(R.layout.dialog_wx_share, null);
        TextView tv_cancel=outerView.findViewById(R.id.tv_cancel);
        TextView tv_dete=outerView.findViewById(R.id.tv_dete);

        final WXShareDialog wxShareDialog = new WXShareDialog(mContext, R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        wxShareDialog.setContentView(outerView);
        wxShareDialog.show();//显示对话框
        //点击确定
        tv_dete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendShare(1,mContext,webpageUrl,title,description);
                wxShareDialog.dismiss();
            }
        });
        //点击取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendShare(0,mContext,webpageUrl,title,description);
                wxShareDialog.dismiss();
            }
        });
    }

    private static void sendShare(int type,Context mContext,String webpageUrl,String title,String description){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
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
        MyApplication.api.sendReq(req);
    }
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
