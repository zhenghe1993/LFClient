package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class TelephoneValidator implements Validator {

    @Override
    public ErrorResult validator(String title, Object o, Object o2) {


        String sourceData= (String) o;


        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

        boolean res=  pattern.matcher(sourceData).matches();

        if(!res){
            return new ErrorResult<>(title, ValidatorType.TELEPHONE,"");
        }

        return new ErrorResult<>(title,ValidatorType.SUCCESS,"");
    }
}
