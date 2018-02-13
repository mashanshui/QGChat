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

//    public static final String BaseWeb = "http://139.199.158.151";
    public static final String BaseWeb = "http://192.168.31.189";
    /**
     * 主域名
     */
    public static final String BaseURL = BaseWeb+"/qgchat";
    /**
     * 发送验证码
     */
    public static final String sendCodeURL = BaseURL+"/VerifyCode/SendMsg";
    /**
     * 验证验证码
     */
    public static final String checkCodeURL = BaseURL+"/VerifyCode/CheckMsg";
    /**
     * 验证验证码
     */
    public static final String registerURL = BaseURL+"/user/register";
    /**
     * 上传图片
     */
    public static final String uploadImageURL = BaseURL+"/user/uploadIcon";
    /**
     * 获取分组信息
     */
    public static final String getGroupMessageURL = BaseURL + "/getMessage";

    /**
     * 获取账户信息
     */
    public static final String getUserMessageURL = BaseURL + "/user/getUserMessage";

    /**
     * 添加好友，检查好友是否存在，是否已经是好友了
     */
    public static final String checkFriendURL = BaseURL + "/user/checkFriend";

    /**
     * 添加好友
     */
    public static final String addFriendURL = BaseURL + "/user/addFriend";

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
