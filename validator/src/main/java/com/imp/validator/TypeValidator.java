package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class TypeValidator implements Validator {

    @Override
    public ErrorResult validator(String title, Object o, Object o2) {

        String s=  o2.getClass().getSimpleName();
        String checkedS=o.getClass().getSimpleName();
        if(!s.equals(checkedS)){
            return new ErrorResult<>(title, ValidatorType.TYPE,s);
        }
        return new ErrorResult<>(title,ValidatorType.SUCCESS,s);
    }
}
