package com.example.ajaychowdhary.attendance;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by AJAY CHOWDHARY on 05-07-2016.
 */
public class attendance {
    String subject;
    int day;
    int month;
    int year;
    int hr;
    int min;
    String string_day;
    int present=0;
    String[] d= {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    Calendar calendar;



    public attendance(String subject, int present, Calendar c)
    {
        this.subject=subject;
        this.present=present;
        this.calendar=c;
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }
    public int getDay(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHr() {
        hr=calendar.get(Calendar.HOUR_OF_DAY);

        return hr;
    }

    public int getMin() {
        min=calendar.get(Calendar.MINUTE);
        return min;
    }

    public String getDay_string() {
        int day=calendar.get(Calendar.DAY_OF_WEEK);
        string_day=d[day-1];
        return string_day;
    }


    public String getSubject() {
        return subject;
    }



    public int getPresent() {
        return present;
    }


}
