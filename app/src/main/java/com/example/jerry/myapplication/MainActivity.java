package com.example.jerry.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final int MSG_ERROR = -1;
    private static final int MSG_CONNECTED = 1;
    private static final int MSG_DISCONNECTED = 2;

    private GoogleApiClient client;
    private DateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private ArrayList<String> list =  new ArrayList<>();

    public static int now = 4;
    public static String[] status = {"Reading", "Watching TV", "Using Smart Phone", "Using Computer", "Relaxing"};
    public static int[] cnt = {0,0,0,0,0};
    public static int min = 30;
    public static int sec = 0;

    public static int total_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView t01 = (TextView) this.findViewById(R.id.T01);
        final TextView countdown = (TextView) this.findViewById(R.id.countdown);
        final TextView status = (TextView) this.findViewById(R.id.status);
        final TextView bright = (TextView) this.findViewById(R.id.bright);
        final TextView distance = (TextView) this.findViewById(R.id.distance);

        final ImageView cloud = (ImageView) this.findViewById(R.id.cloud);
        final ImageView clock = (ImageView) this.findViewById(R.id.clock);
        final ImageView sleep = (ImageView) this.findViewById(R.id.sleeping);


        cloud.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent history = new Intent(MainActivity.this, History.class);
                history.putExtra("history",list);
                startActivity(history);
            }
        });

        clock.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent history = new Intent(MainActivity.this, settime.class);
                startActivity(history);
            }
        });


        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if(msg.obj.equals("TU")){
                            list.add("Time's up : " + t01.getText());
                            list.add(dateFormat.format(new Date()));
                            Intent goalert = new Intent(MainActivity.this, timesup.class);
                            startActivity(goalert);
                            break;
                        }
                        if(msg.obj.equals("GG")){
                            list.add("Too Close");
                            list.add(dateFormat.format(new Date()));
                            Intent goalert = new Intent(MainActivity.this, Alert.class);
                            startActivity(goalert);
                            break;
                        }
                        int val = Integer.parseInt(msg.obj.toString());
                        if(val>=0 && val<=4) {
                            if(val != 4){
                                sleep.setVisibility(View.INVISIBLE);
                                countdown.setVisibility(View.VISIBLE);
                            }
                            else{
                                countdown.setVisibility(View.INVISIBLE);
                                sleep.setVisibility(View.VISIBLE);
                            }
                            total_time = 0;
                            MainActivity.this.now = val;
                            t01.setText(MainActivity.this.status[val]);
                        }
                        break;
                    case MSG_CONNECTED:
                        status.setText("Connected");
                        break;
                    case MSG_DISCONNECTED:
                        status.setText("Disconnected");
                        break;
                    case MSG_ERROR:
                        status.setText(msg.obj.toString());
                        break;
                    default:
                        status.setText("WTF is this message");
                        break;
                }

            }
        };

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Socket s = new Socket("linux1.csie.org", 2887);
                     BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                    handler.sendEmptyMessage(MSG_CONNECTED);
                    String line;
                    while ((line = br.readLine()) != null)
                        handler.sendMessage(handler.obtainMessage(0, line));
                    handler.sendEmptyMessage(MSG_DISCONNECTED);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendMessage(handler.obtainMessage(MSG_ERROR, e));
                }
            }
        });
        t.start();

        // set timer

        final Handler setrandom = new Handler(Looper.getMainLooper()) {
            private double bri_now = 1.0;
            private double dis_now = 2.5;
            private Random ran = new Random();
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        this.bri_now += (ran.nextDouble()-0.5)/100;
                        if(bri_now < 0) bri_now = 0;
                        if(bri_now > 2) bri_now = 2;
                        this.dis_now += (ran.nextDouble()-0.5)/100;
                        if(dis_now < 0) dis_now = 0;
                        if(dis_now > 5) dis_now = 5;

                        bright.setText("Bright : " + String.format("%.3f", bri_now));
                        distance.setText("Distance : " + String.format("%.3f", dis_now) + " m");
                        break;
                    case 2:
                        cnt[now] ++;
                        int res = min * 60 + sec - total_time;
                        int nmin = res / 60;
                        int nsec = res % 60;
                        countdown.setText(String.format("%02d : %02d",nmin,nsec));
                        if(res == 0){
                            list.add("Time's up : " + t01.getText());
                            list.add(dateFormat.format(new Date()));
                            Intent goalert = new Intent(MainActivity.this, timesup.class);
                            startActivity(goalert);
                            MainActivity.total_time = -120;
                            break;
                        }
                        if(t01.getText().equals("Relaxing")) break;
                        total_time ++;
                        break;
                    default:
                        break;
                }
            }
        };

        Timer timer1 = new Timer(true);
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                setrandom.sendEmptyMessage(1);
            }
        }, 0, 100);

        Timer timer2 = new Timer(true);
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                setrandom.sendEmptyMessage(2);
            }
        }, 0, 1000);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
