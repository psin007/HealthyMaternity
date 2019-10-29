package com.example.rural_healthy_mom_to_be.View;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
* This class is responsible to handle profile page which allows user to edit information she entered during registration
* */
public class Profile extends Fragment {

    View view;
    LoggedInUserDb loggedInUserdb;
    TextView tvuserValue;
    String userValue;
    TextView tvHeight;
    Double height;
    TextView tvWeight;
    Double weightVal;
    LoggedinUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        loggedInUserdb = Room.databaseBuilder(view.getContext(),
                LoggedInUserDb.class, "LoggedInUserDatabase")
                .fallbackToDestructiveMigration()
                .build();
        tvuserValue = view.findViewById(R.id.usertvValue);
        tvHeight =view.findViewById(R.id.weight_current);
        tvWeight =view.findViewById(R.id.etprePregWeightValue);

        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();
        return view;
    }

    //Reads and prints already stored user info
    private class ReadDatabase extends AsyncTask<Void, Void, LoggedinUser> {


        @Override
        protected LoggedinUser doInBackground(Void... voids) {
            List<LoggedinUser> userList = loggedInUserdb.loggedInUserDao().getAll();
            currentUser = userList.get(0);
            return userList.get(0);
        }
        protected void onPostExecute(LoggedinUser details) {

            tvuserValue.setText(currentUser.getUsername());
            tvHeight.setText(currentUser.getHeightInCm()+"");
            tvWeight.setText(String.valueOf(currentUser.getWeightBeforePregnancy()));
        }
    }
    //Allows to edit value
    public void alertChangeHeight(View view) {
        Log.d("editCurrentWeight","editWeight");
        final EditText editWeight = new EditText(this.getActivity());
        editWeight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editWeight.setHint("Enter new height value");
        editWeight.setPadding(55,55,55,55);
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setView(editWeight);

        alert.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!editWeight.getText().toString().isEmpty())
                {
                    currentUser.setHeightInCm(Double.parseDouble(editWeight.getText().toString()));
                    UpdateDatabase updateDatabase = new UpdateDatabase();
                    updateDatabase.execute();
                }

            }
        });
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    //Updates  already stored user info to new info in Db

    private class UpdateDatabase extends AsyncTask<Void, Void, String > {


        @Override
        protected String doInBackground(Void... voids) {
            loggedInUserdb.loggedInUserDao().updateUsers(currentUser);
            return currentUser.getHeightInCm()+"";
        }
        @Override
        protected void onPostExecute(String newHeight) {
            tvHeight.setText(newHeight);
            tvWeight.setText(String.valueOf(currentUser.getWeightBeforePregnancy()));
                 }

    }
    //  Dialog to change pre pregnancy weight
    public void dialogChangePreWeight(View view) {
        final EditText editWeight = new EditText(this.getActivity());
        editWeight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editWeight.setHint("Enter new pre-pregnancy weight value");
        editWeight.setPadding(55,55,55,55);
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setView(editWeight);

        alert.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!editWeight.getText().toString().isEmpty())
                {
                    currentUser.setWeightBeforePregnancy(Double.parseDouble(editWeight.getText().toString()));
                    UpdateDatabase updateDatabase = new UpdateDatabase();
                    updateDatabase.execute();
                }

            }
        });
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }


}
