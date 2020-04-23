package com.example.greenmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CancellationSignal;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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

public class MapActivity extends FragmentActivity implements GoogleMap.OnCameraMoveListener, OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, databaseInteracter {

    User user;
    LatLng cameraLoc;
    Button profileButton;
    Button confirmButton;
    Button moreInfoButton;
    FloatingActionButton refreshButton;
    FloatingActionButton personButton;
    GoogleMap mapAPI;
    FloatingActionButton plotButton;

    boolean userMarkerPlaced = false;
    Marker userMarker;
    boolean animationFinished = false;

    final databaseInteracter dbInt = this;
    boolean userLoc;

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
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();

    PointOfInterest markerSelected;

    boolean called;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        called = false;

        userLoc = true;
        Log.i("MAP ACTIVITY CREATED", "IM CREATED YALL");

        try {
            Log.i("Tag", "Using Map Version: " + getPackageManager().getPackageInfo("com.google.android.gms",0).versionName);
        } catch (Exception e) {}

        //Load in the users preferences (or the default ones) and pull the necessary points from the DB to show on map
        loadPreferences();

        //Load markers based on users saved preferences

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        profileButton = findViewById(R.id.button27);
        plotButton = findViewById(R.id.floatingAddButton);
        confirmButton = findViewById(R.id.button3);
        moreInfoButton = findViewById(R.id.moreInfoButton);
        refreshButton = findViewById(R.id.refreshButton);
        personButton = findViewById(R.id.personFloatingButton);

        confirmButton.setVisibility(View.GONE);
        moreInfoButton.setVisibility(View.GONE);
        personButton.setVisibility(View.GONE);

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

