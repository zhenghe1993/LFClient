package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tx.lfclient.R;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.cascade.CharacterParser;
import com.tx.lfclient.cascade.PinyinComparator;
import com.tx.lfclient.cascade.SideBar;
import com.tx.lfclient.cascade.SortAdapter;
import com.tx.lfclient.cascade.SortModel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
@ContentView(R.layout.activity_province)
public class ProvinceActivity extends ImpActivity {

    @ViewInject(R.id.activity_province_list_view)
    private ListView sortListView;
    @ViewInject(R.id.activity_province_sidebar)
    private SideBar sideBar;
    @ViewInject(R.id.activity_province_dialog)
    private TextView dialog;
    private SortAdapter adapter;
    private String kind = null;
    private String sortPage = null;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,this);
        init();
        initData();
        initView();
    }

    protected void initData() {
        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        sortPage = intent.getStringExtra("sortPage");
    }

    protected void init() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
    }


    protected void initView() {

        sideBar.setTextView(dialog);


        List<SortModel> sourceDateList = filledData(getResources().getStringArray(R.array.province));

        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, sourceDateList);
        sortListView.setAdapter(adapter);
    }

    // 根据输入框输入值的改变来过滤搜索
    @Event(type = AdapterView.OnItemClickListener.class, value = R.id.activity_province_list_view)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        {
            String province = ((SortModel) adapter.getItem(position)).getName();
            // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
            Toast.makeText(getApplication(), province, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LocationActivity.class);
            intent.putExtra("province", province);
            intent.putExtra("sortPage", sortPage);
            intent.putExtra("kind", kind);
            startActivity(intent);

        }
    }

    @Event(type = View.OnClickListener.class, value = R.id.activity_province_back)
    private void onClick(View v) {
        back();
    }

    //触摸监听
    @Event(type = SideBar.OnTouchingLetterChangedListener.class, value = R.id.activity_province_sidebar)
    private void onTouchingLetterChanged(String s) {
        {
            // 该字母首次出现的位置
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                sortListView.setSelection(position);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            back();
        }
        return super.onKeyDown(keyCode, event);
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

    private void back() {

        Intent intent;

        switch (sortPage){
            case "register":
                intent=new Intent(this,RegisterLocationActivity.class);
                startActivity(intent);

                break;
        }
        myApplication.finishActivity(getClass());
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
