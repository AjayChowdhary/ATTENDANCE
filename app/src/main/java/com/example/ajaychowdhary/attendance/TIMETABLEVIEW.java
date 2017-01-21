package com.example.ajaychowdhary.attendance;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.LogRecord;

public class TIMETABLEVIEW extends AppCompatActivity {

    dbHelper dbhelper;
    SQLiteDatabase db;
    timetableadapter addap;
    HashMap<String, List<String>> hashMap1;
    List<String> list, daylist;
    String subject;
    String[] days_name = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    int [] percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetableview);
        daylist = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            daylist.add(days_name[i]);
        }

         hashMap1 = new HashMap<String, List<String>>();

        dbhelper = new dbHelper(getApplicationContext());
        db = dbhelper.getWritableDatabase();


        for (int i = 1, j; i < 8; i++) {
            list = new ArrayList<String>();
            j = i - 1;
            Cursor c = db.rawQuery("select * from timetable where day_of_week='" + i + "'", null);
            while (c.moveToNext()) {

                subject = c.getString(c.getColumnIndex("subject"));
                Log.d("timetableitem", subject + " " + c.getInt(c.getColumnIndex("hour_of_day")) + ":" + c.getInt(c.getColumnIndex("minutes")));
                list.add(subject + "         " + c.getInt(c.getColumnIndex("hour_of_day")) + ":" + c.getInt(c.getColumnIndex("minutes")));
            }

            Log.d("listsize", list.size() + "");
            hashMap1.put(daylist.get(j), list);
            Log.d("listArrayListsize", hashMap1.get("Sunday") + "");

        }
        addap = new timetableadapter(this, daylist, hashMap1,0);
        ExpandableListView lv = (ExpandableListView) findViewById(R.id.ExpandablelistViewfortimetable);
        lv.setAdapter(addap);

        // Log.d("removingaaaaa ", list_hashMap.get(0).get("Monday")+"");

        lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        lv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        daylist.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

    }

}
