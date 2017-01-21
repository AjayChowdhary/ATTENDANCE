package com.example.ajaychowdhary.attendance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class attendance_record extends AppCompatActivity {
    List<String> list;
    HashMap<String,List<String>> hashMap;
    attendancedbhelper adbhelper;
    SQLiteDatabase db;
    String[] days_name = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetableview);              //recyle timetable layout
        list =new ArrayList<String>();
        hashMap=new HashMap<String,List<String>>();
        adbhelper=new attendancedbhelper(this);
        db=adbhelper.getWritableDatabase();

        getsubjects();                    //list will get updated
        getdata();                       //hashmap updated
        timetableadapter addap=new timetableadapter(this,list,hashMap,1);
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.ExpandablelistViewfortimetable);
        lv.setAdapter(addap);
    }
    public void getsubjects()
    {
        Cursor cursor =db.rawQuery("select * from attendance",null);
        while(cursor.moveToNext())
        {

            Log.d("subject", cursor.getString(cursor.getColumnIndex("subject")));
            if(!list.contains(cursor.getString(cursor.getColumnIndex("subject")))) {
                list.add(cursor.getString(cursor.getColumnIndex("subject")));
                attendancedbhelper.getpercent(cursor.getString(cursor.getColumnIndex("subject")));
            }
        }
        cursor.close();
    }
    public void getdata() {
        for (int i = 0; i < list.size(); i++) {
            List<String> temp = new ArrayList<String>();
            Cursor c = db.rawQuery("select * from attendance where subject='" + list.get(i) + "'",null);
            while (c.moveToNext()) {
                Calendar calendar=Calendar.getInstance();
                calendar.set((c.getInt(c.getColumnIndex("year"))),(c.getInt(c.getColumnIndex("month"))),c.getInt(c.getColumnIndex("day")));
                temp.add(days_name[calendar.get(Calendar.DAY_OF_WEEK)-1]+" "+(c.getInt(c.getColumnIndex("day")))+"/"+c.getInt(c.getColumnIndex("month"))+"/" +c.getInt(c.getColumnIndex(("year")))+ "    " + c.getInt(c.getColumnIndex("hour")) + ":" + c.getInt(c.getColumnIndex("minutes")) + getp(c.getInt(c.getColumnIndex("present"))));


            }
            c.close();
            hashMap.put(list.get(i), temp);

        }
    }
    public String getp(int i)
    {
        if(i==1)
            return "P";

            return "A";
    }
}
