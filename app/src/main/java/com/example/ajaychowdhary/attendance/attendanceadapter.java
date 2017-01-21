package com.example.ajaychowdhary.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AJAY CHOWDHARY on 05-07-2016.
 *
 */

public class attendanceadapter extends BaseAdapter {
ArrayList<attendance>record;
    LayoutInflater layoutInflater;
    Context context;

    public attendanceadapter(ArrayList<attendance> record, Context context) {
        this.record = record;
        this.context = context;
    }

    @Override
    public int getCount() {
        return record.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            layoutInflater=LayoutInflater.from(context);
        LinearLayout linearLayout=(LinearLayout)layoutInflater.inflate(R.layout.pending_attendance_content,parent,false);
        TextView subject=(TextView)linearLayout.findViewById(R.id.pending_subject);
        TextView day=(TextView)linearLayout.findViewById(R.id.day_string);
        TextView time=(TextView)linearLayout.findViewById(R.id.time);

        attendance tt=record.get(position);
        if(tt.getSubject()=="ALL ATTENDANCE IS MARKED")
          subject.setText(tt.getSubject());
        else{
            subject.setText(tt.getSubject());
            if (tt.getMonth() == 0 && tt.getYear() == 0) {
                day.setText("DAY:" + tt.getDay());
            }
            day.setText("DAY: " + tt.getDay_string());
            time.setText(tt.getHr() + ":" + tt.getMin());
            TextView suggestion = (TextView) linearLayout.findViewById(R.id.suggestion);
            suggestion.setText("attended: " + attendancedbhelper.getpercent(tt.getSubject()) + "%");
        }
            return linearLayout;
    }
}
