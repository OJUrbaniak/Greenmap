package com.example.greenmap;

public class WaterFountainPOI extends PointOfInterest {
    boolean safeToDrinkStraight;
    boolean bottleFilling;
    boolean filtered;
    int carbonSaved = 2;

    //With reviewRating and distance - RETRIEVING FROM DB?
    WaterFountainPOI(int id, String name, String desc, Double lat, Double lon, char type, int reviewRating, boolean safeToDrinkStraight, boolean bottleFilling, boolean filtered, double distance) {
        super(id, name, desc, lat, lon, type, reviewRating, distance);
        this.safeToDrinkStraight = safeToDrinkStraight;
        this.bottleFilling = bottleFilling;
        this.filtered = filtered;
    }

    //Without reviewRating and distance - POI to get submitted
    WaterFountainPOI(int id, String name, String desc, Double lat, Double lon, char type, boolean safeToDrinkStraight, boolean bottleFilling, boolean filtered) {
        super(id,  name, desc, lat, lon, type);
        this.safeToDrinkStraight = safeToDrinkStraight;
        this.bottleFilling = bottleFilling;
        this.filtered = filtered;
    }
}
