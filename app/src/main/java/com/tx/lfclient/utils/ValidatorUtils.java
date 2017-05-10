package com.tx.lfclient.utils;

import android.content.Context;
import android.text.TextUtils;

import com.imp.ToastShow;
import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.enumeration.ValidatorType;

import org.xutils.common.util.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IMP(郑和明)
 * date is 2017/3/24.
 * <p>
 * <p>
 * 字符串验证
 * <p>
 * 不为空
 * <p>
 * 长度约束
 * <p>
 * 异常字符
 * <p>
 * 异常格式
 */

public class ValidatorUtils {

     private static MyApplication myApplication=MyApplication.getInstance();


    private static ValidatorType isLength(String string, int min, int max) {
        if (TextUtils.isEmpty(string)) {
            return ValidatorType.EMPTY;
        }
        int len = string.length();

        boolean res = len >= min && len <= max;
        if (res) {
            return ValidatorType.TRUE;
        }
        return ValidatorType.LENGTH;
    }

    private static ValidatorType isFormat(String string, String format) {
        if (TextUtils.isEmpty(string)) {
            return ValidatorType.EMPTY;
        }
        if (TextUtils.isEmpty(format)) {
            return ValidatorType.TRUE;
        }
        Pattern pattern = Pattern.compile(format);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string);
        // 字符串是否与正则表达式相匹配
        boolean res = matcher.matches();
        if (res) {
            return ValidatorType.TRUE;
        }
        return ValidatorType.FORMAT;
    }

    public static ValidatorType isValidator(String string, String format, int min, int max) {

        ValidatorType validatorType = isLength(string, min, max);

        if (validatorType == ValidatorType.TRUE) {

            validatorType = isFormat(string, format);
            return validatorType;

        }
        return validatorType;
    }

    public static void showToast(Context context, ValidatorType type, String name) {
        if (type == ValidatorType.EMPTY) {
            ToastShow.showShort(context, name + "不能为空，请重新输入！");
        } else if (type == ValidatorType.LENGTH) {
            ToastShow.showShort(context, name + "输入长度不符合标准，请重新输入！");
        } else if (type == ValidatorType.FORMAT) {
            ToastShow.showShort(context, name + "输入格式不符合标准，请重新输入！");
        }
    }


    public static void showNetConnection(Context context){
        if (!myApplication.isNet()) {
            ToastShow.showShort(context, "网络错误，请查看网络是否连接");
        }
    }

}
