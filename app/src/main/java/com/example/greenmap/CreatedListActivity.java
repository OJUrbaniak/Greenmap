package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

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

public class CreatedListActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_list);
        table = findViewById(R.id.createdPOITable);

        PointOfInterest[] data = new PointOfInterest[]{};
        data = (PointOfInterest[]) getIntent().getSerializableExtra("dataArray");

        for (int i = 0; i < data.length; i++) {
            final PointOfInterest currItem = data[i];
            TableRow tr = new TableRow(this);
            TextView name = new TextView(this); name.setText(currItem.name);
            TextView desc = new TextView(this); desc.setText(currItem.desc);
            Button viewButton = new Button(this);
            viewButton.setText("View POI");
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
