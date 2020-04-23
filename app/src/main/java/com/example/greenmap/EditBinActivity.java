package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditBinActivity extends AppCompatActivity {

    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    TextView nameLabel;
    TextView descLabel;
    EditText nameBox;
    EditText descBox;
    PointOfInterest currentPOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bin);
        Intent i = getIntent();
        currentPOI = (PointOfInterest) i.getSerializableExtra("currentPOI");

        nameLabel = findViewById(R.id.nameLabel);
        descLabel = findViewById(R.id.descLabel);
        nameBox = findViewById(R.id.nameBox);
        descBox = findViewById(R.id.descBox);
    }

    public void backToView(View view){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        startActivity(intent);
    }

    public void editClicked(View view){
        //need to fix bin type
        dbi.updateRecyclingBin(nameBox.getText().toString(), descBox.getText().toString(), "bin", currentPOI.id);
    }
}
