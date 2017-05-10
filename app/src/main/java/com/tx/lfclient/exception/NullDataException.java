package com.tx.lfclient.exception;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/28.
 */

public class NullDataException extends RuntimeException {
    public NullDataException() {
        this("the data is null,please input right data ");
    }

    private NullDataException(String message) {
        super(message);
    }
}
