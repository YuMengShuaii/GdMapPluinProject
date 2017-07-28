package com.enation.javashop.mapparents.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enation.javashop.map.model.PathModel;
import com.enation.javashop.mapparents.R;

import java.util.ArrayList;

/**
 * Created by LDD on 17/2/23.
 */

public class RouteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PathModel.Path.BusStep.Info> data;
    private LayoutInflater inflater;
    public RouteAdapter(Context context, PathModel.Path data) {
        this.context = context;
        this.data = data.getAllInfo();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PathModel.Path.BusStep.Info getItem(int position) {
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
            convertView = inflater.inflate(R.layout.routeitem,null);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.type);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PathModel.Path.BusStep.Info info = getItem(position);

        if (info.getWalkStartPoint()!=null){
            viewHolder.type.setImageResource(R.drawable.itemwalk);
            if (position==getCount()-1){
                viewHolder.content.setText(info.getInfo()+"到达目的地！");
            }else{
                viewHolder.content.setText(info.getInfo());
            }
        }

        if (info.getBusStartPoint()!=null){
            viewHolder.type.setImageResource(R.drawable.itembus);
            viewHolder.content.setText("乘坐"+info.getInfo()+info.getStartStation()+"上车 "+info.getEndStartion()+"下车 ");
        }

        if (info.getDirverpoint()!=null){
            viewHolder.type.setImageResource(R.drawable.itemcar);
            viewHolder.content.setText(info.getInfo());
        }
        return convertView;
    }
     static class ViewHolder{
         ImageView type;
         TextView content;
     }
}
