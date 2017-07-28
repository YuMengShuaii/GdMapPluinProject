package com.enation.javashop.mapparents.application;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.model.LatLng;
import com.enation.javashop.map.cloudmap.CloudMap;

import java.util.HashMap;

/**
 * Created by LDD on 17/7/27.
 */

public class Application extends android.app.Application {
    public static LatLng latlon = new LatLng(39.954959, 116.820641);
    /**
     * 自己的坐标位置
     */
    public static AMapLocation mylocation;

    /**
     * 储存数据公共空间
     */
    private static HashMap map;

    /**
     * 云图ID
     */
    public static final String TABLEID = "58abae3cafdf520ea88fc350" ;

    @Override
    public void onCreate() {
        super.onCreate();
        map = new HashMap<>();
        /**初始化云图表ID*/
        CloudMap.initCloudMap(TABLEID);
        /**初始化云图查询时分页大小*/
        CloudMap.initCloudQueryPageSize(10);
    }

    /**
     * 存储数据
     * @param key
     * @param o
     */
    public static void put(String key,Object o){
        map.put(key,o);
    }

    /**
     * 获取数据
     * @param key
     * @param isDetele
     * @return
     */
    public static Object get(String key,boolean isDetele){
        if (isDetele){
            return map.remove(key);
        }else{
            return map.get(key);
        }
    }
}
