package com.io.rx.interceptor;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Stack;

/**
 * activity堆栈式管理
 * Created by yanglinghui on 2016-05-24.
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static ArrayList<Activity> arrayList = new ArrayList<>();
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);

//        Logger.i("add activity : %s into stack!", activity.getClass().getName());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        try {
            Activity activity = activityStack.lastElement();
            return activity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack.size() > 0) {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
//            Logger.i("finish Activity : %s , and remove in stack!", activity.getClass().getName());
            activityStack.remove(activity);
//            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束列表中的Activity
     */
    public static void finishListActivity() {
        ArrayList<Activity> arrayList = new ArrayList<>();
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            arrayList.add(activity);
        }
        activityStack.clear();
        synchronized (arrayList) {
            for (Activity activity : arrayList) {
                activity.finish();
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public static  void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
//                finishActivity(activityStack.get(i));
//                break;
            }
        }
        activityStack.clear();
    }


    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            MobclickAgent.onKillProcess(context);
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
