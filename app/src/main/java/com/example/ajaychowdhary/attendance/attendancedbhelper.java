package com.example.ajaychowdhary.attendance;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by AJAY CHOWDHARY on 08-06-2016.
 */
public class attendancedbhelper extends SQLiteOpenHelper {
    public static String Databasename="attendance_database.db";
    public static String attendance="attendance";
    public static String DAY="day";
    public static String HOUR="hour";
    public static String MIN="minutes";
    public static String Status="present";
    public static String Subject="subject";
    public static String DAY_OF_WEEK="day_of_week";
    public int hr,day,min,dateint;
    SQLiteDatabase db,db_timetable;
    dbHelper dbhelper;
    List<String[]> list;
    String[] s;
     static Context context;
    public attendancedbhelper(Context context) {

        super(context,Databasename,null, 1);
        attendancedbhelper.context =context;
    }


    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + attendance + " (subject TEXT,day INTEGER,month INTEGER,year INTEGER,hour INTEGER,minutes INTEGER,present INTEGER)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+attendance);
        onCreate(db);
    }


    public  static float getpercent(String subject)
    {
     SQLiteDatabase   db=new attendancedbhelper(context).getWritableDatabase();
        float percent=0,total=0,a=0;
        Cursor c=db.rawQuery("select * from attendance where subject='"+subject+"'",null);
        while(c.moveToNext())
        {
            if(c.getInt(c.getColumnIndex("present"))==1)
              a++;
            total++;
        }
        try {
            percent = (a / total) * 100;
        }
        catch (Exception e)
        {
            percent=0;
        }
        return percent;

    }


}
