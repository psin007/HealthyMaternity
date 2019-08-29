package com.example.rural_healthy_mom_to_be.View;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WeightTrackerFragment extends Fragment {
    View vWeightTracker;
    LoggedInUserDb loggedInUserdb;
    LoggedinUser currentUser;
    HashMap<String,String> map;
    Context context;
    TextView tvTrackerCurrentWeight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vWeightTracker = inflater.inflate(R.layout.weighttrackerfragment, container, false);
        loggedInUserdb = Room.databaseBuilder(vWeightTracker.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();
        context = vWeightTracker.getContext();
//        homeHeader = vHomePage.findViewById(R.id.home_header_text);

        tvTrackerCurrentWeight = vWeightTracker.findViewById(R.id.trackerWeight);
    //    tvTrackerCurrentWeight.setText(currentUser.getCurrentWeight()+" KG");
        //ToDo: create a new table for weeks and respective weights
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
        return vWeightTracker;
    }
    private class ReadDatabase extends AsyncTask<Void, Void, LoggedinUser> {


        @Override
        protected LoggedinUser doInBackground(Void... voids) {
            List<LoggedinUser> userList = loggedInUserdb.loggedInUserDao().getAll();
            currentUser = userList.get(0);
            return userList.get(0);
        }
        protected void onPostExecute(LoggedinUser details) {
            tvTrackerCurrentWeight.setText(currentUser.getCurrentWeight()+" KG");
            List<HashMap<String, String>> listArray;
            SimpleAdapter myListAdapter;
            ListView weightLV;
            map = new HashMap<String,String>();
            HashMap mapHead = new HashMap<String,String>();

            String[] colHEAD = new String[] {"Week","Weight","Weight in range"};
            int[] dataCell = new int[] {R.id.weekLV,R.id.weightLV,R.id.InRangeLV};
            weightLV = vWeightTracker.findViewById(R.id.listView);
            listArray = new ArrayList<HashMap<String, String>>();
            mapHead.put("Week","Week");
            mapHead.put("Weight","   Weight");
            mapHead.put("Weight in range","   Weight in range /week");

            map.put("Week","Week " + currentUser.getCurrentWeek()+"");
            map.put("Weight",currentUser.getCurrentWeight()+" KG");

            //TODO all of it needs to be redone
            if(currentUser.getCurrentWeight()> HomePageFragment.maxWeightValue){
                map.put("Weight in range","   Higher ");
            }
            else if (currentUser.getCurrentWeight() < HomePageFragment.minWeightValue){
                map.put("Weight in range","   Lower ");

            }

            else{
                map.put("Weight in range","In range");
            }
            listArray.add(mapHead);
            listArray.add(map);
            myListAdapter = new SimpleAdapter(context,listArray,R.layout.list_view,colHEAD,dataCell);
            weightLV.setAdapter(myListAdapter);
        }
    }

}
