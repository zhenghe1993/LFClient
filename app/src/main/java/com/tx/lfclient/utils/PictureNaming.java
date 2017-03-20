package com.tx.lfclient.utils;

import com.tx.lfclient.enumeration.DataType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/14
 */
public class PictureNaming {

    public static String getNaming(DataType dataType, int order) {
        return dataType.name() + order + getDate();
    }


    private static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
        String date = sdf.format(new Date());
        return date + getTime();
    }

    private static int getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("ssSSS", Locale.CHINA);
        return Integer.valueOf(sdf.format(new Date()));
    }
}
