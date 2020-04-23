package com.example.greenmap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditBikeActivity extends FragmentActivity implements OnMapReadyCallback {

    Coords location;

    SupportMapFragment mapFragment;

    PointOfInterest currentPOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bike);

        Intent i = getIntent();
        currentPOI = (PointOfInterest) i.getSerializableExtra("currentPOI");

        location = currentPOI.coords;

        DatabaseInterfaceDBI DBI = new DatabaseInterfaceDBI();

        TextView nameLabel = findViewById(R.id.nameLabel);
        TextView descLabel = findViewById(R.id.descLabel);
        EditText nameBox = findViewById(R.id.nameBox);
        EditText descBox = findViewById(R.id.descBox);

        Button confirmbutton = findViewById(R.id.button32);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);

    }

    public void backToView(View view){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("map ready", "like my poo hitting the toilet");
        //Disable any movement of the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        if (location != null){
            //Sync the map
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.i("map ready", "like my poo hitting the toilet");
                    //Initialise the latitude and longitude
                    LatLng latLng = new LatLng(location.latitude, location.longitude);
                    //Create a marker
                    MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(currentPOI.name);
                    //Zoom in on the map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    //Add marker on map
                    googleMap.addMarker(options);
                }
            });
        }
    }

    public void editClicked(View view){
        //Update in the DB
    }
}
