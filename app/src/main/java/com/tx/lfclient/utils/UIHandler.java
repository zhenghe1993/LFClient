package com.tx.lfclient.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.tx.lfclient.utils.inter.IUIHandler;

import java.lang.ref.WeakReference;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/13
 */
public class UIHandler<T extends Activity & IUIHandler> extends Handler {

    private WeakReference<T> weakReference;

    public UIHandler(T activity) {
        weakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        T activity = weakReference.get();
        activity.sendMessage(msg);
    }


}
