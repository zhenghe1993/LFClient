package com.imp.utils;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/30.
 *
 * 封装 类型
 *
 * 验证参数
 *
 */

public class BaseResult<K,V> {

    private String title;
    private ValidatorType type;
    private K k;
    private V v;


    public BaseResult(String title, ValidatorType type, K k, V v) {
        this.title = title;
        this.type = type;
        this.k = k;
        this.v = v;
    }

    public String getTitle() {
        return title;
    }

    public ValidatorType getType() {
        return type;
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }
}
