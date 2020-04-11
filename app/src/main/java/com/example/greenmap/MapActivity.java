package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
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
        LatLng guild = new LatLng(53.405403,-2.966129);
        //V dont work idk why
        //mapAPI.addMarker(new MarkerOptions().position(guild).title("Liverpool guild of students"));
        //mapAPI.moveCamera(CameraUpdateFactory.newLatLng(guild));
    }

    public void goToNearbyList(View view){
        Intent intent = new Intent(this, NearbyList.class);
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void goToFilters(View view){
        Intent intent = new Intent(this, Filters.class);
        startActivity(intent);
    }

}

