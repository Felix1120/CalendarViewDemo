package com.yuan.calendar.utils;

import com.yuan.calendar.bean.DayBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 * 日历辅助工具类
 */
public class CalendarUtils {

    private volatile static CalendarUtils calendarUtils;
    // 最多可能存在6行
    public static int ROWS = 6;
    // 每周7天
    public static int COLUMN = 7;
    public static int DAY_CELL = ROWS * COLUMN;
    public static int ONE_DAY = 1;
    // 缓存月份的天数信息
    private HashMap<String, List<DayBean>> cacheDays;
    public static List<String> WEEKS_SUNDAY = Arrays.asList("周日", "周一", "周二", "周三", "周四", "周五", "周六");
    public static List<String> WEEKS_MONDAY = Arrays.asList("周一", "周二", "周三", "周四", "周五", "周六", "周日");

    private CalendarUtils() {
        cacheDays = new HashMap<>();
    }

    public static CalendarUtils getInstance() {
        if (calendarUtils == null) {
            synchronized (CalendarUtils.class) {
                if (calendarUtils == null) {
                    calendarUtils = new CalendarUtils();
                }
            }
        }
        return calendarUtils;
    }

    /**
     * 查询当月1号是周几 来确定起始位置
     *
     * @param year          年
     * @param month         月
     * @param isSundayFirst 周日是否为第一天
     * @return
     */
    private int getMonthFirstDateAtWeek(int year, int month, boolean isSundayFirst) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, ONE_DAY);
        int result = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (!isSundayFirst) {
            if (result == 0) {
                result = 7;
            }
            result -= 1;
        }
        return result;
    }

    /**
     * @param year  当前年份
     * @param month 当前月份
     * @return
     */
    public List<DayBean> getDayBeans(int year, int month, boolean isSundayFirst) {
        // 判断是否有缓存
        if (cacheDays.containsKey(getKey(year, month))) {
            return cacheDays.get(getKey(year, month));
        }
        // 保存DayBean
        List<DayBean> dayBeans = new ArrayList<>();
        int firstIndex = getMonthFirstDateAtWeek(year, month, isSundayFirst);
        for (int index = 0; index < firstIndex; index++) {
            dayBeans.add(new DayBean());
        }
        for (int index = 0; index < getMonthDays(year, month); index++) {
            dayBeans.add(addDay(year, month, index + 1));
        }
        for (int index = 0; index < CalendarUtils.DAY_CELL - getMonthDays(year, month) - firstIndex; index++) {
            dayBeans.add(new DayBean());
        }
        // 缓存年月的天数
        cacheDays.put(getKey(year, month), dayBeans);
        return dayBeans;
    }

    /**
     * 根据年月 获取天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0))
                        || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 创建DayBean
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    private DayBean addDay(int year, int month, int day) {
        DayBean dayBean = new DayBean();
        dayBean.setYear(year);
        dayBean.setMonth(month);
        dayBean.setDay(day);
        return dayBean;
    }

    /**
     * 获取缓存的key
     *
     * @param year
     * @param month
     */
    private String getKey(int year, int month) {
        return String.valueOf(year).concat(String.valueOf(month));
    }
}
