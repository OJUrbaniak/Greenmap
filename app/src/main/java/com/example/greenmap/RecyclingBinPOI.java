package com.example.greenmap;

public class RecyclingBinPOI extends PointOfInterest {
    String binType;

    RecyclingBinPOI(int id, String name, String desc, Double lat, Double lon, char type, int reviewRating, String binType, double distance) {
        super(id, name, desc, lat, lon, type, reviewRating, distance);
        this.binType = binType;
    }
}
