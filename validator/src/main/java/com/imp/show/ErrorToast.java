package com.imp.show;


import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;
import com.tx.lfclient.validator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/30.
 */

public class ErrorToast {

    private static ErrorToast toast;
    private Context context;
    private ErrorToast(Context context){
        this.context=context;
    }

    public static synchronized ErrorToast getInstance(Context context){

        if(toast==null){
            toast=new ErrorToast(context);
        }
        return toast;
    }


    public void showToast(ErrorResult result){

        String show;
        String title=result.getTitle();

        ValidatorType type=result.getType();

        Object object=result.getObj();

        show=ErrorTypeMap.getErrorType(type.name(),title,object);
        Toast.makeText(this.context,show,Toast.LENGTH_SHORT).show();

    }

}
