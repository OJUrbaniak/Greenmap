package com.example.greenmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ViewNearbyPOIActivity  extends FragmentActivity implements OnMapReadyCallback, databaseInteracter {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    PointOfInterest currentPOI;
    User currentUser;
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    Coords currentCoords;
    TextView infoPopUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby_p_o_i);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");
        currentCoords = (Coords)i.getSerializableExtra("Current Coords");
        infoPopUp = findViewById(R.id.infoPopUp);
        infoPopUp.setText("");
        currentPOI = (PointOfInterest) i.getSerializableExtra("currentPOI");
        /*switch (currentPOI.type) {
            case "w":
                dbi.selectWaterPOIbyPOI_ID(currentPOI.id, this);
                break;
            case "b":
                dbi.selectBikePOIbyPOI_ID(currentPOI.id, this);
                break;
            case "r":
                dbi.selectRecyclingPOIbyPOI_ID(currentPOI.id, this);
                break;
        }*/


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Disable any movement of the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        LatLng currLatLng = new LatLng(currentPOI.coords.latitude, currentPOI.coords.longitude);
        //Create a marker
        MarkerOptions marker;
        if(currentPOI.type.equals("w")){
            marker = new MarkerOptions().position(currLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(currentPOI.name);
        } else if(currentPOI.type.equals("b")){
            marker = new MarkerOptions().position(currLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(currentPOI.name);
        } else{
            marker = new MarkerOptions().position(currLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(currentPOI.name);
        }
        googleMap.addMarker(marker);
        //Zoom in on the map
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 17));
        //Add marker on map
    }

    public void goToRate(View view){
        Intent intent = new Intent(this, RatePOIActivity.class);
        intent.putExtra("currentPOI", currentPOI);
        intent.putExtra("User", (Parcelable) currentUser);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent i = getIntent();
        currentCoords = (Coords)i.getSerializableExtra("Current Coords");
        switch (currentPOI.type) {
            case "w":
                dbi.selectWaterPOIbyPOI_ID(currentPOI.id, this);
                break;
            case "b":
                dbi.selectBikePOIbyPOI_ID(currentPOI.id, this);
                break;
            case "r":
                dbi.selectRecyclingPOIbyPOI_ID(currentPOI.id, this);
                break;
        }
    }

    public void usePOI(View view){
        float[] results = new float[1];
        Location.distanceBetween(currentCoords.latitude, currentCoords.longitude, currentPOI.coords.latitude, currentPOI.coords.longitude, results);
        if(results[0] <= 200){
            dbi.updateCarbon_Saved_Points(10, currentUser.userID);
            //used
            Log.i("used", "distance = "+results[0]+ " coords= " +currentCoords.latitude+" " +currentCoords.longitude+" " + currentPOI.coords.latitude+" " + currentPOI.coords.longitude);
            finish();
        } else {
            Log.i("used", "distance = "+results[0]+ "  not used");
            //show error message
            infoPopUp.setText("You are too far from the Point of Interest!");
        }

    }

    @Override
    public void resultsReturned(JsonArray jArray) {
        if (jArray.size() > 0){
            JsonObject jObj = jArray.get(0).getAsJsonObject();
            float rating = (float) jObj.get("Review_Rating").getAsInt() / jObj.get("No_Reviews").getAsInt();
            Log.i("rating", "loaded rating as "+ rating);
            currentPOI = new PointOfInterest(
                    jObj.get("POI_ID").getAsInt(),
                    jObj.get("Name").toString(),
                    jObj.get("Description").toString(),
                    currentPOI.coords.latitude,
                    currentPOI.coords.longitude,
                    currentPOI.type,
                    (float) rating
            );
            if (currentCoords != null){
                Log.i("veiw POI", "location obtained");
            }

            TextView nameLabel = findViewById(R.id.nameInfoLabel);
            TextView typeLabel = findViewById(R.id.typeInfoLabel);
            TextView ratingLabel = findViewById(R.id.ratingInfoLabel);
            TextView descLabel = findViewById(R.id.descInfoLabel);

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
            mapFragment.getMapAsync(this);


            String typeString = "";
            switch (currentPOI.type) {
                case "w":
                    typeString = "Water Fountain";
                    break;
                case "b":
                    typeString = "Bike Rack";
                    break;
                case "r":
                    typeString = "Recycling Bin";
                    break;
            }
            nameLabel.setText(currentPOI.name.replaceAll("\"", ""));
            typeLabel.setText(typeString);
            ratingLabel.setText(String.valueOf(currentPOI.reviewRating));
            descLabel.setText(String.valueOf(currentPOI.desc.replaceAll("\"", "")));
        }
    }
}
