package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class CreatePOIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_p_o_i);
    }

    public void goToCreateBike(View view){
        Intent intent = new Intent(this,CreateBikeActivity.class);
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
