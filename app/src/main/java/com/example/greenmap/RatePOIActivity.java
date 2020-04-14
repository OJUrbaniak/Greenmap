package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class RatePOIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_p_o_i);
    }

    public void backToList(View view){
        Intent intent = new Intent(this,NearbyListActivity.class);
        startActivity(intent);
    }
}
