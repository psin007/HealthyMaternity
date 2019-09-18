package com.example.rural_healthy_mom_to_be.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rural_healthy_mom_to_be.Model.FertilityCentre;
import com.example.rural_healthy_mom_to_be.Model.Hospital;
import com.example.rural_healthy_mom_to_be.Model.HospitalAdapter;
import com.example.rural_healthy_mom_to_be.Model.ObsGy;
import com.example.rural_healthy_mom_to_be.R;
import com.example.rural_healthy_mom_to_be.Repository.FertilityCenterAPI;
import com.example.rural_healthy_mom_to_be.Repository.HospitalAPI;
import com.example.rural_healthy_mom_to_be.Repository.WeightValuesAPI;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class NearbyServicesFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap myMap;
    View vmyMaps;
    LocationManager locationManager;
    LocationListener locationListener;
    private Spinner mainSpinner;
    ArrayList<Hospital> hospitals;
    Button mapButton,listButton;
    EditText etRadius;
    double radius;
    Hospital hospital;
    double curlongitude;
    double curlatitude;
    double dist;
    RecyclerView placeList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vmyMaps = inflater.inflate(R.layout.fragment_nearby_services, container, false);
        mainSpinner = vmyMaps.findViewById(R.id.services_spinner);
        etRadius = (EditText)vmyMaps.findViewById(R.id.etRadius);
        placeList = vmyMaps.findViewById(R.id.recyclerView);
        mapButton = vmyMaps.findViewById(R.id.map_button);
        listButton=vmyMaps.findViewById(R.id.list_button);
        if(etRadius.getText().toString().length() != 0){
            radius = (Double.parseDouble(etRadius.getText().toString()));
        }
        hospitals = new ArrayList<Hospital>();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                curlatitude = location.getLatitude();
                //get the longitude
                curlongitude = location.getLongitude();
                // instantiate the class Latlng
                LatLng latLng = new LatLng(curlatitude, curlongitude);
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

        final SupportMapFragment maps = (SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.map));
        maps.getMapAsync(this);
        mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test",mainSpinner.getItemAtPosition(position).toString());
                if(etRadius.getText().toString().length() != 0){
                    radius = (Double.parseDouble(etRadius.getText().toString()));
                }
                else{
                    radius = 40;
                }
                switch(mainSpinner.getItemAtPosition(position).toString()){
                    case "Search for a service here":
                        break;
                    case "Hospitals":
                        clearData();
                        searchHospitals();
                        break;
//                    case "Fertility centers": searchFertilityCentres();
//                        break;
//                    case "Obstetrician-gynaecologist": searchObstetricianGynaecologist();
//                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeList.setVisibility(View.GONE);
                mapButton.setBackgroundResource(R.color.colorAccent);
                listButton.setBackgroundColor(Color.parseColor("#b5651d"));
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeList.setVisibility(View.VISIBLE);
                listButton.setBackgroundResource(R.color.colorAccent);
                mapButton.setBackgroundColor(Color.parseColor("#b5651d"));
            }
        });
        return vmyMaps;
    }
    private void searchObstetricianGynaecologist() {
//        GetObsGyCenter getObsGyCenter = new GetObsGyCenter();
//        getObsGyCenter.execute();
    }

//    private class GetObsGyCenter extends AsyncTask<String,Void,String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            return ObsGyAPI.getObsGyInfo(String.valueOf(curlatitude),String.valueOf(curlongitude),"40000","Obstetrician-Gynaecologist);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.d("ObsGyAPI",result);
//            final ObsGy[] nearByOG=ObsGyAPI.getObsGy(result);
//            for(int j =0;j<nearByOG.length;j++){
//
//                myMap.addMarker(new MarkerOptions().position(new LatLng(nearByOG[j].getLatitude(),nearByOG[j].getLongitude())).title(nearByOG[j].getObsGyName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(j);
//
//            }
//            myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    Object tagObject = marker.getTag(); //because red markup is not given any tag and if clicked, tag is null and it crashes
//                    if(tagObject!=null) {
//                        int tag = (int) tagObject;
//                        new AlertDialog.Builder(getActivity()).setTitle("Obstetrician - Gynaecologist details").setMessage(nearByOG[tag].getObsGyName()).setPositiveButton("Ok", null).show();
//                    }
//                    return false;
//                }
//            });
//        }
//    }


    private void searchHospitals() {

        GetHospitalData getHospitalData = new GetHospitalData();
        getHospitalData.execute();
    }

    private void clearData() {
        hospitals.removeAll(hospitals);
        myMap.clear();
    }

    private void searchFertilityCentres() {
//        GetFertilityCenter getFertilityCenter = new GetFertilityCenter();
//        getFertilityCenter.execute();
    }
