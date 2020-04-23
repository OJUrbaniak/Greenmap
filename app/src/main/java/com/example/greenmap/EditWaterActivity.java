package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditWaterActivity extends AppCompatActivity {

    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    TextView nameLabel;
    TextView descLabel;
    EditText nameBox;
    EditText descBox;
    PointOfInterest currentPOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_water);
        Intent i = getIntent();
        currentPOI = (PointOfInterest) i.getSerializableExtra("currentPOI");

        nameLabel = findViewById(R.id.nameLabel);
        descLabel = findViewById(R.id.descLabel);
        nameBox = findViewById(R.id.nameBox);
        descBox = findViewById(R.id.descBox);
    }

    public void backToView(){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        intent.putExtra("currentPOI", currentPOI);
        startActivity(intent);
    }

    public void editClicked(View view){
        //need to fix extras
        dbi.updateWaterFountain(nameBox.getText().toString(), descBox.getText().toString(), true, true, true, currentPOI.id);
        backToView();
    }
}
