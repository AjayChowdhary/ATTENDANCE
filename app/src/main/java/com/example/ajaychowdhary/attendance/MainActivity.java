package com.example.ajaychowdhary.attendance;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayAdapter<String> adapter;

    String[] days_name = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    public int lhr, lday, lmin, lday_of_week, lmonth, lyear, present=1;

    SQLiteDatabase adb, db_timetable;
    ListView lv;
    String sub;
    dbHelper dbhelper;
    attendancedbhelper adbhelper;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder alertDialog;
    int min, hr, day, month, year, day_of_week;
    attendanceadapter addap;

    Calendar calender2;
    private int current_hr;

    Calendar calendar;
    int current_min ;
    int current_Year ;
    int current_Month;
    int current_Day ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "SAVIOUR MODE", Snackbar.LENGTH_LONG)
                        .show();
                Intent i = new Intent(getApplicationContext(), saviour.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        calendar = Calendar.getInstance();                         //getting current time
        current_hr = calendar.get(Calendar.HOUR_OF_DAY);
        current_min = calendar.get(Calendar.MINUTE);
         current_Year = calendar.get(Calendar.YEAR);
         current_Month = calendar.get(Calendar.MONTH);
         current_Day = calendar.get(Calendar.DAY_OF_MONTH);
        //int current_day_of_week = calendar.get(Calendar.DAY_OF_WEEK);


        sharedPreferences = getSharedPreferences("last_attended_class", Context.MODE_PRIVATE);
        lday = sharedPreferences.getInt("day", -1);
        lmonth = sharedPreferences.getInt("month", -1);
        lyear = sharedPreferences.getInt("year", -1);
        lhr = sharedPreferences.getInt("hour", -1);
        lmin = sharedPreferences.getInt("min", -1);
        if(lmonth<0||lyear<0)
        {
            Intent intent=new Intent(this,newsemdate.class);
            startActivity(intent);
        }
        else{
            new loadattendance().execute();
        }

        mark_attendance();
    }
    private class loadattendance extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {

            showupdata();
            return null;
        }
    }
    void showupdata()
    {

        calender2 = Calendar.getInstance();
        calendar.set(current_Year, current_Month, current_Day, current_hr, current_min);
        calender2.set(lyear, lmonth, lday, lhr, lmin);
        long milli_diff = calendar.getTimeInMillis() - calender2.getTimeInMillis();//include the case of registring new sem 7 8 months before then long mill_diff will be negative
        Log.d("millidiff", milli_diff + "");
        long min_diff = milli_diff / 60000;
        Log.d("mindif",min_diff+"");
        dbhelper = new dbHelper(getApplicationContext());
        db_timetable = dbhelper.getWritableDatabase();
        adbhelper=new attendancedbhelper(getApplicationContext());
        adb=adbhelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        int i = 0;
        while (i < min_diff) {
            calender2.add(Calendar.MINUTE, 1);
            Cursor cursor = db_timetable.rawQuery("select * from timetable where day_of_week='" + calender2.get(Calendar.DAY_OF_WEEK) + "'AND hour_of_day='" + calender2.get(Calendar.HOUR_OF_DAY) + "'AND minutes='" + calender2.get(Calendar.MINUTE) + "'", null);
            Log.d("date", calender2.get(Calendar.MONTH) + "  " + calender2.get(Calendar.DAY_OF_MONTH));
            while (cursor.moveToNext()) {
                sub = cursor.getString(cursor.getColumnIndex("subject"));
                day_of_week = cursor.getInt(cursor.getColumnIndex("day_of_week"));
                hr = cursor.getInt(cursor.getColumnIndex("hour_of_day"));
                min = cursor.getInt(cursor.getColumnIndex("minutes"));
                year = calender2.get(Calendar.YEAR);
                month = calender2.get(Calendar.MONTH);
                day = calender2.get(Calendar.DAY_OF_MONTH);
                values.put("year",year);
                values.put("day",day);
                values.put("hour",hr);
                values.put("minutes",min);
                values.put("subject",sub);
                values.put("month",month);
                values.put("present",-1);
                long d= adb.insert("attendance",null,values);
                if(d<0)
                {Log.d("data in attendance"," not added ");}
            }
            i++;
            cursor.close();
        }
        SharedPreferences sharedPreferences=MainActivity.this.getSharedPreferences("last_attended_class", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1=sharedPreferences.edit();
        editor1.putInt("day",current_Day);
        editor1.putInt("year",current_Year);
        editor1.putInt("month",current_Month);
        editor1.putInt("hour",current_hr);
        editor1.putInt("min", current_min);
        editor1.commit();

    }



    public void mark_attendance() {

        final ArrayList<attendance> record = new ArrayList<attendance>();
        String s;
        attendance arec;
        // arec=new attendance("snldksn",-1,calender2); to check working
        adbhelper = new attendancedbhelper(getApplicationContext());
        adb = adbhelper.getWritableDatabase();
        Calendar c = Calendar.getInstance();


        final Cursor cursor = adb.rawQuery("select * from attendance where present='-1'", null);
        while (cursor.moveToNext()) {
            c.set(cursor.getInt(cursor.getColumnIndex("year")), cursor.getInt(cursor.getColumnIndex("month")), cursor.getInt(cursor.getColumnIndex("day")), cursor.getInt(cursor.getColumnIndex("hour")), cursor.getInt(cursor.getColumnIndex("minutes")));
            s = cursor.getString(cursor.getColumnIndex("subject"));
            arec = new attendance(s, -1, c);
            record.add(arec);


        }
        if(record.size()==0)
            record.add(new attendance("ALL ATTENDANCE IS MARKED",-1,c));
        cursor.close();


         addap = new attendanceadapter(record, getApplicationContext());
        lv = (ListView) findViewById(R.id.pending_attendance_view);
        lv.setAdapter(addap);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final attendance arec = record.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("mark your attendance");
                builder.setCancelable(true);
                builder.setNegativeButton("MISSED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adb.execSQL("update attendance SET present=0 where present='-1' AND subject='" + arec.getSubject() + "'AND hour='" + arec.getHr() + "'AND minutes='" + arec.getMin() + "'AND day='" + arec.getDay() + "'AND month='" + arec.getMonth() + "'AND year='" + arec.getYear() + "'"); //mark_attendance(sub, year, month+1, day, hr, min, present);
                      Log.d("showing arec ", "update attendance SET present=0 where present='-1' AND subject='" + arec.getSubject() + "'AND hour='" + arec.getHr() + "'AND minutes='" + arec.getMin() + "'AND day='" + arec.getDay() + "'AND month='" + arec.getMonth() + "'AND year='" + arec.getYear() + "'"); //mark_attendance(sub, year, month+1, day, hr, min, present);

                        record.remove(position);
                        addap = new attendanceadapter(record, getApplicationContext());
                        dialog.cancel();

                    }

                });
                builder.setPositiveButton("Present", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adb.execSQL("update attendance SET present=1 where present='-1' AND subject='" + arec.getSubject() + "'AND hour='" + arec.getHr() + "'AND minutes='" + arec.getMin() + "'AND day='" + arec.getDay() + "'AND month='" + arec.getMonth() + "'AND year='" + arec.getYear() + "'"); //mark_attendance(sub, year, month+1, day, hr, min, present);
                        record.remove(position);
                        addap = new attendanceadapter(record, getApplicationContext());
                        dialog.cancel();
                    }
                });
                builder.setMessage("attendance");
                builder.show();
                lv.setAdapter(addap);

                mark_attendance();
            }


        });

    }

