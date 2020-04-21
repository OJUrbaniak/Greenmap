package com.example.greenmap;

public class RecyclingBinPOI extends PointOfInterest {
    String binType;
    int carbonSaved = 5;


    //With reviewRating and distance
    RecyclingBinPOI(int id, String name, String desc, Double lat, Double lon, String type, int reviewRating, String binType, double distance) {
        super(id, name, desc, lat, lon, type, reviewRating, distance);
        this.binType = binType;
    }

    //Without reviewRating and distance
    public RecyclingBinPOI(int id, String name, String desc, Double lat, Double lon, String type, String binType) {
        super(id, name, desc, lat, lon, type);
        this.binType = binType;
    }
}
