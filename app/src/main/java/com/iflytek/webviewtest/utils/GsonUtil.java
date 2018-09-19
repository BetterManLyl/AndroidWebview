package com.iflytek.webviewtest.utils;

import com.google.gson.Gson;

/**
 * Created by zhwang16 on 2018/5/4.
 */

public class GsonUtil {
    private static Gson gson;

    public static synchronized Gson getInstance() {
        if (null == gson) {
            gson = new Gson();
        }
        return gson;
    }
}
