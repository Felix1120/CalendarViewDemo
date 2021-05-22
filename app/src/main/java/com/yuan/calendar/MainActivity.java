package com.yuan.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.yuan.calendar.custom.Calendar;
import com.yuan.calendar.custom.CalendarView;

/**
 * @author XueChaoFei
 * @date :2021/5/22
 */
public class MainActivity extends AppCompatActivity {

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = findViewById(R.id.calendar_activity_main);
        // 设置初始化时间
        calendar.setDate(2020,12, false);
        // 设置时间范围
        calendar.setDateRange(2019,5, 2021, 6);
        // 设置点击监听
        calendar.setItemClickListener((date)->{
            Toast.makeText(this, date, Toast.LENGTH_LONG).show();
        });
    }
}