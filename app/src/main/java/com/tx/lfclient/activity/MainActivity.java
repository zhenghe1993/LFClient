package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.imp.ToastShow;

import com.imp.adapter.ValidatorForm;
import com.imp.utils.BaseResult;
import com.imp.utils.ValidatorType;
import com.tx.lfclient.R;
import com.tx.lfclient.event.MainEvent;
import com.tx.lfclient.fragment.FragmentDiscoverList;
import com.tx.lfclient.fragment.FragmentHomePage;
import com.tx.lfclient.fragment.FragmentMessage;
import com.tx.lfclient.fragment.FragmentMine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends ImpActivity {

    @ViewInject(R.id.activity_main_home_page)
    private RadioButton radioButtonHomePage;
    @ViewInject(R.id.activity_main_discover)
    private RadioButton radioButtonDiscover;
    @ViewInject(R.id.activity_main_message)
    private RadioButton radioButtonMessage;
    @ViewInject(R.id.activity_main_mine)
    private RadioButton radioButtonMine;


    private FragmentMine fragmentMine;
    private FragmentHomePage fragmentHomePage;
    private FragmentManager fragmentManager;
    private List<RadioButton> radioButtonList = new ArrayList<>();

    private long exitTime = 0;

    private boolean isNet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        EventBus.getDefault().register(this);
        init();
        initBottomBar();


        ValidatorForm form=new ValidatorForm(this);
        form.addValidator(new BaseResult("账户",ValidatorType.STRING_MAX_LENGTH,"im9p",4));
        boolean res=form.form();
        System.out.println(res);
    }


    protected void init() {

        fragmentManager = getSupportFragmentManager();
        fragmentHomePage = new FragmentHomePage();
        fragmentMine = new FragmentMine();
    }

    @Override
    void initData() {
        isNet = myApplication.isNet();
    }

    @Override
    void initView() {
        radioButtonList.add(radioButtonHomePage);
        radioButtonList.add(radioButtonDiscover);
        radioButtonList.add(radioButtonMessage);
        radioButtonList.add(radioButtonMine);
        fragmentHomePage.setKind("所有");
        fragmentHomePage.setType("失物");

    }

    private void initBottomBar() {
        fragmentManager.beginTransaction().replace(R.id.activity_main_page, fragmentHomePage).commit();
    }

    @Event(type = RadioGroup.OnCheckedChangeListener.class, value = R.id.activity_main_radioGroup)
    private void OnCheckedChangeListener(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.activity_main_home_page:
                fragmentManager.beginTransaction().replace(R.id.activity_main_page, fragmentHomePage).commit();
                break;
            case R.id.activity_main_discover:
                setCheck(1);
                fragmentManager.beginTransaction().replace(R.id.activity_main_page, new FragmentDiscoverList()).commit();
                break;

            case R.id.activity_main_message:
                setCheck(2);
                fragmentManager.beginTransaction().replace(R.id.activity_main_page, new FragmentMessage()).commit();
                break;
            case R.id.activity_main_mine:
                setCheck(3);
                fragmentManager.beginTransaction().replace(R.id.activity_main_page, fragmentMine).commit();
                break;
            default:
                break;
        }


    }

    @Event(type = View.OnClickListener.class, value = R.id.activity_main_plus)
    private void onClickPlus(View v) {
        Intent intent = new Intent(MainActivity.this, PlusWindowActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.rotate, R.anim.rotateout);
    }

    private void setCheck(int index) {

        for (int i = 0; i < radioButtonList.size(); i++) {

            if (i == index) {
                radioButtonList.get(index).setChecked(true);
            } else {
                radioButtonList.get(i).setChecked(false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainEvent event) {
        if (event.isSetting()) {
            setCheck(3);
            fragmentManager.beginTransaction().replace(R.id.activity_main_page, fragmentMine).commitAllowingStateLoss();
        } else {
            fragmentHomePage.setKind(event.getKind());
            fragmentHomePage.setType(event.getType());
            fragmentManager.beginTransaction().replace(R.id.activity_main_page, fragmentHomePage).commitAllowingStateLoss();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastShow.showShort(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                myApplication.exit();// 可直接关闭所有的Acitivity并退出应用程序。
            }
            return true;
        }
        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
        EventBus.getDefault().unregister(this);

    }
}
