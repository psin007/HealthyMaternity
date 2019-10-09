package com.example.rural_healthy_mom_to_be;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.example.rural_healthy_mom_to_be.View.NavigationDrawer;
import com.example.rural_healthy_mom_to_be.View.WelcomeActvity;

public class MainActivity extends AppCompatActivity {

    private int SPLASH_TIME = 1500;
    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        SharedPreferences loggedUser = getApplicationContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
        final String loggedIn = loggedUser.getString("loggedIn","no");

//        Thread timer = new Thread() {
//            public void run() {
//                try {
//                    sleep(SPLASH_TIME);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//
//                }
//            }
//        };
//        timer.start();
        if(loggedIn.equals("yes")){
            Intent intent = new Intent(MainActivity.this, NavigationDrawer.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this, WelcomeActvity.class);
            startActivity(intent);
            finish();
        }

//        SharedPreferences loggedUser = getApplicationContext().getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
//        String loggedIn = loggedUser.getString("loggedIn","no");
//        if(loggedIn.equals("yes")){
//            Intent intent = new Intent(MainActivity.this, NavigationDrawer.class);
//            startActivity(intent);
//            //ToDo: RR added this line to remove the MainActivity from back stack.
//            finish();
//        }
//        else{
//            Intent intent = new Intent(MainActivity.this, WelcomeActvity.class);
//            startActivity(intent);
//            //ToDo: RR added this line to remove the MainActivity from back stack.
//            finish();
//        }
    }

    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
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
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted

                //initialize();
                break;
        }

    }
}
