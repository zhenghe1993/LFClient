package com.tx.lfclient.application;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;

import com.tx.lfclient.R;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.utils.InitUtils;

import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.SMSSDK;

public class MyApplication extends Application {

    private static MyApplication myApplication;
    private Map<String, Activity> activityMap = new HashMap<>();
    private Boolean isLogin = false;
    private String location = "全国";
    private User user;
    private String version;
    private ImageOptions imageOptions;
    private boolean isNet;
    private boolean expire;

    public void onCreate() {
        super.onCreate();
        myApplication = new MyApplication();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        SMSSDK.initSDK(this, "1b3738590413e", "9a60462d052655f550620dcee692e4f2");
        myApplication.imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(90), DensityUtil.dip2px(90))//图片大小
                .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
//                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)//缩放
                .setLoadingDrawableId(R.mipmap.logo)//加载中默认显示图片
                .setUseMemCache(true)//设置使用缓存
                .setFailureDrawableId(R.mipmap.logo)//加载失败后默认显示图片
                .build();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            myApplication.version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.w(e);
        }

        InitUtils.isFirst(this);
    }

    public void addActivity(Activity activity) {
        Activity old = activityMap.get(activity.getLocalClassName());
        if (old != null) {
            old.finish();
        }
        activityMap.put(activity.getClass().getSimpleName(), activity);
    }

    public void finishActivity(Class... clazz) {
        for (Class ac : clazz) {
            LogUtil.i(ac.getSimpleName());
            Activity activity = activityMap.get(ac.getSimpleName());
            if (activity != null) {
                LogUtil.i("activity!=null");
                activity.finish();
            }
        }

    }

    public void removeActivity(String name) {
        activityMap.remove(name);
    }

    public void exit() {
        for (Activity activity : activityMap.values()) {
            activity.finish();
        }
        System.exit(0);
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVersion() {
        return version;
    }

    public ImageOptions getImageOptions() {
        return imageOptions;
    }

    public boolean isNet() {
        return isNet;
    }

    public void setNet(boolean net) {
        isNet = net;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }



}