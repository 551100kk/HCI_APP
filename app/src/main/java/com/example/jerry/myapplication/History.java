package com.example.jerry.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

class Pack {
    String thing;
    String time;

    Pack(String name, String desc) {
        thing = name;
        time = desc;
    }
}

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //list
        ArrayList<String> test = getIntent().getStringArrayListExtra("history");
        ListView mList = (ListView) findViewById(R.id.history);
        ArrayList<Pack> arr = new ArrayList<>();
        for (int i=0;i<test.size();i+=2) {
            arr.add(new Pack(test.get(i), test.get(i + 1)));
        }
        ListAdapter mAdapter =
                new ArrayAdapter<Pack>(this,
                        android.R.layout.simple_list_item_2,
                        android.R.id.text1,
                        arr) {
                    @Override
                    public View getView(int pos, View convert, ViewGroup group) {
                        View v = super.getView(pos, convert, group);
                        TextView t1 = (TextView) v.findViewById(android.R.id.text1);
                        TextView t2 = (TextView) v.findViewById(android.R.id.text2);
                        t1.setTextSize(16);
                        t1.setTypeface(null, Typeface.BOLD);
                        t2.setTextColor(0xFF777777);
                        t1.setText(getItem(pos).thing);
                        t2.setText(getItem(pos).time);
                        return v;
                    }
                };
        mList.setAdapter(mAdapter);

        //pie chart
        PieChart pieChart = (PieChart) findViewById(R.id.pie);
        pieChart.setRotationEnabled(false);
        Description des = new Description();
        des.setText("in seconds");
        pieChart.setDescription(des);
        pieChart.setHoleRadius(7);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setDrawCenterText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setExtraOffsets(30,0,0,0);


        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        for(int i=0;i<5;i++){
            yEntrys.add(new PieEntry(MainActivity.cnt[i], MainActivity.status[i]));
        }


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setSelectionShift(10.0f);
        pieDataSet.setValueLinePart1OffsetPercentage(100.f);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.GREEN);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setXOffset(-15.f);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        System.out.println("-------------------------GG------------");
        for(int i=0;i<5;i++){
            System.out.println(MainActivity.cnt[i]);
        }
    }
}
