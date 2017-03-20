package com.tx.lfclient.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tx.lfclient.application.MyApplication;

import org.xutils.x;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/13
 * <p>
 * 当前 Activity 父类
 * <p>
 * 存放一些公共成员  比如  MyApplication
 */
public abstract class ImpActivity extends AppCompatActivity {
    protected MyApplication myApplication;

    protected void onCreate(@Nullable Bundle savedInstanceState, ImpActivity activity) {
        super.onCreate(savedInstanceState);
        init(activity);

    }

    private void init(ImpActivity activity) {
        x.view().inject(activity);
        myApplication = MyApplication.getInstance();
        myApplication.addActivity(activity);
    }

    abstract void init();

    abstract void initData() throws Exception;

    abstract void initView();


    protected void onDestroy(Activity activity) {

        myApplication.removeActivity(activity.getClass().getSimpleName());
    }
}
