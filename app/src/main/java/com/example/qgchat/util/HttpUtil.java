package com.example.qgchat.util;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/9.
 */

public class HttpUtil {
    private static OkHttpClient client;

    public static void sendOkHttpRequest(String address, Callback callback) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void cancelOkHttpRequest() {
        client.dispatcher().cancelAll();
    }


    public static void uploadImage(String url, Map<String, String> paramsMap, String filePath, Callback callback) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath));
        MultipartBody.Builder builder = new MultipartBody.Builder();

        builder.setType(MultipartBody.FORM)
                .addFormDataPart("file", "head_image", fileBody);
        for (String key : paramsMap.keySet()) {
            String object = paramsMap.get(key);
            builder.addFormDataPart(key, object);
        }
        RequestBody requestBody = builder.build();
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", "head_image", fileBody)
//                .addFormDataPart("user", user)
//                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
