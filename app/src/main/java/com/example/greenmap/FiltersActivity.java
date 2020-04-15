package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class FiltersActivity extends AppCompatActivity {

    boolean covered = false;       //defaults
    int rackRange = 0;             //defaults
    int rackMinRating = 0;         //defaults

    boolean drinkingTap = false;   //defaults
    boolean bottleRefill = false;  //defaults
    int tapRange = 100000000;      //defaults
    int tapMinRating = 0;          //defaults

    TextView numberPOI;
    Button saveFiltersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        numberPOI = findViewById(R.id.textView16);
        saveFiltersButton = findViewById(R.id.button4);
    }
}
