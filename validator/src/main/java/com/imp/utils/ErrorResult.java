package com.imp.utils;

import android.content.res.Resources;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/30.
 */

public class ErrorResult<T> {

    private String title;

    private ValidatorType type;

    private T obj;

    public ErrorResult(String title,ValidatorType type, T obj) {
        this.title=title;
        this.type =type;
        this.obj = obj;
    }


    public String getTitle() {
        return title;
    }

    public ValidatorType getType() {
        return type;
    }

    public T getObj() {
        return obj;
    }


}
