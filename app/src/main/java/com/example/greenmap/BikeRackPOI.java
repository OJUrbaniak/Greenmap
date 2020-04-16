package com.example.greenmap;

public class BikeRackPOI extends PointOfInterest {
    boolean covered;

    BikeRackPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating) {
        super(name, desc, lat, lon, type, reviewRating);
    }
}
