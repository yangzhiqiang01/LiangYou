package com.inhim.pj.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inhim.pj.R;


public class TransparentProgressDialog extends Dialog {
    public TransparentProgressDialog(Context context, String text) {
        super(context, R.style.TransparentProgressDialog);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        ((TextView)view.findViewById(R.id.loading_tv)).setText(text);
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(view, params);
    }
}