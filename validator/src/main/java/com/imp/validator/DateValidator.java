package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class DateValidator implements Validator {

    @Override
    public ErrorResult validator(String title, Object o, Object o2) {

        String dateFormat= (String) o2;

        String sourceData= (String) o;

        SimpleDateFormat format=new SimpleDateFormat(dateFormat, Locale.CHINA);
        try {
            format.parse(sourceData);
        } catch (ParseException e) {
            return new ErrorResult<>(title, ValidatorType.DATE,dateFormat);
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,dateFormat);
    }
}
