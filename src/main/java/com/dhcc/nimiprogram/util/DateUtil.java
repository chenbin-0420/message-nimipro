package com.dhcc.nimiprogram.util;

import java.util.Date;

/**
 * @author cb
 * @date 2020/6/28
 * description：
 */
public class DateUtil {

    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }
}
