package com.tx.lfclient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tx.lfclient.R;
import com.tx.lfclient.adapter.DisplayPictureAdapter;
import com.tx.lfclient.custom.NineBitMap;
import com.tx.lfclient.event.OnActivityResult;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IMP(郑和明)
 * date is 2017/3/12.
 */
@ContentView(R.layout.activity_display_picture)
public class DisplayPictureActivity extends ImpActivity {
    @ViewInject(R.id.display_viewpager)
    private ViewPager viewPager;

    private int position;

    private NineBitMap nineBitMap;
    private DisplayPictureAdapter adapter;
    private List<View> list;

    private int currentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initData();
        initView();
    }

    @Override
    void init() {
        nineBitMap = NineBitMap.getInstance(this);
        list = new ArrayList<>();
        initListViews(nineBitMap);
        adapter = new DisplayPictureAdapter(list);
    }

    @Override
    void initData() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
    }

    @Override
    void initView() {
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(listener);
        viewPager.setCurrentItem(position);
    }

    @Event(type = View.OnClickListener.class, value = {R.id.display_cancel, R.id.display_confirm, R.id.display_delete})
    private void OnClick(View v) {
        switch (v.getId()) {

            case R.id.display_confirm:
            case R.id.display_cancel:
                myApplication.finishActivity(DisplayPictureActivity.class);
                break;
            case R.id.display_delete:

                boolean flag = nineBitMap.deleteBitmap(currentPosition + 1);
                if (flag) {
                    initListViews(nineBitMap);
                    adapter.updataList(list);
                    OnActivityResult onActivityResult=new OnActivityResult();
                    onActivityResult.setTrue(true);
                    EventBus.getDefault().post(onActivityResult);
                    myApplication.finishActivity(DisplayPictureActivity.class);
                }

        }


    }

    private void initListViews(NineBitMap map) {
        list.clear();
        for (int i = 1; i < map.size(); i++) {
            Bitmap bitmap = map.queryBitmap(i);
            ImageView img = new ImageView(this);// 构造textView对象
            img.setBackgroundColor(0xff000000);
            img.setImageBitmap(bitmap);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            list.add(img);// 添加view
        }
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
