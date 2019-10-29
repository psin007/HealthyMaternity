package com.example.rural_healthy_mom_to_be.Model;
/*
Note- Class Not in use
This class was written to include Obstretician-Gynaecologist in nearby services section but since we couldnt find open data, we didnt use this
* */
public class ObsGy {
    private double latitude;
    private double longitude;
    private String obsGyName;

    public ObsGy(double latitude, double longitude, String obsGyName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.obsGyName = obsGyName;
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

    public String getObsGyName() {
        return obsGyName;
    }

    public void setObsGyName(String obsGyName) {
        this.obsGyName = obsGyName;
    }
}
