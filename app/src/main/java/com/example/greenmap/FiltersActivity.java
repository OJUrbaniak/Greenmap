package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class FiltersActivity extends AppCompatActivity {

    TextView numberPOI;
    CheckBox showRacks;
    CheckBox rackCovered;

    TextView warningTextLabel;
    TextView savedLabel;

    CheckBox showTaps;
    CheckBox drinkingTap;
    CheckBox tapBottleRefill;

    CheckBox showBins;

    EditText range;
    EditText minRating;

    User user;

    SharedPreferences pref = null;
    Preferences userPref = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        showRacks = findViewById(R.id.showRacks);
        showTaps = findViewById(R.id.showTaps);
        showBins = findViewById(R.id.showBins);

        warningTextLabel = findViewById(R.id.warningTextLabel);
        savedLabel = findViewById(R.id.savedTextLabel);
        savedLabel.setVisibility(View.INVISIBLE);

        rackCovered = findViewById(R.id.checkBox);
        drinkingTap = findViewById(R.id.checkBox2);
        tapBottleRefill = findViewById(R.id.checkBox3);

        range = findViewById(R.id.rangeText);
        minRating = findViewById(R.id.ratingNumber);

        //Get user from login
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");

        loadPreferences();

        numberPOI = findViewById(R.id.warningTextLabel);
    }

    public void savePreferences(View view) {    //CALLED BY SAVE FILTERS BUTTON
        String rangeText = range.getText().toString();
        int rangeValue = Integer.parseInt(rangeText);
        if(rangeValue <= 500 || rangeValue >= 1){
            warningTextLabel.setText("");
            Log.d("PREF-LOAD","Save button hit trying to save prefs");
            pref = getSharedPreferences("com.example.greenmap", 0);
            try {
                userPref = new Preferences(
                        showRacks.isChecked(),
                        showTaps.isChecked(),
                        showBins.isChecked(),
                        rackCovered.isChecked(),
                        drinkingTap.isChecked(),
                        tapBottleRefill.isChecked(),
                        Integer.parseInt(range.getText().toString()),
                        Integer.parseInt(minRating.getText().toString())
                );
            }
            catch (NumberFormatException e) {
                Log.d("SavePreferences","Parsing error");
            }   
            SharedPreferences.Editor prefsEdit = pref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(userPref);
            prefsEdit.putString("userPref", json);
            prefsEdit.commit();
            savedLabel.setVisibility(View.VISIBLE);
            finish();
        } else {
            Log.i("warning Label", "out of range");
            warningTextLabel.setText("Please enter a range between 1-500");
            range.setText("");
        }
    }

    private void loadPreferences() {
        pref = getSharedPreferences("com.example.greenmap", 0);
        if (pref.contains("userPref")) {
            Log.d("PREF-LOAD","Preferences detected");
            Gson gson = new Gson();
            String json = pref.getString("userPref","");
            Preferences deviceUserPref = gson.fromJson(json, Preferences.class);
            userPref = deviceUserPref;
        }
        else {
            Log.d("PREF-LOAD","No preferences detected");
            userPref = new Preferences();
        }
        showPreferences(userPref);
    }

    private void showPreferences(Preferences p) {
        if ((showRacks.isChecked() == true && p.showRacks == false) || (showRacks.isChecked() == false && p.showRacks == true)) {
            showRacks.toggle();
        }
        if ((showTaps.isChecked() == true && p.showTaps == false) || (showTaps.isChecked() == false && p.showTaps == true)) {
            showTaps.toggle();
        }
        if ((showBins.isChecked() == true && p.showBins == false) || (showBins.isChecked() == false && p.showBins == true)) {
            showBins.toggle();
        }
        if ((rackCovered.isChecked() == true && p.rackCovered == false) || (rackCovered.isChecked() == false && p.rackCovered == true)) {
            rackCovered.toggle();
        }
        if ((drinkingTap.isChecked() == true && p.drinkingTap == false) || (drinkingTap.isChecked() == false && p.drinkingTap == true)) {
            drinkingTap.toggle();
        }
        if ((tapBottleRefill.isChecked() == true && p.tapBottleRefill == false) || (tapBottleRefill.isChecked() == false && p.tapBottleRefill == true)) {
            tapBottleRefill.toggle();
        }
        range.setText(Integer.toString(p.range));
        minRating.setText(Integer.toString(p.minRating));
    }
}
