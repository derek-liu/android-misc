package com.example.derek.app;

import android.content.Context;
import com.example.derek.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeFormat {
    private final SimpleDateFormat mDateFormat;
    private final SimpleDateFormat mDateFormatWithYear;
    final String mTimeMinutes;
    final String mTimeMinute;
    final String mTimeHours;
    final String mTimeHour;
    final String mTimeNow;
    final String mTimeDays;
    final String mTimeDay;
    final String mTimeMonth;
    final String mTimeMonths;
    final Date mTmpDate;
    final Calendar mCalendar;

    public DateTimeFormat(Context context) {
        mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        mDateFormatWithYear = new SimpleDateFormat("yy-MM-dd HH:mm");
        mTimeMinutes = " " + context.getString(R.string.ss_time_minutes);
        mTimeMinute = " " + context.getString(R.string.ss_time_minute);
        mTimeHours = " " + context.getString(R.string.ss_time_hours);
        mTimeHour = " " + context.getString(R.string.ss_time_hour);
        mTimeDay = " " + context.getString(R.string.ss_time_day);
        mTimeDays = " " + context.getString(R.string.ss_time_days);
        mTimeMonth = " " + context.getString(R.string.ss_time_month);
        mTimeMonths = " " + context.getString(R.string.ss_time_months);
        mTimeNow = context.getString(R.string.ss_time_now);
        mTmpDate = new Date();
        mCalendar = Calendar.getInstance();
    }

    /**
     * format date time.
     * this method is not synchronized.
     *
     * @param time time in millisecond
     */
    public String format(long time) {
        long now = System.currentTimeMillis();
        long delta = (now - time) / 1000L;
        if (delta < 60) {
            // 1分钟内
            return mTimeNow;
        } else if (delta < 3600) {
            // 1小时内
            long minute = delta / 60;
            return (minute + (minute <= 1 ? mTimeMinute : mTimeMinutes));
        } else if (delta < 24 * 3600) {
            // 1天内
            long hour = delta / 3600;
            return (hour + (hour <= 1 ? mTimeHour : mTimeHours));
        } else if (delta < 30 * 24 * 3600) {
            // 1个月内
            long day = delta / (24 * 3600);
            return (day + (day <= 1 ? mTimeDay : mTimeDays));
        } else if (delta < 12 * 30 * 24 * 3600) {
            long month = delta / (30 * 24 * 3600);
            return (month + (month <= 1 ? mTimeMonth : mTimeMonths));
        } else {
            // 超过一个月 显示日期
            mCalendar.setTimeInMillis(now);
            int yearNow = mCalendar.get(Calendar.YEAR);
            mCalendar.setTimeInMillis(time);
            int yearTarget = mCalendar.get(Calendar.YEAR);
            mTmpDate.setTime(time);
            return (yearTarget < yearNow) ? mDateFormatWithYear.format(mTmpDate) : mDateFormat.format(mTmpDate);
        }
    }
}
