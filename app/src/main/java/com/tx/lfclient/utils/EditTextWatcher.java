package com.tx.lfclient.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.tx.lfclient.utils.inter.IEditTextWatcher;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/13
 */
public class EditTextWatcher<T extends IEditTextWatcher> implements TextWatcher {


    private T t;

    public EditTextWatcher(T t) {
        this.t = t;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
           t.onTextChanged(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



}
