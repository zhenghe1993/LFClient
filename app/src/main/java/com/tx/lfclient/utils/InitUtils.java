package com.tx.lfclient.utils;

import android.content.Context;

import com.imp.SPUtils;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.db.UserDbManager;
import com.tx.lfclient.entities.User;

import java.util.Date;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/17.
 */

public class InitUtils {


    private static boolean getExpire(Context context, boolean isLogin) {
        Date current = new Date();
        long expireTime = current.getTime();
        if (isLogin) {
            expireTime = (long) SPUtils.get(context, "LOGIN_TIME", current.getTime());
        }
        return current.getTime() - expireTime > 1000 * 60 * 60 * 24;
    }


    public static boolean isFirst(Context context) {
        boolean isFirst = (boolean) SPUtils.get(context, "IS_FIRST", true);
        MyApplication myApplication = MyApplication.getInstance();
        myApplication.setNet(NetWorkJudge.getJudge(context));
        //判断是否是第一次登录
        if (!isFirst) {
            boolean isLogin = (Boolean) SPUtils.get(context, "LOGIN", false);
            myApplication.setLogin(isLogin);
            myApplication.setExpire(getExpire(context,isLogin));
            //将user信息导入
            Long id = (Long) SPUtils.get(context, "LOGIN_USER_ID", -1L);
            if(!isLogin){
                return false;
            }
            User user =  UserDbManager.getInstance().getUserById(id);

            if (user != null) {
                myApplication.setUser(user);
            }

        }
        return isFirst;
    }





}
