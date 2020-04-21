package com.example.greenmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class PointOfInterest implements Serializable {
    //Base point of interest class for types to expand upon

    int id;             //Set as data is pulled from db, used to keep track of POIs
    String name;
    String desc;
    String type;          //char r = recycling, b = bike rack, w = waterfountain
    int carbonSaved;    //Will have to decide what this is for different POIs and whether it will change over time
    int reviewRating;
    double distance;
    Coords coords;

    //Without reviewRating and distance - POI to get submitted
    public PointOfInterest(int id, String name, String desc, Double lat, Double lon, String type) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
        coords = new Coords(lat, lon);
        reviewRating = -1;
    }

    //With reviewRating and distance -- when POIs are retrieved from DB?
    PointOfInterest(int id, String name, String desc, Double lat, Double lon, String type, int reviewRating, double distance) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        coords = new Coords(lat, lon);
        this.type = type;
        this.reviewRating = reviewRating;
        this.distance = distance;
    }
}
