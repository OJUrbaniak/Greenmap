package com.example.greenmap;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateWaterActivity extends FragmentActivity implements OnMapReadyCallback {

    int carbon_points_saved = 10;

    DatabaseInterfaceDBI DBI = new DatabaseInterfaceDBI();

    SupportMapFragment mapFragment;

    TextView nameView;
    CheckBox safeToDrinkStraight;
    CheckBox bottleFilling;
    CheckBox filtered;

    EditText nameBox;
    EditText descBox;

    Coords location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_water);

        nameView = findViewById(R.id.nameLabel);
        nameBox = findViewById(R.id.nameBox);
        descBox = findViewById(R.id.descBox);
        safeToDrinkStraight = findViewById(R.id.safeToDrinkStraightCheckBox);
        bottleFilling = findViewById(R.id.bottledRefillCheckBox);
        filtered = findViewById(R.id.filteredCheckBox);

        //Get user and location from the previous screen
        if (getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("User");
            location = getIntent().getExtras().getParcelable("Location");
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Disable any movement of the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        if (location != null){
            //Sync the map
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    //Initialise the latitude and longitude
                    LatLng latLng = new LatLng(location.latitude, location.longitude);
                    //Create a marker
                    MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("New Water Fountain");
                    //Zoom in on the map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    //Add marker on map
                    googleMap.addMarker(options);
                }
            });
        }
    }

    public void backToProfile(View view){
        finish();
    }

    public void createPOI(View view) {
        if(nameBox.getText().length() > 0 && descBox.getText().length() > 0){
            try {
                Log.i("Water fountain activity", "Try Started");
                // SEND TO DB
                Log.i("Water fountain activity", "Trying to insert into DB");
                DBI.insertWaterFountain((float)location.latitude, (float)location.longitude, nameBox.getText().toString(), carbon_points_saved,descBox.getText().toString(),
                        user.userID, safeToDrinkStraight.isChecked(), bottleFilling.isChecked(), filtered.isChecked());
            }
            catch (Exception e) {
                Log.i("Water fountain activity", "DB insert failed"+e);
                // POI couldn't be made
                Toast.makeText(getApplicationContext(), "Couldn't create POI", Toast.LENGTH_SHORT );
            }
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("User", (Parcelable) user);
            startActivity(intent);
        }
    }
}
