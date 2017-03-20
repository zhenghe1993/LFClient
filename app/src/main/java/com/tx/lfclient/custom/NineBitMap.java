package com.tx.lfclient.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.tx.lfclient.R;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;

/**
 * Created by (IMP)郑和明
 * Date is 2017/3/4
 */
public class NineBitMap {

    private static SparseArray<Bitmap> bitmaps;
    private static NineBitMap nineBitMap;
    private static Bitmap indexBitmap;
    private static int count = 0;


    private NineBitMap() {
    }

    private NineBitMap(Context context) {
        indexBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.nine_add_unfocused);
        LogUtil.i("初始化添加图片  "+indexBitmap);
        bitmaps = new SparseArray<>();
        bitmaps.put(1, indexBitmap);
        count++;
    }

    public static NineBitMap getInstance(Context context) {
        if (nineBitMap == null) {
            nineBitMap = new NineBitMap(context);
        }
        return nineBitMap;
    }

    //添加
    public boolean addBitmap(Bitmap bitmap) {

        if (bitmap == null) {
            return false;
        }

        if (count < 4) {
            LogUtil.i("正在添加  "+bitmap);
            bitmaps.put(count++, bitmap);
            bitmaps.put(count, indexBitmap);
            return true;
        } else {
            return false;
        }
    }

    //修改
    public boolean updateBitmap(Integer id, Bitmap bitmap) {

        if (id < 1 || id > count-1 || bitmap == null) {
            return false;
        }

        bitmaps.put(id, bitmap);

        return true;
    }

    //删除

    public boolean deleteBitmap(Integer id) {
        SparseArray<Bitmap> temp = new SparseArray<>();
        int index = 1;
        if (id > count-1 || id < 1) {
            return false;
        }
        for (int i = 1; i <count; i++) {
            if (i != id) {
                temp.put(index++, bitmaps.get(i));
            }

        }
        temp.put(index, indexBitmap);
        bitmaps = temp;
        count--;
        return true;
    }

    //查转为字节数组

    public byte[] getByteImage(Integer id) {
        if (id < 1 || id > count-1) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmaps.get(id).compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    //
    public Bitmap queryBitmap(Integer id) {
        if (id < 1 || id > count) {
            return null;
        }

        return bitmaps.get(id);

    }

    public int size() {
        return count;
    }

}
