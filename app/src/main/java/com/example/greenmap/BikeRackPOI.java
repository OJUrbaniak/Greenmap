package com.example.greenmap;

public class BikeRackPOI extends PointOfInterest {
    boolean covered;
    int carbonSaved = 15;

    //With reviewRating and distance
    BikeRackPOI(int id, String name, String desc, Double lat, Double lon, String type, int reviewRating, double distance) {
        super(id, name, desc, lat, lon, type, reviewRating, distance);
    }

    //Without reviewRating and distance
    public BikeRackPOI(int id, String name, String desc, Double lat, Double lon, String type, boolean covered) {
        super(id, name, desc, lat, lon, type);
        this.covered = covered;
    }
}
