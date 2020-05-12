package com.fenghuang.caipiaobao.utils;

import android.content.Context;

import com.fenghuang.caipiaobao.constant.UserInfoSp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @ Author  QinTian
 * @ Date  2020/4/30
 * @ Describe
 */
public class HttpClient {
    private OkHttpClient client;
    private static HttpClient mClient;
    private Context context;
    private HttpClient(Context c) {
        context = c;
        client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    public static HttpClient getInstance(Context c){
        if (mClient == null) {
            mClient = new HttpClient(c);
        }
        return  mClient;
    }


    // GET方法
    public void get(String url, HashMap<String,String> param, MyCallback callback) {
        // 拼接请求参数
        if (!param.isEmpty()) {
            StringBuffer buffer = new StringBuffer(url);
            buffer.append('?');
            for (Map.Entry<String,String> entry: param.entrySet()) {
                buffer.append(entry.getKey());
                buffer.append('=');
                buffer.append(entry.getValue());
                buffer.append('&');
            }
            buffer.deleteCharAt(buffer.length()-1);
            url = buffer.toString();
        }
        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET", null);
        Request request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }

    public void get(String url, MyCallback callback) {
        get(url, new HashMap<>(), callback);
    }

    // POST 方法
    public void post(String url,HashMap<String, String> param, MyCallback callback) {
        FormBody.Builder formBody = new FormBody.Builder();
        if(!param.isEmpty()) {
            for (Map.Entry<String,String> entry: param.entrySet()) {
                formBody.add(entry.getKey(),entry.getValue());
            }
        }
        RequestBody form = formBody.build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.post(form)
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }

    // POST 方法
    public void postWithHead(String url,HashMap<String, String> param, MyCallback callback) {
        FormBody.Builder formBody = new FormBody.Builder();
        if(!param.isEmpty()) {
            for (Map.Entry<String,String> entry: param.entrySet()) {
                formBody.add(entry.getKey(),entry.getValue());
            }
        }
        RequestBody form = formBody.build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.post(form)
                .url(url)
                .addHeader("Authorization", Objects.requireNonNull(UserInfoSp.INSTANCE.getTokenWithBearer()))
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }
    public interface MyCallback {
        void success(Response res) throws IOException;
        void failed(IOException e);
    }
}
