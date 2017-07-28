package com.enation.javashop.map.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Map类型
 */

public class MapType {

    public static final int BUS = 0;
    public static final int WALK = 1;
    public static final int DRIVE = 2;

    @IntDef({BUS, WALK, DRIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MT {
    }
}
