package com.tx.lfclient.utils;

import com.imp.ToastShow;
import com.tx.lfclient.activity.ImpActivity;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.entities.User;
import com.tx.lfclient.enumeration.DataType;
import com.tx.lfclient.url.UrlPath;
import com.tx.lfclient.utils.inter.IAlertUser;

import org.xutils.common.util.LogUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/15
 */
public class AlertUser<T extends ImpActivity & IAlertUser> {

    private MyApplication myApplication;
    private T activity;
    private User user;


    public AlertUser(T activity) {
        myApplication = MyApplication.getInstance();
        this.activity = activity;
        user = myApplication.getUser();
    }

    public void post(String name, String location, String password, byte[] portraitFile) {
        {

            if (!myApplication.isNet()) {
                ToastShow.showShort(activity, "无网络访问！！！");
                return;
            }
            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("id", user.getId() + "")
                    .addFormDataPart("name", name)
                    .addFormDataPart("location", location)
                    .addFormDataPart("password", password)
                    .addFormDataPart("token", user.getToken())
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"portrait\";filename=\"" + PictureNaming.getNaming(DataType.PORTRAIT,1) + ".jpg\""),
                            RequestBody.create(MediaType.parse("image/png"), portraitFile));
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .addHeader("Connection", "close")
                    .url(UrlPath.alertUserPath)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    activity.onFailure(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        activity.onResponse(call, response);
                    } catch (Exception e) {
                        LogUtil.w("okHttp",e);
                    }
                }
            });
        }
    }


}
