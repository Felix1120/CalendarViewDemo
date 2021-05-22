package com.yuan.calendar.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.yuan.calendar.R;
import com.yuan.calendar.bean.DayBean;
import com.yuan.calendar.callback.DateItemClickListener;
import com.yuan.calendar.utils.CalendarUtils;
import com.yuan.calendar.utils.DimenUtils;
import com.yuan.calendar.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 * 核心日历绘制类
 */
public class CalendarView extends View {

    // 背景画笔
    private Paint bgPaint;
    // 文本画笔
    private Paint textPaint;
    // 文字颜色
    private int textColor;
    // 文字大小
    private float textWidth;
    // 周文字大小
    private float weekWidth;
    // 控件宽度
    private float viewWidth;
    // 控件高度
    private float viewHeight;
    // Day的Item高度
    private float dayItemHeight;
    // Day的总高度 = view高度 - week高度
    private float dayHeight;
    // 周的高度
    private float weekHeight;
    // 整个控件可绘制区域
    private RectF viewRectF;
    // 每一份区域的宽度 总宽度 / 7
    private float itemWidth;
    // DayBean对应的绘制区域
    private List<RectF> dayRectF;
    // 周一 到 周日的绘制区域
    private List<RectF> weekRectF;
    // DayBeans
    private List<DayBean> dayBeans;
    // 第一天是否周天
    private boolean isSunday;
    // 点击事件回调
    private DateItemClickListener listener;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttributeSet(context, attrs);
        initPaint();
        initRectF();
    }

    private void getAttributeSet(Context context, AttributeSet set) {
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.CalendarView);
        textColor = typedArray.getColor(R.styleable.CalendarView_textColor, getResources().getColor(R.color.day_color));
        textWidth = typedArray.getDimension(R.styleable.CalendarView_textSize, DimenUtils.dp2px(getContext(), 16));
        weekWidth = typedArray.getDimension(R.styleable.CalendarView_weekSize, DimenUtils.dp2px(getContext(), 14));
        weekHeight = typedArray.getDimension(R.styleable.CalendarView_weekHeight, DimenUtils.dp2px(getContext(), 20));
        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        viewWidth = DimenUtils.dp2px(getContext(), 300);
        viewHeight = DimenUtils.dp2px(getContext(), 350);
    }

    /**
     * 初始化绘制区域
     */
    private void initRectF() {
        viewRectF = new RectF();
        dayRectF = new ArrayList<>(CalendarUtils.DAY_CELL);
        for (int index = 0; index < CalendarUtils.DAY_CELL; index++) {
            dayRectF.add(new RectF());
        }
        weekRectF = new ArrayList<>(CalendarUtils.COLUMN);
        for (int index = 0; index < CalendarUtils.COLUMN; index++) {
            weekRectF.add(new RectF());
        }
        dayBeans = new ArrayList<>(CalendarUtils.DAY_CELL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize((int) viewWidth, widthMeasureSpec),
                getDefaultSize((int) viewHeight, heightMeasureSpec));
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        calcRectF();
    }

    /**
     * 计算绘制区域
     */
    private void calcRectF() {
        viewRectF.set(0, 0, viewWidth, viewHeight);
        itemWidth = viewWidth / CalendarUtils.COLUMN;
        calcWeek();
        calcDay();
    }

    /**
     * 计算周的绘制区域
     */
    private void calcWeek() {
        for (int index = 0; index < CalendarUtils.COLUMN; index++) {
            float startX = index * itemWidth;
            weekRectF.get(index).set(startX, 0, startX + itemWidth, weekHeight
                    + DimenUtils.dp2px(getContext(), 15));
        }
    }

    /**
     * 计算每一天的绘制区域
     */
    private void calcDay() {
        dayHeight = viewHeight - weekHeight;
        dayItemHeight = dayHeight / (CalendarUtils.ROWS + 1);
        for (int row = 0; row < CalendarUtils.ROWS; row++) {
            float startY = row * dayItemHeight + DimenUtils.dp2px(getContext(), 40);
            for (int column = 0; column < CalendarUtils.COLUMN; column++) {
                int index = row * CalendarUtils.COLUMN + column;
                float startX = column * itemWidth + viewRectF.left;
                dayRectF.get(index).set(startX + DimenUtils.dp2px(getContext(), 5), startY, startX + itemWidth - DimenUtils.dp2px(getContext(), 5), startY + dayItemHeight);
            }
        }
    }

    public static int getDefaultSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(defaultSize, specSize);
                break;
            default:
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWeek(canvas);
        drawDays(canvas);
    }

    /**
     * 绘制星期
     *
     * @param canvas 画布
     */
    private void drawWeek(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        for (int index = 0; index < CalendarUtils.COLUMN; index++) {
            textPaint.setTextSize(weekWidth);
            textPaint.setColor(getResources().getColor(R.color.week_color));
            canvas.drawText(isSunday ? CalendarUtils.WEEKS_SUNDAY.get(index) : CalendarUtils.WEEKS_MONDAY.get(index),
                    weekRectF.get(index).centerX(), getTextBaseline(weekRectF.get(index)), textPaint);
        }
    }

    /**
     * 绘制天
     *
     * @param canvas 画布
     */
    private void drawDays(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        canvas.save();
        canvas.clipRect(0, viewHeight - dayHeight, viewWidth, viewHeight);
        for (int index = 0; index < CalendarUtils.DAY_CELL; index++) {
            drawDay(canvas, dayBeans.get(index));
        }
        canvas.restore();
    }

    /**
     * 绘制天
     *
     * @param canvas 画布
     */
    private void drawDay(Canvas canvas, DayBean dayBean) {
        if (canvas == null || dayBean == null) {
            return;
        }
        // 无数据时跳过绘制
        if (dayBean.getDay() == 0) {
            return;
        }
        textPaint.setColor(textColor);
        textPaint.setTextSize(textWidth);
        // 判断是否被选中
        if (dayBean.isSelected()) {
            // 画选中背景
            LinearGradient linearGradient = new LinearGradient(dayBean.getRectF().left, dayBean.getRectF().top,
                    dayBean.getRectF().right, dayBean.getRectF().bottom,
                    new int[]{Color.parseColor("#ffba00"),
                            Color.parseColor("#ff9102")}, null, LinearGradient.TileMode.CLAMP);
            bgPaint.setShader(linearGradient);
            canvas.drawRoundRect(dayBean.getRectF(), 20, 20, bgPaint);
            textPaint.setColor(getResources().getColor(R.color.white));
        } else {
            textPaint.setColor(getResources().getColor(R.color.day_color));
        }
        // 画文本
        canvas.drawText(String.valueOf(dayBean.getDay()), dayBean.getRectF().centerX(),
                getTextBaseline(dayBean.getRectF()), textPaint);
    }

    private float getTextBaseline(RectF rectF) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        return rectF.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2;
    }

    /**
     * 处理Day点击事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eventDown(event);
                break;
            case MotionEvent.ACTION_UP:
                eventUp();
                break;
        }
        return true;
    }

    /**
     * 记录点击时的坐标
     */
    private float clickX;
    private float clickY;

    private void eventDown(MotionEvent event) {
        if (event == null) {
            return;
        }
        clickX = event.getX();
        clickY = event.getY();
    }

    /**
     * 计算点击的坐标属于哪一个区域
     */
    private void eventUp() {
        for (int index = 0; index < dayBeans.size(); index++) {
            if (dayBeans.get(index).isContains(clickX, clickY)) {
                dayBeans.get(index).setSelected(true);
                if (listener != null) {
                    listener.click(dayBeans.get(index).getDate());
                }
            } else {
                dayBeans.get(index).setSelected(false);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * ******* 公共方法 ********
     */
    /**
     * 时间戳格式
     *
     * @param ts
     */
    public void setDate(long ts, boolean isSundayFirst) {
        setDate(TimeUtils.tsToYear(ts), TimeUtils.tsToMonth(ts), isSundayFirst);
    }

    /**
     * 年 月 字符串格式
     *
     * @param year
     * @param month
     */
    public void setDate(String year, String month, boolean isSundayFirst) {
        setDate(Integer.parseInt(year), Integer.parseInt(month), isSundayFirst);
    }

    /**
     * 年 月 数字格式
     *
     * @param year
     * @param month
     */
    public void setDate(int year, int month, boolean isSundayFirst) {
        dayBeans.clear();
        dayBeans.addAll(CalendarUtils.getInstance().getDayBeans(year, month, isSundayFirst));
        // 天数与区域进行关联
        for (int index = 0; index < dayBeans.size(); index++) {
            dayBeans.get(index).setRectF(dayRectF.get(index));
        }
        isSunday = isSundayFirst;
        invalidate();
    }

    /**
     * 点击回调方法
     *
     * @param listener
     */
    public void setItemClickListener(DateItemClickListener listener) {
        this.listener = listener;
    }
}
