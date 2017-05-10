package com.tx.lfclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imp.SPUtils;
import com.imp.ToastShow;
import com.tx.lfclient.R;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.db.MySQLiteHelp;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.url.UrlPath;
import com.tx.lfclient.utils.DesHelper;
import com.tx.lfclient.utils.ResultValidatorUtils;
import com.tx.lfclient.utils.inter.IUIHandler;
import com.tx.lfclient.utils.UIHandler;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by (IMP)郑和明
 * Date is 2017/2/6
 * <p>
 * 来源   点击登录    注册进入
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends ImpActivity implements IUIHandler {
    @ViewInject(R.id.activity_login_user)
    private EditText login_user;// 登陆账号
    @ViewInject(R.id.activity_login_password)
    private EditText login_password;// 登陆密码


    private UIHandler<LoginActivity> handler;

    private User user;

    private boolean expire = false;


    private UserDbManager userDbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        init();
        initData();
        initView();
    }


    protected void init() {
        handler = new UIHandler<>(this);
        userDbManager = UserDbManager.getInstance();
    }

    protected void initData() {
        user = myApplication.getUser();
        expire = myApplication.isExpire();
    }

    protected void initView() {
        login_user.setFocusable(true);
        login_user.setFocusableInTouchMode(true);
        login_user.requestFocus();

        if (user != null && !expire) {
            login_user.setText(user.getPhoneNumber());
            login_password.setText(user.getPassword());
        }
    }


    @Event(type = View.OnClickListener.class,
            value = {R.id.activity_login_back,
                    R.id.activity_login_forgetPassword,
                    R.id.activity_login_login,
                    R.id.activity_login_register})
    private void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_login_back:
                myApplication.finishActivity(getClass());
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
            case R.id.activity_login_forgetPassword:
                Intent intent = new Intent(LoginActivity.this, RegisterLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_login_login:
                String loginUser = login_user.getText().toString().trim();
                String password = login_password.getText().toString().trim();
                boolean res = validate(loginUser, password);
                if (res)
                    postLogin(loginUser, password);
                break;
            case R.id.activity_login_register:
                intent = new Intent(LoginActivity.this, RegisterLocationActivity.class);
                startActivity(intent);
                break;
        }
    }

    //网络连接
    private void postLogin(String loginUser, String password) {



        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("pn", DesHelper.getEncode(loginUser))
                    .add("pw", DesHelper.getEncode(password))
                    .build();
            Request request = new Request.Builder().addHeader("Connection", "close").url(UrlPath.loginPath).post(requestBody).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(0);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

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
            });
        } catch (Exception e) {
            LogUtil.e(getClass().getSimpleName(), e);
        }
    }

    //输入验证
    private boolean validate(String loginUser, String password) {

        switch (1) {
            case 1:
                if (loginUser == null || loginUser.equals("") || loginUser.length() != 11) {
                    ToastShow.showShort(this, "请正确填写手机号码");
                    return false;
                }
            case 2:
                if (password == null || password.equals("") || password.length() <= 6 || password.length() >= 18) {
                    ToastShow.showShort(this, "密码必须大于6位并且小于18位");
                    return false;
                }
        }
        return true;
    }

    @Override
    public void sendMessage(Message msg) {


        switch (msg.what) {

            case 0:
                ToastShow.showShort(this, "登录失败");
                break;
            case 1:
                Bundle bundle = msg.getData();
                String res = bundle.getString("res");
                int code = bundle.getInt("code");

                res = ResultValidatorUtils.getResult(LoginActivity.this, code, res);

                if (res.equals("ERROR")) {
                    return;
                }



                User user = null;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    user = mapper.readValue(res, User.class);
                } catch (Exception e) {
                    ToastShow.showShort(this, "数据格式错误");
                    LogUtil.w(e);
                }
                if (user == null) {
                    ToastShow.showShort(this, "登陆失败");
                    break;
                }
                String password = user.getPassword();
                password = DesHelper.getDecode(password);
                password = DesHelper.getDecode(password);
                user.setPassword(password);
                LogUtil.i(user.toString());
                userDbManager.saveOrUpdateUser(user);
                myApplication.setUser(user);
                SPUtils.put(this, "LOGIN", true);
                SPUtils.put(this, "LOGIN_TIME", new Date().getTime());
                SPUtils.put(this, "LOGIN_USER_ID", user.getId());
                myApplication.setLogin(true);
                ToastShow.showShort(this, "登陆成功");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                myApplication.finishActivity(getClass());
                myApplication.finishActivity(MainActivity.class);

                break;
            case 10:
                ToastShow.showShort(this, "无网络访问！！！");
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy(this);
    }
}
