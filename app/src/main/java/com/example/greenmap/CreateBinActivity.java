package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateBinActivity extends FragmentActivity implements OnMapReadyCallback {

    DatabaseInterfaceDBI DBI = new DatabaseInterfaceDBI();

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
        String[] binTypes = new String[]{"Paper", "Glass", "All"};
        ArrayAdapter<String> typesOfBin = new ArrayAdapter<String> (getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, binTypes);
        binType.setAdapter(typesOfBin);

        if (getIntent().getExtras() != null) {
            user = getIntent().getExtras().getParcelable("User");
            location = getIntent().getExtras().getParcelable("Location");
        }

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
                    MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title("New Recyclying Bin");
                    //Zoom in on the map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    //Add marker on map
                    googleMap.addMarker(options);
                }
            });
        }
    }

    public void createPOI(View view) {
        if(nameBox.getText().length() > 0 && descBox.getText().length() > 0){
            try {
                Log.i("Recycling Bin Activity", "Trying to insert into DB started");
                // SEND TO DB
                //CHANGE HARD CODED CARBON SAVED VALUE ASAP
                DBI.insertRecyclingBin((float) location.latitude, (float) location.longitude, nameBox.getText().toString(), 5, descBox.getText().toString(), user.userID, binType.getSelectedItem().toString());
            }
            catch (Exception e) {
                // POI couldn't be made
                Toast.makeText(getApplicationContext(), "Couldn't create POI", Toast.LENGTH_SHORT );
            }
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("User", (Parcelable) user);
            startActivity(intent);
        }
    }
}
