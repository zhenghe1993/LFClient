package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class UrlValidator implements Validator {

    @Override
    public ErrorResult validator(String title, Object o, Object o2) {

        String sourceData= (String) o;

        Pattern pattern=Pattern.compile("[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&]]* ");

        Matcher matcher=pattern.matcher(sourceData);
        boolean res=matcher.matches();
        if(!res){
            return new ErrorResult<>(title, ValidatorType.URL,"");
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,"");
    }
}
