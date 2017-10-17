package com.example.qgchat.util;

import java.io.File;
import java.util.Map;
import java.util.zip.ZipEntry;

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

    //public static final String BaseWeb = "http://172.16.6.66";
    public static final String BaseWeb = "http://www.chemaxianxing.com";
    /**
     * 主域名
     */
    public static final String BaseURL = BaseWeb+"/QGChat";
    /**
     * 发送验证码
     */
    public static final String sendCodeURL = BaseURL+"/SendMsg";
    /**
     * 验证验证码
     */
    public static final String checkCodeURL = BaseURL+"/CheckMsg";
    /**
     * 上传图片
     */
    public static final String uploadImageURL = BaseURL+"/UploadIcon";
    /**
     * 获取分组信息
     */
    public static final String getGroupMessageURL = BaseURL + "/getMessage";

    /**
     * 获取账户信息
     */
    public static final String getAccountMessageURL = BaseURL + "/getAccountMessage";

    /**
     * 获取天气信息（和风天气）
     */
    public static final String getWeatherURL = "https://free-api.heweather.com/v5/now?city=CITY&key=07bf16f33106400baf180ec36a061501";

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
