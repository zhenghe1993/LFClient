package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.cascade.CharacterParser;
import com.tx.lfclient.cascade.PinyinComparator;
import com.tx.lfclient.cascade.SideBar;
import com.tx.lfclient.cascade.SortAdapter;
import com.tx.lfclient.cascade.SortModel;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.utils.AlertUser;
import com.tx.lfclient.utils.LocationSelector;
import com.tx.lfclient.utils.ResultValidatorUtils;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.inter.IAlertUser;
import com.tx.lfclient.utils.inter.IUIHandler;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by (IMP)郑和明
 * Date is 2017/2/5
 */
@ContentView(R.layout.activity_location)
public class LocationActivity extends ImpActivity implements IUIHandler, IAlertUser {

    @ViewInject(R.id.activity_location_list_view)
    private ListView sortListView;
    @ViewInject(R.id.activity_location_sidebar)
    private SideBar sideBar;
    @ViewInject(R.id.activity_location_dialog)
    private TextView dialog;
    private SortAdapter adapter;
    private int kind;
    private String sortPage = null;
    private String province = null;
    private LocationSelector locationSelector;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;


    private UIHandler<LocationActivity> handler;
    private AlertUser<LocationActivity> alertUser;

    private User user;

    private UserDbManager userDbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initData();
        initView();
    }

    protected void initData() {
        Intent intent = getIntent();
        province = intent.getStringExtra("province");
        sortPage = intent.getStringExtra("sortPage");
        kind = intent.getStringExtra("kind").equals("school") ? 1 : 0;
        user = myApplication.getUser();
    }


    protected void init() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        locationSelector = new LocationSelector(this);
        handler = new UIHandler<>(this);
        alertUser = new AlertUser<>(this);
        userDbManager = UserDbManager.getInstance();

    }


    protected void initView() {

        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });


        List<SortModel> sourceDateList = filledData(locationSelector.getStringArray(province, kind));
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, sourceDateList);
        sortListView.setAdapter(adapter);
    }

    @Event(type = View.OnClickListener.class, value = R.id.activity_location_back)
    private void onClick(View v) {
        back();
    }

    // 根据输入框输入值的改变来过滤搜索
    @Event(type = AdapterView.OnItemClickListener.class, value = R.id.activity_location_list_view)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        {
            String location = ((SortModel) adapter.getItem(position)).getName();
            // 这里要利用adapter.getItem(position)来获取当前position所对应的对象


            //跳转，或者修改操作

            switch (sortPage) {
                case "register":
                    Intent intent = new Intent(this, SmsActivity.class);
                    User user = myApplication.getUser();
                    user.setLocation(location);
                    myApplication.setUser(user);
                    startActivity(intent);
                    break;
                case "home_page":
                    myApplication.setLocation(location);
                    break;
                case "setting":
                    alertUser.post("1", location, "1", null);
                    break;
            }
        }
    }

    /**
     * 为ListView填充数据
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<>();

        for (String aDate : date) {
            SortModel sortModel = new SortModel();
            sortModel.setName(aDate);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(aDate);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        myApplication.finishActivity(getClass());
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onResponse(Call call, Response response) throws Exception {
        int code = response.code();
        String res = response.body().string();

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("res", res);
        bundle.putInt("code", code);
        message.setData(bundle);
        message.what = 1;
        handler.sendMessage(message);


    }

    @Override
    public void sendMessage(Message msg) {
        switch (msg.what) {
            case 0:
                ToastShow.showShort(this, "修改失败");
                break;
            case 1:
                Bundle bundle = msg.getData();
                String res = bundle.getString("res");
                int code = bundle.getInt("code");

                res = ResultValidatorUtils.getResult(LocationActivity.this, code, res);


                if (res.equals("ERROR")) {
                    return;
                }
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    user = mapper.readValue(res, User.class);
                } catch (Exception e) {
                    ToastShow.showShort(this, "数据格式错误");
                    LogUtil.w(e);
                }
                if (user == null) {
                    ToastShow.showShort(this, "修改失败");
                    break;
                }

                ToastShow.showShort(this, "修改位置成功");
                myApplication.setUser(user);
                userDbManager.saveOrUpdateUser(user);
                myApplication.finishActivity(LocationActivity.class, ProvinceActivity.class);
                break;
        }
    }
}
