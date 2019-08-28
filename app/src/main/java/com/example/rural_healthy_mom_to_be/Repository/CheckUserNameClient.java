package com.example.rural_healthy_mom_to_be.Repository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CheckUserNameClient {
    //ToChange - parameters value of method
    public static String checkIfUserNameExist(String username) {
        //ToChange methodpath
        final String methodPath = "restcalorietracker.credential/checkCredentials/" + username ;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //Making HTTP request
        try {
            url = new URL(RestClient.address + methodPath);
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

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;

    }
}
