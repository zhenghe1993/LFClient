package com.imp.validator;

import com.imp.utils.ErrorResult;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/30.
 */

public interface Validator<K,T> {


    ErrorResult validator(String title,K k,T t);


}
