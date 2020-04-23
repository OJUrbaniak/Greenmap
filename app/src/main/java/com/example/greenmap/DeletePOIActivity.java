package com.example.greenmap;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class DeletePOIActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_p_o_i);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //
    }

    public void backToView(View view){
        Intent intent = new Intent(this,CreatedListActivity.class);
        startActivity(intent);
    }

    public void deletePOI(View view){
        backToView(view);
    }
}
