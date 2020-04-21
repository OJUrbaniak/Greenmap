package com.example.greenmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MapActivity extends FragmentActivity implements GoogleMap.OnCameraMoveListener, OnMapReadyCallback, databaseInteracter {

    User user;
    LatLng cameraLoc;
    Button profileButton;
    Button confirmButton;
    GoogleMap mapAPI;
    FloatingActionButton plotButton;
    boolean userMarkerPlaced = false;
    Marker userMarker;

    Preferences userPref;
    SharedPreferences pref = null;

    SupportMapFragment mapFragment;
    ArrayList<PointOfInterest> data = new ArrayList<PointOfInterest> ();

    //For user location
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MAP ACTIVITY CREATED", "IM CREATED YALL");

        //Load in the users preferences (or the default ones) and pull the necessary points from the DB to show on map
        loadPreferences();
        Log.i("UserPref values", String.valueOf(userPref.drinkingTap)+String.valueOf(userPref.range)+String.valueOf(userPref.minRating));
        DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
        //Inital load of markers based on users saved preferences
        dbi.selectWaterPOIs(53.4053f, -2.9660f, userPref.range, userPref.tapBottleRefill, userPref.drinkingTap, userPref.tapFiltered, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        profileButton = findViewById(R.id.button27);
        plotButton = findViewById(R.id.floatingAddButton);
        confirmButton = findViewById(R.id.button3);

        confirmButton.setVisibility(View.GONE);

        //Get user from the previous page
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");

        profileButton.setText(user.username);

        //Initialise
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        client = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment.getMapAsync(MapActivity.this);
        //Check permission for location
        if(ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //When permission is granted
            //Call method
            getCurrentLocation();
        } else{
            //When permission is denied
            //Request permission
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {
        //Initialize task location
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //When successful
                if (location != null){
                    //Sync the map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialise the latitude and longitude
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //Create a marker
                            MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Current location");
                            //Zoom in on the map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            //Add marker on map
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //When permission granted
                //Call method
                getCurrentLocation();
            }
        }
    }

    //Disable back button being pressed - dont wanna go back to login/sign up
    @Override
    public void onBackPressed() { }

    //!--Movement between views functions--!
    public void goToNearbyList(View view){
        Intent intent = new Intent(this, NearbyListActivity.class);
        intent.putExtra("dataArray", data); //data will store the POIs retrieved from DB
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("User", (Parcelable) user);
        intent.putExtra("dataArray", data); //This then goes to CreatedListActivity, dataArray will need to be changed to an array of the USER CREATED POIs
        startActivity(intent);
    }

    public void goToFilters(View view){
        Intent intent = new Intent(this, FiltersActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Log.i("onActivityResult", "RESULT OK");
            Intent refresh = new Intent(this, MapActivity.class);
            refresh.putExtra("User", (Parcelable) user);
            startActivity(refresh);
            this.finish();
        }
    }

    public void goToCreatePOI(View view){
        Intent intent = new Intent(this, CreatePOIActivity.class);
        Bundle bundle = new Bundle();
        Coords location = new Coords(cameraLoc.latitude, cameraLoc.longitude);
        bundle.putParcelable("Location", location);
        bundle.putParcelable("User", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCameraMove() {
        cameraLoc = mapAPI.getCameraPosition().target;
        //Log.d("CMOVE","Camera moved, lat "+cameraLoc.latitude + " lon "+cameraLoc.longitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MAP READY", "IM READY YALL");
        mapAPI = googleMap;
        mapAPI.setOnCameraMoveListener(this);
        for(PointOfInterest currentI : data) {
            MarkerOptions marker;
            //Initialise the latitude and longitude
            LatLng latLng = new LatLng(currentI.coords.latitude, currentI.coords.longitude);
            //Create a marker
            if(currentI.type.equals("w")){
                marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(currentI.name);
            } else if(currentI.type.equals("b")){
                marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(currentI.name);
            } else{
                marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(currentI.name);
            }
            Log.i("Adding marker", currentI.name);
            mapAPI.addMarker(marker); //Add marker on map
            Log.i("MAP MARKER ADDED", "added");
        }
    }

    public void placeMarker(View view) {
        if (cameraLoc != null) {
            Log.d("CMOVE","Placing point");
            MarkerOptions cameraCenter = new MarkerOptions().position(cameraLoc).title("New POI Location");
            if (userMarkerPlaced == false) {
                confirmButton.setVisibility(View.VISIBLE);
                userMarkerPlaced = true;
                userMarker = mapAPI.addMarker(cameraCenter);
                //plotButton.setImageDrawable();
                plotButton.setImageResource(R.drawable.ic_clear_black_24dp);
                //plotButton.setImageDrawable(R.drawable.ic_add_black_24dp);
            }
            else {
                Log.i("CAMERA LOC", "IS NOT NULL BUT USERMARKERPLACED IS TRUE");
                confirmButton.setVisibility(View.GONE);
                userMarkerPlaced = false;
                userMarker.remove();
                plotButton.setImageResource(R.drawable.ic_add_black_24dp);
            }
        } else {
            Log.i("CAMERA LOC", "IS NULL");
        }
    }

    @Override
    public void resultsReturned(JsonArray jArray) { //Plot marker points after receiving them from the database
        if(jArray.size() > 0) {
            float rating;
            for(int n = 0; n < jArray.size(); n++) {
                Log.i("JARRAYS FAT ASS SIZE IS", String.valueOf(jArray.size()));
                Log.i("dbiMap", "POI found");
                JsonObject jObj = jArray.get(n).getAsJsonObject(); //Get the POI object
                //Define attributes for passing user information around front end
                rating = (float) jObj.get("Review_Rating").getAsInt() / jObj.get("No_Reviews").getAsInt();
                //searchResults.add(new PoiSearchInfo(jObj.get("Name").toString(), jObj.get("Description").toString(), jObj.get("Type").getAsString(), rating, jObj.get("Latitude").getAsFloat(), jObj.get("Longitude").getAsFloat(), jObj.get("POI_ID").getAsInt()));

                data.add(new PointOfInterest(
                        jObj.get("POI_ID").getAsInt(),
                        jObj.get("Name").toString(),
                        jObj.get("Description").toString(),
                        (double) jObj.get("Latitude").getAsFloat(),
                        (double) jObj.get("Longitude").getAsFloat(),
                        jObj.get("Type").getAsString()
                ));
                Log.i("dbiMap", "added POI name= "+ jObj.get("Name").toString());
                Log.i("POI search info class check", data.get(n).name);
            }
            Log.i("SEARCH RESULTS SIZE TWO DING", String.valueOf(data.size()));
            //Loop round search results and display on map

        } else {
            //no POIs found
        }
    }

    private void loadPreferences() {
        pref = getSharedPreferences("com.example.greenmap", 0);
        if (pref.contains("userPref")) {
            Log.d("PREF-LOAD","Preferences detected");
            Gson gson = new Gson();
            String json = pref.getString("userPref","");
            Preferences deviceUserPref = gson.fromJson(json, Preferences.class);
            userPref = deviceUserPref;
        }
        else {
            Log.d("PREF-LOAD","No preferences detected");
            userPref = new Preferences();
        }
    }
}

