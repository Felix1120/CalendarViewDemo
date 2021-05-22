package com.yuan.calendar.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yuan.calendar.R;
import com.yuan.calendar.adapter.MonthAdapter;
import com.yuan.calendar.bean.DateBean;
import com.yuan.calendar.callback.DateItemClickListener;
import com.yuan.calendar.callback.OnClickListener;
import com.yuan.calendar.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 * 对外提供的组件类
 */
public class Calendar extends LinearLayout {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private TextView tvYear;

    private int startYear;
    private int endYear;
    private int startMonth;
    private int endMonth;

    private MonthAdapter adapter;
    private DateItemClickListener listener;
    private List<DateBean> beanList;

    public Calendar(Context context) {
        super(context);
    }

    public Calendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.calendar_view, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Calendar);
        initWidget(typedArray);
    }

    private void initWidget(TypedArray typedArray) {
        /**
         * 说明
         * 可以使用typedArray参数 来进行Calendar到CalendarView的参数设置
         * 或者调整组合view里面的大小 颜色等参数  Demo由于时间关系 此部分代码省略
         */
        calendarView = findViewById(R.id.cv_calendar_view);
        recyclerView = findViewById(R.id.rv_month_calendar_view);
        tvYear = findViewById(R.id.tv_year_calendar_view);

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        adapter = new MonthAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener((index, year, month) -> {
            tvYear.setText(String.valueOf(year));
            // 加载日历
            calendarView.setDate(year, month, false);
            // 刷新月份颜色
            updateList(index, beanList);
        });
    }

    /**
     * 获取开始到结束时间的集合
     *
     * @return
     */
    private List<DateBean> getDateList() {
        List<DateBean> dateBeans = new ArrayList<>();
        // 开始年份到12月
        for (int month = startMonth; month <= 12; month++) {
            dateBeans.add(new DateBean(startYear, month, false));
        }
        // 开始到结束年份之间的月份
        for (int year = startYear + 1; year <= endYear - 1; year++) {
            for (int month = 1; month <= 12; month++) {
                dateBeans.add(new DateBean(year, month, false));
            }
        }
        // 最后一年的月份
        for (int month = 1; month <= endMonth; month++) {
            dateBeans.add(new DateBean(endYear, month, false));
        }
        return dateBeans;
    }

    /**
     * 修改选中月份的颜色标志位
     *
     * @param position
     * @param beanList
     */
    private void updateList(int position, List<DateBean> beanList) {
        if (beanList == null) {
            return;
        }
        for (int index = 0; index < beanList.size(); index++) {
            if (position == index) {
                beanList.get(index).setSelected(true);
            } else {
                beanList.get(index).setSelected(false);
            }
        }
        adapter.updateList(beanList);
    }


    /**
     * ******* 公共方法 ********
     */
    /**
     * 设置默认选中的月份
     * 时间戳格式
     *
     * @param ts
     */
    public void setDate(long ts, boolean isSundayFirst) {
        setDate(TimeUtils.tsToYear(ts), TimeUtils.tsToMonth(ts), isSundayFirst);
    }

    /**
     * 设置默认选中的月份
     * 年 月 字符串格式
     *
     * @param year
     * @param month
     */
    public void setDate(String year, String month, boolean isSundayFirst) {
        setDate(Integer.parseInt(year), Integer.parseInt(month), isSundayFirst);
    }

    /**
     * 设置默认选中的月份
     * 年 月 数字格式
     *
     * @param year
     * @param month
     */
    public void setDate(int year, int month, boolean isSundayFirst) {
        calendarView.setDate(year, month, isSundayFirst);
    }

    /**
     * 设置范围
     */
    public void setDateRange(int startYear, int startMonth, int endYear, int endMonth) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.endYear = endYear;
        this.endMonth = endMonth;
        beanList = getDateList();
        adapter.updateList(beanList);
        tvYear.setText(String.valueOf(startYear));
    }

    /**
     * 设置选中回调
     */
    public void setItemClickListener(DateItemClickListener listener) {
        if (calendarView != null) {
            calendarView.setItemClickListener(listener);
        }
    }
}
