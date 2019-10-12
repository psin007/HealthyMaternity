package com.example.rural_healthy_mom_to_be.View;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.Model.Weight;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;
import com.example.rural_healthy_mom_to_be.Repository.RestClient;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
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
    CardView cdGraph;
    LoggedInUserDb loggedInUserdb;
    List<HashMap<String, String>> listArray;
    List<LoggedinUser> userList;
    List<Weight> weightList;
    ListView weightLV;
    FloatingActionButton shareBtn;
    private LineChart chart;
    private String [] upperRange;
    private String [] lowerRange;
    private String userName;
    private float bmi;
    LoggedinUser currentUser;
    HashMap<String,String> map;
    Context context;
    TextView tvTrackerCurrentWeight;
    FloatingActionButton fab_add;

    ArrayList<Entry>lineEntries1 = new ArrayList<>();
    ArrayList<Entry>lineEntries2 = new ArrayList<>();
    ArrayList<Entry>lineEntries3 = new ArrayList<>();

    SimpleAdapter myListAdapter;
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
        fab_add = vReport.findViewById(R.id.fab);
        listArray = new ArrayList<>();
        shareBtn =vReport.findViewById(R.id.fab_share);
        cdGraph = vReport.findViewById(R.id.cd_graph);

        tvTrackerCurrentWeight = vReport.findViewById(R.id.trackerWeight);
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

        //draw at background
        //chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setNoDataText("Please wait a few seconds...");
        chart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
        chart.getXAxis().setTextColor(Color.WHITE);

        chart.getDescription().setEnabled(false);

        //get X axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(0.5f);
        xAxis.setLabelCount(10, true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((float) 40);
        xAxis.setDrawGridLines(true);

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

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewWeight(view);
            }
        });

        weightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //show the input box
                showInputBox(String.valueOf(currentUser.getCurrentWeight()),(double) position);
                myListAdapter.notifyDataSetChanged();
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
        canvas.drawText("Date&Time: " + curdate, leftMargin,titleBaseLine+160, paint);

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
        cdGraph.draw(page.getCanvas());
        doc.finishPage(page);

        PdfDocument.Page page2 = doc.startPage(pageInfo);
        int addSpace = 150;
        canvas=page2.getCanvas();
        paint.setTextSize(70);
        canvas.drawText("Weight Log",leftMargin,titleBaseLine,paint);
        canvas.drawLine(leftMargin, titleBaseLine+50, rowLine+500,titleBaseLine+50,paint);

        paint.setTextSize(50);
        for(Weight weight : weightList)
        {
            canvas.drawText("Week "+weight.getWeek(), leftMargin, titleBaseLine + (addSpace-30) ,paint);
            canvas.drawText(weight.getWeight()+" kg", leftMargin+250, titleBaseLine + (addSpace-30) ,paint);

            if(weight.getWeight() > HomePageFragment.maxWeightValue)
            {
                paint.setColor(Color.RED);
                canvas.drawText("Upper", leftMargin+500, titleBaseLine + (addSpace-30) ,paint);
                paint.setColor(Color.BLACK);
            }
            else if(weight.getWeight() < HomePageFragment.minWeightValue)
            {
                paint.setColor(Color.BLUE);
                canvas.drawText("Lower", leftMargin+500, titleBaseLine + (addSpace-30) ,paint);
                paint.setColor(Color.BLACK);
            }
            else
                {
                paint.setColor(Color.GREEN);
                canvas.drawText("Ideal", leftMargin+500, titleBaseLine + (addSpace-30) ,paint);
                paint.setColor(Color.BLACK);
            }

            canvas.drawLine(leftMargin,titleBaseLine+addSpace,rowLine+500,titleBaseLine+addSpace,paint);


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
        chart.notifyDataSetChanged();
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
        chart.notifyDataSetChanged();
    }

    private void setUsrWeight(){
        LineDataSet ds3 = new LineDataSet(lineEntries3, "current weight");
        ds3.setDrawValues(false);
        ds3.setValueTextColor(Color.GRAY);
        ds3.setColor(Color.parseColor("#f8b95c"));
        ds3.setDrawCircles(true);
        ds3.setCircleRadius(5f);
        ds3.setCircleColor(Color.parseColor("#f8b95c"));
        ds3.setLineWidth(2f);
        ds3.setValueTextSize(7f);
        ds3.setFormLineWidth(2f);
        ds3.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        ds3.setFormSize(15.f);
        ds3.setDrawCircleHole(false);
        dataSets.add(ds3);
        chart.notifyDataSetChanged();
    }

    public class getRange extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
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
            populateWeightDB();
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
            // get the legend (only possible after setting data)
            Legend l = chart.getLegend();
            chart.getLegend().setTextColor(Color.WHITE);
            l.setEnabled(true);

            chart.invalidate();

            HashMap mapHead = new HashMap<String,String>();
            String[] colHEAD = new String[] {"Week","Weight","Weight in range"};
            int[] dataCell = new int[] {R.id.weekLV,R.id.weightLV,R.id.InRangeLV};
            listArray = new ArrayList<HashMap<String, String>>();
            mapHead.put("Week","Week");
            mapHead.put("Weight","   Weight");
            mapHead.put("Weight in range","    Range");
            listArray.add(mapHead);

            myListAdapter = new SimpleAdapter(context,listArray,R.layout.list_view,colHEAD,dataCell);
            weightLV.setAdapter(myListAdapter);

            for(Weight weight:weightList) {
                map = new HashMap<String, String>();
                map.put("Week", "Week " + weight.getWeek() + "");
                map.put("Weight", weight.getWeight() + " KG");

                if (weight.getWeight() > HomePageFragment.maxWeightValue) {
                    map.put("Weight in range", "   Higher ");
                } else if (weight.getWeight() < HomePageFragment.minWeightValue) {
                    map.put("Weight in range", "   Lower ");
                } else {
                    map.put("Weight in range", "   Ideal");
                }
                listArray.add(map);
                myListAdapter.notifyDataSetChanged();
            }
        }
    }

    private class UpdateUserInfo extends AsyncTask<Double,Void,String>{
        @Override protected String doInBackground(Double... params){
            double pos1 = (double) params[1];
            int pos = (int) pos1 - 1;
            Weight weight = weightList.get(pos);
            weight.setWeight(params[0]);
            populateWeightDB();
            return "";
        }

        @Override
        protected void onPostExecute(String details){
            Toast.makeText(context,"The weight has been updated!",Toast.LENGTH_SHORT).show();
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

    private void showInputBox(String oldWeight, final double index){
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.input_box);
        dialog.setTitle("Edit Weight");
        TextView textMsg = dialog.findViewById(R.id.txtmsg);
        textMsg.setText("Update weight (Kg)");
        final EditText editText = dialog.findViewById(R.id.weight_input);
        editText.setText(oldWeight);
        Button btn = dialog.findViewById(R.id.btn_box_done);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserInfo updateUserInfo = new UpdateUserInfo();
                try
                {
                    Double.parseDouble(editText.getText().toString());
                    updateUserInfo.execute(Double.valueOf(editText.getText().toString()),index);
                    myListAdapter.notifyDataSetChanged();
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(context,"The input is invalid!",Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
                chart.invalidate();
            }
        });
        dialog.show();
    }

    private class InsertRecord extends AsyncTask<String, Void, String>
    {
        @Override protected String doInBackground(String... params) {
            int week = Integer.valueOf(params[0]);
            double weight = (double) Double.valueOf(params[1]);
            Weight newRecord = new Weight(currentUser.getUserid(),weight,week);
            loggedInUserdb.weightDao().insert(newRecord);

            return params[0];
        }
        protected void onPostExecute(String week){
            Snackbar.make(getView(), "Record for week "+week+" has been updated", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            ReadDatabase readDatabase = new ReadDatabase();
            readDatabase.execute();
        }
    }

    public void addNewWeight(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText addWeek = new EditText(context);
        addWeek.setInputType(InputType.TYPE_CLASS_NUMBER);
        addWeek.setHint("Enter any past week");
        layout.addView(addWeek);

        final EditText addWeight = new EditText(context);
        addWeight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        addWeight.setHint("Enter weight value for above week");
        layout.addView(addWeight);
        alert.setView(layout);
        alert.setCancelable(false);
        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            int flag = 0;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addWeek.getText().toString().isEmpty()||addWeight.getText().toString().isEmpty())
                {
                    Toast.makeText(context,"The fields can not be empty",Toast.LENGTH_LONG).show();
                    flag = 1;
                }

                try
                {
                    Double weight = Double.parseDouble(addWeight.getText().toString());
                    int week = Integer.parseInt(addWeek.getText().toString());

                    if(week>=currentUser.getCurrentWeek()|| week<0)
                    {
                        Toast.makeText(context,"Please input valid week (from 0 to current week)",Toast.LENGTH_LONG).show();
                        flag = 1;
                    }
                    else if(weight>300|| weight<15)
                    {
                        Toast.makeText(context,"Please input the weight within valid range (from 25-250kg)",Toast.LENGTH_LONG).show();
                        flag = 1;
                    }

                    else if(checkIfWeekExists(week))
                    {
                        Toast.makeText(context,"The record for this week exists!",Toast.LENGTH_LONG).show();
                        flag = 1;
                    }

                    else {
                        InsertRecord insertRecord = new InsertRecord();
                        insertRecord.execute(addWeek.getText().toString(), addWeight.getText().toString());
                        chart.invalidate();
                    }
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(context,"The input is invalid!",Toast.LENGTH_LONG).show();
                }

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private boolean checkIfWeekExists(int week)
    {
        for(Weight record:weightList)
        {
            if(week == record.getWeek())
                return true;
        }
        return false;
    }

    private void populateWeightDB()
    {
        loggedInUserdb.weightDao().deleteAll();
        for(Weight weight:weightList)
        {
            loggedInUserdb.weightDao().insert(weight);
        }
    }


}