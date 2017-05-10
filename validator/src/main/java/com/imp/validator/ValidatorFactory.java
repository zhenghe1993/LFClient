package com.imp.validator;

import com.imp.utils.ValidatorType;

/**
 * Created by IMP(郑和明)
 * date is 2017/5/1.
 */

public class ValidatorFactory {
    
    
    public static Validator getValidator(ValidatorType type){
        
        Validator validator = null;
        
        switch (type){
            case STRING_MAX_LENGTH:
                validator=new StringMaxValidator();




        }
        
         return validator;
    }
    
    
}
