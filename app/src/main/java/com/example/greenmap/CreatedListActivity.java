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
    PointOfInterest data;
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_list);
        table = findViewById(R.id.createdPOITable);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");
        dbi.selectPOIbyUser_ID(currentUser.userID, this);
    }

    private void goToViewPOI(PointOfInterest currentPOI){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        intent.putExtra("currentPOI", currentPOI);
        startActivity(intent);
    }

    private void deletePOI(int POI_ID){
        dbi.deletePOI(POI_ID);
    }


    public void resultsReturned(JsonArray jArray) { //Plot marker points after receiving them from the database
        if(jArray.size() > 0) {
            float rating;
            for(int n = 0; n < jArray.size(); n++) {
                Log.i("CreatedList", "POI found");
                JsonObject jObj = jArray.get(n).getAsJsonObject(); //Get the POI object
                //Define attributes for passing user information around front end
                rating = (float) jObj.get("Review_Rating").getAsInt() / jObj.get("No_Reviews").getAsInt();
                //searchResults.add(new PoiSearchInfo(jObj.get("Name").toString(), jObj.get("Description").toString(), jObj.get("Type").getAsString(), rating, jObj.get("Latitude").getAsFloat(), jObj.get("Longitude").getAsFloat(), jObj.get("POI_ID").getAsInt()));

                data=(new PointOfInterest(
                        jObj.get("POI_ID").getAsInt(),
                        jObj.get("Name").toString(),
                        jObj.get("Description").toString(),
                        (double) jObj.get("Latitude").getAsFloat(),
                        (double) jObj.get("Longitude").getAsFloat(),
                        jObj.get("Type").getAsString()
                ));

                Log.i("CreatedList", "added POI name= "+ jObj.get("Name").toString());
                //add the POIs to the table
                TableRow tr = new TableRow(this);
                TextView name = new TextView(this); name.setText(jObj.get("Name").toString()); name.setTextColor(Color.parseColor("#F4F4F4")); name.setWidth(150);
                TextView desc = new TextView(this); desc.setText(jObj.get("Description").toString()); desc.setTextColor(Color.parseColor("#F4F4F4")); desc.setWidth(150);
                Button viewButton = new Button(this);
                Button delete = new Button(this);

                viewButton.setText("Edit POI"); viewButton.setBackgroundColor(Color.parseColor("#777777"));
                viewButton.setTextColor(Color.parseColor("#F4F4F4")); viewButton.setBackground(getDrawable(R.drawable.button_rounded));
                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToViewPOI(data);
                    }
                });
                viewButton.setWidth(50);

                final int user_id= data.id;
                delete.setText("Delete"); delete.setBackgroundColor(Color.parseColor("#777777"));
                delete.setTextColor(Color.parseColor("#922a31")); delete.setBackground(getDrawable(R.drawable.button_rounded));
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePOI(user_id);
                    }
                }); delete.setWidth(50);


                tr.addView(name);
                tr.addView(desc);
                tr.addView(viewButton);
                tr.addView(delete);
                table.addView(tr);
            }

        } else {
            //no POIs found
        }
    }
}
