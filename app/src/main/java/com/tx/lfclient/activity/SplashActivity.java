package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.imp.SPUtils;
import com.tx.lfclient.R;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.Pane;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.utils.NetWorkJudge;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/8
 */
@ContentView(R.layout.activity_splash)
public class SplashActivity extends ImpActivity {
    @ViewInject(R.id.activity_splash_one)
    private ImageView splash_one;
    @ViewInject(R.id.activity_splash_two)
    private ImageView splash_two;
    @ViewInject(R.id.activity_splash_three)
    private ImageView splash_three;

    private UserDbManager userDbManager;

    private boolean isFirst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);

        try {
            init();
            initData();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void initView() {
        Animation animation_one = AnimationUtils.loadAnimation(this, R.anim.splash_one);
        Animation animation_two = AnimationUtils.loadAnimation(this, R.anim.splash_two);
        Animation animation_three = AnimationUtils.loadAnimation(this, R.anim.splash_three);
        animation_one.setAnimationListener(new AnimationImpl());
        splash_one.startAnimation(animation_one);
        splash_two.startAnimation(animation_two);
        splash_three.startAnimation(animation_three);

    }

    protected void initData() {
        isFirst = (boolean) SPUtils.get(this, "IS_FIRST", true);
        myApplication.setNet(NetWorkJudge.getJudge(this));
        //判断是否是第一次登录
        if (!isFirst) {
            boolean isLogin = (Boolean) SPUtils.get(this, "LOGIN", false);
            myApplication.setLogin(isLogin);
            myApplication.setExpire(getExpire(isLogin));
            //将user信息导入
            Long id = (Long) SPUtils.get(this, "LOGIN_USER_ID", -1L);
            User user = userDbManager.getUserById(id);

            if (user != null) {
                myApplication.setUser(user);
                LogUtil.i(user.toString());
            }

        }
    }

    protected void init() {
        userDbManager = UserDbManager.getInstance();
    }


    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent;
            if (isFirst) {
                intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            SplashActivity.this.startActivity(intent);
            myApplication.finishActivity(SplashActivity.class);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    private boolean getExpire(boolean isLogin) {
        Date current = new Date();
        long expireTime = current.getTime();
        if (isLogin) {
            expireTime = (long) SPUtils.get(this, "LOGIN_TIME", current.getTime());
        }
        return current.getTime() - expireTime > 1000 * 60 * 60 * 24;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
