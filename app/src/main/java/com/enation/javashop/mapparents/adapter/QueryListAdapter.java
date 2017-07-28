package com.enation.javashop.mapparents.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.cloud.CloudItem;
import com.bumptech.glide.Glide;
import com.enation.javashop.mapparents.R;

import java.util.ArrayList;

/**
 * Created by LDD on 17/2/28.
 */

public class QueryListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CloudItem> data;
    private LayoutInflater inflater;

    public QueryListAdapter(Context context, ArrayList<CloudItem> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CloudItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.query_listitem,null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.name);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.dicance = (TextView) convertView.findViewById(R.id.dicdance);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
            CloudItem cloudItem = getItem(position);
            if (cloudItem.getCloudImage().size()>0){
                Glide.with(context).load(cloudItem.getCloudImage().get(0).getUrl()).asBitmap().centerCrop().dontAnimate().into(viewHolder.icon);
            }
            viewHolder.title.setText(cloudItem.getTitle());
            viewHolder.dicance.setText(cloudItem.getDistance()+"m");
            viewHolder.address.setText(cloudItem.getSnippet());

        return convertView;
    }
    static class ViewHolder{
        TextView title,address,dicance;
        ImageView icon;
    }
}
