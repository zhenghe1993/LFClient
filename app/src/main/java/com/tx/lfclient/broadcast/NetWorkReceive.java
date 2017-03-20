package com.tx.lfclient.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.utils.NetWorkJudge;

import org.xutils.common.util.LogUtil;


/**
 * Created by (IMP)郑和明
 * Date is 2017/2/14
 */
public class NetWorkReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        MyApplication myApplication = MyApplication.getInstance();
        myApplication.setNet(NetWorkJudge.getJudge(context));

    }
}
