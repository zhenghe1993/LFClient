package com.tx.lfclient.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tx.lfclient.R;
import com.tx.lfclient.custom.NineBitMap;

import org.xutils.common.util.LogUtil;

import java.util.Map;

/**
 * Created by (IMP)郑和明
 * Date is 2017/3/2
 */
public class NineGridAdapter extends BaseAdapter {


    private NineBitMap nineBitMap;
    private Context context;

    public NineGridAdapter(NineBitMap nineBitMap, Context context) {
        this.nineBitMap = nineBitMap;
        this.context = context;

    }

    public void reFleshData(NineBitMap nineBitMap) {
        this.nineBitMap = nineBitMap;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return nineBitMap.size();
    }

    @Override
    public Object getItem(int position) {
        return nineBitMap.queryBitmap(position + 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.nine_layout_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_nine_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Bitmap bitmap = nineBitMap.queryBitmap(position + 1);
        LogUtil.i(position + "---" + bitmap);
        viewHolder.imageView.setImageBitmap(bitmap);
        if (position==3) {
            viewHolder.imageView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }


}
