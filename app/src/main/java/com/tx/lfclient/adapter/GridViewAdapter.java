package com.tx.lfclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tx.lfclient.R;
import com.tx.lfclient.entities.Pane;

import java.util.List;


public class GridViewAdapter extends BaseAdapter {


    private Context context;
    private List<Pane> categories;

    public GridViewAdapter(Context context, List<Pane> categories) {
        this.context = context;
        this.categories=categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_kind_square, null);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.square_relative);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.square_image);
            viewHolder.text = (TextView) convertView.findViewById(R.id.square_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Pane pane = categories.get(position);
        viewHolder.relativeLayout.setBackgroundResource(R.drawable.fragment_mine_grey_blue);


        viewHolder.image.setImageResource(pane.getImageId());
        viewHolder.text.setText(pane.getTitle());
        return convertView;
    }


    private static class ViewHolder {
        RelativeLayout relativeLayout;
        ImageView image;
        TextView text;
    }

}
