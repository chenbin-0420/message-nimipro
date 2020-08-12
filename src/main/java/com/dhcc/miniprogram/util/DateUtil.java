package com.dhcc.miniprogram.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cb
 * @date 2020/6/28
 * description：
 */
public class DateUtil {

    /**
     * 时间格式
     */
    private static final String YYYY_MM_DD_PATERN = "yyyy-MM-dd";
    private static final String YYYY_MM_DD_HH_MI_SS_PATERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取计算月份开始日期
     * @param diffMonth 相差月份
     * @return 时间格式字符串
     */
    public static Date getDateStart(Integer diffMonth) throws ParseException {
        // 获取相差月份日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,diffMonth);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        String dateStr = format(calendar.getTime(), YYYY_MM_DD_PATERN) + " 00:00:00";
        return parse(dateStr,YYYY_MM_DD_HH_MI_SS_PATERN);
    }

    /**
     * 获取计算月份开始日期
     * @return 时间格式字符串
     */
    public static Date getCurrentDayStart() throws ParseException {
        // 获取相差月份日期
        Calendar calendar = Calendar.getInstance();
        String dateStr = format(calendar.getTime(), YYYY_MM_DD_PATERN) + " 00:00:00";
        return parse(dateStr,YYYY_MM_DD_HH_MI_SS_PATERN);
    }

    /**
     * 获取计算月份结束日期
     * @param diffMonth 相差月份
     * @return 时间格式字符串
     */
    public static Date getDateEnd(Integer diffMonth) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,diffMonth);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String dateStr = format(calendar.getTime(), YYYY_MM_DD_PATERN) + " 23:59:59";
        return parse(dateStr,YYYY_MM_DD_HH_MI_SS_PATERN);
    }

    /**
     * 获取计算月份开始日期
     * @return 时间格式字符串
     */
    public static Date getCurrentDayEnd() throws ParseException {
        // 获取相差月份日期
        Calendar calendar = Calendar.getInstance();
        String dateStr = format(calendar.getTime(), YYYY_MM_DD_PATERN) + " 23:59:59";
        return parse(dateStr,YYYY_MM_DD_HH_MI_SS_PATERN);
    }




    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();


    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 如果新的线程中没有SimpleDateFormat，才会new一个
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }
}
