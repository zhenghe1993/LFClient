package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class IntegerMaxValidator implements Validator {


    @Override
    public ErrorResult validator(String title, Object o, Object o2) {
        int len= (int) o2;
        Integer sourceData= (Integer) o;


        if(sourceData>len){
            return new ErrorResult<>(title, ValidatorType.INTEGER_MAX_LENGTH,len);
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,len);
    }
}
