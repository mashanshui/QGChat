package com.example.qgchat.util;

import java.io.File;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/9/9.
 */

public class HttpUtil {
    private static OkHttpClient client = null;

    public static void sendOkHttpRequest(String address, Callback callback) {
        if (client == null) {
            client = new OkHttpClient();
        }
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void cancelOkHttpRequest() {
        if (client != null) {
            client.dispatcher().cancelAll();
        }
    }


    public static void uploadImage(String url, Map<String, String> paramsMap, String filePath,String filename, Callback callback) {
        if (client == null) {
            client = new OkHttpClient();
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath));
        builder.addFormDataPart("file",filename , fileBody);
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
