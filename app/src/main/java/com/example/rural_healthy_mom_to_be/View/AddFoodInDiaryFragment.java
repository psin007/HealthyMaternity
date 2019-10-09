package com.example.rural_healthy_mom_to_be.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.Model.Food;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.NutritionAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFoodInDiaryFragment extends Fragment {
    View vFoodAdd;
    String foodToSearch;
    Food food;
    TextView tvCaloriesFacts;
    TextView tvFatFacts;
    EditText etSearchFood;
    Button searchButton;
    Button confirmButton;
    TextView tvServingUnit;
    RelativeLayout addQuantity;
    RelativeLayout layoutFacts;
    LinearLayout quantityLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vFoodAdd = inflater.inflate(R.layout.add_a_meal, container, false);
        tvCaloriesFacts = vFoodAdd.findViewById(R.id.tv_cal_values);
        tvFatFacts = vFoodAdd.findViewById(R.id.tv_fat_values);
        tvServingUnit = vFoodAdd.findViewById(R.id.tvServingUnit);
        etSearchFood = vFoodAdd.findViewById(R.id.et_searchFood);
        searchButton = vFoodAdd.findViewById(R.id.searchButton);
        confirmButton = vFoodAdd.findViewById(R.id.confirmAddButton);
        addQuantity = vFoodAdd.findViewById(R.id.addQuantity);
        layoutFacts = vFoodAdd.findViewById(R.id.layoutFacts);
        quantityLayout = vFoodAdd.findViewById(R.id.quantityLayout);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(etSearchFood.getText().toString().isEmpty())) {
                    String foodToSearch = etSearchFood.getText().toString();
                    new GetNDBNumber().execute(foodToSearch);

                }
                else{
                    etSearchFood.setError("Enter food to search");
                }

            }
        });

        return vFoodAdd;

    }
    class GetNDBNumber extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return NutritionAPI.getNDBNO(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if(result.contains("errors")){
                   etSearchFood.setError("Please enter another food item");
                }
                else {
                    confirmButton.setVisibility(View.VISIBLE);
                    addQuantity.setVisibility(View.VISIBLE);
                    layoutFacts.setVisibility(View.VISIBLE);
                    quantityLayout.setVisibility(View.VISIBLE);
                    JSONObject jsonobject = new JSONObject(result);

                    JSONArray jsonArray = jsonobject.getJSONObject("list").getJSONArray("item");
                    String nbdno = jsonArray.getJSONObject(0).getString("ndbno");
                    GetNutrientData getNutrientData = new GetNutrientData();
                    getNutrientData.execute(nbdno);
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }


        }
    }
    class GetNutrientData extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return NutritionAPI.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("result",result);
            if(result.equals("")){
                Toast.makeText(getActivity(),"can not find info ",Toast.LENGTH_SHORT).show();
            }
            else{
                paresResult(result);
            }
        }
    }

    private void paresResult(String result) {
        double fat;
        double calories;
        String servingUnit;
        double servingAmount;
        String category;
        String foodName;
        String fatFact="";
        String calFact="";
        try {
            food = new Food();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("report").getJSONObject("food").getJSONArray("nutrients");
            for(int i = 0;i<jsonArray.length();i++){
                String nutrientId = jsonArray.getJSONObject(i).getString("nutrient_id");
                if(nutrientId.equals("204")){
                    fat = Double.parseDouble(jsonArray.getJSONObject(i) .getString("value"));
                    JSONArray measuresArray = jsonArray.getJSONObject(i).getJSONArray("measures");
                    servingAmount = measuresArray.getJSONObject(0).getDouble("qty");
                    servingUnit = measuresArray.getJSONObject(0).getString("label");
                    food.setFat(fat);
                    food.setServingunit(servingUnit);
                    fatFact = "\nFat - "+fat;

                }
                if(nutrientId.equals("208")){
                    calories = Double.parseDouble(jsonArray.getJSONObject(i) .getString("value"));
                    food.setCalorieamount(calories);
                    calFact = "\nCalories -"+calories;
                }
            }
            String NutrientFact = "Nutrient facts:"+fatFact+calFact;
            tvCaloriesFacts.setText(food.getCalorieamount()+"");
            tvFatFacts.setText(food.getFat()+"");
            tvServingUnit.setText("  ("+food.getServingunit()+")");
            //set nutrientTv values

        }

        catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
