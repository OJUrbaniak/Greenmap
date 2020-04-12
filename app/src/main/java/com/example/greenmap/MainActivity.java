package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMap(View view){
        //if log in is correct:

        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToSignup(View view){

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

    private class PointOfInterest {
        //Base point of interest class for types to expand upon
        int x;
        int y;
        int distanceFromUser; //This gets set later

        String id;
        String name;
        String desc;
    }
}
