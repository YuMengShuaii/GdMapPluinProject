package com.enation.javashop.map.utils;

import com.amap.api.location.AMapLocation;

/**
 * Map静态配置类
 */

public class MapConst {
    /**
     * 自己的坐标位置
     */
    public static AMapLocation MYLOCATION;

    /**
     * 查询分页的大小
     */
    public static int PAGE_SIZE = 10;

    /**
     * 云图ID
     */
    public static String TABLEID = "" ;

    /**
     * "公里"的Ascii编码
     */
    public static final String Kilometer = "\u516c\u91cc";// "公里";

    /**
     * "米"Ascii编码
     */
    public static final String Meter = "\u7c73";// "米";
}
