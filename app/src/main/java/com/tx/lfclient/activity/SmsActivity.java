package com.tx.lfclient.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.url.UrlPath;
import com.tx.lfclient.utils.EditTextWatcher;
import com.tx.lfclient.utils.inter.IEditTextWatcher;
import com.tx.lfclient.utils.inter.IUIHandler;
import com.tx.lfclient.utils.UIHandler;

import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by (IMP)郑和明
 * Date is 2017/2/10
 */
@ContentView(R.layout.smssdk_regist_page)
public class SmsActivity extends ImpActivity implements IUIHandler, IEditTextWatcher {


    private static final String DEFAULT_COUNTRY_ID = "86";


    @ViewInject(R.id.tv_country_num)
    private TextView country_num;
    @ViewInject(R.id.et_write_phone)
    private EditText phone_number;
    @ViewInject(R.id.iv_clear)
    private ImageView clear;
    @ViewInject(R.id.smssdk_register_page_checkBox)
    private CheckBox check;
    private Dialog dialog;


    private EditTextWatcher<SmsActivity> textWatch;

    private boolean isPhoneCount;

    private String phoneNumber;
    private String countryCode;
    private boolean isRead;

    // 国家号码规则
    private HashMap<String, String> countryRules;

    private EventHandler handler;

    private UIHandler<SmsActivity> uiHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initData();
        initView();
    }


    protected void init() {
        textWatch = new EditTextWatcher<>(this);
        dialog = new Dialog(this, R.style.CommonDialog);
        dialog.setContentView(R.layout.smssdk_progress_dialog);
        countryRules = new HashMap<>();
        uiHandler = new UIHandler<>(this);

    }

    protected void initData() {
        countryCode = getCurrentCountry();
    }


    protected void initView() {
        country_num.setText(String.format("+%s", countryCode));
        phone_number.addTextChangedListener(textWatch);
        handler = new EventHandler() {
            public void afterEvent(final int event, final int result,
                                   final Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        // 请求支持国家列表
                        getCountryList((ArrayList<HashMap<String, Object>>) data);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 请求验证码后，跳转到验证码填写页面
                        try {
                            dialog.dismiss();
                            Intent in = new Intent(SmsActivity.this, IdentifyCodeActivity.class);
                            in.putExtra("phoneNumber", phoneNumber);
                            in.putExtra("countryCode", countryCode);
                            startActivity(in);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    // 根据服务器返回的网络错误，给toast提示
                    try {
                        Throwable throwable = (Throwable) data;
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("res", des);
                        message.setData(bundle);
                        message.what = -1;
                        uiHandler.sendMessage(message);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        SMSSDK.registerEventHandler(handler);
    }

    @Event(type = View.OnClickListener.class, value = {R.id.back, R.id.iv_clear, R.id.btn_next, R.id.service})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                myApplication.finishActivity(getClass());
                break;
            case R.id.iv_clear:
                phone_number.setText("");
                break;
            case R.id.service:
                isRead = true;
                Intent in = new Intent(this, ServiceActivity.class);
                startActivity(in);
                break;
            case R.id.btn_next:
                phoneNumber = phone_number.getText().toString().trim();
                check();
                break;
        }

    }


    private void getCountryList(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }
            countryRules.put(code, rule);
        }
        checkPhoneNum();
    }

    private void check() {

        switch (0) {
            case 0:
                if (!myApplication.isNet()) {
                    ToastShow.showShort(this, "无网络访问！！！");
                    return;
                }

            case 1: //验证服务是否已选
                if (!isPhoneCount) {
                    ToastShow.showShort(this, "手机号码不是11位数字");
                    return;
                }
            case 2: //验证手机号是否正确
                if (!check.isChecked()) {
                    ToastShow.showShort(this, "请查看易丢丢服务条例");
                    return;
                }
            case 3:
                postIsRegister();

        }
    }

    /**
     * 检查电话号码
     */
    private void checkPhoneNum() {

        String rule = countryRules.get(countryCode);
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phoneNumber);
        if (!m.matches()) {
            dialog.dismiss();
            ToastShow.showShort(this, "请填写正确的手机号码");
            return;
        }

        SMSSDK.getVerificationCode(countryCode, phoneNumber);
    }

    private String getCurrentCountry() {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if (country == null) {
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country[1];
    }

    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();

        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        String simOperator = tm.getSimOperator();

        String mcc = null;
        if (!TextUtils.isEmpty(networkOperator) && networkOperator.length() >= 5) {
            mcc = networkOperator.substring(0, 3);
        }

        if (TextUtils.isEmpty(mcc)) {
            if (!TextUtils.isEmpty(simOperator) && simOperator.length() >= 5) {
                mcc = simOperator.substring(0, 3);
            }
        }
        return mcc;
    }

    @Override
    public void sendMessage(Message msg) {
        {
            Bundle bundle = msg.getData();
            String res = bundle.getString("res");
            if (res == null) {
                ToastShow.showShort(this, "服务请求失败");
                return;
            }
            if (msg.what == 0) {
                switch (res) {
                    case "Incomplete":
                        dialog.dismiss();
                        ToastShow.showShort(this, "服务器没收到手机号码");
                        break;
                    case "Repeat":
                        dialog.dismiss();
                        ToastShow.showShort(this, "手机号码已经被注册啦");
                        break;
                    case "no":
                        // 请求国家号码规则
                        LogUtil.w(res);
                        SMSSDK.getSupportedCountries();
                        break;

                }
            } else {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s) {
        if (s.length() == 11) {
            isPhoneCount = true;
            check.setChecked(true);
        } else {
            isPhoneCount = false;
            check.setChecked(false);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (isRead)
            check.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
        SMSSDK.unregisterEventHandler(handler);
    }

    private void postIsRegister() {

        try {
            dialog.show();
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("phone_number", phoneNumber)
                    .build();
            Request request = new Request.Builder().addHeader("Connection", "close").url(UrlPath.isRegisterPath).post(requestBody).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    dialog.dismiss();
                    LogUtil.w(e);
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("res", "");
                    message.setData(bundle);
                    uiHandler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    LogUtil.e(res);
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("res", res);
                    message.setData(bundle);
                    uiHandler.sendMessage(message);
                }
            });

        } catch (Exception e) {
            LogUtil.w(getClass().getSimpleName(), e);
        }
    }


}
