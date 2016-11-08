package com.cooloongwu.coolchat.base;

import android.app.Activity;

import java.util.Stack;

/**
 * 管理Activity的自定义栈
 * Created by CooLoongWu on 2016-11-8 15:30.
 */

public class AppManager {

    private static Stack<Activity> activityStack;

    private AppManager() {

    }

    public static AppManager getInstance() {
        return AppManagerHolder.INSTANCE;
    }

    private static class AppManagerHolder {
        private static AppManager INSTANCE = new AppManager();
    }

    /**
     * 向自定义Activity栈中添加Activity
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 删除自定义Activity栈中的Activity
     *
     * @param activity activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null) {
            activityStack.remove(activity);
            activity = null;
            System.gc();
        }
    }

    /**
     * 销毁某一Activity
     *
     * @param activity 要销毁的Activity引用
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 销毁特定类名的Activity
     *
     * @param cls 要销毁的Activity的类名
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
     * 销毁自定义Activity栈的中所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (activityStack.get(i) != null) {
                finishActivity(activityStack.get(i));
            }
        }
    }

    /**
     * 退出App
     */
    public void exitApp() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
