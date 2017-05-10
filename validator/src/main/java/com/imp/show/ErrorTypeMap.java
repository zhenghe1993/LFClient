package com.imp.show;

import com.imp.utils.ErrorType;


/**
 * Created by IMP(郑和明)
 * date is 2017/5/1.
 */

public class ErrorTypeMap {

    public static String getErrorType(String type,String title,Object obj){
        String res = null;
        switch (type){

            case "NULL":
                res= ErrorType.getErrorNull(title);
                break;
            case "DATE":
                res=ErrorType.getErrorDate(title, (String) obj);
                break;
            case "TYPE":
                res=ErrorType.getErrorType(title);
                break;
            case "EMAIL":
                res=ErrorType.getErrorEmail(title);
                break;
            case "URL":
                res=ErrorType.getErrorUrl(title);
                break;
            case "TELEPHONE":
                res=ErrorType.getErrorTelephone(title);
                break;
            case "STRING_MAX_LENGTH":
                res=ErrorType.getErrorStringMax(title, (Integer) obj);
                break;
            case "STRING_MIN_LENGTH":
                res=ErrorType.getErrorStringMin(title, (Integer) obj);
                break;
             case "INTEGER_MAX_LENGTH":
                res=ErrorType.getErrorIntegerMax(title, (Integer) obj);
                break;
             case "INTEGER_MIN_LENGTH":
                res=ErrorType.getErrorIntegerMin(title, (Integer) obj);
                break;
             case "LONG_MAX_LENGTH":
                res=ErrorType.getErrorLongMax(title, (Long) obj);
                break;
             case "LONG_MIN_LENGTH":
                res=ErrorType.getErrorLongMin(title, (Long) obj);
                break;
             case "STRING_CHAR":
                res=ErrorType.getErrorStringChar(title);
                break;
        }
        return res;
    }

}
