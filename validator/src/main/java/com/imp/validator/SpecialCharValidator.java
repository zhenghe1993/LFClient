package com.imp.validator;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/3.
 */

public class SpecialCharValidator implements Validator {


    @Override
    public ErrorResult validator(String title, Object o, Object o2) {

        String sourceData= (String) o;

        if(sourceData.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0){
            return new ErrorResult<>(title, ValidatorType.STRING_CHAR,"");
        }


        return new ErrorResult<>(title, ValidatorType.SUCCESS,"");
    }
}
