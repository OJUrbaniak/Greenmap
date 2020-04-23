
package com.example.greenmap;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;

public class RatePOIActivity  extends FragmentActivity implements OnMapReadyCallback, databaseInteracter{

    int rateValue;
    boolean valueChecked;
    Button rateButton;
    int newRating;
    User currentUser;

    SupportMapFragment mapFragment;
    PointOfInterest currentPOI;
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_p_o_i);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");

        TextView nameLabel = findViewById(R.id.nameInfoLabel);
        TextView descLabel = findViewById(R.id.descInfoLabel);
        final TextView infoPopUp = findViewById(R.id.infoPopUp);

        infoPopUp.setText("");

        rateButton = findViewById(R.id.rateButton);

        final CheckBox checkBox1 = findViewById(R.id.checkBox1);
        final CheckBox checkBox2 = findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = findViewById(R.id.checkBox3);
        final CheckBox checkBox4 = findViewById(R.id.checkBox4);
        final CheckBox checkBox5 = findViewById(R.id.checkBox5);
        final CheckBox checkBoxArray[] = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5};


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);

        currentPOI = (PointOfInterest) getIntent().getSerializableExtra("currentPOI");
        nameLabel.setText(nameLabel.getText()+" "+currentPOI.name);
        descLabel.setText(descLabel.getText()+" "+String.valueOf(currentPOI.desc));

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("rateButton Onclicklistenr", "clicked");
                clickedRate(checkBoxArray, infoPopUp);
            }
        });

        //Lord forgive me for this code
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(checkBox1.isChecked()){
                    for(int i = 0; i < checkBoxArray.length; i++){
                        if(checkBoxArray[i] != checkBox1 && checkBoxArray[i].isChecked()){
                            checkBoxArray[i].toggle();
                        }
                    }
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(checkBox2.isChecked()){
                    for(int i = 0; i < checkBoxArray.length; i++){
                        if(checkBoxArray[i] != checkBox2 && checkBoxArray[i].isChecked()){
                            checkBoxArray[i].toggle();
                        }
                    }
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(checkBox3.isChecked()){
                    for(int i = 0; i < checkBoxArray.length; i++){
                        if(checkBoxArray[i] != checkBox3 && checkBoxArray[i].isChecked()){
                            checkBoxArray[i].toggle();
                        }
                    }
                }
            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(checkBox4.isChecked()){
                    for(int i = 0; i < checkBoxArray.length; i++){
                        if(checkBoxArray[i] != checkBox4 && checkBoxArray[i].isChecked()){
                            checkBoxArray[i].toggle();
                        }
                    }
                }
            }
        });
        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(checkBox5.isChecked()){
                    for(int i = 0; i < checkBoxArray.length; i++){
                        if(checkBoxArray[i] != checkBox5 && checkBoxArray[i].isChecked()){
                            checkBoxArray[i].toggle();
                        }
                    }
                }
            }
        });
    } //I know i know im not going to heaven for this, it was late ok

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Disable any movement of the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        LatLng currLatLng = new LatLng(currentPOI.coords.latitude, currentPOI.coords.longitude);
        //Create a marker
        MarkerOptions marker;
        if(currentPOI.type.equals("w")){
            marker = new MarkerOptions().position(currLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(currentPOI.name);
        } else if(currentPOI.type.equals("b")){
            marker = new MarkerOptions().position(currLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(currentPOI.name);
        } else{
            marker = new MarkerOptions().position(currLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(currentPOI.name);
        }
        googleMap.addMarker(marker);
        //Zoom in on the map
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 17));
        //Add marker on map
    }

    public void clickedRate(CheckBox[] checkBoxArray, TextView infoPopUp){
        for(int i = 0; i < checkBoxArray.length; i++){
            if(checkBoxArray[i].isChecked()){
                rateValue = i+1;
                valueChecked = true;
                break;
            } else{
                valueChecked = false;
            }
        }
        if(valueChecked == true){
            //then update the POI in database and go back to the map
            Log.i("rateValue", String.valueOf(rateValue));
            newRating = rateValue;
            dbi.selectRating(currentUser.userID, currentPOI.id, this);
            infoPopUp.setText("Rating saved! Thanks for your feedback!");
        } else{
            //display an error message asking to check a box
            infoPopUp.setText("Please check a box!");
        }
    }

    public void returnToMap(View view){
        finish();
    }

    @Override
    public void resultsReturned(JsonArray jArray) {
        int oldRating;
        oldRating = 0;
        if(jArray.size() > 0){
            oldRating = jArray.get(0).getAsJsonObject().get("Rating").getAsInt();
            dbi.updateRating(newRating, currentPOI.id, currentUser.userID);
        } else{
            dbi.insertRating(currentUser.userID, currentPOI.id, newRating);
        }
        Log.i("Rate", "type = "+currentPOI.type+ " new rating = "+ newRating);
        if(currentPOI.type.equals("w")){
            dbi.updateWaterFountainRating(newRating, oldRating, currentPOI.id);
        }else if (currentPOI.type.equals("b")){
            dbi.updateBikeRackRating(newRating, oldRating, currentPOI.id);
        }else if (currentPOI.type.equals("r")){
            dbi.updateRecyclingBinRating(newRating, oldRating, currentPOI.id);
        } else {
            Log.i("Rate", "wtf");
        }
    }


}
