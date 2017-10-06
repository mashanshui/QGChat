package com.example.qgchat.util;

/**
 * Created by Administrator on 2017/9/25.
 */

public class StringUtil {

    public static boolean isEmpty(String... args) {
        for (String arg : args) {
            if (arg == null || arg.equals("")) {
                return true;
            }
        }
        return false;
    }
}
