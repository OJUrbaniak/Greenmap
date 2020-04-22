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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class CreatedListActivity extends AppCompatActivity implements databaseInteracter {

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

    public void resultsReturned(JsonArray jArray) { //Plot marker points after receiving them from the database
        if(jArray.size() > 0) {
            float rating;
            for(int n = 0; n < jArray.size(); n++) {
                Log.i("JARRAYS FAT ASS SIZE IS", String.valueOf(jArray.size()));
                Log.i("dbiMap", "POI found");
                JsonObject jObj = jArray.get(n).getAsJsonObject(); //Get the POI object
                //Define attributes for passing user information around front end
                rating = (float) jObj.get("Review_Rating").getAsInt() / jObj.get("No_Reviews").getAsInt();
                //searchResults.add(new PoiSearchInfo(jObj.get("Name").toString(), jObj.get("Description").toString(), jObj.get("Type").getAsString(), rating, jObj.get("Latitude").getAsFloat(), jObj.get("Longitude").getAsFloat(), jObj.get("POI_ID").getAsInt()));

                data.add(new PointOfInterest(
                        jObj.get("POI_ID").getAsInt(),
                        jObj.get("Name").toString(),
                        jObj.get("Description").toString(),
                        (double) jObj.get("Latitude").getAsFloat(),
                        (double) jObj.get("Longitude").getAsFloat(),
                        jObj.get("Type").getAsString()
                ));
                Log.i("dbiMap", "added POI name= "+ jObj.get("Name").toString());
            }
            Log.i("SEARCH RESULTS SIZE TWO DING", String.valueOf(data.size()));
            //Loop round search results and display on map

        } else {
            //no POIs found
        }
    }
}
