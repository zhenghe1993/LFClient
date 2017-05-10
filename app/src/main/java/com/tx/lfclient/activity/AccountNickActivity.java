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

                res = ResultValidatorUtils.getResult(AccountNickActivity.this, code, res);


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

                ToastShow.showShort(this, "修改昵称成功");
                myApplication.setUser(user);
                userDbManager.saveOrUpdateUser(user);
                myApplication.finishActivity(AccountNickActivity.class);
                break;
        }
    }

}
