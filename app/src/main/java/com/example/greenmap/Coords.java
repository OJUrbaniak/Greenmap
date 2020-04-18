package com.example.greenmap;

import java.io.Serializable;

public class Coords implements Serializable {

    double longitude;
    double latitude;

    public Coords(double lat, double lon){
        this.longitude = lon;
        this.latitude = lat;
    }
}
