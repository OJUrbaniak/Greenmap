package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

public class CreatedListActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_list);
        table = findViewById(R.id.createdPOITable);
        // Extract the array from the Bundle object
        //PointOfInterest[] dataArray = getIntent().getExtras().getarray
        PointOfInterest[] data = new PointOfInterest[]{};
        data = (PointOfInterest[]) getIntent().getSerializableExtra("dataArray");
        for (int i = 0; i < data.length; i++) {
            Log.d("CreatedListActivity","Item "+i+": "+data[i].name);
            TableRow tr = new TableRow(this);
            TextView name = new TextView(this); name.setText(data[i].name);
            tr.addView(name);
            table.addView(tr);
        }
    }

    public void goToViewPOI(View view){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        startActivity(intent);
    }
}
