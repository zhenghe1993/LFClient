package com.tx.lfclient.utils;

import android.content.Context;
import android.content.Intent;

import com.imp.ToastShow;

/**
 * Created by IMP(郑和明)
 * date is 2017/3/26.
 */

public class ResultValidatorUtils {

    private static final int SUCCESS = 200;

    private static final int FAILURE = 430;


    //argument lost
    private static final int PARAMETER_LOST = 420;


    //login
    private static final int PHONE_ERROR = 418;
    private static final int PASSWORD_ERROR = 419;

    //register
    private static final int PHONE_REPEAT = 422;

    private static final int NAME_REPEAT = 423;


    private static final int TOKEN_ERROR = 421;


    //words

    private static final int WORDS_ERROR = 425;

    //attention
    private static final int ATTENTION_ERROR = 426;

    //lfmessage
    private static final int USER_ERROR = 427;

    private static final int QUERY_ERROR = 428;

    //service error

    private static final int SQL_ERROR = 510;


    public static String getResult(Context context, Integer code, String res) {

        switch (code) {
            case SUCCESS:
                return res;
         default:
             ToastShow.showShort(context,res);
             return "ERROR";
        }
    }

}
