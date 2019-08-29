package com.example.rural_healthy_mom_to_be.View;
import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;
import com.example.rural_healthy_mom_to_be.Repository.RestClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WeightGraphFragment extends Fragment {
    View vWeightGraph;
    LoggedInUserDb loggedInUserdb;
    private LineChart chart;
    private String [] upperRange;
    private String [] lowerRange;
    LoggedinUser currentUser;

    ArrayList<Entry>lineEntries1 = new ArrayList<>();
    ArrayList<Entry>lineEntries2 = new ArrayList<>();
    ArrayList<Entry>lineEntries3 = new ArrayList<>();

    float PRE_WEIGHT;
    int curWeek;
    float currentWeight;

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vWeightGraph = inflater.inflate(R.layout.weightgraphfrgment, container, false);

        loggedInUserdb = Room.databaseBuilder(vWeightGraph.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();

        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
        //get values from dataset

        //use asy to get range
        getRange getRangeVal = new getRange();
        getRangeVal.execute();


        chart = vWeightGraph.findViewById(R.id.weight_line_chart);
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        chart.setPinchZoom(true);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(true);

        //draw at background
        chart.setDrawGridBackground(true);

        chart.setDrawBorders(true);

        chart.setNoDataText("Please wait a few seconds...");

        //get X axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(0.5f);
//        //replace 3
        xAxis.setLabelCount(10, true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((float) 40);
        xAxis.setDrawGridLines(true);

        Description description = new Description();
        description.setText("Expected Weight Each Week");
        description.setEnabled(true);
        chart.setDescription(description);

        //refresh
        chart.invalidate();

        return vWeightGraph;
    }

    private void setUpperRange(){
        LineDataSet ds1 = new LineDataSet(lineEntries1, "max weight");

        ds1.setDrawValues(false);
        ds1.setColor(Color.parseColor("#495af2"));
        ds1.setDrawCircles(false);
        ds1.setLineWidth(4f);
        ds1.setValueTextSize(10f);
        ds1.setFormLineWidth(2f);
        ds1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        ds1.setFormSize(15.f);
        dataSets.add(ds1);
    }

    private void setLowerRange(){
        LineDataSet ds2 = new LineDataSet(lineEntries2, "min weight");

        ds2.setDrawValues(false);
        ds2.setColor(Color.parseColor("#F15A4A"));
        ds2.setDrawCircles(false);
        ds2.setLineWidth(4f);
        ds2.setValueTextSize(10f);
        ds2.setFormLineWidth(2f);
        ds2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        ds2.setFormSize(15.f);
        dataSets.add(ds2);
    }

    private void setUsrWeight(){
        LineDataSet ds3 = new LineDataSet(lineEntries3, "current weight");

        ds3.setDrawValues(false);
        ds3.setColor(Color.parseColor("#3dd8db"));
        ds3.setDrawCircles(true);
        ds3.setCircleRadius(5f);
        ds3.setCircleHoleRadius(3f);
        ds3.setLineWidth(4f);
        ds3.setValueTextSize(10f);
        ds3.setFormLineWidth(2f);
        ds3.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        ds3.setFormSize(15.f);
        dataSets.add(ds3);
    }

    public class getRange extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            String response = null;

            try {
                upperRange = RestClient.getWeightGainForUnderWeight("max");
                lowerRange = RestClient.getWeightGainForUnderWeight("min");

                int xaxis = 0;
                for(String val:upperRange)
                {
                    float yaxis = Float.parseFloat(val) + PRE_WEIGHT;
                    lineEntries1.add(new BarEntry(xaxis,yaxis));
                    xaxis+=1;
                }
                setUpperRange();

                xaxis = 0;
                for(String val:lowerRange)
                {
                    float yaxis = Float.parseFloat(val) + PRE_WEIGHT;
                    lineEntries2.add(new BarEntry(xaxis,yaxis));
                    xaxis+=1;
                }
                setLowerRange();
                LineData data = new LineData(dataSets);
                chart.setData(data);

                //refresh
                chart.invalidate();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "200 OK!";
        }
        @Override
        protected void onPostExecute(String response) { }
    }

    private class ReadDatabase extends AsyncTask<Void, Void, LoggedinUser> {


        @Override
        protected LoggedinUser doInBackground(Void... voids) {
            List<LoggedinUser> userList = loggedInUserdb.loggedInUserDao().getAll();
            currentUser = userList.get(0);
            return userList.get(0);
        }
        protected void onPostExecute(LoggedinUser details) {
           currentWeight = (float)details.getCurrentWeight();
           PRE_WEIGHT = (float)details.getWeightBeforePregnancy();
           curWeek = details.getCurrentWeek();

           lineEntries3.add(new BarEntry(curWeek,currentWeight));
           setUsrWeight();
            chart.invalidate();
        }
    }

}