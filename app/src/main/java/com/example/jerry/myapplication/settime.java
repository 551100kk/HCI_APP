package com.example.jerry.myapplication;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;

public class settime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);

        final TextView back = (TextView) this.findViewById(R.id.back);
        final Spinner mm = (Spinner) this.findViewById(R.id.mm);
        final Spinner ss = (Spinner) this.findViewById(R.id.ss);

        back.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.min = Integer.parseInt(mm.getSelectedItem().toString());
                MainActivity.sec = Integer.parseInt(ss.getSelectedItem().toString());
                MainActivity.total_time = 0;
                finish();
            }
        });
        // set mm

        final ArrayList<String> mm_arr = new ArrayList<>();
        for(int i=0;i<=90;i++){
            mm_arr.add(Integer.toString(i));
        }
        ArrayAdapter<String> mm_list = new ArrayAdapter<>(settime.this,
                android.R.layout.simple_spinner_dropdown_item,
                mm_arr);
        mm.setAdapter(mm_list);
        mm.setSelection(MainActivity.min);


        //set ss

        final ArrayList<String> ss_arr = new ArrayList<>();
        for(int i=0;i<=59;i++){
            ss_arr.add(Integer.toString(i));
        }
        ArrayAdapter<String> ss_list = new ArrayAdapter<>(settime.this,
                android.R.layout.simple_spinner_dropdown_item,
                ss_arr);
        ss.setAdapter(ss_list);
        ss.setSelection(MainActivity.sec);

    }

}
