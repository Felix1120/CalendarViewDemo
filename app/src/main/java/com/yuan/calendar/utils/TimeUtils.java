package com.yuan.calendar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 */
public class TimeUtils {

    private final static int ONE_DAY = 1000 * 60 * 60 * 24;
    private static Date date = new Date();

    /**
     * 获取时间戳的年
     */
    public static int tsToYear(long ts) {
        String format = "yyyy";
        date.setTime(ts);
        return Integer.parseInt(new SimpleDateFormat(format).format(date));
    }

    /**
     * 获取时间戳的月
     */
    public static int tsToMonth(long ts) {
        String format = "MM";
        date.setTime(ts);
        return Integer.parseInt(new SimpleDateFormat(format).format(date));
    }

    /**
     * 获取时间戳的日
     */
    public static int tsToDay(long ts) {
        String format = "dd";
        date.setTime(ts);
        return Integer.parseInt(new SimpleDateFormat(format).format(date));
    }
}
