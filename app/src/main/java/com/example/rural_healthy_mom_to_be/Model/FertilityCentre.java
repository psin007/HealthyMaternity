package com.example.rural_healthy_mom_to_be.Model;

/*
Note- Class Not in use
This class was written to include Fertility centers in nearby services section but since we couldnt find open data, we didnt use this
* */
public class FertilityCentre {
    private double latitude;
    private double longitude;
    private String fertilityName;

    public FertilityCentre(double latitude, double longitude, String fertilityName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.fertilityName = fertilityName;
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

    public String getFertilityName() {
        return fertilityName;
    }

    public void setFertilityName(String fertilityName) {
        this.fertilityName = fertilityName;
    }
}
