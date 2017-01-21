package com.example.ajaychowdhary.attendance;

/**
 * Created by AJAY CHOWDHARY on 06-07-2016.
 */
public class timetable {
    String subject;
    String day;
    int hr;
    int min;

    public timetable( String day,String subject, int hr, int min) {
        this.subject = subject;
        this.day = day;
        this.hr = hr;
        this.min = min;
    }

    public String getSubject() {
        return subject;
    }

    public String getDay() {
        return day;
    }

    public int getHr() {
        return hr;
    }

    public int getMin() {
        return min;
    }
}
