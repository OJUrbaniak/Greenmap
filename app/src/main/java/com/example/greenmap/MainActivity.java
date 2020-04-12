package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;

import android.content.Intent;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMap(View view){
        //if log in is correct:

        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToSignup(View view){

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

    private class PointOfInterest {
        //Base point of interest class for types to expand upon
        String id; //Set as data is pulled from db, used to keep track of POIs
        String name;
        String desc;
        int type;
        int carbonSaved; //Will have to decide what this is for different POIs and whether it will change over time
        int reviewRating;

        double lat;
        double lon;
        int distanceFromUser; //This gets set later

        public PointOfInterest(String name, String desc, Double lat, Double lon, int type, int reviewRating) {
            this.name = name;
            this.desc = desc;
            this.lat = lat;
            this.lon = lon;
            this.type = type;
            this.reviewRating = reviewRating;
        }
    }

    private class WaterFountainPOI extends PointOfInterest {

        boolean safeToDrinkStraight;
        boolean bottleFilling;
        boolean filtered;

        public WaterFountainPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating, int carbonSaved, boolean safeToDrinkStraight, boolean bottleFilling, boolean filtered) {
            super(name, desc, lat, lon, type, reviewRating);
            this.carbonSaved = carbonSaved;
            this.safeToDrinkStraight = safeToDrinkStraight;
            this.bottleFilling = bottleFilling;
            this.filtered = filtered;
        }
    }

    private class BikeRackPOI extends PointOfInterest {

        boolean covered;

        public BikeRackPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating) {
            super(name, desc, lat, lon, type, reviewRating);
        }
    }

    private class RecyclingBinPOI extends PointOfInterest {

        String binType;

        public RecyclingBinPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating, String binType) {
            super(name, desc, lat, lon, type, reviewRating);
            this.binType = binType;
        }
    }
}
