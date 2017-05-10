package com.tx.lfclient.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.tx.lfclient.application.MyApplication;
import com.tx.lfclient.enumeration.DataType;

import org.xutils.common.util.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by (IMP)郑和明
 * Date is 2017/3/5
 *
 *
 * 图片下载规则
 *
 *
 * 图片名称拆分规则
 *
 *
 * 图片查询规则
 *
 *
 * 图片删除规则
 *
 *
 *
 */
public class DownLoadImageToLocal {

    private static File cacheFile;
    private static File tempFile;
    private static String root;

    static{
        if(hasSDCard()){
           root= Environment.getExternalStorageDirectory().getPath();
        }else{
            root= Environment.getRootDirectory().getPath();
        }
        cacheFile=new File(root+"/lf/cache/");
        tempFile=new File(root+"/lf/temp/");
        boolean flag;
        LogUtil.e(cacheFile.getPath());
        if(!cacheFile.exists()){
           flag= cacheFile.mkdirs();
            LogUtil.e("创建缓存文件夹 "+flag);
        }
        if(!tempFile.exists()){
            flag= tempFile.mkdirs();
            LogUtil.e("创建临时文件夹 "+flag);
        }
    }


    //图片下载
    public static boolean saveCacheFile(DataType dataType, int order, Bitmap bitmap){

        File file=new File(cacheFile+"/"+PictureNaming.getNaming(dataType,order));//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
            return true;
    }



    public static File getTempFile(){



        return new File(tempFile, String.valueOf(System.currentTimeMillis()) + ".jpg");

    }
    private static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
}
