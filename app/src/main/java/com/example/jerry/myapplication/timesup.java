package com.example.jerry.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class timesup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesup);
        final TextView back = (TextView) this.findViewById(R.id.back);
        final Timer timer = new Timer(true);
        back.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finish();
            }
        });
        final Handler dovib = new Handler(Looper.getMainLooper()) {
            private Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            @Override
            public void handleMessage(Message msg) {
                System.out.println("123");
                vib.vibrate(1000);
            }
        };



        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dovib.sendEmptyMessage(1);
            }
        }, 0, 2000);
    }
}
