package com.example.greenmap;

public class RecyclingBinPOI extends PointOfInterest {
    String binType;

    RecyclingBinPOI(String name, String desc, Double lat, Double lon, char type, int reviewRating, String binType) {
        super(name, desc, lat, lon, type, reviewRating);
        this.binType = binType;
    }
}
