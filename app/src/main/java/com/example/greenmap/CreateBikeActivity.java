package com.example.greenmap;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateBikeActivity extends FragmentActivity implements OnMapReadyCallback {

    int carbon_points_saved = 25;

    databaseInterface DBI = new databaseInterface();

    SupportMapFragment mapFragment;

    TextView nameView;
    TextView descLabel;
    EditText nameBox;
    EditText descBox;
    CheckBox coveredCheckBox;
    Button createButton;

    Coords location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bike);

        //Get location from the previous page
        //Intent i = getIntent();
        //location = (Coords)i.getSerializableExtra("Location");

        nameView = findViewById(R.id.nameLabel);
        descLabel = findViewById(R.id.descLabel);
        nameBox = findViewById(R.id.nameBox);
        descBox = findViewById(R.id.descBox);
        coveredCheckBox = findViewById(R.id.safeToDrinkStraightCheckBox);
        createButton = findViewById(R.id.createButton);

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
                    MarkerOptions options = new MarkerOptions().position(latLng).title("New Bike Rack");
                    //Zoom in on the map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    //Add marker on map
                    googleMap.addMarker(options);
                }
            });
        }
    }

    /*
    public void createPressed(View view){
        //Check the information boxes have been filled
        if(nameBox.getText().length() > 0 && descBox.getText().length() > 0) {
            //If so then insert into the database
            //DBI.insertUser("HADOOB", "SHRONGONE", "PASSMYASS", 25);
            //DBI.insertAdmin(5, 2);
            //DBI.insertBikeRack((float) location.latitude, (float) location.longitude, user.userID, nameBox.getText().toString(), carbon_points_saved, descBox.getText().toString(), coveredCheckBox.isChecked());
            //DBI.insertRecyclingBin(50.30f, 70.30f, user.userID, nameBox.getText().toString(), carbon_points_saved, descBox.getText().toString(), "hobobin");
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, MapActivity.class);
            bundle.putParcelable("Location", location);
            bundle.putParcelable("User", user);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    */

    public void createPOI(View view) {
        try {
            BikeRackPOI userPOI = new BikeRackPOI(
                    1,
                    nameBox.getText().toString(),
                    descBox.getText().toString(),
                    location.latitude,
                    location.longitude,
                    'w',
                    coveredCheckBox.isChecked()
            );
            // SEND TO DB
            databaseInterface db = new databaseInterface();
            //db.insertBikeRack(userPOI);
        }
        catch (Exception e) {
            // POI couldn't be made
            Toast.makeText(getApplicationContext(), "Couldn't create POI", Toast.LENGTH_SHORT );
        }
    }
}

