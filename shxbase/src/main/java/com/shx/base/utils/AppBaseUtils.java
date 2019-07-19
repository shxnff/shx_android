package com.shx.base.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class AppBaseUtils {

    public static Application globalApplication;

    public static void init(Application application) {
        globalApplication = application;
    }

    public static Application getApplication() {
        if (globalApplication == null) {
            new RuntimeException("please init application");
        }
        return globalApplication;
    }

    /**
     * 兼容高版本的fileprovider
     */
    public static Uri getUri(Context mContext, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName() {
        try {
            PackageManager packageManager = globalApplication.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(globalApplication.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return globalApplication.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @return 当前应用的版本名称
     */
    public static String getVersionName() {
        try {
            PackageManager packageManager = globalApplication.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(globalApplication.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本号]
     * @return 当前应用的版本号
     */
    public static int getVersionCode() {
        try {
            PackageManager packageManager = globalApplication.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(globalApplication.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取wifi下的mac地址
     */
    public static String getMacAddress() {
        try {
            WifiManager wifi = (WifiManager) globalApplication.getSystemService(Context.WIFI_SERVICE);
            return wifi.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取手机imei
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) globalApplication.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getDeviceId();
        } else {
            return null;
        }

    }

    /**
     * 获取手机imsi
     */
    @SuppressLint("MissingPermission")
    public static String getIMSI() {
        TelephonyManager telephonyManager = (TelephonyManager) globalApplication.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getSubscriberId();
        }
        return null;
    }

    /**
     * 获取手机iccdi
     */
    @SuppressLint("MissingPermission")
    public static String getICCDI() {
        TelephonyManager telephonyManager = (TelephonyManager) globalApplication.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getSimSerialNumber();
        }
        return null;
    }

    /**
     * 获取移动运营商信息
     */
    public static String getCarrier() {
        String carrier = "UNKNOW";
        TelephonyManager telManager = (TelephonyManager) globalApplication.getSystemService(Context.TELEPHONY_SERVICE);

        String operator = telManager.getSimOperator();

        if (operator != null) {
            //中国移动
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                carrier = "中国移动";
                //中国联通
            } else if (operator.equals("46001")) {
                carrier = "中国联通";
                //中国电信
            } else if (operator.equals("46003")) {
                carrier = "中国电信";
            }

        }
        return carrier;
    }


    /**
     * 先取androidid，然后为deviceid，然后mac地址,最后为随机的uuid
     */
    public static String getDeviceID() {
        String deviceID = "";

        try {
            deviceID = ((TelephonyManager) globalApplication.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            deviceID = getUuid(globalApplication);
        } finally {
            return deviceID;
        }
    }

    @SuppressLint("NewApi")
    public static String getUuid(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = "";
        String tmSerial = "";
        final String androidId;
        try {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
        } catch (Exception e) {

        }

        androidId = ""
                + android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        Log.d("debug", "uuid=" + uniqueId);
        return uniqueId;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(String key) {
        if (globalApplication == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = globalApplication.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(globalApplication.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 是否是主进程
     * 如果getCurProcessName为空就默认认为不是主进程
     *
     * @return
     */
    public static boolean isMainProcess() {
        String name = getCurProcessName();
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return name.equals(getApplication().getPackageName());
    }

    /**
     * 获得当前进程名称, >5.1的系统，有几率会关闭getRunningAppProcesses方法(只会返回我们自己的进程)
     *
     * @return
     */
    public static String getCurProcessName() {
        int pid = Process.myPid();
        //获取app进程
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = ((ActivityManager) getApplication()
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningAppProcesses();
        if (appProcessInfos != null && !appProcessInfos.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo info : appProcessInfos) {
                if (info.pid == pid && !TextUtils.isEmpty(info.processName)) {
                    return info.processName;
                }
            }
        }
        //如果没有获取到运行的进程，那么久调用运行的服务来判断
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = ((ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE);
        if (runningServiceInfos != null && !runningServiceInfos.isEmpty()) {
            for (ActivityManager.RunningServiceInfo info : runningServiceInfos) {
                if (info.pid == pid && !TextUtils.isEmpty(info.process)) {
                    return info.process;
                }
            }
        }
        //返回默认值
        return "";
    }


    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if(cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;
    }
}
