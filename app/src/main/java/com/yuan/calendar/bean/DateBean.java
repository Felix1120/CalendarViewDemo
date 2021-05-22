package com.yuan.calendar.bean;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 */
public class DateBean {
    private int year;
    private int month;
    private boolean isSelected;

    public DateBean(int year, int month , boolean isSelected) {
        this.year = year;
        this.month = month;
        this.isSelected = isSelected;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
