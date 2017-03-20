package com.tx.lfclient.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tx.lfclient.R;
import com.tx.lfclient.activity.ChangeLocation;
import com.tx.lfclient.activity.KindActivity;
import com.tx.lfclient.application.MyApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
@ContentView(R.layout.fragment_home_page)
public class FragmentHomePage extends Fragment {

    @ViewInject(R.id.fragment_home_page_scrollview)
    private HorizontalScrollView mHorizontalScrollView;
    @ViewInject(R.id.fragment_home_page_linearLayout)
    private LinearLayout mLinearLayout;
    @ViewInject(R.id.fragment_home_page_tab_viewpager)
    private ViewPager pager;
    @ViewInject(R.id.fragment_home_page_imageView)
    private ImageView mImageView;
    @ViewInject(R.id.fragment_home_page_tab_city_school)
    private TextView city;


    private ArrayList<Fragment> fragments;

    private MyApplication myApplication;

    private int mScreenWidth;

    private int item_width;

    private int endPosition;

    private int beginPosition;

    private int currentFragmentIndex;

    private boolean isEnd;


    private String type;
    private String kind;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this,inflater,container);
        init();
        initView();
        initNav();
        initViewPager();
        return view;
    }
    private void init() {
        myApplication = MyApplication.getInstance();
        fragments = new ArrayList<>();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取屏幕分辨率
        mScreenWidth = dm.widthPixels;
        item_width = mScreenWidth / 4;
    }

    private void initView() {
        city.setText(myApplication.getLocation());
        mImageView.getLayoutParams().width = item_width;// 初始化图片位置
        mLinearLayout.getLayoutParams().width = 2 * item_width + 4;


    }

    private void initNav() {
        for (int i = 0; i < 2; i++) {
            RelativeLayout layout = new RelativeLayout(getActivity());
            TextView view = new TextView(getActivity());

            if (i == 0) {
                view.setText("失物");

            } else if (i == 1) {
                view.setText("招领");

            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(view, params);
            mLinearLayout.addView(layout, (int) (mScreenWidth / 4 + 0.5f), 50);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem((Integer) v.getTag());
                }
            });

            layout.setTag(i);
        }
    }


    private void initViewPager() {

        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);

        FragmentHomePageLeft fragmentHomePageLeft = new FragmentHomePageLeft();

        fragments.add(fragmentHomePageLeft);

        Bundle bundle_left = new Bundle();
        bundle_left.putString("Kind", kind);
        fragmentHomePageLeft.setArguments(bundle_left);
        // 视频标签页面
        FragmentHomePageRight fragmentHomePageRight = new FragmentHomePageRight();
        fragments.add(fragmentHomePageRight);
        Bundle bundle_right = new Bundle();
        bundle_left.putString("Kind", kind);
        fragmentHomePageRight.setArguments(bundle_right);
        pager.setAdapter(fragmentPagerAdapter);
        fragmentPagerAdapter.setFragments(fragments);
        pager.addOnPageChangeListener(new MyOnPageChangeListener());

        if (type == null) {
            pager.setCurrentItem(0);// 设置默认页卡
        } else {
            if ("失物".equals(type)) {

                pager.setCurrentItem(0);// 设置默认页卡

            } else if ("招领".equals(type)) {

                pager.setCurrentItem(1);// 设置默认页卡

            }
        }

    }


    @Event(type = View.OnClickListener.class,value = {R.id.fragment_home_page_tab_kind, R.id.fragment_home_page_tab_city_school})
    private void turnToKind(View v) {

        switch (v.getId()) {

            case R.id.fragment_home_page_tab_kind:
                Intent intent = new Intent(getActivity(), KindActivity.class);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                intent.putExtra("currentFragmentIndex", currentFragmentIndex);
                startActivity(intent);
                break;
            case R.id.fragment_home_page_tab_city_school:
                intent = new Intent(getActivity(), ChangeLocation.class);
                intent.putExtra("location", myApplication.getLocation());
                startActivity(intent);
                break;
        }


    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private FragmentManager fm;


        MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        void setFragments(ArrayList<Fragment> fragments) {
            if (this.fragments != null) {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.fragments) {
                    ft.remove(f);
                }
                ft.commit();
                fm.executePendingTransactions();
            }
            this.fragments = fragments;
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            return super.instantiateItem(container, position);
        }

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        /**
         * 此方法是页面跳转完后得到调用， arg0是你当前选中的页面的Position（位置编号）。
         */
        @Override
        public void onPageSelected(int position) {


            Animation animation = new TranslateAnimation(endPosition, position * item_width, 0, 0);
            beginPosition = position * item_width;
            currentFragmentIndex = position;
            animation.setFillAfter(true);// 停留最后效果
            animation.setDuration(200);// 设置动画持续时间
            mImageView.startAnimation(animation);// 开始


        }

        /**
         * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到
         * <p>
         * 调用。其中三个参数的含义分别为：
         * <p>
         * position :当前页面，及你点击滑动的页面
         * <p>
         * positionOffset:当前页面偏移的百分比
         * <p>
         * positionOffsetPixels:当前页面偏移的像素位置
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if (!isEnd) {
                if (currentFragmentIndex == position) {
                    endPosition = item_width * currentFragmentIndex + (int) (item_width * positionOffset);
                }

                if (currentFragmentIndex == position + 1) {
                    endPosition = item_width * currentFragmentIndex - (int) (item_width * (1 - positionOffset));
                }

                Animation mAnimation = new TranslateAnimation(beginPosition, endPosition, 0, 0);

                mAnimation.setFillAfter(true);

                mAnimation.setDuration(200);

                mImageView.startAnimation(mAnimation);

                mHorizontalScrollView.invalidate();

                beginPosition = endPosition;
            }

        }

        /**
         * 此方法是在状态改变的时候调用，state
         * <p>
         * 有三种状态（0，1，2）。state ==1的时辰默示正在滑动，state==2的时辰默示滑动完毕了，state==0的时辰默示什么都没做。
         * <p>
         * 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
         */

        @Override
        public void onPageScrollStateChanged(int state) {

            switch (state) {

                case ViewPager.SCROLL_STATE_DRAGGING:
                    isEnd = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    isEnd = true;
                    beginPosition = currentFragmentIndex * item_width;
                    if (pager.getCurrentItem() == currentFragmentIndex) {

                        // 未跳入下一个页面

                        mImageView.clearAnimation();

                        //恢复位置
                        Animation animation = new TranslateAnimation(endPosition, beginPosition, 0, 0);

                        animation.setFillAfter(true);

                        animation.setDuration(250);

                        mImageView.startAnimation(animation);

                        mHorizontalScrollView.invalidate();

                        endPosition = currentFragmentIndex * item_width;

                        break;
                    }
            }
        }

    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
