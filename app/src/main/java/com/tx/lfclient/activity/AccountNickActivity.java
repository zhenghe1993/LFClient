package com.tx.lfclient.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.db.MySQLiteHelp;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.ReceiveTemplate;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.utils.AlertUser;
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
@ContentView(R.layout.activity_account_nick)
public class AccountNickActivity extends ImpActivity implements IAlertUser, IUIHandler {
    @ViewInject(R.id.account_nick_edit)
    private EditText nick_edit;

    private AlertUser<AccountNickActivity> alertUser;

    private UIHandler<AccountNickActivity> handler;

    private User user;
    private UserDbManager userDbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);

        try {
            init();
            initData();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    void init() {
        alertUser = new AlertUser<>(this);
        handler = new UIHandler<>(this);
        userDbManager = UserDbManager.getInstance();
    }

    @Override
    void initData() throws Exception {
        user = myApplication.getUser();
    }

    @Override
    void initView() {

    }

    @Event(type = View.OnClickListener.class, value = {R.id.account_nick_back, R.id.account_nick_submit})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_nick_back:
                myApplication.finishActivity(AccountNickActivity.class);
                break;
            case R.id.account_nick_submit:

                String nick = nick_edit.getText().toString().trim();

                if (TextUtils.isEmpty(nick)) {
                    ToastShow.showShort(this, "请输入昵称");
                    return;
                } else {
                    if (nick.length() > 10) {
                        ToastShow.showShort(this, "请设置昵称长度小于10！");
                        return;
                    }
                }
                alertUser.post(nick, "1", "1", null);

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
                user.setName(data);
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
                ToastShow.showShort(this, "修改昵称失败");
                break;
            case 1:
                ToastShow.showShort(this, "修改昵称成功");
                myApplication.finishActivity(AccountNickActivity.class);
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
