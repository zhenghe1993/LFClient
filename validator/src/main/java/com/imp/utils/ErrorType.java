package com.imp.utils;

import java.util.Locale;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/1.
 */

public class ErrorType {


    public static String getErrorNull(String title){
        return String.format(Locale.CHINA,"%s不能为空",title);
    }

    public static String getErrorStringMax(String title,int len){
        return String.format(Locale.CHINA,"%s长度不能超过%d，请重新输入",title,len);
    }

    public static String getErrorStringMin(String title,int len){
        return String.format(Locale.CHINA,"%s长度不能小于%d，请重新输入",title,len);
    }
    public static String getErrorEmail(String title){
        return String.format(Locale.CHINA,"%s不符合邮箱地址规则，请重新输入",title);
    }
    public static String getErrorUrl(String title){
        return String.format(Locale.CHINA,"%s不符合URL地址规则，请重新输入",title);
    }
    public static String getErrorIntegerMax(String title,int len){
        return String.format(Locale.CHINA,"%s应不大于%d,请重新输入",title,len);
    }
    public static String getErrorIntegerMin(String title,int len){
        return String.format(Locale.CHINA,"%s应不小于%d,请重新输入",title,len);
    }

    public static String getErrorLongMax(String title,long len){
        return String.format(Locale.CHINA,"%s应不大于%d,请重新输入",title,len);
    }
    public static String getErrorLongMin(String title,long len){
        return String.format(Locale.CHINA,"%s应不小于%d,请重新输入",title,len);
    }

    public static String getErrorDate(String title,String regex){
        return String.format(Locale.CHINA,"%s格式应为%s,请重新输入",title,regex);
    }
    public static String getErrorType(String title){
        return String.format(Locale.CHINA,"%s数据格式不正确,请重新输入",title);
    }

    public static String getErrorTelephone(String title){
        return String.format(Locale.CHINA,"%s格式不正确,请重新输入",title);
    }

    public static String getErrorStringChar(String title){
        return String.format(Locale.CHINA,"%s不应该包含特殊字符,请重新输入",title);
    }

}
