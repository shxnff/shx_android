package com.shx.base.utils;

import android.os.Handler;
import android.os.Looper;

public class MainHandle extends Handler {
    static MainHandle mainHandle;

    private MainHandle(Looper looper) {
        super(looper);
    }

    public static MainHandle get() {
        if (mainHandle == null) {
            synchronized (MainHandle.class) {
                if (mainHandle == null) {
                    mainHandle = new MainHandle(Looper.getMainLooper());
                }
            }
        }
        return mainHandle;
    }

}
