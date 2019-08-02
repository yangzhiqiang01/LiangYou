package com.inhim.pj.utils;

import android.widget.EditText;
import android.widget.TextView;

public class ViewShowUtils {
    public static void show(EditText editText,String value){
        if(value!=null){
            editText.setText(value);
        }
    }

    public static void show(TextView editText, String value){
        if(value!=null){
            editText.setText(value);
        }
    }
}
