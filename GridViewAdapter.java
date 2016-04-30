package com.example.a.finalproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter{

    private Context mContext;

    public GridViewAdapter(Context c) {mContext = c;}

    public int getCount() {return mThumbIds.length;}

    public Object getItem(int position) {return null;}

    public long getItemId(int position) {return 0;}

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }

    private String[] mThumbIds = {
            "Hello", "there",
            "Second", "Line",
            "Still", "fine"
    };
}
