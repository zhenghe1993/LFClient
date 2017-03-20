package com.tx.lfclient.utils;

import android.app.Activity;
import android.content.res.Resources;

import com.imp.L;
import com.tx.lfclient.R;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/6
 */

public class LocationSelector {

    private Resources resources;
    private String[] result;


    public LocationSelector(Activity activity) {
        resources = activity.getResources();
    }


    public String[] getStringArray(String province, int kind) {


        switch (province) {
            case "安徽":
                result = resources.getStringArray(R.array.anhui+kind);
                break;
            case "福建":
                result = resources.getStringArray(R.array.fujian+kind);
                break;
            case "甘肃":
                result = resources.getStringArray(R.array.gansu+kind);
                break;
            case "广东":
                result = resources.getStringArray(R.array.guangdong+kind);
                break;
            case "广西":
                result = resources.getStringArray(R.array.guangxi+kind);
                break;
            case "贵州":
                result = resources.getStringArray(R.array.guizhou+kind);
                break;
            case "海南":
                result = resources.getStringArray(R.array.hainan+kind);
                break;
            case "河南":
                result = resources.getStringArray(R.array.henan+kind);
                break;
            case "河北":
                result = resources.getStringArray(R.array.hebei+kind);
                break;
            case "黑龙江":
                result = resources.getStringArray(R.array.heilongjiang+kind);
                break;
            case "湖北":
                result = resources.getStringArray(R.array.hubei+kind);
                break;
            case "湖南":
                result = resources.getStringArray(R.array.hunan+kind);
                break;
            case "吉林":
                result = resources.getStringArray(R.array.jilin+kind);
                break;
            case "江苏":
                result = resources.getStringArray(R.array.jiangsu+kind);
                break;
            case "江西":
                result = resources.getStringArray(R.array.jiangxi+kind);
                break;
            case "辽宁":
                result = resources.getStringArray(R.array.liaoning+kind);
                break;
            case "内蒙古":
                result = resources.getStringArray(R.array.neimenggu+kind);
                break;
            case "宁夏":
                result = resources.getStringArray(R.array.ningxia+kind);
                break;
            case "青海":
                result = resources.getStringArray(R.array.qinghai+kind);
                break;
            case "山东":
                result = resources.getStringArray(R.array.shandong+kind);
                break;
            case "山西":
                result = resources.getStringArray(R.array.yishanxi+kind);
                break;
            case "四川":
                result = resources.getStringArray(R.array.sichuan+kind);
                break;
            case "西藏":
                result = resources.getStringArray(R.array.xizang+kind);
                break;
            case "新疆":
                result = resources.getStringArray(R.array.xinjiang+kind);
                break;
            case "云南":
                result = resources.getStringArray(R.array.yunnan+kind);
                break;
            case "浙江":
                result = resources.getStringArray(R.array.zhejiang+kind);
                break;
            case "陕西":
                result = resources.getStringArray(R.array.sanshanxi+kind);
                break;
            case "北京":
                result = resources.getStringArray(R.array.beijing+kind);
                break;
            case "天津":
                result = resources.getStringArray(R.array.tianjin+kind);
                break;
            case "上海":
                result = resources.getStringArray(R.array.shanghai+kind);
                break;
            case "重庆":
                result = resources.getStringArray(R.array.chongqing+kind);
                break;
            default:
                break;
        }
        return result;
    }


}
