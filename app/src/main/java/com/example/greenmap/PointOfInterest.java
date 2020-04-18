package com.example.greenmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class PointOfInterest implements Serializable {
    //Base point of interest class for types to expand upon
    int id; //Set as data is pulled from db, used to keep track of POIs
    String name;
    String desc;
    char type;       //char r = recycling, b = bike rack, w = waterfountain
    int carbonSaved; //Will have to decide what this is for different POIs and whether it will change over time
    int reviewRating;
    double distance;

    static LatLng coords;

    public PointOfInterest(int id, String name, String desc, Double lat, Double lon, char type, int reviewRating) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
        coords = new LatLng(lat, lon);
        this.reviewRating = reviewRating;
    }

    PointOfInterest(int id, String name, String desc, Double lat, Double lon, char type, int reviewRating, double distance) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        coords = new LatLng(lat, lon);
        this.type = type;
        this.reviewRating = reviewRating;
        this.distance = distance;
    }
}
