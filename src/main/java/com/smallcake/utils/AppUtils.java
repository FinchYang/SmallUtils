package com.smallcake.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

public class AppUtils {
    /**
     * get this App install package name
     * @param context
     * @return  com.cake.page
     */
    public static String getAppPackageName(Context context) {
        return context.getApplicationInfo().packageName;
    }

    /**
     * get string app version
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * get int app version
     */
    public static int getVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    /**
      install APK

     6.0++ need Dynamic permissions,you need add this part permissions into your AndroidManifest.xml

            <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
            <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

       7.0++ need FileProvider to make Uri ,don't care for it ,I have already do it!
        more info about FileProvider [https://developer.android.google.cn/reference/android/support/v4/content/FileProvider.html]



      TODO 1:create xml folder in [res] and create file_paths.xml in this xml

      <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <paths xmlns:android="http://schemas.android.com/apk/res/android">
                <root-path path="" name="camera_photos" />
                <root-path path="." name="download" />
            </paths>
        </resources>

      TODO 2: take under code into <application></application>

        <provider
             android:name="android.support.v4.content.FileProvider"
             android:authorities="${applicationId}.fileprovider"
             android:exported="false"
             android:grantUriPermissions="true">
                 <meta-data
                 android:name="android.support.FILE_PROVIDER_PATHS"
                 android:resource="@xml/file_paths"/>
        </provider>
     */
    public static  void installApk(Context context,String downloadApk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(downloadApk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, AppUtils.getAppPackageName(context)+".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }


}