/*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                s = parent.getItemAtPosition(position).toString();
                sp = s.split("  ");
                Log.d(" yaaa", sp[0] + sp[1] + sp[2] + sp[3] + sp[4] + sp[5]);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("mark your attendance");
                builder.setCancelable(true);
                builder.setNegativeButton("MISSED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adb.execSQL("update attendance SET present=0 where present='-1' AND subject='" + sp[3] + "'AND hour='" + sp[4] + "'AND minutes='" + sp[5] + "'AND day='" + sp[2] + "'AND month='" + sp[1] + "'AND year='" + sp[0] + "'"); //mark_attendance(sub, year, month+1, day, hr, min, present);
                        adap.remove(s);
                        dialog.cancel();

                    }

                });
                builder.setPositiveButton("Present", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adb.execSQL("update attendance SET present=1 WHERE present='-1' AND subject='" + sp[3] + "' AND hour='" + sp[4] + "'AND minutes= '" + sp[5] + "' AND day='" + sp[2] + "' AND month='" + sp[1] + "' AND year='" + sp[0] + "'"); //mark_attendance(sub, year, month+1, day, hr, min, present);
                        adap.remove(s);
                        dialog.cancel();
                    }
                });
                builder.setMessage("attendance");
                builder.show();
                lv.setAdapter(adap);


            }
        });
        }*/












    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_timetable) {

            sharedPreferences=getSharedPreferences("start_date", Context.MODE_PRIVATE);
            if(sharedPreferences.getInt("day",-1)==-1){
                Intent intent=new Intent(getApplicationContext(),newsemdate.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, add_data.class);
                startActivity(intent);
            }
            // Handle the camera action


        } else if (id == R.id.timetable_menu) {
            Intent intent = new Intent(this, TIMETABLEVIEW.class);
            startActivity(intent);


        } else if (id == R.id.newsem) {
            alertDialog=new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("DELETE ON GOING SEM?");
            alertDialog.setMessage("This will delete all the data of thus sem");
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(getApplicationContext(), newsemdate.class);
                    startActivity(i);

                   }
            });
            alertDialog.show();
        } else if (id == R.id.record) {
            Intent intent = new Intent(this, attendance_record.class);
            startActivity(intent);


        } else if (id == R.id.ajay_chowdhary) {
            Intent intent = new Intent(this, contact_us.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
