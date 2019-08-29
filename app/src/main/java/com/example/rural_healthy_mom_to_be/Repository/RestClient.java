package com.example.rural_healthy_mom_to_be.Repository;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient {
    protected static String address;

    private static final String BASE_URL = "https://87y2yd8o05.execute-api.us-east-1.amazonaws.com/weight/weight";

    public static String [] getWeightGainForUnderWeight(String limit) throws JSONException {
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        String [] resultArr = new String [40];
        //Making HTTP request
        try {
            url = new URL(BASE_URL);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response

            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        JSONArray arr = new JSONArray(textResult);


        if (limit == "max")
        {
            for(int i = 0;i<=39;i++)
            {
                String maxWeight = arr.getJSONObject(i).getString("max_weight_gain");
                resultArr[i] = maxWeight;
            }
        }
        else
        {
            for(int i = 0;i<=39;i++)
            {
                String minWeight = arr.getJSONObject(i).getString("min_weight_gain");
                resultArr[i] = minWeight;
            }
        }
        return resultArr;
    }

    public static String getWeightGainForNorWeight() {
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //Making HTTP request
        try {
            url = new URL("https://87y2yd8o05.execute-api.us-east-1.amazonaws.com/weight/weight");
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            if (inStream.nextLine().equals("[]"))
                return "error";
            else
            {
                while (inStream.hasNextLine()) {
                    textResult += inStream.nextLine();
                }
            }

            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getWeightGainForOverWeight() {
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //Making HTTP request
        try {
            url = new URL("https://87y2yd8o05.execute-api.us-east-1.amazonaws.com/weight/weight");
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            if (inStream.nextLine().equals("[]"))
                return "error";
            else
            {
                while (inStream.hasNextLine()) {
                    textResult += inStream.nextLine();
                }
            }

            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getWeightGainForObesity() {
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //Making HTTP request
        try {
            url = new URL("https://87y2yd8o05.execute-api.us-east-1.amazonaws.com/weight/weight");
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            if (inStream.nextLine().equals("[]"))
                return "error";
            else
            {
                while (inStream.hasNextLine()) {
                    textResult += inStream.nextLine();
                }
            }

            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

}
