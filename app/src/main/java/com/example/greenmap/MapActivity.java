package com.example.greenmap;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Point;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mapAPI = googleMap;
        LatLng guild = new LatLng(53.405403,-2.966129);
        googleMap.addMarker(new MarkerOptions().position(guild)
                .title("UOL Guild"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(guild));

    }

    /* This is an example method which places a marker on the map *//*
    public void testAddMarker(View view) {
        LatLng liverpool = new LatLng(53.400002, 	-2.983333);
        mapAPI.addMarker(new MarkerOptions().position(liverpool)
                .title("Marker in the pool"));
        mapAPI.moveCamera(CameraUpdateFactory.newLatLng(liverpool));
    }
    */

    public void goToNearbyList(View view){
        Intent intent = new Intent(this, NearbyListActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void goToFilters(View view){
        Intent intent = new Intent(this, FiltersActivity.class);
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

        PointOfInterest(String name, String desc, Double lat, Double lon, int type, int reviewRating) {
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

        WaterFountainPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating, int carbonSaved, boolean safeToDrinkStraight, boolean bottleFilling, boolean filtered) {
            super(name, desc, lat, lon, type, reviewRating);
            this.carbonSaved = carbonSaved;
            this.safeToDrinkStraight = safeToDrinkStraight;
            this.bottleFilling = bottleFilling;
            this.filtered = filtered;
        }
    }

    private class BikeRackPOI extends PointOfInterest {

        boolean covered;

        BikeRackPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating) {
            super(name, desc, lat, lon, type, reviewRating);
        }
    }

    private class RecyclingBinPOI extends PointOfInterest {

        String binType;

        RecyclingBinPOI(String name, String desc, Double lat, Double lon, int type, int reviewRating, String binType) {
            super(name, desc, lat, lon, type, reviewRating);
            this.binType = binType;
        }
    }

}

