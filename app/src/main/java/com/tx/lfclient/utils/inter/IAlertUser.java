package com.tx.lfclient.utils.inter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/15
 */
public interface IAlertUser {

   void onFailure(Call call, IOException e);
    void onResponse(Call call, Response response)throws Exception ;
}
