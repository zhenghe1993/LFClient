package com.tx.lfclient.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.utils.AlertUser;
import com.tx.lfclient.utils.DesHelper;
import com.tx.lfclient.utils.ResultValidatorUtils;
import com.tx.lfclient.utils.UIHandler;
import com.tx.lfclient.utils.inter.IAlertUser;
import com.tx.lfclient.utils.inter.IUIHandler;

import org.xutils.common.util.LogUtil;
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

                res = ResultValidatorUtils.getResult(AccountPasswordActivity.this, code, res);


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

                ToastShow.showShort(this, "修改密码成功");
                myApplication.setUser(user);
                userDbManager.saveOrUpdateUser(user);
                myApplication.finishActivity(AccountPasswordActivity.class);
                break;
        }
    }
}
