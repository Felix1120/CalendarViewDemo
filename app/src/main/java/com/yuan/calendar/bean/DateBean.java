package com.yuan.calendar.bean;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 */
public class DateBean {
    private int year;
    private int month;

    public DateBean(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
