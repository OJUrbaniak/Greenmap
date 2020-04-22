package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class EditBikeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bike);

        Intent i = getIntent();
        PointOfInterest currentPOI = (PointOfInterest) i.getSerializableExtra("currentPOI");
    }

    public void backToView(View view){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        startActivity(intent);
    }
}
