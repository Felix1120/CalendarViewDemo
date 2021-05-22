package com.yuan.calendar.bean;

import android.graphics.RectF;

import java.io.Serializable;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 */
public class DayBean implements Serializable {

    public static final long serialVersionUID = 1619232347000L;

    private RectF rectF;
    private int year;
    private int month;
    private int day = 0;
    private boolean isSelected = false;

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * 判断坐标区域
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isContains(float x, float y) {
        return rectF.contains(x, y);
    }

    public String getDate() {
        return year + "-" + month + "-" + day;
    }
}