//    private class GetFertilityCenter extends AsyncTask<String,Void,String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            return FertilityCenterAPI.getFertilityCenterInfo(String.valueOf(curlatitude),String.valueOf(curlongitude),"40000","Fertility Center");
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.d("fertility center",result);
//            final FertilityCentre[] nearByFC=FertilityCenterAPI.getFertilityCenter(result);
//            for(int j =0;j<nearByFC.length;j++){
//
//                myMap.addMarker(new MarkerOptions().position(new LatLng(nearByFC[j].getLatitude(),nearByFC[j].getLongitude())).title(nearByFC[j].getFertilityName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(j);
//
//            }
//            myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    Object tagObject = marker.getTag(); //because red markup is not given any tag and if clicked, tag is null and it crashes
//                    if(tagObject!=null) {
//                        int tag = (int) tagObject;
//                        new AlertDialog.Builder(getActivity()).setTitle("Fertility center details").setMessage(nearByFC[tag].getFertilityName()).setPositiveButton("Ok", null).show();
//                    }
//                    return false;
//                }
//            });
//        }
//    }

    private class GetHospitalData extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return HospitalAPI.gethospitalInfo();


        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("hospital",result);
            JSONArray jsonarray = null;


            try {
                jsonarray = new JSONArray(result);
                for(int i =0;i<jsonarray.length();i++){
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    // Log.d("jsonobject",jsonobject.toString());

                    String name = jsonobject.getString("Hospital name");
                    String address = jsonobject.getString("Full_address");

                    double latitude = jsonobject.getDouble("Latitude");
                    double longitude = jsonobject.getDouble("Longitude");
                    boolean close = distanceLessThanRadius(latitude,longitude,curlatitude,curlongitude);
                    if(close){
                        hospital = new Hospital();
                        hospital.setDistanceFromCur(dist);
                        hospital.setHospitalName(name);
                        hospital.setLatitude(latitude);
                        hospital.setLongitude(longitude);
                        hospital.setHosAddress(address);
                        hospitals.add(hospital);
                    }
                }


                showMarker();
                showList();

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showList() {
        HospitalAdapter adapter = new HospitalAdapter(hospitals.toArray(new Hospital[hospitals.size()]));
        placeList.setHasFixedSize(true);
        placeList.setLayoutManager(new LinearLayoutManager(getContext()));
        placeList.setAdapter(adapter);
    }

    private boolean distanceLessThanRadius(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if(dist > radius)
            return false;
        else
            return true;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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
        final Spinner services = (Spinner) vmyMaps.findViewById(R.id.services_spinner);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (this.getActivity(),android.R.layout.simple_spinner_item,list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        services.setAdapter(spinnerAdapter );

    }

    public void showMarker(){
        LatLng loc;
        String foundname;
        for(int i = 0; i<hospitals.size();i++){
            loc = new LatLng(hospitals.get(i).getLatitude(),hospitals.get(i).getLongitude());
            foundname = hospitals.get(i).getHospitalName();
            myMap.addMarker(new MarkerOptions().position(loc).title(foundname)).setTag(i);
            myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Object tagObject = marker.getTag();
                    if(tagObject!=null) {
                        int tag = (int) tagObject;
                        new AlertDialog.Builder(getActivity()).setTitle("Hospital details").setMessage(hospitals.get(tag).getHospitalName()+
                                "\nAddress: "+hospitals.get(tag).getHosAddress()+
                                "\nDistance from current location:"+
                                String.format("%.2f",hospitals.get(tag).getDistanceFromCur())+" km").setPositiveButton("Ok", null).show();
                    }
                    return false;
                }
            });
        }

    }


}
