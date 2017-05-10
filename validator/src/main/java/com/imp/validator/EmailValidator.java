package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class EmailValidator implements Validator {

    @Override
    public ErrorResult validator(String title, Object o, Object o2) {

        String sourceData= (String) o;

        Pattern pattern=Pattern.compile("^\\\\s*\\\\w+(?:\\\\.{0,1}[\\\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\\\.[a-zA-Z]+\\\\s*$");

        Matcher matcher=pattern.matcher(sourceData);
        boolean res=matcher.matches();
        if(!res){
            return new ErrorResult<>(title, ValidatorType.EMAIL,"");
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,"");
    }
}
