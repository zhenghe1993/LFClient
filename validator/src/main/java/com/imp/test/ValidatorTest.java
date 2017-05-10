package com.imp.test;

import android.content.Context;

import com.imp.adapter.ValidatorForm;
import com.imp.utils.BaseResult;
import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/1.
 */

public class ValidatorTest {

    public static void main(String[] args) {
        Context context = null;
        ValidatorForm form=new ValidatorForm(context);

        form.addValidator(new BaseResult("Title", ValidatorType.STRING_MAX_LENGTH,"imp",2));


        boolean res=form.form();

        System.out.println(res);
    }
}
