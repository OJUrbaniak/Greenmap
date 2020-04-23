package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditWaterActivity extends FragmentActivity implements OnMapReadyCallback {

    Coords location;

    SupportMapFragment mapFragment;

    PointOfInterest currentPOI;
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();

    EditText nameBox;
    EditText descBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_water);
        Intent i = getIntent();
        currentPOI = (PointOfInterest) i.getSerializableExtra("currentPOI");

        nameBox = findViewById(R.id.nameBox);
        descBox = findViewById(R.id.descBox);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    public void backToView(){
//        Intent intent = new Intent(this,CreatedListActivity.class);
//        intent.putExtra("currentPOI", currentPOI);
//        startActivity(intent);
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("map ready", "like my poo hitting the toilet");
        //Disable any movement of the map
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        if (location != null){
            //Sync the map
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.i("map ready", "like my poo hitting the toilet");
                    //Initialise the latitude and longitude
                    LatLng latLng = new LatLng(location.latitude, location.longitude);
                    //Create a marker
                    MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(currentPOI.name);
                    //Zoom in on the map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    //Add marker on map
                    googleMap.addMarker(options);
                }
            });
        }
    }

    public void editClicked(View view){
        //need to fix extras
        dbi.updateWaterFountain(nameBox.getText().toString(), descBox.getText().toString(), true, true, true, currentPOI.id);
        backToView();
    }
}
