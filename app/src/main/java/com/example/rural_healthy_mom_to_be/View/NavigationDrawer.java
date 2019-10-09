package com.example.rural_healthy_mom_to_be.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.rural_healthy_mom_to_be.R;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomePageFragment.FragmentInteracion {
    static public final int REQUEST_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("Homepage");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, new HomePageFragment(),"HomePageFragment").commit();


        //map

    }

    public void editCurrentWeight(View view){
         Log.d("edit","edit");
         HomePageFragment homePageFragment = (HomePageFragment)getSupportFragmentManager().findFragmentByTag("HomePageFragment");
         if(homePageFragment!=null){
             homePageFragment.editCurrentWeight(view);
         }
    }

    public void alertIdealMessage(View view){
        HomePageFragment homePageFragment = (HomePageFragment)getSupportFragmentManager().findFragmentByTag("HomePageFragment");
        if(homePageFragment!=null){
            homePageFragment.alertIdealMessage(view);
        }
    }


    public void messageDialog(View view){
        HomePageFragment homePageFragment = (HomePageFragment)getSupportFragmentManager().findFragmentByTag("HomePageFragment");
        if(homePageFragment!=null){
            homePageFragment.messageDialog(view);
        }
    }

    public void alertChangeHeight(View view){
        Profile profile = (Profile) getSupportFragmentManager().findFragmentByTag("Profile");
        if(profile!=null){
            profile.alertChangeHeight(view);
        }
    }
    public void dialogChangePreWeight(View view){
        Profile profile = (Profile) getSupportFragmentManager().findFragmentByTag("Profile");
        if(profile!=null){
            profile.dialogChangePreWeight(view);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startMap();
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

    private void startMap() {
        Fragment nextFragment = null;
        String tag = "";

        nextFragment = new NearbyServicesFragment();
        getSupportActionBar().setTitle("Nearby Services");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment,tag).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String tag = "";
        Fragment nextFragment = null;
        switch (id) {
            case R.id.nav_homepage:
                nextFragment = new HomePageFragment();
                getSupportActionBar().setTitle("Homepage");
                tag="HomePageFragment";
                break;
            case R.id.nav_weighttracker:
                nextFragment = new WeightTrackerFragment();
                getSupportActionBar().setTitle("Weight Tracker");
                break;
            case R.id.nav_weightGraph:
                nextFragment = new WeightGraphFragment();
                getSupportActionBar().setTitle("Weight Graph");
                break;

            case R.id.nav_nearbyservices:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                    } else {
                        nextFragment = new NearbyServicesFragment();
                        getSupportActionBar().setTitle("Nearby Services");
                }
                }
                break;
            case R.id.nav_myProfile:
                nextFragment = new Profile();
                getSupportActionBar().setTitle("My Profile");
                tag="Profile";
                break;

            case R.id.nav_fooddiary:
                nextFragment = new FoodDiaryFragement();
                getSupportActionBar().setTitle("Food Diary");
                break;
        }
        if(nextFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment, tag).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void editWeightClicked(View v) {

    }
}
