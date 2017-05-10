package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class LongMinValidator implements Validator {


    @Override
    public ErrorResult validator(String title, Object o, Object o2) {
        long len= (long) o2;
        long sourceData= (long) o;


        if(sourceData<len){
            return new ErrorResult<>(title, ValidatorType.LONG_MIN_LENGTH,len);
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,len);
    }
}
