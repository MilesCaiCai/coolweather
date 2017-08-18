package com.fionacos.myapp.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Miles on 2017/8/17.
 * Http 工具类
 */

public class HttpUtil {
    public static void senOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
