package com.example.greenmap;

public class WaterFountainPOI extends PointOfInterest {
    boolean safeToDrinkStraight;
    boolean bottleFilling;
    boolean filtered;

    WaterFountainPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating, int carbonSaved, boolean safeToDrinkStraight, boolean bottleFilling, boolean filtered) {
        super(name, desc, lat, lon, type, reviewRating);
        this.carbonSaved = carbonSaved;
        this.safeToDrinkStraight = safeToDrinkStraight;
        this.bottleFilling = bottleFilling;
        this.filtered = filtered;
    }
}
