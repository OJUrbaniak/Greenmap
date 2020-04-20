package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateBinActivity extends FragmentActivity implements OnMapReadyCallback {

    int carbon_points_saved = 10;

    SupportMapFragment mapFragment;

    TextView nameView;

    EditText nameBox;
    EditText descBox;

    Spinner binType;

    Coords location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bin);

        //Finding components
        nameView = findViewById(R.id.nameView);

        nameBox = findViewById(R.id.nameBox);
        descBox = findViewById(R.id.descBox);
        binType = findViewById(R.id.binTypeSpinner);

        //Setting up binType spinner
        String[] binTypes = new String[]{"Recycling, Other"};
        ArrayAdapter<String> typesOfBin = new ArrayAdapter<String> (getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, binTypes);
        binType.setAdapter(typesOfBin);

        if (getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("User");
            location = getIntent().getExtras().getParcelable("Location");
        }

        nameView.setText(user.username);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);

    }

    public void backToProfile(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
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
                    MarkerOptions options = new MarkerOptions().position(latLng).title("New Recycling Bin");
                    //Zoom in on the map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    //Add marker on map
                    googleMap.addMarker(options);
                }
            });
        }
    }

    public void createPOI(View view) {
        try {
            RecyclingBinPOI userPOI = new RecyclingBinPOI(
                    1,
                    nameBox.getText().toString(),
                    descBox.getText().toString(),
                    location.latitude,
                    location.longitude,
                    'w',
                    binType.getSelectedItem().toString()
            );
            // SEND TO DB
            DatabaseInterfaceDBI db = new DatabaseInterfaceDBI();
            db.insertRecyclingBin((float) userPOI.coords.latitude, (float) userPOI.coords.longitude, nameBox.getText().toString(), userPOI.carbonSaved, descBox.getText().toString(), 20, binType.getSelectedItem().toString());
        }
        catch (Exception e) {
            // POI couldn't be made
            Toast.makeText(getApplicationContext(), "Couldn't create POI", Toast.LENGTH_SHORT );
        }
    }
}
