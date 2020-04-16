package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.OnMapReadyCallback;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    User user;
    Button profileButton;
    GoogleMap mapAPI;
    SupportMapFragment mapFragment;
    PointOfInterest[] sampleData = new PointOfInterest[] { new PointOfInterest("Switzerland",	46.818188,8.227512), new PointOfInterest("Ireland", 53.41291,-8.24389	), new PointOfInterest("United Kingdom", 55.378051,-3.435973)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        profileButton = findViewById(R.id.button27);

        //Get user from login
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");

        profileButton.setText(user.username);

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

        for (int i = 0; i < sampleData.length; i++) {
            System.out.println("Plotting Item "+i);
            PointOfInterest curr = sampleData[i];
            mapAPI.addMarker(new MarkerOptions().position(curr.coords)).setTitle(curr.name);
        }
        mapAPI.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.d("Map","CAMERA MOVED to "+mapAPI.getCameraPosition().target);
            }
        });
        //mapAPI.setOnCameraMoveListener(cameraMove());
    }

    public void goToNearbyList(View view){
        Intent intent = new Intent(this, NearbyListActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public void goToFilters(View view){
        Intent intent = new Intent(this, FiltersActivity.class);
        startActivity(intent);
    }
}

