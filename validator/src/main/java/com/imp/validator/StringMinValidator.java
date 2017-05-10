package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/2.
 */

public class StringMinValidator implements Validator {

    @Override
    public ErrorResult validator(String title, Object o, Object o2) {
        int len= (int) o2;
        String sourceData= (String) o;

        int currentLen=sourceData.length();

        if(currentLen<len){
            return new ErrorResult<>(title, ValidatorType.STRING_MIN_LENGTH,len);
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,len);
    }
}
