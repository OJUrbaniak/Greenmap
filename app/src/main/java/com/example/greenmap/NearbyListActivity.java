package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;


public class NearbyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
    }

    public void backToMap(View view){
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }

    public void goToRate(View view){
        Intent intent = new Intent(this,RatePOIActivity.class);
        startActivity(intent);
    }

    public void goToReport(View view){
        Intent intent = new Intent(this,ReportPOIActivity.class);
        startActivity(intent);
    }
}
