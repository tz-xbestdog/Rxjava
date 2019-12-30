package com.io.rx.util.util

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat

object SystemUtil {
    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    fun getVerName(context: Context): String {
        var verName = ""
        try {
            val packageName = context.packageName
            verName = context.packageManager
                .getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }


    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    fun getVerCode(context: Context): Int {
        var verName = 0
        try {
            val packageName = context.packageName
            verName = context.packageManager
                .getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }

    fun isPackvilible(context: Context, string: String): Boolean {
        val packageManager = context.packageManager// 获取packagemanager
        val pinfo = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                if (pn == string) {
                    return true
                }
            }
        }
        return false
    }


    /**
     * 获取渠道信息
     */

    fun getChannelData(context: Context): String? {
        val packageManager = context.packageManager
        if (packageManager != null) {
            val application =
                packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            if (application != null) {
                if (application.metaData != null) {
                    return application.metaData.getString("UMENG_CHANNEL")
                }
            }
        }
        return "base"
    }

    /**
     * 判断通知是否打开
     */
    fun isNotificationEnable(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var notificationManagerCompat = NotificationManagerCompat.from(context);
            var areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
            return areNotificationsEnabled;
        }

        var CHECK_OP_NO_THROW = "checkOpNoThrow";
        var OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        var mAppOps = context.getSystemService(Context.APP_OPS_SERVICE) as (AppOpsManager)
        var appInfo = context.getApplicationInfo();
        var pkg = context.getApplicationContext().getPackageName();
        var uid = appInfo.uid;

        var appOpsClass: Class<*>? = null
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager::class.java.getName());
            var checkOpNoThrowMethod = appOpsClass.getMethod(
                CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                String::class.java
            );
            var opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            var value = opPostNotificationValue.get(Integer::class.java) as Int
            return (checkOpNoThrowMethod.invoke(
                mAppOps,
                value,
                uid,
                pkg
            ) as Int == AppOpsManager.MODE_ALLOWED);

        } catch (e: Exception) {
            e.printStackTrace();
        }
        return false;


    }

}