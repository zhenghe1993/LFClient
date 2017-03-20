package com.tx.lfclient.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.ReceiveTemplate;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.utils.AlertUser;
import com.tx.lfclient.utils.DesHelper;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.inter.IAlertUser;
import com.tx.lfclient.utils.inter.IUIHandler;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/15
 */
@ContentView(R.layout.activity_account_password)
public class AccountPasswordActivity extends ImpActivity implements IUIHandler, IAlertUser {
    @ViewInject(R.id.account_password_phone)
    private EditText phone;
    @ViewInject(R.id.account_password_old_password)
    private EditText old_password;
    @ViewInject(R.id.account_password_new_password)
    private EditText new_password;

    private User user;

    private UserDbManager userDbManager;

    private UIHandler<AccountPasswordActivity> handler;

    private AlertUser<AccountPasswordActivity> alertUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
    }

    @Override
    void init() {
        userDbManager = UserDbManager.getInstance();
        handler = new UIHandler<>(this);
        alertUser = new AlertUser<>(this);
    }

    @Override
    void initData() throws Exception {
        user = myApplication.getUser();
    }

    @Override
    void initView() {

    }

    @Event(type = View.OnClickListener.class, value = {R.id.account_password_back, R.id.account_password_submit})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_password_back:
                myApplication.finishActivity(AccountPasswordActivity.class);
                break;
            case R.id.account_password_submit:

                String p = phone.getText().toString().trim();
                String op = old_password.getText().toString().trim();
                String np = new_password.getText().toString().trim();

                //验证
                if (TextUtils.isEmpty(p)) {
                    ToastShow.showShort(this, "请填写手机号码");
                    return;
                }
                if (TextUtils.isEmpty(op)) {
                    ToastShow.showShort(this, "请填写旧密码");
                    return;
                }
                if (TextUtils.isEmpty(np)) {
                    ToastShow.showShort(this, "请填写新密码");
                    return;
                }
                //手机号验证
                if (!p.equals(user.getPhoneNumber())) {
                    ToastShow.showShort(this, "请填写注册时使用的手机号码");
                    return;
                }
                //旧密码验证
                if (!op.equals(user.getPassword())) {
                    ToastShow.showShort(this, "旧密码错误");
                    return;
                }
                //新密码格式验证
                int len = np.length();
                if (len < 7 || len > 17) {
                    ToastShow.showShort(this, "密码必须大于6位小于18位！");
                    return;
                }
                alertUser.post("1", "1", np, null);
                break;
        }
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
        String res = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        ReceiveTemplate template = objectMapper.readValue(res, ReceiveTemplate.class);
        String data = (String) template.getData();
        switch (data) {
            case "Incomplete":
                handler.sendEmptyMessage(2);
                break;
            case "Remote":
                handler.sendEmptyMessage(3);
                break;
            default:
                data = DesHelper.getDecode(data);
                data = DesHelper.getDecode(data);
                user.setPassword(data);
                user.setToken(template.getToken());
                myApplication.setUser(user);
                userDbManager.saveOrUpdateUser(user);
                handler.sendEmptyMessage(1);
                break;
        }
    }

    @Override
    public void sendMessage(Message msg) {
        switch (msg.what) {
            case 0:
                ToastShow.showShort(this, "修改密码失败");
                break;
            case 1:
                ToastShow.showShort(this, "修改密码成功");
                myApplication.finishActivity(AccountPasswordActivity.class);
                break;
            case 2:
                ToastShow.showShort(this, "请求失败");
                break;
            case 3:
                ToastShow.showShort(this, "账号异地登录，请重新登录");
                break;
        }
    }
}
