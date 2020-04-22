package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CreatedListActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_list);
        table = findViewById(R.id.createdPOITable);

        ArrayList<PointOfInterest> data = new ArrayList<>();
        data = (ArrayList<PointOfInterest>) getIntent().getSerializableExtra("userCreatedPOIArray");

        for (final PointOfInterest currItem : data) {
            TableRow tr = new TableRow(this);
            TextView name = new TextView(this); name.setText(currItem.name); name.setTextColor(Color.parseColor("#F4F4F4"));
            TextView desc = new TextView(this); desc.setText(currItem.desc); desc.setTextColor(Color.parseColor("#F4F4F4"));
            Button viewButton = new Button(this);
            viewButton.setText("View POI"); viewButton.setBackgroundColor(Color.parseColor("#777777"));
            viewButton.setTextColor(Color.parseColor("#F4F4F4")); viewButton.setBackground(getDrawable(R.drawable.button_rounded));
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToViewPOI(currItem);
                }
            });
            tr.addView(name);
            tr.addView(desc);
            tr.addView(viewButton);
            table.addView(tr);
        }
    }

    public void goToViewPOI(PointOfInterest currentPOI){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        intent.putExtra("currentPOI", currentPOI);
        startActivity(intent);
    }
}
