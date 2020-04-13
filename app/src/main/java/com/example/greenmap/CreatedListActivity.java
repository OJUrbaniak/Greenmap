package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class CreatedListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_list);
    }

    public void goToViewPOI(View view){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        startActivity(intent);
    }
}
