package com.shx.base.utils;

import android.app.Activity;
import android.os.Process;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Activity管理
 */
public class ShxActivityStack {

    private Stack<Activity> mActivities = new Stack<>();
    private static ShxActivityStack INSTANCE;
    public static final int MAX_LIMIT = 3;

    public static ShxActivityStack getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShxActivityStack();
        }
        return INSTANCE;
    }

    /**
     * 关闭顶部页面
     */
    public void popActivity() {
        if (!CollectionUtils.isEmpty(mActivities)) {
            Activity activity = mActivities.pop();
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 添加Activity
     */
    public void pushActivity(Activity activity) {
        mActivities.add(activity);
    }


    /**
     * 添加Activity
     */
    public void pushActivity(Activity activity,int num) {
        mActivities.add(activity);
        delMoreActivity(activity,num);
    }

    /**
     * 删除超出数量限制的Activity
     */
    public void delMoreActivity(Activity activity,int num){
        if(getActivitySize(activity)>num){
            for (Activity a : mActivities) {
                if (a != null && a.getClass() == activity.getClass()) {
                    popActivity(a);
                    break;
                }
            }
        }
    }

    /**
     * 获取指定Activity的数量
     */
    public int getActivitySize(Activity activity) {
        int size = 0;
        for (Activity a : mActivities) {
            if (a != null && a.getClass() == activity.getClass()) {
                size++;
            }
        }
        return size;
    }

    /**
     * 关闭指定页面
     */
    public void popActivity(Activity activity) {
        if (!CollectionUtils.isEmpty(mActivities) && mActivities.contains(activity)) {
            mActivities.remove(activity);
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 获取顶部Activity
     */
    public Activity getTopActivity() {
        try {
            return mActivities.lastElement();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 当前栈顶是不是这个activity
     */
    public boolean isTopActivity(Class<? extends Activity> activityClas) {
        Activity activity = getTopActivity();
        if (activity != null && activity.getClass() == activityClas) {
            return true;
        }
        return false;
    }

    /**
     * 是否已经有这个activity
     */
    public boolean isContains(Class<? extends Activity> activityClas) {
        if (!CollectionUtils.isEmpty(mActivities)) {
            for (int i = 0 ; i < getSize() ; i++) {
                Activity activity = mActivities.get(i);
                if (activity != null && activity.getClass() == activityClas) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 关闭所有页面
     */
    public void popAllActivity(boolean isForceClose) {
        while (mActivities.size() > 0) {
            popActivity();
        }
        if (isForceClose) {
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 返回到指定页面，关闭该页面栈堆上的所有页面
     * true 无需新打开，栈堆中存在该页面
     * false 需要重新打开该页面，栈堆中不存在该页面
     */
    public boolean popActivityUntilCls(Class<? extends Activity> clz) {
        if(isContains(clz)){
            while (mActivities.size() > 1) {
                if (getTopActivity() != null && getTopActivity().getClass() != clz) {
                    popActivity();
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 关闭其他页面
     */
    public void popOtherActivity(Class<? extends Activity> clz) {
        if(!CollectionUtils.isEmpty(mActivities)){
            Iterator it = mActivities.iterator();
            while(it.hasNext()) {
                Activity mActivity = (Activity) it.next();
                it.remove();
                popActivity(mActivity);
            }
        }
    }

    /**
     * 获取activity数量
     */
    public int getSize() {
        return mActivities.size();
    }

    /**
     * 获取Stack
     */
    public Stack<Activity> getStack(){
        return mActivities;
    }

}
