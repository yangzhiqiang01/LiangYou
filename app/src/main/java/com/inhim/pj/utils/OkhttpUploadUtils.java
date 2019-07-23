package com.inhim.pj.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpUploadUtils {
    private HttpCallBack httpCallBack;
    public OkhttpUploadUtils(HttpCallBack httpCallBack){
        this.httpCallBack = httpCallBack;
    }
    //上传
    public void upLoad(String upLoadUrl ,File file) {
        //创建文件对象
        //创建RequestBody封装参数
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //创建MultiparBody,给RequestBody进行设置
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        //创建Request
        final Request request = new Request.Builder()
                .url(upLoadUrl)
                .post(multipartBody)
                .build();
        //创建okHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(8, TimeUnit.SECONDS)
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8,TimeUnit.SECONDS)
                .build();
        //创建call对象
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("wjh","upLoad 、  e=" + e);
                httpCallBack.onError(request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                httpCallBack.onSuccess(request,response.body().string());
            }
        });

    }

    public interface HttpCallBack {
        void onError(Request request, IOException e);

        void onSuccess(Request request, String result);
    }

}
