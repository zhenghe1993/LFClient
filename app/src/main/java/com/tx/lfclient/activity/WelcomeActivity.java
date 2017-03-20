package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.imp.SPUtils;
import com.tx.lfclient.R;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.view.GuideImageView;
import com.tx.lfclient.view.VerticalSlidingView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/8
 */
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends ImpActivity {

    @ViewInject(R.id.activity_welcome_sliding_view)
    private VerticalSlidingView verticalSlidingView;

    private List<GuideImageView> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initView();

    }


    @Event(type = VerticalSlidingView.OnPageScrollListener.class, value = R.id.activity_welcome_sliding_view)
    private void onPageChanged(int position) {

        GuideImageView imageView = list.get(position);
        imageView.startAnimation(this);
        if (position == 0) {
            GuideImageView subImageView = list.get(position + 1);
            subImageView.clearAnimation();
        } else if (position == 4) {
            GuideImageView subImageView = list.get(position - 1);
            subImageView.clearAnimation();
        } else {
            GuideImageView subImageView_1 = list.get(position + 1);
            GuideImageView subImageView_2 = list.get(position - 1);
            subImageView_1.clearAnimation();
            subImageView_2.clearAnimation();
        }

    }

    @Override
    void init() {
        list = new ArrayList<>();
    }

    @Override
    void initData() {

    }

    @Override
    void initView() {

        int[] layouts = new int[]{R.layout.guide_num_one, R.layout.guide_num_two, R.layout.guide_num_three, R.layout.guide_num_four, R.layout.guide_num_five};
        for (int id : layouts) {
            GuideImageView imageView = new GuideImageView(this, id);
            View view = imageView.getView();
            if (id == R.layout.guide_num_five) {
                Button begin = (Button) view.findViewById(R.id.guide_begin);
                begin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPUtils.put(WelcomeActivity.this, "IS_FIRST", false);//将应用标记为不是第一次登录
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        WelcomeActivity.this.startActivity(intent);
                        myApplication.finishActivity(WelcomeActivity.class);
                    }
                });
            }
            list.add(imageView);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            verticalSlidingView.addView(view, lp);
        }
        GuideImageView imageView = list.get(0);
        imageView.startAnimation(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
