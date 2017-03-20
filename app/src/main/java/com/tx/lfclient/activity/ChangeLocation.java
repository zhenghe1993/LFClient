package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tx.lfclient.R;
import com.tx.lfclient.application.MyApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
@ContentView(R.layout.change_location)
public class ChangeLocation extends ImpActivity {


    @ViewInject(R.id.change_location_location_now)
    TextView change_location_location_now;
    @ViewInject(R.id.change_location_city_image)
    ImageView change_location_city_image;
    @ViewInject(R.id.change_location_school_image)
    ImageView change_location_school_image;
    @ViewInject(R.id.change_location_scrollview)
    private HorizontalScrollView mScrollView;

    private Handler mHandler = new Handler();

    private String country;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initView();

    }

    @Override
    protected void init() {
        country = getIntent().getStringExtra("location");
    }

    @Override
    void initData() {

    }

    @Override
    protected void initView() {
        change_location_location_now.setText(country);
        mHandler.post(mScrollToBottom); // 关键是这里
    }


    @Event(type = View.OnClickListener.class, value = {R.id.change_location_location_china,
            R.id.change_location_one,
            R.id.change_location_two,
            R.id.change_location_back,
            R.id.change_location_cancel})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_location_location_china:
                Intent intent = new Intent(ChangeLocation.this, MainActivity.class);
                startActivity(intent);
                myApplication.setLocation("全国");
                myApplication.finishActivity(getClass());
                break;
            case R.id.change_location_one:
                change_location_city_image.isClickable();
                intent = new Intent(getApplicationContext(), ProvinceActivity.class);
                intent.putExtra("kind", "society");
                intent.putExtra("sortPage", "home_page");
                startActivity(intent);
                break;
            case R.id.change_location_two:
                change_location_school_image.isClickable();
                intent = new Intent(getApplicationContext(), ProvinceActivity.class);
                intent.putExtra("kind", "school");
                intent.putExtra("sortPage", "home_page");
                startActivity(intent);
                break;
            case R.id.change_location_back:
            case R.id.change_location_cancel:
                myApplication.finishActivity(getClass());
                break;
        }
    }

    private Runnable mScrollToBottom = new Runnable() {
        @Override
        public void run() {
            int off = change_location_location_now.getMeasuredWidth() - mScrollView.getWidth(); // 计算移动量
            if (off > 0) {
                mScrollView.scrollTo(0, off); // 自动移动
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
