package com.example.ajaychowdhary.attendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class add_data extends  Activity implements OnItemSelectedListener  {
    ArrayList<attendance> record;
    attendance aRecord;
    Calendar c;

    dbHelper dbhelper;
    SQLiteDatabase db;

    EditText subject;
    //  SharedPreferences sharedPreferences;



    ImageButton add_button;
    Spinner day_selector;
    TimePicker timePicker;
    String sub=null;



    int day_id,hr,min;
    String day;
    String[] days_name={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    int day_id_array[]={1,2,3,4,5,6,7};
    public ArrayAdapter<String> data_added;
    public ListView data;

    String[] split;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_timetable);

        subject=(EditText)findViewById(R.id.subject);
        sub=subject.getText().toString();

        day_selector=(Spinner)findViewById(R.id.day_selector);
        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, days_name);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_selector.setAdapter(adap);
        day_selector.setOnItemSelectedListener(this);


        timePicker=(TimePicker)findViewById(R.id.timePicker);


        data=(ListView)findViewById(R.id.listView);
        data_added=new ArrayAdapter<String>(this,R.layout.newlayout);
        dbhelper=new dbHelper(getApplicationContext());
        db=dbhelper.getWritableDatabase();

        showlist(); //to show the previously added data;


    }

    public void add(View v)
    {


        min=timePicker.getCurrentMinute();

        hr=timePicker.getCurrentHour();

        sub=subject.getText().toString();

        String s=sub+" "+days_name[day_id]+" "+hr+" "+min+" ";

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

        ContentValues values=new ContentValues();
        values.put("day_of_week", day_id+1);       //day_id starts from 0
        values.put("subject",sub);
        values.put("hour_of_day", hr);
        values.put("minutes", min);
        long h = db.insert(dbHelper.timetable, null, values);
        if (h == -1) {
            Toast.makeText(getApplicationContext(), "UNABLE TO ADD", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "SUBJECT ADDED", Toast.LENGTH_SHORT).show();

        showlist();
        //data added
    }
    public void showlist()
    {
        data_added.clear();
        Cursor c=db.rawQuery("select * from " + dbHelper.timetable, null);
        while(c.moveToNext())
        {
            data_added.add(days_name[c.getInt(c.getColumnIndex(dbHelper.day_of_week))-1]+" "+c.getString(c.getColumnIndex(dbHelper.subject))+" " + c.getString(c.getColumnIndex("hour_of_day"))+" " + c.getString(c.getColumnIndex("minutes")));
        }
        data.setAdapter(data_added);
        data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                alet(s);
            }

        });
        c.close();

    }


    public void alet(String s)
    {

        split=s.split(" ");

        AlertDialog.Builder alert=new AlertDialog.Builder(add_data.this);
        alert.setCancelable(true);
        alert.setMessage("DELETE  " + s);
        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getApplicationContext()," "+ get_day_id(split[0])+" ", Toast.LENGTH_SHORT).show();
                db.execSQL("DELETE FROM timetable WHERE subject='" + split[1] + "'" + " AND day_of_week='" + get_day_id(split[0]) + "'" + "AND hour_of_day='" + split[2] + "'" + "AND minutes='" + split[3] + "'");
                data_added.clear();
                showlist();
            }
        });


        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        day_id=position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public int get_day_id(String s1)
    { int id;
        for(id=0;id<7;id++)
        {
            if(s1.equals(days_name[id]))
                break;
            else {
                Log.d("s", s1);
                Log.d("days_name", days_name[id]);
            }
        }


        return id+1;
    }


}
