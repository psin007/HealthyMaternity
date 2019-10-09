package com.example.rural_healthy_mom_to_be.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.Model.Weight;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WeightTrackerFragment extends Fragment {
    View vWeightTracker;
    ListView weightLV;
    LoggedInUserDb loggedInUserdb;
    List<LoggedinUser> userList;
    List<Weight> weightList;
    LoggedinUser currentUser;
    HashMap<String,String> map;
    Context context;
    TextView tvTrackerCurrentWeight;
    List<HashMap<String, String>> listArray;
    SimpleAdapter myListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vWeightTracker = inflater.inflate(R.layout.weighttrackerfragment, container, false);
        loggedInUserdb = Room.databaseBuilder(vWeightTracker.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();
        context = vWeightTracker.getContext();
        weightLV = vWeightTracker.findViewById(R.id.listView);
        FloatingActionButton fab = vWeightTracker.findViewById(R.id.fab);
//        homeHeader = vHomePage.findViewById(R.id.home_header_text);

        tvTrackerCurrentWeight = vWeightTracker.findViewById(R.id.trackerWeight);
    //    tvTrackerCurrentWeight.setText(currentUser.getCurrentWeight()+" KG");
        //ToDo: create a new table for weeks and respective weights
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

//        weightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //show the input box
//                showInputBox(String.valueOf(currentUser.getCurrentWeight()),(double) position);
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewWeight(view);
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        return vWeightTracker;
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
                if(addWeek.getText().toString().isEmpty())
                {
                    Toast.makeText(context,"The fields can not be empty",Toast.LENGTH_LONG).show();
                    flag = 1;
                }
                else if(addWeight.getText().toString().isEmpty())
                {
                    Toast.makeText(context,"The fields can not be empty",Toast.LENGTH_LONG).show();
                    flag = 1;
                }
                else if(Integer.valueOf(addWeek.getText().toString())>=currentUser.getCurrentWeek()||
                        Integer.valueOf(addWeek.getText().toString())<0)
                {
                    Toast.makeText(context,"Please input valid week (from 0 to current week)",Toast.LENGTH_LONG).show();
                    flag = 1;
                }
                else if(Double.valueOf(addWeight.getText().toString())>300||
                        Double.valueOf(addWeight.getText().toString())<15)
                {
                    Toast.makeText(context,"Please input the weight within valid range (from 25-250kg)",Toast.LENGTH_LONG).show();
                    flag = 1;
                }

                else {
                    InsertRecord insertRecord = new InsertRecord();
                    insertRecord.execute(addWeek.getText().toString(), addWeight.getText().toString());
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
                map.put("Weight",editText.getText().toString()+" KG");
                UpdateUserInfo updateUserInfo = new UpdateUserInfo();
                updateUserInfo.execute(Double.valueOf(editText.getText().toString()),index);
                myListAdapter.notifyDataSetChanged();
                dialog.dismiss();
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
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


    private class ReadDatabase extends AsyncTask<Void, Void, LoggedinUser> {
        @Override
        protected LoggedinUser doInBackground(Void... voids) {
            weightList = loggedInUserdb.weightDao().getAll();
            userList = loggedInUserdb.loggedInUserDao().getAll();
            currentUser = userList.get(0);
            //sort out the list
            bubbleSort();
            loggedInUserdb.weightDao().deleteAll();
            for(Weight weight:weightList)
            {
                loggedInUserdb.weightDao().insert(weight);
            }
            return userList.get(0);
        }
        protected void onPostExecute(LoggedinUser details) {
            tvTrackerCurrentWeight.setText(currentUser.getCurrentWeight()+" KG");

            HashMap mapHead = new HashMap<String,String>();

            String[] colHEAD = new String[] {"Week","Weight","Gain"};
            int[] dataCell = new int[] {R.id.weekLV,R.id.weightLV,R.id.InRangeLV};
            listArray = new ArrayList<HashMap<String, String>>();
            mapHead.put("Week","Week");
            mapHead.put("Weight","   Weight");
            mapHead.put("Weight in range","   Weight in range /week");
            listArray.add(mapHead);

            myListAdapter = new SimpleAdapter(context,listArray,R.layout.list_view,colHEAD,dataCell);
            weightLV.setAdapter(myListAdapter);


            for(Weight weight:weightList) {
                map = new HashMap<String, String>();
                map.put("Week", "Week " + weight.getWeek() + "");
                map.put("Weight", weight.getWeight() + " KG");

                //TODO all of it needs to be redone
                if (weight.getWeight() > HomePageFragment.maxWeightValue) {
                    map.put("Weight in range", "   Higher ");
                } else if (weight.getWeight() < HomePageFragment.minWeightValue) {
                    map.put("Weight in range", "   Lower ");
                } else {
                    map.put("Weight in range", "In range");
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
            loggedInUserdb.weightDao().updateUsers();
            myListAdapter.notifyDataSetChanged();
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
    }

}
