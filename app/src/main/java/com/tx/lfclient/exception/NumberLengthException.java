package com.tx.lfclient.exception;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/28.
 */

public class NumberLengthException extends RuntimeException {

    public NumberLengthException() {
        this("the number is not fit standard，please input the number macrocosm zero");
    }

    private NumberLengthException(String message) {
        super(message);
    }
}
