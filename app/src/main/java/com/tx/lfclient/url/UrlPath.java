package com.tx.lfclient.url;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/7
 */
public class UrlPath {
//    public static final String root = "http://zhengheming.imwork.net:13789/lost-and-found/";
    public static final String root = "http://192.168.0.108:8080/lost-and-found/";
//    private static final String root = "http://192.168.9.84:8080/lost-and-found/";

    public static final String loginPath = root + "user/login.action";//登录
    public static final String isRegisterPath = root + "user/isRegister.action";//检查手机号码是否被注册过
    public static final String registerPath = root + "/user/register.action";//检查手机号码是否被注册过
    public static final String alertUserPath = root + "/user/alertUser.action";//修改人物信息
    public static final String addLFMessagePath = root + "/LFMessage/addLFMessage.action";//添加失物招领信息







}
