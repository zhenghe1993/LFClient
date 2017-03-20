package com.tx.lfclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tx.lfclient.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/10
 */
@ContentView(R.layout.activity_service)
public class ServiceActivity extends ImpActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,this);
    }

    @Event(type = View.OnClickListener.class, value = R.id.back)
    private void onClick(View v) {
       myApplication.finishActivity(getClass());
    }

    @Override
    void init() {
    }

    @Override
    void initData() {

    }

    @Override
    void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
