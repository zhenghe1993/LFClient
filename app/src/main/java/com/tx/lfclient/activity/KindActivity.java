package com.tx.lfclient.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;


import com.imp.ScreenUtils;
import com.tx.lfclient.R;
import com.tx.lfclient.adapter.GridViewAdapter;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.entities.Pane;
import com.tx.lfclient.event.MainEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
@ContentView(R.layout.activity_kind)
public class KindActivity extends ImpActivity {

    @ViewInject(R.id.activity_kind)
    private RelativeLayout relative;
    @ViewInject(R.id.activity_kind_gird_view)
    private GridView gridview;

    private int mScreenWidth;

    private int isIndex;

    private List<Pane> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initData();
        initView();
    }

    protected void initData() {
        isIndex = getIntent().getIntExtra("currentFragmentIndex", 0);
        TypedArray ar = getResources().obtainTypedArray(R.array.category_image);
        String[] titles = getResources().getStringArray(R.array.category_title);
        int len = ar.length();
        int[] images = new int[len];
        for (int i = 0; i < len; i++) {
            images[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        for (int i = 0; i < titles.length; i++) {
            if (titles[i].equals("找人") && isIndex == 1) {
                continue;
            }
            list.add(new Pane(titles[i], images[i]));
        }
    }

    protected void init() {
        list = new ArrayList<>();
        mScreenWidth = ScreenUtils.getScreenWidth(this);
    }

    protected void initView() {
        relative.getLayoutParams().height = mScreenWidth / 3 * 5;
        gridview.setEnabled(false);
        //添加适配器
        gridview.setAdapter(new GridViewAdapter(this, list));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gridview.setEnabled(true);
            }
        }, 2000);

    }


    @Event(type = AdapterView.OnItemClickListener.class, value = R.id.activity_kind_gird_view)
    private void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        MainEvent event = new MainEvent();
        event.setSetting(false);
        event.setType(isIndex == 0 ? "失物" : "招领");
        event.setKind(list.get(arg2).getTitle());
        EventBus.getDefault().post(event);
        myApplication.finishActivity(getClass());

    }


    @Event(type = View.OnClickListener.class, value = R.id.activity_kind_back)
    private void onClick(View v) {
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        myApplication.finishActivity(getClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
