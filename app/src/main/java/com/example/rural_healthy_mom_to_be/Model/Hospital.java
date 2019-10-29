package com.example.rural_healthy_mom_to_be.Model;
/*
 *Model class of Hospital to be shown in Nearby hospitals page. It will consist all the hospitals attributes and getters and setters
 * */

public class Hospital
{   private double latitude;
    private double longitude;
    private String hospitalName;
    private String hosAddress;
    private double distanceFromCur;
    private String phoneNumber;


    public Hospital(double latitude, double longitude, String hospitalName, String hosAddress, double distanceFromCur) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.hospitalName = hospitalName;
        this.hosAddress = hosAddress;
        this.distanceFromCur = distanceFromCur;
    }

    public Hospital(double latitude, double longitude, String hospitalName, String hosAddress, double distanceFromCur, String phoneNumber) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.hospitalName = hospitalName;
        this.hosAddress = hosAddress;
        this.distanceFromCur = distanceFromCur;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getDistanceFromCur() {
        return distanceFromCur;
    }

    public void setDistanceFromCur(double distanceFromCur) {
        this.distanceFromCur = distanceFromCur;
    }

    public Hospital() {
    }

    public String getHosAddress() {
        return hosAddress;
    }

    public void setHosAddress(String hosAddress) {
        this.hosAddress = hosAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
