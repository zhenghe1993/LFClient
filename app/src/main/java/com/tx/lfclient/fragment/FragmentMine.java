package com.tx.lfclient.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imp.ScreenUtils;
import com.tx.lfclient.R;
import com.tx.lfclient.activity.AccountSettingActivity;
import com.tx.lfclient.activity.LoginActivity;
import com.tx.lfclient.activity.MainActivity;
import com.tx.lfclient.adapter.GridViewAdapter;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.entities.Pane;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.url.UrlPath;

import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
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
@ContentView(R.layout.fragment_mine)
public class FragmentMine extends Fragment {
    @ViewInject(R.id.fragment_mine_sub)
    private LinearLayout sub;
    @ViewInject(R.id.fragment_mine_sub_un)
    private LinearLayout sub_un;
    @ViewInject(R.id.fragment_mine_sub_portrait)
    private ImageView portrait;
    @ViewInject(R.id.fragment_mine_sub_name)
    private TextView name;
    @ViewInject(R.id.fragment_mine_sub_location)
    private TextView location;
    @ViewInject(R.id.fragment_mine_relative)
    private RelativeLayout relative;
    @ViewInject(R.id.fragment_mine_grid_view)
    private GridView gridView;


    private MyApplication myApplication;
    private int mScreenWidth;

    private List<Pane> list;
    private User user;
    private ImageOptions imageOptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        LogUtil.i("onCreateView");
        try {
            init();
            initData();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initData() {
        mScreenWidth = ScreenUtils.getScreenWidth(getContext());
        String[] titles = getResources().getStringArray(R.array.mine_page_text);
        TypedArray ar = getResources().obtainTypedArray(R.array.mine_page_image);
        int len = ar.length();
        int[] images = new int[len];
        for (int i = 0; i < len; i++) {
            images[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        for (int i = 0; i < titles.length; i++) {
            list.add(new Pane(titles[i], images[i]));
        }

    }

    private void initView() {
        //九宫格内容
        relative.getLayoutParams().height = mScreenWidth / 3 * 3;
        gridView.setAdapter(new GridViewAdapter(getActivity(), list));


    }

    private void init() {
        myApplication = MyApplication.getInstance();
        list = new ArrayList<>();
        user = myApplication.getUser();
        imageOptions = myApplication.getImageOptions();
    }


    @Event(type = AdapterView.OnItemClickListener.class, value = R.id.fragment_mine_grid_view)
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


        switch (arg2) {
            case 0:// 失物记录
               Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                startActivity(intent);

                break;
            case 1:// 招领记录


                break;
            case 2:// 发现记录


                break;
            case 3:// 账户设置
                intent = new Intent(getActivity(), AccountSettingActivity.class);
                startActivity(intent);
                break;
            case 4://分享


                break;
            case 5:// 清空缓存


                break;
            case 6:// 意见反馈


                break;
            case 7:// 系统更新

                break;
            case 8:// 关于我们

                break;

            default:
                break;
        }
    }

    @Event(type = View.OnClickListener.class, value = {R.id.fragment_mine_sub_un_login, R.id.fragment_mine_sub_portrait})
    private void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_mine_sub_un_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_mine_sub_portrait:
                intent = new Intent(getActivity(), AccountSettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //控制登录与非登陆状态
        if (myApplication.getLogin()) {
            sub.setVisibility(View.VISIBLE);
            sub_un.setVisibility(View.GONE);
            x.image().bind(portrait, UrlPath.root + user.getPortrait(), imageOptions);
            location.setText(user.getLocation());
            name.setText(user.getName());

        } else {
            sub.setVisibility(View.GONE);
            sub_un.setVisibility(View.VISIBLE);
        }
    }



}
