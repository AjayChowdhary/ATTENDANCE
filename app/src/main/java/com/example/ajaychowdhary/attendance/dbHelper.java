package com.example.ajaychowdhary.attendance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AJAY CHOWDHARY on 08-06-2016.
 */
public class dbHelper extends SQLiteOpenHelper{
    public static final String timetable="timetable";
    public static final String DATABASENAME="timetablenew.db";
    public static final String subject="subject";
    public static final String day_of_week="day_of_week";
    public static final String hr="hour";
    public static final String min="minutes";
    public static final String []s={subject,day_of_week,hr,min};
    public static final int DATABASEVERSION=1;

    public dbHelper(Context context) {
        super(context, DATABASENAME, null,DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+timetable+" (day_of_week INTEGER,subject TEXT,hour_of_day INTEGER,minutes INTEGER)");   //create table

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+timetable);
        onCreate(db);

    }


}
