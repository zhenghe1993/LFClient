package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.utils.EditTextWatcher;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.inter.IEditTextWatcher;
import com.tx.lfclient.utils.inter.IUIHandler;

import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/10
 */
@ContentView(R.layout.smssdk_input_identify_num_page)
public class IdentifyCodeActivity extends ImpActivity implements IUIHandler, IEditTextWatcher {

    private static final int RETRY_INTERVAL = 60;


    @ViewInject(R.id.tv_phone)
    private TextView text_phone;
    @ViewInject(R.id.tv_identify_notify)
    private TextView tv_identify_notify;
    @ViewInject(R.id.et_put_identify)
    private EditText edit_code;
    @ViewInject(R.id.iv_clear)
    private ImageView clear;
    @ViewInject(R.id.tv_unreceive_identify)
    private TextView unReceive;


    private String phoneNumber;
    private String countryCode;

    private int time;

    private UIHandler<IdentifyCodeActivity> uiHandler;

    private EventHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initData();
        initView();
    }


    protected void init() {
        EditTextWatcher<IdentifyCodeActivity> textWatch = new EditTextWatcher<>(this);
        edit_code.addTextChangedListener(textWatch);
        uiHandler = new UIHandler<>(this);
    }


    protected void initData() {
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        countryCode = intent.getStringExtra("countryCode");


    }

    protected void initView() {
        text_phone.setText("+" + countryCode + " " + phoneNumber);
        tv_identify_notify.setText((Html.fromHtml(this.getString(R.string.smssdk_make_sure_mobile_detail))));
        countDown();
        handler = new EventHandler() {
            public void afterEvent(final int event, final int result,
                                   final Object data) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    uiHandler.sendEmptyMessage(0);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功后的执行动作
                    uiHandler.sendEmptyMessage(6);
                }
            }
        };

        SMSSDK.registerEventHandler(handler);
    }

    @Event(type = View.OnClickListener.class, value = {R.id.back, R.id.tv_unreceive_identify, R.id.btn_submit, R.id.iv_clear})
    private void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                myApplication.finishActivity(getClass());
                break;
            case R.id.tv_unreceive_identify:
                if (!myApplication.isNet()) {
                    ToastShow.showShort(this, "无网络访问！！！");
                    break;
                }
                SMSSDK.getVerificationCode(countryCode, phoneNumber);
                break;
            case R.id.btn_submit:
                if (!myApplication.isNet()) {
                    ToastShow.showShort(this, "无网络访问！！！");
                    break;
                }
                String currentCode = edit_code.getText().toString().trim();
                if (!TextUtils.isEmpty(currentCode)) {
                    SMSSDK.submitVerificationCode(countryCode, phoneNumber, currentCode);
                }
                break;
            case R.id.iv_clear:
                edit_code.setText("");
                break;
        }
    }

    // 倒数计时
    private void countDown() {

        time = RETRY_INTERVAL;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time > 0) {
                    uiHandler.sendEmptyMessage(7);
                } else {
                    uiHandler.sendEmptyMessage(8);
                }
            }
        }, 0, 1000);


    }

    @Override
    public void sendMessage(Message msg) {

        Bundle bundle = msg.getData();
        String res = bundle.getString("res");
        switch (msg.what) {
            case -1:
                ToastShow.showShort(this, res);
                break;
            case 0:
                Intent in = new Intent(this, RegisterActivity.class);
                User user = myApplication.getUser();
                user.setPhoneNumber(phoneNumber);
                myApplication.setUser(user);
                startActivity(in);
                break;
            case 6:
                ToastShow.showShort(this, "验证码获取成功");
                countDown();
                break;
            case 7:
                unReceive.setText(Html.fromHtml(String.format(this.getString(R.string.smssdk_receive_msg), "" + time--)));
                break;
            case 8:
                unReceive.setText(Html.fromHtml(this.getString(R.string.smssdk_unreceive_identify_code)));
                break;
        }


    }

    @Override
    public void onTextChanged(CharSequence s) {
        if (s.length() > 0) {
            clear.setVisibility(View.VISIBLE);
        } else {
            clear.setVisibility(View.GONE);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
        SMSSDK.unregisterEventHandler(handler);
    }


}
