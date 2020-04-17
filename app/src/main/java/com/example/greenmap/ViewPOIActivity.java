package com.example.greenmap;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ViewPOIActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_p_o_i);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //
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
