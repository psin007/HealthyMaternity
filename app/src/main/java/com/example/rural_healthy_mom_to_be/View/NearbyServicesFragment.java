package com.example.rural_healthy_mom_to_be.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class NearbyServicesFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap myMap;
    View vmyMaps;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vmyMaps = inflater.inflate(R.layout.fragment_nearby_services, container, false);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                //get the longitude
                double longitude = location.getLongitude();
                // instantiate the class Latlng
                LatLng latLng = new LatLng(latitude, longitude);
                Log.d("latitude","latitude");
                Log.d("longitude","latitude");
//                 myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                myMap.animateCamera(cameraUpdate);
                locationManager.removeUpdates(this);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        fillSpinnerData(vmyMaps);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        SupportMapFragment maps = (SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.map));
        maps.getMapAsync(this);
        return vmyMaps;
    }

    @SuppressLint("MissingPermission")
    private void updateLocation() {
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

            myMap = googleMap;
            myMap.getUiSettings().setMyLocationButtonEnabled(true);
            myMap.setMyLocationEnabled(true);
            updateLocation();


    }

    public void fillSpinnerData(View view){
        List<String> list = new ArrayList<String>();
        list.add("Search for a service here");
        list.add("Hospitals");
        list.add("Fertility centers");
        list.add("Obstetrician-gynecologist");
        final Spinner services = (Spinner) vmyMaps.findViewById(R.id.services_spinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (this.getActivity(),android.R.layout.simple_spinner_item,list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        services.setAdapter(spinnerAdapter );

    }


}