    @Override
    protected void onResume(){
        super.onResume();
        called = false;
        userLoc = true;
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
                    currentLocation = location;
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialise the latitude and longitude
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //Create a marker
                            MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Current Location");
                            //Zoom in on the map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            //Add marker on map
                            googleMap.addMarker(options);
                        }
                    });
                    Log.i("map", "calling for POI with user Location");
                    if (called == false) {
                        if (userPref.showTaps) {
                            dbi.selectWaterPOIs((float) location.getLatitude(), (float) location.getLongitude(), userPref.range, userPref.minRating, userPref.tapBottleRefill, userPref.drinkingTap, userPref.tapFiltered, dbInt);
                        }
                        if (userPref.showBins) {
                            dbi.selectRecyclingPOIs((float) location.getLatitude(), (float) location.getLongitude(), userPref.range, userPref.minRating, dbInt);
                        }
                        if (userPref.showRacks) {
                            dbi.selectBikePOIs((float) location.getLatitude(), (float) location.getLongitude(), userPref.range, userPref.minRating, userPref.rackCovered, dbInt);
                        }
                        called = true;
                    }
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
        intent.putExtra("User", (Parcelable) user);
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("User", (Parcelable) user);
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

    int cameraAnimation = 0;

    @Override
    public void onCameraMove() {
        cameraLoc = mapAPI.getCameraPosition().target;
        //Log.d("CMOVE","Camera moved, lat "+cameraLoc.latitude + " lon "+cameraLoc.longitude);
        if(animationFinished == true) {
            personButton.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MAP READY", "IM READY YALL");
        mapAPI = googleMap;
        mapAPI.setOnCameraMoveStartedListener(this);
        mapAPI.setOnCameraMoveListener(this);
        mapAPI.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("Google Map", "Clicked");
                markerSelected = null;
                moreInfoButton.setVisibility(View.GONE);
            }
        });

        mapAPI.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("Google Map Marker", "Clicked");
                if(marker.getTitle().equals("New POI Location")|| marker.getTitle().equals("Current Location")|| marker.getTitle().equals("Around here")){
                    moreInfoButton.setVisibility(View.GONE);
                    markerSelected = null;
                } else {
                    LatLng pos = marker.getPosition();
                    markerSelected = returnPointOfInterest(pos);
                    Log.i("Marker selecteds name is", markerSelected.name);
                    moreInfoButton.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

    public PointOfInterest returnPointOfInterest(LatLng position){
        PointOfInterest retVal = null;
        for(PointOfInterest d : data){
            if(d.coords.latitude == position.latitude && d.coords.longitude == position.longitude) {
                retVal = d;
                break;
            }
        }
        return retVal;
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
            float[] results = new float[1];
            float lng = 0f;
            float lat =0f;
            float currentLat;
            float currentLng;
            if (userLoc == true){
                Log.i("userLoc", "true" );
                currentLat = (float) currentLocation.getLatitude();
                currentLng = (float) currentLocation.getLongitude();
            } else {
                Log.i("userLoc", "false" );
                LatLng refreshCameraLoc = mapAPI.getCameraPosition().target;
                currentLat = (float) refreshCameraLoc.latitude;
                currentLng = (float) refreshCameraLoc.longitude;
            }

            for(int n = 0; n < jArray.size(); n++) {
                Log.i("JARRAYS FAT ASS SIZE IS", String.valueOf(jArray.size()));
                Log.i("dbiMap", "POI found");
                JsonObject jObj = jArray.get(n).getAsJsonObject(); //Get the POI object
                //Define attributes for passing user information around front end
                rating = (float) jObj.get("Review_Rating").getAsInt() / jObj.get("No_Reviews").getAsInt();
                lat = (float) jObj.get("Latitude").getAsFloat();
                lng = (float) jObj.get("Longitude").getAsFloat();


                Log.i("distance", "current location lat = " +currentLat + " long = "+currentLng );
                float[] distance = new float[1];
                Location.distanceBetween(currentLat, currentLng, lat, lng, distance);
                Log.i("distance", "POI name = " +jObj.get("Name").toString() + " distance = "+distance[0]+ " lat= " +lat+ " lng = "+ lng );
                if (distance[0]<= userPref.range) {
                    PointOfInterest newPOI = new PointOfInterest(
                            jObj.get("POI_ID").getAsInt(),
                            jObj.get("Name").toString(),
                            jObj.get("Description").toString(),
                            (double) lat,
                            (double)lng,
                            jObj.get("Type").getAsString(),
                            (float) rating
                    );
                    data.add(newPOI);
                    Log.i("dbiMap", "added POI name= " + jObj.get("Name").toString());
                    Log.i("POI search info class check", data.get(n).name);

                    MarkerOptions marker;
                    //Initialise the latitude and longitude
                    LatLng latLng = new LatLng(newPOI.coords.latitude, newPOI.coords.longitude);
                    //Create a marker
                    if (newPOI.type.equals("w")) {
                        marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(newPOI.name.replaceAll("\"", ""));
                    } else if (newPOI.type.equals("b")) {
                        marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(newPOI.name.replaceAll("\"", ""));
                    } else {
                        marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(newPOI.name.replaceAll("\"", ""));
                    }
                    Log.i("Adding marker", newPOI.name);
                    mapAPI.addMarker(marker); //Add marker on map
                    Log.i("MAP MARKER ADDED", "added");
                }

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

    public void toMoreInfo(View view){
        Intent intent = new Intent(this, ViewNearbyPOIActivity.class);
        intent.putExtra("currentPOI", markerSelected);
        intent.putExtra("User", (Parcelable) user);
        intent.putExtra("Current Coords", (Parcelable) new Coords(currentLocation.getLatitude(), currentLocation.getLongitude()));

        startActivity(intent);
    }

    boolean refreshMarkerPlaced = false;    //Keeps track of whether the refresh button's marker has been placed
    Marker locationMarker;                  //The marker placed when the user presses the refresh button in another location
    //LatLng oldLoc;                //Keeps track of the camera location when the user presses the refresh button

    public void refreshMap(View view) {
        userLoc = false;
        LatLng refreshCameraLoc = mapAPI.getCameraPosition().target;
        if (refreshCameraLoc != cameraLoc) {
            personButton.setVisibility(View.VISIBLE);
            //Refresh button needs to get coords from middle of camera
            refreshCameraLoc = mapAPI.getCameraPosition().target;
            //Initialise the latitude and longitude
            LatLng latLng = new LatLng(cameraLoc.latitude, cameraLoc.longitude);
            //Create a marker
            MarkerOptions options = new MarkerOptions().position(cameraLoc).title("Position");

            if (refreshMarkerPlaced == true) {
                locationMarker.remove();
            }

            locationMarker = mapAPI.addMarker(options);
            //Need to send cameraLoc to db to get POIS
            DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
            mapAPI.clear();
            data.clear();
            mapAPI.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Current Location"));
            mapAPI.addMarker(new MarkerOptions()
                    .position(new LatLng(refreshCameraLoc.latitude, refreshCameraLoc.longitude))
                    .title("Around here")
            );
            //Load markers based on users saved preferences
            if (userPref.showTaps) {
                dbi.selectWaterPOIs((float) refreshCameraLoc.latitude, (float) refreshCameraLoc.longitude, userPref.range, userPref.minRating, userPref.tapBottleRefill, userPref.drinkingTap, userPref.tapFiltered, this);
            }
            if (userPref.showBins) {
                dbi.selectRecyclingPOIs((float) refreshCameraLoc.latitude, (float) refreshCameraLoc.longitude, userPref.range, userPref.minRating, this);
            }
            if (userPref.showRacks) {
                dbi.selectBikePOIs((float) refreshCameraLoc.latitude, (float) refreshCameraLoc.longitude, userPref.range, userPref.minRating, userPref.rackCovered, this);
            }

            refreshMarkerPlaced = true;
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),"Move the map to use this button!",Toast.LENGTH_SHORT);
            personButton.setVisibility(View.GONE);
        }
    }

    public void returnToUser(View view) {
        if (locationMarker != null) locationMarker.remove();
        refreshMarkerPlaced = false;
        Log.d("UserLocation","Current location lat "+currentLocation.getLatitude()+" lon"+currentLocation.getLongitude());
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        mapAPI.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        personButton.setVisibility(View.GONE);
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            personButton.setVisibility(View.VISIBLE);
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
        }
    }
}

