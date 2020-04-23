package com.example.greenmap;
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

import org.w3c.dom.Text;

public class ViewNearbyPOIActivity  extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    PointOfInterest currentPOI;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby_p_o_i);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");

        TextView nameLabel = findViewById(R.id.nameInfoLabel);
        TextView typeLabel = findViewById(R.id.typeInfoLabel);
        TextView ratingLabel = findViewById(R.id.ratingInfoLabel);
        TextView descLabel = findViewById(R.id.descInfoLabel);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);

        currentPOI = (PointOfInterest) i.getSerializableExtra("currentPOI");
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
}
