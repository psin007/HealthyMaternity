package com.example.rural_healthy_mom_to_be.View;
import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.Model.Weight;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LinearRegression;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;
import com.example.rural_healthy_mom_to_be.Repository.RestClient;
import com.github.mikephil.charting.animation.Easing;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.graphics.pdf.PdfDocument.PageInfo;


public class WeightGraphFragment extends Fragment {
    View vReport;
    View vWeightGraph;
    View vWeightTracker;
    LoggedInUserDb loggedInUserdb;
    List<HashMap<String, String>> listArray;
    List<LoggedinUser> userList;
    List<Weight> weightList;
    ListView weightLV;
    ImageView shareBtn;
    private LineChart chart;
    private String [] upperRange;
    private String [] lowerRange;
    private String userName;
    private float bmi;
    LoggedinUser currentUser;
    HashMap<String,String> map;
    Context context;
    TextView tvTrackerCurrentWeight;

    ArrayList<Entry>lineEntries1 = new ArrayList<>();
    ArrayList<Entry>lineEntries2 = new ArrayList<>();
    ArrayList<Entry>lineEntries3 = new ArrayList<>();
    ArrayList<Entry>lineEntries4 = new ArrayList<>();

    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };


    float PRE_WEIGHT;
    float currentWeight;
    float currentHeight;

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vReport = inflater.inflate(R.layout.activity_report, container, false);
        vWeightGraph = inflater.inflate(R.layout.weightgraphfrgment, container,false);
        vWeightTracker = inflater.inflate(R.layout.weighttrackerfragment,container,false);

        context = vReport.getContext();
        weightLV = vReport.findViewById(R.id.listView);
        listArray = new ArrayList<>();

        tvTrackerCurrentWeight = vReport.findViewById(R.id.trackerWeight);
        shareBtn = vReport.findViewById(R.id.imageShare);
        loggedInUserdb = Room.databaseBuilder(vReport.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();

        checkPermissions();

        //read SQLite database
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        //use async to get range
        getRange getRangeVal = new getRange();
        getRangeVal.execute();

        //Initialize the chart
        chart = vReport.findViewById(R.id.weight_line_chart);
        //chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(true);

        //draw at background
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
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
        chart.animateX(2500, Easing.EaseInExpo);
        chart.getAxisRight().setEnabled(false);
        //refresh
        chart.invalidate();

        //Generate graph
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });

        return vReport;
    }

    private void createPdf(){
        //Create a new document
        PdfDocument doc = new PdfDocument();
        String curdate = new SimpleDateFormat("EEE dd-MMM-yyyy hh-mm aaa").format(new Date());

        //create a page description
        PageInfo pageInfo = new PdfDocument.PageInfo.Builder(vReport.getMeasuredWidth(),
                vReport.getMeasuredHeight(), 1).create();

        // start a page
        PdfDocument.Page titlePage = doc.startPage(pageInfo);
        Canvas canvas = titlePage.getCanvas();
        int titleBaseLine = 72;
        int leftMargin = 54;
        int rowLine = 900;
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(80);
        canvas.drawText("The Weight Log Report",leftMargin,titleBaseLine,paint);
        paint.setTextSize(60);
        canvas.drawText("User name: " + userName, leftMargin,titleBaseLine+80, paint);
        paint.setTextSize(50);
        canvas.drawText("Generated date&time: " + curdate, leftMargin,titleBaseLine+160, paint);

        paint.setTextSize(60);
        canvas.drawText("Personal Information: ", leftMargin,titleBaseLine+300, paint);

        paint.setTextSize(50);
        canvas.drawLine(leftMargin, titleBaseLine+350, rowLine,titleBaseLine+350,paint);
        canvas.drawText("Height", leftMargin, titleBaseLine + 420 ,paint);
        canvas.drawText(currentUser.getHeightInCm()+" cm", leftMargin+500, titleBaseLine + 420 ,paint);

        canvas.drawLine(leftMargin, titleBaseLine+450, rowLine,titleBaseLine+450,paint);
        canvas.drawText("Current Weight", leftMargin, titleBaseLine + 520 ,paint);
        canvas.drawText(currentUser.getCurrentWeight()+" kg", leftMargin+500, titleBaseLine + 520 ,paint);

        canvas.drawLine(leftMargin, titleBaseLine+550, rowLine,titleBaseLine+550,paint);
        canvas.drawText("Pre-pregnancy Weight", leftMargin, titleBaseLine + 620 ,paint);
        canvas.drawText(currentUser.getWeightBeforePregnancy()+" kg", leftMargin+500, titleBaseLine + 620 ,paint);

        canvas.drawLine(leftMargin, titleBaseLine+650, rowLine,titleBaseLine+650,paint);
        canvas.drawText("Current Week", leftMargin, titleBaseLine + 720 ,paint);
        canvas.drawText("Week "+currentUser.getCurrentWeek(), leftMargin+500, titleBaseLine + 720 ,paint);

        canvas.drawLine(leftMargin, titleBaseLine+750, rowLine,titleBaseLine+750,paint);
        canvas.drawText("BMI Class", leftMargin, titleBaseLine + 820 ,paint);
        canvas.drawText(currentUser.getBMIClass(), leftMargin+500, titleBaseLine + 820 ,paint);
        canvas.drawLine(leftMargin, titleBaseLine+850, rowLine,titleBaseLine+850,paint);
        doc.finishPage(titlePage);

        PdfDocument.Page page = doc.startPage(pageInfo);
        vReport.draw(page.getCanvas());
        doc.finishPage(page);

        PdfDocument.Page page2 = doc.startPage(pageInfo);
        int addSpace = 150;
        canvas=page2.getCanvas();
        paint.setTextSize(70);
        canvas.drawText("Weight Log",leftMargin,titleBaseLine,paint);
        canvas.drawLine(leftMargin, titleBaseLine+50, rowLine,titleBaseLine+50,paint);

        paint.setTextSize(50);
        for(Weight weight : weightList)
        {
            canvas.drawText("Week "+weight.getWeek(), leftMargin, titleBaseLine + (addSpace-30) ,paint);
            canvas.drawText(weight.getWeight()+" kg", leftMargin+500, titleBaseLine + (addSpace-30) ,paint);
            canvas.drawLine(leftMargin,titleBaseLine+addSpace,rowLine,titleBaseLine+addSpace,paint);

            addSpace+=100;
        }
        doc.finishPage(page2);

        //write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MyWeightReport/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        String targetPdf = directory_path+"WeightGraph "+ curdate +".pdf";
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

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File output = new File(targetPdf);
//        Uri uri = FileProvider.getUriForFile(
//                this.getActivity().getApplicationContext(),
//                this.getContext().getPackageName() + ".provider", output);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Uri uri = Uri.fromFile(output);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(shareIntent);
    }

    public static Bitmap drawToBitmap(Context context,final int layoutResId,
                                      final int width,final int height)
    {
        final Bitmap bmp = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        final LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(layoutResId,null);
        layout.setDrawingCacheEnabled(true);
        layout.measure(
                View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY));
        layout.layout(0,0,layout.getMeasuredWidth(),layout.getMeasuredHeight());
        canvas.drawBitmap(layout.getDrawingCache(),0,0,new Paint());
        return bmp;
    }
    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this.getActivity(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this.getActivity(), permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this.getActivity(), "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        this.getActivity().finish();
                        return;
                    }
                }
                // all permissions were granted

                //initialize();
                break;
        }
    }

    private void setUpperRange(){
        LineDataSet ds1 = new LineDataSet(lineEntries1, "max weight");

        ds1.setDrawValues(false);
        ds1.setColor(Color.parseColor("#F15A4A"));
        ds1.enableDashedLine(10f, 5f, 0f);
        ds1.enableDashedHighlightLine(10f, 5f, 0f);
        ds1.setDrawCircles(false);
        ds1.setLineWidth(2f);
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
        ds2.enableDashedLine(10f, 5f, 0f);
        ds2.enableDashedHighlightLine(10f, 5f, 0f);
        ds2.setDrawCircles(false);
        ds2.setLineWidth(2f);
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
        ds3.setCircleRadius(5f);
        ds3.setCircleHoleRadius(2.5f);
        ds3.setCircleColor(Color.parseColor("#e08b4a"));
        ds3.setLineWidth(2f);
        ds3.setValueTextSize(7f);
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

//    private void preUsrWeight(){
//
//        calLinearRegFunction();
//
//        LineDataSet ds4 = new LineDataSet(lineEntries4, "Predication Line");
//        ds4.setDrawValues(true);
//        ds4.setColor(Color.parseColor("#48fa5d"));
//        ds4.setDrawCircles(false);
////        ds4.setCircleRadius(10f);
////        ds4.setCircleHoleRadius(5f);
////        ds4.setCircleColor(Color.parseColor("#e08b4a"));
//        ds4.setLineWidth(2f);
//        ds4.setValueTextSize(10f);
//        ds4.setFormLineWidth(2f);
//        ds4.enableDashedLine(15f,10f,2f);
//        ds4.setFormSize(15.f);
//        dataSets.add(ds4);
//    }


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
            userList = loggedInUserdb.loggedInUserDao().getAll();
            weightList = loggedInUserdb.weightDao().getAll();
            currentUser = userList.get(0);
            return userList.get(0);
        }
        protected void onPostExecute(LoggedinUser details) {

            //my code
           currentWeight = (float)details.getCurrentWeight();
           currentHeight = (float)details.getHeightInCm();
           PRE_WEIGHT = (float)details.getWeightBeforePregnancy();
           userName = details.getUsername();
           bmi = PRE_WEIGHT * 10000/(currentHeight*currentHeight);

            bubbleSort();

            setUsrWeight();
            //preUsrWeight();
            chart.notifyDataSetChanged();
            chart.invalidate();
        }
    }

    public void bubbleSort()
    {
        int i,j;
        int n = weightList.size();
        for(i=0;i<n-1;i++)
            for(j=0;j<n-i-1;j++)
            {
                if(weightList.get(j).getWeek()>weightList.get(j+1).getWeek())
                {
                    Collections.swap(weightList,j,(j+1));
                }
            }

        for(Weight newUser : weightList)
        {
            lineEntries3.add(new Entry(newUser.getWeek(),newUser.getWeight().floatValue()));
        }

    }

}