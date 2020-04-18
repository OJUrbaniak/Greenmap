package com.example.greenmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class PointOfInterest implements Serializable {
    //Base point of interest class for types to expand upon
    String id; //Set as data is pulled from db, used to keep track of POIs
    String name;
    String desc;
    char type;       //char r = recycling, b = bike rack, w = waterfountain
    int carbonSaved; //Will have to decide what this is for different POIs and whether it will change over time
    int reviewRating;

    static LatLng coords;
    int distanceFromUser; //This gets set later

    PointOfInterest(String name, String desc, Double lat, Double lon, char type, int reviewRating) {
        this.name = name;
        this.desc = desc;
        coords = new LatLng(lat, lon);
        this.type = type;
        this.reviewRating = reviewRating;
    }

    PointOfInterest(String name, char type, Double lat, Double lon) {
        this.name = name;
        this.type = type;
        coords = new LatLng(lat, lon);
    }
}
