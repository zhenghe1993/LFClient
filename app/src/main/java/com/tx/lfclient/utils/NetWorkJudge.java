package com.tx.lfclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.xutils.common.util.LogUtil;


/**
 * Created by (IMP)郑和明
 * Date is 2017/2/6
 * 网络判断器
 */
public class NetWorkJudge {


    public static boolean getJudge(Context context) {



        LogUtil.w("开始判断网络状态");
        int res = GetNetType(context);
        switch (res) {
            case 0:
                return isMobileConnected(context);
            case 1:
                return isWifiConnected(context) && Ping("121.42.52.89");

            case -1:
                return false;
        }
        return false;
    }

    //判断网络状态   是否开启网络  0 网络关闭状态 1 2G/3G/4G  2  wifi




    private static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
               boolean isWifi= mWiFiNetworkInfo.isAvailable();
                LogUtil.w("wifi网络状态 "+isWifi);
                return isWifi;
            }
        }
        return false;
    }

    private static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                boolean isMobile=mMobileNetworkInfo.isAvailable();
                LogUtil.w("mobile网络状态 "+isMobile);
                return isMobile;
            }
        }
        return false;
    }


    //返回值 -1：没有网络 0 2G/3G/4G  1：WIFI网络
    private static int GetNetType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            LogUtil.w("网络状态 "+netType);
            return netType;
        }
        netType = networkInfo.getType();
        LogUtil.w("网络状态 "+netType);
        return netType;
    }

    private static boolean Ping(String str) {
        boolean result = false;
        Process p;
        try {
            //ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒
            p = Runtime.getRuntime().exec("ping -c 3 -w 10 " + str);
            int status = p.waitFor();
            result = status == 0;
        } catch (Exception e) {
            LogUtil.e("error", e);
        }
        LogUtil.w("ping 状态  "+result);
        return result;
    }

}
