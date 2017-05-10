package com.tx.lfclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IMP(郑和明)
 * date is 2017/3/24.
 */

public class DateFormatUtils {

    private SimpleDateFormat dateFormat;

    public DateFormatUtils(String template) {
        dateFormat = new SimpleDateFormat(template, Locale.CHINA);
    }

    public String dateToString(Date date) {

        return dateFormat.format(date);
    }

    public Date stringToDate(String time) {
        try {
            return dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
