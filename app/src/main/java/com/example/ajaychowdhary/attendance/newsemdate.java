package com.example.ajaychowdhary.attendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class newsemdate extends AppCompatActivity {
    dbHelper dbhelper;
    int day,month,year;
    Intent intent;
    SharedPreferences sp,sp_for_last_attended_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        DatePicker datePicker=(DatePicker)findViewById(R.id.datePicker);
         month=datePicker.getMonth();
         year=datePicker.getYear();
         day=datePicker.getDayOfMonth();
        sp=getSharedPreferences("start_date", Context.MODE_PRIVATE);
        Button b=(Button)findViewById(R.id.button_for_newsem);
        b.setText("create new sem");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("day", day);
                editor.putInt("month", month);
                editor.putInt("year", year);
                editor.commit();


                sp_for_last_attended_class = getSharedPreferences("last_attended_class", Context.MODE_PRIVATE); ///set last attended sharedprefrence to default;
                SharedPreferences.Editor editor1 = sp_for_last_attended_class.edit();
                editor1.putInt("day", day);
                editor1.putInt("year", year);
                editor1.putInt("month", month);
                editor1.putInt("hour", 0);
                editor1.putInt("min", 1);
                editor1.commit();

                SQLiteDatabase db;                      //to delete the current sem table
                dbhelper = new dbHelper(getApplicationContext());
                db = dbhelper.getWritableDatabase();
                dbhelper.onUpgrade(db, 1, 1);
                Toast.makeText(getApplicationContext(), "DELETED OLD SEM AND CREATED NEW ONE", Toast.LENGTH_LONG).show();

                intent = new Intent(getApplicationContext(), add_data.class);
                startActivity(intent);
                finish();

            }
        });


    }


}
