package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

public class CreateWaterActivity extends FragmentActivity implements OnMapReadyCallback {

    int carbon_points_saved = 15;

    SupportMapFragment mapFragment;

    TextView nameView;
    CheckBox safeToDrinkStraight;
    CheckBox bottleFilling;

    EditText nameBox;
    EditText descBox;

    Coords location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_water);

//        //Get location from the previous page
//        Intent i = getIntent();
//        location = (Coords)i.getSerializableExtra("Location");

        nameView = findViewById(R.id.nameLabel);
        nameBox = findViewById(R.id.nameBox);
        descBox = findViewById(R.id.descBox);
        safeToDrinkStraight = findViewById(R.id.safeToDrinkStraightCheckBox);
        bottleFilling = findViewById(R.id.bottleTapCheckBox);

        if (getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("User");
            location = getIntent().getExtras().getParcelable("Location");
        }

        nameView.setText(user.username);

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
                    MarkerOptions options = new MarkerOptions().position(latLng).title("New Water Fountain");
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
        try {
            WaterFountainPOI userPOI = new WaterFountainPOI(
                    1,
                    nameBox.getText().toString(),
                    descBox.getText().toString(),
                    location.latitude,
                    location.longitude,
                    'w',
                    safeToDrinkStraight.isChecked(),
                    bottleFilling.isChecked(),
                    false
            );
            // SEND TO DB
            DatabaseInterfaceDBI db = new DatabaseInterfaceDBI();
            //db.insertWaterFountain((float) userPOI.coords.latitude, (float) userPOI.coords.longitude,nameBox.getText().toString(),userPOI.carbonSaved,descBox.getText().toString(),22,safeToDrinkStraight.isChecked(),bottleFilling.isChecked(),false);
        }
        catch (Exception e) {
            // POI couldn't be made
            Toast.makeText(getApplicationContext(), "Couldn't create POI", Toast.LENGTH_SHORT );
        }
    }
}
