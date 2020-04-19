package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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
            final PointOfInterest currItem = data[i];
            TableRow tr = new TableRow(this);
            TextView name = new TextView(this); name.setText(data[i].name); name.setTextColor(Color.parseColor("#F4F4F4"));
            name.setWidth(100);
            TextView desc = new TextView(this); desc.setText(data[i].desc); name.setTextColor(Color.parseColor("#F4F4F4"));
            desc.setWidth(80);
            Button viewButton = new Button(this);
            viewButton.setText("View POI"); viewButton.setBackgroundColor(Color.parseColor("#777777"));
            viewButton.setTextColor(Color.parseColor("#F4F4F4")); viewButton.setBackground(getDrawable(R.drawable.button_rounded));
            viewButton.setWidth(80);
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

    public void backToMap(View view){
        finish();
    }

    public void goToViewPOI(PointOfInterest currentPOI){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        intent.putExtra("currentPOI", currentPOI);
        startActivity(intent);
    }
}
