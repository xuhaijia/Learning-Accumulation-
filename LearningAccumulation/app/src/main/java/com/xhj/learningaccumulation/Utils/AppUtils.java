package com.xhj.learningaccumulation.Utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.xhj.learningaccumulation.Custom.Toast;
import com.xhj.learningaccumulation.MainApp;

import java.io.File;
import java.util.List;

public class AppUtils {

    public static final String APP_NAME = "应用名";

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取版本名称
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 安装APK
     *
     * @param context
     * @param filePath
     * @return
     */
    public static boolean installNormal(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= 24) {
            //		android 7.0
            Intent i = new Intent();
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            i.setAction(Intent.ACTION_VIEW);
            File file = new File(filePath);
            if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
                return false;
            }
            Uri uri = FileProvider.getUriForFile(context, MainApp.provider, file);
            i.setDataAndType(uri, "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            File file = new File(filePath);
            if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
                return false;
            }
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        return true;
    }

    /**
     * 打电话
     *
     * @param context
     * @param tel
     */
    public static void callTelphone(Context context, String tel) {
        if (TextUtils.isEmpty(tel)) {
            Toast.makeText(context, "电话号码有错", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        context.startActivity(intent);
    }

    /**
     * 获取应用的当前Activity
     * <uses-permission android:name="android.permission.GET_TASKS" />
     *
     * @return
     */
    public static ComponentName getCurrentActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> info = am.getRunningTasks(Integer.MAX_VALUE);

        for (RunningTaskInfo entity : info) {
            ComponentName componentName = entity.topActivity;
            String selfPackage = context.getPackageName();
            if (componentName.getPackageName().equals(selfPackage)) {
                return componentName;
            }
        }
        return null;
    }

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
        }
        return false;
    }


    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static void jumpToBrowser(String url, Context context) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }


}
