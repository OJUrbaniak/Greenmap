package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

public class CreateBikeActivity extends FragmentActivity implements OnMapReadyCallback {

    int carbon_points_saved = 25;

    SupportMapFragment mapFragment;

    TextView nameView;

    Coords location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bike);

        //Get location from the previous page
        //Intent i = getIntent();
        //location = (Coords)i.getSerializableExtra("Location");

        nameView = findViewById(R.id.nameView);

        if (getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("User");
            location = getIntent().getExtras().getParcelable("Location");
        }

        nameView.setText(user.username);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Disable any movement of the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        if (location != null){
            //Sync the map
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    //Initialise the latitude and longitude
                    LatLng latLng = new LatLng(location.latitude, location.longitude);
                    //Create a marker
                    MarkerOptions options = new MarkerOptions().position(latLng).title("New Bike Rack");
                    //Zoom in on the map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    //Add marker on map
                    googleMap.addMarker(options);
                }
            });
        }
    }

    public void backToProfile(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
}

