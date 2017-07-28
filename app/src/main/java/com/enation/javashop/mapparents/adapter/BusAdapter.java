package com.enation.javashop.mapparents.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.enation.javashop.map.model.PathModel;
import com.enation.javashop.mapparents.R;

/**
 * Created by LDD on 17/2/22.
 */

public class BusAdapter extends BaseAdapter {
    /**
     * Activity上下文
     */
    private Context context;
    /**
     * 数据对象
     */
    private PathModel busModel;
    /**
     * 布局填充器
     */
    private LayoutInflater inflater;

    /**
     * 构造方法
     * @param context
     * @param busModel
     */
    public BusAdapter(Context context, PathModel busModel) {
        this.context = context;
        this.busModel = busModel;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (busModel==null||busModel.getPaths()==null){
            return 0;
        }
        return busModel.getPaths().size();
    }

    @Override
    public PathModel.Path getItem(int position) {
        return busModel.getPaths().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewholder = null;
        if (convertView==null){
            viewholder=new viewHolder();
            convertView = inflater.inflate(R.layout.layout,null);
            viewholder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewholder);
        }else{
            viewholder = (viewHolder) convertView.getTag();
        }

        /**
         * 判断是否为公交Info
         */
        if (getItem(position).getBusNames()!=null){
            String bus = "";
        for (int i = 0; i < getItem(position).getBusNames().size(); i++) {
            if (i>=0&&i!=getItem(position).getBusNames().size()-1){
                bus+=getItem(position).getBusNames().get(i)+">";
            }else{
                bus+=getItem(position).getBusNames().get(i);
            }
        }
            viewholder.tv.setText(bus+" | 耗时"+getItem(position).getBusRotueTitle());
        }

        /**
         * 判断是否为自驾Info
         */
          if (getItem(position).getDriverTitle()!=null&&!getItem(position).getDriverTitle().equals("")){
              viewholder.tv.setText(getItem(position).getDriver());
          }
        /**
         * 判断是否为步行Info
         */
        if (getItem(position).getWalkTitle()!=null&&!getItem(position).getWalkTitle().equals("")){
            viewholder.tv.setText(getItem(position).getWalkTitle());
        }

        return convertView;
    }
    static class viewHolder{
        TextView tv;
    }
}
