package com.enation.javashop.map.monitor;

/**
 * Created by LDD on 17/2/24.
 */

public class Listener {
    /**
     * 获取数据监听事件
     */
   public interface DataLisener<T>{
        void success(T cloudResult);
        void faild(String errorMessage);
    }
}
