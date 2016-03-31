package com.melvinphilips.moviefreak;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.*;

/**
 * Created by melvin on 3/30/16.
 */
public class GridViewAdapter extends ArrayAdapter<GridItem>{

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();
    public GridViewAdapter(Context context, int resource, ArrayList<GridItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
    }

    public void setGridData(ArrayList<GridItem> mGridData){
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ImageView imgView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            imgView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(imgView);
        } else {
            imgView = (ImageView) row.getTag();
        }

        GridItem item = mGridData.get(position);

        Picasso.with(mContext).load(item.getImage()).into(imgView);
        return row;
    }
}
