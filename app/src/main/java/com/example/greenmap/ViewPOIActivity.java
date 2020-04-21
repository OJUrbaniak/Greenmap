package com.example.greenmap;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class ViewPOIActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    PointOfInterest currentPOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_p_o_i);

        TextView nameLabel = findViewById(R.id.nameLabel);
        TextView extrasLabel = findViewById(R.id.extrasLabel);
        TextView typeLabel = findViewById(R.id.typeLabel);
        TextView ratingLabel = findViewById(R.id.ratingLabel);
        TextView descLabel = findViewById(R.id.descLabel);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);

        currentPOI = (PointOfInterest) getIntent().getSerializableExtra("currentPOI");
        nameLabel.setText(nameLabel.getText()+" "+currentPOI.name);
        //extrasLabel.setText(currentPOI.name);
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
        typeLabel.setText(typeLabel.getText()+" "+ typeString);
        ratingLabel.setText(ratingLabel.getText()+" "+String.valueOf(currentPOI.reviewRating));
        descLabel.setText(descLabel.getText()+" "+String.valueOf(currentPOI.desc));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Disable any movement of the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        LatLng currLatLng = new LatLng(currentPOI.coords.latitude, currentPOI.coords.longitude);
        //Create a marker
        MarkerOptions marker = new MarkerOptions().position(currLatLng).title(currentPOI.name);
        googleMap.addMarker(marker);
        //Zoom in on the map
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 17));
        //Add marker on map
    }

    public void goToEdit(View view){
        //if POI type is bike
        Intent intent = new Intent(this, EditBikeActivity.class);
        //if type is bin
        // Intent intent = new Intent(this, EditBinActivity.class);
        //if type is water
        // Intent intent = new Intent(this, EditWaterActivity.class);
        startActivity(intent);

    }

    public void goToDelete(View view){
        Intent intent = new Intent(this,DeletePOIActivity.class);
        startActivity(intent);
    }

}
