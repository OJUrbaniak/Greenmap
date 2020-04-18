package com.example.greenmap;

public class BikeRackPOI extends PointOfInterest {
    boolean covered;

    BikeRackPOI(int id, String name, String desc, Double lat, Double lon, char type, int reviewRating, double distance) {
        super(id, name, desc, lat, lon, type, reviewRating, distance);
    }
}
