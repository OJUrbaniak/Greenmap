package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

public class CreatePOIActivity extends AppCompatActivity {

    Coords location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_p_o_i);

        //Get location from the previous page
        Intent i = getIntent();
        location = (Coords)i.getSerializableExtra("Location");
    }

    public void goToCreateBike(View view){
        Intent intent = new Intent(this,CreateBikeActivity.class);
        Coords nextLocation = new Coords(location.latitude, location.longitude);
        intent.putExtra("Location", nextLocation);
        startActivity(intent);
    }

    public void goToCreateWater(View view) {
        Intent intent = new Intent(this,CreateWaterActivity.class);
        startActivity(intent);
    }

    public void goToCreateBin(View view){
        Intent intent = new Intent(this,CreateBinActivity.class);
        startActivity(intent);
    }
}
