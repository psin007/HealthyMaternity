package com.example.rural_healthy_mom_to_be.View;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.Model.LoggedinUser;
import com.example.rural_healthy_mom_to_be.Model.Summary;
import com.example.rural_healthy_mom_to_be.Model.Weight;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.LoggedInUserDb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class FoodDiaryFragement extends Fragment {
    View vFood;
    TextView date_select;
    ListView foodList;
    Context context;
    Button btnAddFood;
    LoggedinUser currentUser;
    HashMap<String, String> map;
    List<HashMap<String, String>> listArray;
    SimpleAdapter myListAdapter;
    LoggedInUserDb loggedInUserdb;
    private DatePickerDialog.OnDateSetListener mListener;
    private List<Summary> consumList;
    private List<LoggedinUser> userList;
    private int year;
    private int month;
    private int day;
    private String pattern;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vFood = inflater.inflate(R.layout.fragment_fooddiary, container, false);
        context = this.getContext();
        date_select = vFood.findViewById(R.id.et_changedate);
        foodList = vFood.findViewById(R.id.fooditem_listview);
        btnAddFood = vFood.findViewById(R.id.btn_addNewFood);

        //get current date
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simformat = new SimpleDateFormat(pattern);
        String currentDateTimeString = simformat.format(new Date());
        date_select.setText(currentDateTimeString);

        date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                mListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yr, int mm, int dd) {
                        mm += 1;
                        String monthString = String.valueOf(mm);
                        if (monthString.length() == 1) {
                            monthString = "0" + monthString;
                        }

                        String setDate = dd + "/" + monthString + "/" + yr;
                        date_select.setText(setDate);
                    }
                };
            }
        });

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddFoodInDiaryFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return vFood;
    }

    private class ReadDatabase extends AsyncTask<Void, Void, LoggedinUser> {
        @Override
        protected LoggedinUser doInBackground(Void... voids) {
            consumList = loggedInUserdb.summaryDao().findByDate(date_select.getText().toString());
            userList = loggedInUserdb.loggedInUserDao().getAll();
            currentUser = userList.get(0);
            return userList.get(0);
        }

        protected void onPostExecute(LoggedinUser details) {
            HashMap mapHead = new HashMap<String, String>();
            String[] colHEAD = new String[]{"Food", "Quantity", "Energy"};
            int[] dataCell = new int[]{R.id.food_itemLV, R.id.quantityLV, R.id.energyLV};
            listArray = new ArrayList<>();
            mapHead.put("Food", "Food");
            mapHead.put("Quantity", "   Quantity");
            mapHead.put("Energy", "   Energy");
            listArray.add(mapHead);

            myListAdapter = new SimpleAdapter(context, listArray, R.layout.list_view, colHEAD, dataCell);
            foodList.setAdapter(myListAdapter);

            getDate();


            for (Summary summary : consumList) {
                map = new HashMap<String, String>();
                map.put("Food", summary.getFoodname());
                map.put("Quantity", summary.getQuantity() + " g");
                map.put("Energy", summary.getCalories() + " kcal");
                listArray.add(map);
                myListAdapter.notifyDataSetChanged();
            }
        }

        private void getDate() {
            day = Integer.valueOf(date_select.getText().toString().substring(0, 1));
            month = Integer.valueOf(date_select.getText().toString().substring(3, 4));
            year = Integer.valueOf(date_select.getText().toString().substring(6, 7));
        }
    }
}



