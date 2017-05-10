package com.tx.lfclient.utils;

import android.content.Context;

import com.tx.lfclient.exception.NullDataException;
import com.tx.lfclient.exception.NumberLengthException;

/**
 * Created by IMP(郑和明)
 * date is 2017/4/27.
 *
 *
 * 可扩展
 *
 * 松耦合
 *
 * 支持多方式验证
 *
 * String  Datetime  Number  file  bitmap  stream  特殊字符 等
 *
 *
 * 现支持验证方式
 *
 * 1、NULL
 * 2、长度限制
 * 3、纯数字
 * 4、时间格式
 * 5、特殊字符
 * 6、文件大小验证
 * 7、邮箱格式验证
 * 8、url格式验证
 * 9、数字大小
 * 10、数据格式验证
 *
 *
 */

public class CheckingFormUtils {



    private Context context;//上下文
    //待验证数据
    private Object object;

    private String dataName;

    //数据类型
    private String dateType;

    //待验证方式
    private boolean isNull;
    private boolean isMax;
    private boolean isMin;
    private boolean isNumber;
    private boolean isDateTime;
    private boolean isNormalString;
    private boolean isEmail;
    private boolean isUrl;
    private boolean isNumberMax;
    private boolean isNumberMin;
    private boolean isFileLength;
    private boolean isRegex;

    //预留数据
    private int maxLength;
    private int minLength;

    private String dateformatRegex;

    private int numberMaxLength;
    private int numberMinLength;

    private long fileLength;

    private String regex;




    private CheckingFormUtils(){

    }

    private CheckingFormUtils(Context context){
        this.context=context;
    }

    public CheckingFormUtils build(Context context){
        return new CheckingFormUtils(context);
    }


    //添加验证方式

    //添加null判断
    public CheckingFormUtils addIsNull(){
        this.isNull=true;
        return this;
    }

    public CheckingFormUtils addMaxLength(int maxLength){
        if(maxLength<=0)
            throw new NumberLengthException();
        this.maxLength=maxLength;
        this.isMax=true;
        return this;
    }
    public CheckingFormUtils addMinLength(int minLength){
        if(minLength<0)
            throw new NumberLengthException();
        this.minLength=minLength;
        this.isMin=true;
        return this;
    }


    public CheckingFormUtils addIsNumber(){
        this.isNumber=true;
        return this;
    }

    public CheckingFormUtils addIsDateTime(String dateformatRegex){
        if(dateformatRegex.isEmpty()){
            throw new NullDataException();
        }
        this.dateformatRegex=dateformatRegex;
        this.isDateTime=true;
        return this;
    }

    public CheckingFormUtils addIsNormalString(){
        this.isNormalString=true;
        return this;
    }
    public CheckingFormUtils addIsEmail(){
        this.isEmail=true;
        return this;
    }

    public CheckingFormUtils addIsUrl(){
        this.isUrl=true;
        return this;
    }

    public CheckingFormUtils addIsNumberMax(int numberMaxLength){
        this.numberMaxLength=numberMaxLength;
        this.isNumberMax=true;
        return this;
    }
    public CheckingFormUtils addIsNumberMin(int numberMinLength){
        this.numberMinLength=numberMinLength;
        this.isNumberMin=true;
        return this;
    }

    public CheckingFormUtils addFileLength(long fileLength){
        this.fileLength=fileLength;
        this.isFileLength=true;
        return this;
    }

    public CheckingFormUtils addRegex(String regex){
        if(regex.isEmpty()){
            throw new NullDataException();
        }
        this.regex=regex;
        this.isRegex=true;
        return this;
    }






    //验证主体
    public void build(){












    }





    //验证结果类




    //打印类



    public void reset(){
         isNull=false;
         isMax=false;
         isMin=false;
         isNumber=false;
         isDateTime=false;
         isNormalString=false;
         isEmail=false;
         isUrl=false;
         isNumberMax=false;
         isNumberMin=false;
         isFileLength=false;
         isRegex=false;
    }






}
