package com.tx.lfclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tx.lfclient.R;
import com.tx.lfclient.entities.Pane;

import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/16
 */
public class PostTypeAdapter extends BaseAdapter {


    private List<Pane> list;
    private Context context;

    private int currentIndex;

    public PostTypeAdapter(List<Pane> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void notifyDataChange(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.post_type_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.plugin = (ImageView) convertView.findViewById(R.id.item_choose);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Pane pane = list.get(position);
        viewHolder.title.setText(pane.getTitle());
        viewHolder.image.setImageResource(pane.getImageId());

        if (position == currentIndex) {
            viewHolder.plugin.setVisibility(View.VISIBLE);
        } else {
            viewHolder.plugin.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        ImageView image;
        ImageView plugin;
    }


}
