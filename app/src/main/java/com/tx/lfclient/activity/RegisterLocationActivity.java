package com.tx.lfclient.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tx.lfclient.R;
import com.tx.lfclient.application.MyApplication;

import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/9
 */
@ContentView(R.layout.activity_register_location)
public class RegisterLocationActivity extends ImpActivity {
    @ViewInject(R.id.activity_register_location_image)
    private ImageView imageView;
    @ViewInject(R.id.activity_register_location_type)
    private TextView type;
    @ViewInject(R.id.activity_register_location_please)
    private TextView please;
    @ViewInject(R.id.activity_register_location_school)
    private Button school;
    @ViewInject(R.id.activity_register_location_city)
    private Button city;


    private String kind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,this);
        init();
        initView();
    }

    protected void initView() {

    }

    protected void init() {

    }

    @Override
    void initData() {

    }

    @Event(type = View.OnClickListener.class, value = {R.id.activity_register_location_school, R.id.activity_register_location_city})
    private void onClick(View v) {
        v.bringToFront();
        switch (v.getId()) {
            case R.id.activity_register_location_school:
                kind = "school";
                break;
            case R.id.activity_register_location_city:
                kind = "society";
                break;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(RegisterLocationActivity.this, ProvinceActivity.class);
                intent.putExtra("kind", kind);
                intent.putExtra("sortPage", "register");
                startActivity(intent);
                myApplication.finishActivity(RegisterLocationActivity.class);
            }
        }, 2000);

        schoolAndCity();
    }


    private void imageAndTextView(View view) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 3f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 3f);
        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", 0f, -1000f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY, translateY).setDuration(1000);
        objectAnimator.start();
    }

    private void schoolAndCity() {
        ObjectAnimator right = ObjectAnimator.ofFloat(school, "translationX", 0, DensityUtil.dip2px(75)).setDuration(1000);
        ObjectAnimator left = ObjectAnimator.ofFloat(city, "translationX", 0, -DensityUtil.dip2px(75)).setDuration(1000);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(right).with(left);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if ("society".equals(kind)) {
                    school.setVisibility(View.GONE);
                } else {
                    city.setVisibility(View.GONE);
                }
                imageAndTextView(city);
                imageAndTextView(school);
                imageAndTextView(imageView);
                imageAndTextView(type);
                imageAndTextView(please);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
