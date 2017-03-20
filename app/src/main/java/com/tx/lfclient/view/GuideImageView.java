package com.tx.lfclient.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tx.lfclient.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/8
 */
public class GuideImageView {
    private View view;
    @ViewInject(R.id.guide_bg)
    private ImageView image_bg;
    @ViewInject(R.id.guide_text)
    private ImageView image_text;
    @ViewInject(R.id.guide_next)
    private ImageView image_next;

    public GuideImageView(Context context, int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(layoutId, null);
        x.view().inject(this, view);
    }

    public View getView() {
        return view;
    }

    public void startAnimation(Activity activity) {
        image_bg.setVisibility(View.VISIBLE);
        image_text.setVisibility(View.VISIBLE);
        image_bg.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.in_from_top));
        image_text.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.title_scale_anim));
        image_next.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.next_anim));
    }

    public void clearAnimation() {
        image_bg.clearAnimation();
        image_bg.setVisibility(View.INVISIBLE);
        image_text.clearAnimation();
        image_text.setVisibility(View.INVISIBLE);
    }


}