package com.tx.lfclient.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by IMP(郑和明)
 * date is 2017/3/13.
 */

public class DisplayPictureAdapter extends PagerAdapter {


    private List<View> list;

    public DisplayPictureAdapter(List<View> list) {
        this.list = list;
    }

    public void updataList(List<View> list){
        this.list = list;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=list.get(position);
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }


}
