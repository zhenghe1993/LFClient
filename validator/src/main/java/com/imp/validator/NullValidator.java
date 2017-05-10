package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class NullValidator implements Validator {

    @Override
    public ErrorResult validator(String title, Object o, Object o2) {

        String sourceData= (String) o;

        if(sourceData.isEmpty()){
            return new ErrorResult<>(title, ValidatorType.NULL,"");
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,"");
    }
}
