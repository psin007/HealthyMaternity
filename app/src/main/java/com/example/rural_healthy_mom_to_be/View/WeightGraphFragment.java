package com.example.rural_healthy_mom_to_be.View;

import android.arch.persistence.room.Room;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LinearRegression;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.graphics.pdf.PdfDocument.PageInfo;


public class WeightGraphFragment extends Fragment {
    View vWeightGraph;
    TextView vWeightHeader;
    Button btnGenerate;
    LoggedInUserDb loggedInUserdb;
    private LineChart chart;
    private String [] upperRange;
    private String [] lowerRange;
    private float bmi;
    LoggedinUser currentUser;

    ArrayList<Entry>lineEntries1 = new ArrayList<>();
    ArrayList<Entry>lineEntries2 = new ArrayList<>();
    ArrayList<Entry>lineEntries3 = new ArrayList<>();
    ArrayList<Entry>lineEntries4 = new ArrayList<>();


    float PRE_WEIGHT;
    int curWeek;
    float currentWeight;
    float currentHeight;

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vWeightGraph = inflater.inflate(R.layout.weightgraphfrgment, container, false);
        vWeightHeader = vWeightGraph.findViewById(R.id.graphWeight);
        btnGenerate = vWeightGraph.findViewById(R.id.genReport);
        loggedInUserdb = Room.databaseBuilder(vWeightGraph.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();

        //read SQLite database
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        //use async to get range
        getRange getRangeVal = new getRange();
        getRangeVal.execute();

        //Initialize the chart
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
        xAxis.setLabelCount(10, true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((float) 40);
        xAxis.setDrawGridLines(true);

        Description description = new Description();
        description.setText("Expected Weight Each Week");
        description.setEnabled(true);
        chart.setDescription(description);
        chart.animateXY(500,500);
        //refresh
        chart.invalidate();

        //Generate graph
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                createPdf(vWeightHeader.getText().toString());
            }
        });

        return vWeightGraph;
    }

    private void createPdf(String text){
        //Create a new document
        PdfDocument doc = new PdfDocument();

        //create a page description
        PageInfo pageInfo = new PdfDocument.PageInfo.Builder(vWeightGraph.getMeasuredWidth(),
                vWeightGraph.getMeasuredHeight(), 1).create();

        // start a page
        PdfDocument.Page page = doc.startPage(pageInfo);
        vWeightGraph.draw(page.getCanvas());
        doc.finishPage(page);

        //write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        String curdate = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());
        String targetPdf = directory_path+"WeightGraph"+ curdate +".pdf";
        File filePath = new File(targetPdf);
        try {
            doc.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this.getContext(), "The report has been written to "+directory_path, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this.getContext(), "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        doc.close();

    }

    private void setUpperRange(){
        LineDataSet ds1 = new LineDataSet(lineEntries1, "max weight");

        ds1.setDrawValues(false);
        ds1.setColor(Color.parseColor("#F15A4A"));
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
        ds2.setColor(Color.parseColor("#495af2"));
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
        ds3.setDrawValues(true);
        ds3.setColor(Color.parseColor("#e08b4a"));
        ds3.setDrawCircles(true);
        ds3.setCircleRadius(10f);
        ds3.setCircleHoleRadius(5f);
        ds3.setCircleColor(Color.parseColor("#e08b4a"));
        ds3.setLineWidth(4f);
        ds3.setValueTextSize(10f);
        ds3.setFormLineWidth(2f);
        ds3.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        ds3.setFormSize(15.f);
        dataSets.add(ds3);
    }

    private void calLinearRegFunction(){

        double[] x = {4, 8, 12, 16, 20};
        double[] y = {69.85, 69.98, 70.01, 70.15, 71.12};
        LinearRegression reg = new LinearRegression(x,y);
        for(int i = 10;i <= 40;i++)
        {
            double py = i*reg.slope() + reg.intercept();
            float yy = (float) py;
            lineEntries4.add(new Entry(i,yy));
        }
    }

    private void preUsrWeight(){

        calLinearRegFunction();

        LineDataSet ds4 = new LineDataSet(lineEntries4, "Predication Line");
        ds4.setDrawValues(true);
        ds4.setColor(Color.parseColor("#48fa5d"));
        ds4.setDrawCircles(false);
//        ds4.setCircleRadius(10f);
//        ds4.setCircleHoleRadius(5f);
//        ds4.setCircleColor(Color.parseColor("#e08b4a"));
        ds4.setLineWidth(2f);
        ds4.setValueTextSize(10f);
        ds4.setFormLineWidth(2f);
        ds4.enableDashedLine(15f,10f,2f);
        ds4.setFormSize(15.f);
        dataSets.add(ds4);
    }


    public class getRange extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String response = null;

            try {
                if(bmi < 18.5)
                {
                    upperRange = RestClient.getWeightGainForUnderWeight("max");
                    lowerRange = RestClient.getWeightGainForUnderWeight("min");
                }
                if((bmi >= 18.5) && (bmi < 25))
                {
                    upperRange = RestClient.getWeightGainForNorWeight("max");
                    lowerRange = RestClient.getWeightGainForNorWeight("min");
                }
                if((bmi >=25)&&(bmi < 30))
                {
                    upperRange = RestClient.getWeightGainForOverWeight("max");
                    lowerRange = RestClient.getWeightGainForOverWeight("min");
                }
                else
                {
                    upperRange = RestClient.getWeightGainForObesity("max");
                    lowerRange = RestClient.getWeightGainForObesity("min");
                }

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
//                chart.invalidate();
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
           currentHeight = (float)details.getHeightInCm();
           PRE_WEIGHT = (float)details.getWeightBeforePregnancy();
           curWeek = details.getCurrentWeek();
           String cwt = String.valueOf(currentWeight);
            vWeightHeader.setText(cwt + " kg");
            bmi = PRE_WEIGHT * 10000/(currentHeight*currentHeight);

            lineEntries3.add(new Entry(curWeek,currentWeight));
            setUsrWeight();
            preUsrWeight();
            chart.notifyDataSetChanged();
        }
    }

}