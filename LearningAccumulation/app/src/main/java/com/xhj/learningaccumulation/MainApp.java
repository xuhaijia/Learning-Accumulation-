package com.xhj.learningaccumulation;

import android.app.Application;
import android.content.Context;

/**
 * created by xuhaijia 2017/9/21
 */

public class MainApp extends Application {
    public static String provider = "com.chengfang.fileprovider";
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }



}
