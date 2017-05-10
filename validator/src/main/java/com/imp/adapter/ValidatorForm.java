package com.imp.adapter;

import android.content.Context;

import com.imp.listener.ValidatorListener;
import com.imp.show.ErrorToast;
import com.imp.utils.BaseResult;
import com.imp.utils.ErrorResult;
import com.imp.utils.ValidatorType;
import com.imp.validator.Validator;
import com.imp.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/1.
 */

public class ValidatorForm  {

    private List<BaseResult> baseResults;

    private Context context;

    public ValidatorForm(Context context){
        this.context=context;
        baseResults=new ArrayList<>();
    }

    public ValidatorForm addValidator(BaseResult baseResult){

        baseResults.add(baseResult);

        return this;
    }


    public boolean form(){

       for(BaseResult baseResult:baseResults){

           ValidatorType type=baseResult.getType();

         Validator validator= ValidatorFactory.getValidator(type);
         ErrorResult result=validator.validator(baseResult.getTitle(),baseResult.getK(),baseResult.getV());

         if(!result.getType().equals(ValidatorType.SUCCESS)){
             ErrorToast.getInstance(context).showToast(result);
             return false;
         }
       }
        return true;
    }

}
