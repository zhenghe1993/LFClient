package com.tx.lfclient.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.view.EditorPopupView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
@ContentView(R.layout.activity_main_plus)
public class PlusWindowActivity extends ImpActivity {


    private EditorPopupView editorPopupView;

    @ViewInject(R.id.plus_cancel)
    private ImageView cancelImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initView();


    }

    protected void initView() {
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        cancelImageView.startAnimation(animation);
        editorPopupView = new EditorPopupView(this, itemsOnClick);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 设置layout在PopupWindow中显示的位置
                editorPopupView.showAtLocation(PlusWindowActivity.this.findViewById(R.id.main), Gravity.CENTER_VERTICAL, 0, 0);
            }
        }, 50);


        editorPopupView.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                cancelImageView.startAnimation(animation);
                myApplication.finishActivity(PlusWindowActivity.class);
                overridePendingTransition(R.anim.rotate, R.anim.rotateout);
            }
        });
    }

    protected void init() {

    }

    @Override
    void initData() {

    }


    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {

            if (!myApplication.getLogin()) {
                ToastShow.showLong(getApplicationContext(), "您还未登录哦");
                return;
            }
            Handler handler = new Handler();
            Intent intent = new Intent();
            Animation scaleAnimation = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(500);
            v.startAnimation(scaleAnimation);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editorPopupView.dismiss();
                }
            }, 200);
            switch (v.getId()) {
                case R.id.lost_information:
                    intent.setClass(v.getContext(), PostLFMessage.class);
                    intent.putExtra("type", "LOST");
                    break;
                case R.id.found_information:
                    intent.setClass(v.getContext(), PostLFMessage.class);
                    intent.putExtra("type", "FOUND");
                    break;
                case R.id.discover_information:
                    intent.setClass(v.getContext(), PostDiscoverMessage.class);
                    break;
            }
            v.getContext().startActivity(intent);
            myApplication.finishActivity(PlusWindowActivity.class);
            overridePendingTransition(0, 0);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
