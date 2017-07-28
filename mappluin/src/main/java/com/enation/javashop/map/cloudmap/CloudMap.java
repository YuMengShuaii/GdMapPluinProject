package com.enation.javashop.map.cloudmap;

import com.enation.javashop.map.utils.MapConst;

/**
 * 云图初始化入口
 */

public class CloudMap {

    /**
     * 初始化云图TableId
     * @param tableId  云图TableId
     */
    public static void initCloudMap(String tableId){
        MapConst.TABLEID = tableId;
    }

    /**
     * 初始化云图查询PageSize
     * @param pagesize pagesize
     */
    public static void initCloudQueryPageSize(int pagesize){
        MapConst.PAGE_SIZE = pagesize;
    }
}
