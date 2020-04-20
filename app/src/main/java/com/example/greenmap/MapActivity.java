package com.example.greenmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Parcelable;
import android.util.Log;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MapActivity extends FragmentActivity implements GoogleMap.OnCameraMoveListener, OnMapReadyCallback, databaseInteracter {

    ArrayList<PoiSearchInfo> searchResults  = new ArrayList<PoiSearchInfo>();;
    User user;
    LatLng cameraLoc;
    Button profileButton;
    Button confirmButton;
    GoogleMap mapAPI;
    FloatingActionButton plotButton;
    boolean userMarkerPlaced = false;
    Marker userMarker;

    SupportMapFragment mapFragment;
    PointOfInterest[] data = new PointOfInterest[] {
            new WaterFountainPOI(0,"Switzerland","mountains", 46.818188,8.227512,'w',5,true,true,true,200),
            new BikeRackPOI(1,"Ireland","trouble",53.41291,-8.24389,'b',3,50),
            new RecyclingBinPOI(2,"United Kingdom","boris",55.378051,-3.435973,'b',2,"ok",100)
//            new PointOfInterest("Switzerland",'r',	46.818188,8.227512),
//            new PointOfInterest("Ireland", 'w',53.41291,-8.24389	),
//            new PointOfInterest("United Kingdom", 'b',55.378051,-3.435973)
    };

    //For user location
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TESTING
        DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
        dbi.insertWaterFountain(53.4061f ,-2.9643f, "JoeTest", 20, "wadeeb full", 5, true, true, true);
        dbi.selectWaterPOIs(53.4053f, -2.9660f, 1000, true, true, true, this);
        //TESTING

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
                            MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title("Current location");
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
        startActivity(intent);
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
        Log.d("CMOVE","Camera moved, lat "+cameraLoc.latitude + " lon "+cameraLoc.longitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapAPI = googleMap;
        mapAPI.setOnCameraMoveListener(this);
    }

    public void placeMarker(View view) {
        if (cameraLoc != null) {
            Log.d("CMOVE","Placing point");
            MarkerOptions cameraCenter = new MarkerOptions().position(cameraLoc).title("POI Location");
            if (userMarkerPlaced == false) {
                confirmButton.setVisibility(View.VISIBLE);
                userMarkerPlaced = true;
                userMarker = mapAPI.addMarker(cameraCenter);
                //plotButton.setImageDrawable();
                plotButton.setImageResource(R.drawable.ic_clear_black_24dp);
                //plotButton.setImageDrawable(R.drawable.ic_add_black_24dp);
            }
            else {
                confirmButton.setVisibility(View.GONE);
                userMarkerPlaced = false;
                userMarker.remove();
                plotButton.setImageResource(R.drawable.ic_add_black_24dp);
            }
        }
    }

    @Override
    public void resultsReturned(JsonArray jArray) { //Plot marker points after receiving them from the database
        if(jArray.size() > 0) {
            float rating;
            for(int n = 0; n < jArray.size(); n++) {
                Log.i("dbiMap", "POI found");
                JsonObject jObj = jArray.get(0).getAsJsonObject(); //Get the POI object
                //Define attributes for passing user information around front end
                rating = (float) jObj.get("Review_Rating").getAsInt() / jObj.get("No_Reviews").getAsInt();
                searchResults.add(new PoiSearchInfo(jObj.get("Name").toString(), jObj.get("Description").toString(), jObj.get("Type").getAsString(), rating, jObj.get("Latitude").getAsFloat(), jObj.get("Longitude").getAsFloat(), jObj.get("POI_ID").getAsInt()));
                Log.i("dbiMap", "added POI name= "+ jObj.get("Name").toString());
                Log.i("POI search info class check", searchResults.get(0).Name);
            }
            Log.i("SEARCH RESULTS SIZE TWO DING", String.valueOf(searchResults.size()));
            //Loop round search results and display on map
            for(int i = 0; i < searchResults.size(); i++) {
                //Initialise the latitude and longitude
                LatLng latLng = new LatLng(searchResults.get(i).Lat, searchResults.get(i).Lng);
                //Create a marker
                if(searchResults.get(i).Type.equals("w")){
                    MarkerOptions waterMarker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(searchResults.get(i).Name);
                    mapAPI.addMarker(waterMarker); //Add marker on map
                }
            }
        } else {
            //no POIs found
        }
    }
}

