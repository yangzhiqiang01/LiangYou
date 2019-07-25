package com.inhim.pj.http;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.inhim.pj.utils.PrefUtils;
import com.inhim.pj.view.BToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOkHttpClient {
    private static MyOkHttpClient myOkHttpClient;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private Gson gson;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private MyOkHttpClient() {
        okHttpClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
        gson = new Gson();
    }

    public static MyOkHttpClient getInstance() {
        if (myOkHttpClient == null) {
            synchronized (MyOkHttpClient.class) {
                if (myOkHttpClient == null) {
                    myOkHttpClient = new MyOkHttpClient();
                }
            }
        }

        return myOkHttpClient;
    }

    class StringCallBack implements Callback {
        private HttpCallBack httpCallBack;
        private Request request;

        public StringCallBack(Request request, HttpCallBack httpCallBack) {
            this.request = request;
            this.httpCallBack = httpCallBack;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            final IOException fe = e;
            try {
                if (httpCallBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            BToast.showText("请求失败", false);
                            httpCallBack.onError(request, fe);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String result = response.body().string();
           /* JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 500) {
                    PrefUtils.remove("expire");
                    PrefUtils.remove("token");
                    PrefUtils.remove("isLogin");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            try {
                if (httpCallBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallBack.onSuccess(request, result);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void asyncGet(String url, HttpCallBack httpCallBack) {
        String token = PrefUtils.getString("token", "");
        Request request = new Request.Builder().header("token", token).url(url).build();
        okHttpClient.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }
    public void asyncPost(String url,Headers.Builder headers, HttpCallBack httpCallBack) {
        String param = gson.toJson(new HashMap<>());
        RequestBody requestBody = RequestBody.create(JSON, param);
        Request request = new Request.Builder().headers(headers.build()).url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }
    public void asyncPost(String url, RequestBody requestBody, HttpCallBack httpCallBack) {
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void asyncJsonPost(String url, HashMap map, HttpCallBack httpCallBack) {
        String token = PrefUtils.getString("token", "");
        String param = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(JSON, param);
        Request request = new Request.Builder().header("token", token).url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void asyncJsonPostNoToken(String url, HashMap map, HttpCallBack httpCallBack) {
        String param = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(JSON, param);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new StringCallBack(request, httpCallBack));
    }

    public void asyncPut(String url,HashMap map,HttpCallBack httpCallBack){
        String token = PrefUtils.getString("token", "");
        String param = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(JSON, param);
        Request request=new Request.Builder()
                .header("token", token)
                .url(url)
                .put(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new StringCallBack(request,httpCallBack));
    }

    public void doDelete(String url,RequestBody requestBody,HttpCallBack httpCallBack){
        String token = PrefUtils.getString("token", "");
        Request.Builder builder = new Request.Builder().url(url).delete(requestBody);
        builder.addHeader("token", token);
        Request request = builder.build();

        okHttpClient.newCall(request).enqueue(new StringCallBack(request,httpCallBack));

    }
    public interface HttpCallBack {
        void onError(Request request, IOException e);

        void onSuccess(Request request, String result);
    }


}
