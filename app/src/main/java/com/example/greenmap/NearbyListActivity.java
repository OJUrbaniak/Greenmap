package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class NearbyListActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        table = findViewById(R.id.nearbyPOITable);
        PointOfInterest[] data = new PointOfInterest[]{};
        data = (PointOfInterest[]) getIntent().getSerializableExtra("dataArray");
        for (int i = 0; i < data.length; i++) {
            Log.d("CreatedListActivity","Item "+i+": "+data[i].name);
            TableRow tr = new TableRow(this);
            TextView name = new TextView(this); name.setText(data[i].name);
            TextView desc = new TextView(this); desc.setText(data[i].desc);
            Button viewButton = new Button(this);
            viewButton.setText("View POI");
            tr.addView(name);
            tr.addView(desc);
            tr.addView(viewButton);
            table.addView(tr);
        }
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
