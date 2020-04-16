package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class FiltersActivity extends AppCompatActivity {

    TextView numberPOI;
    CheckBox rackCovered;
    CheckBox drinkingTap;
    CheckBox tapBottleRefill;
    EditText range;
    EditText minRating;
    CheckBox binCovered;

    SharedPreferences pref = null;
    Preferences userPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        rackCovered = findViewById(R.id.checkBox);
        drinkingTap = findViewById(R.id.checkBox2);
        tapBottleRefill = findViewById(R.id.checkBox3);
        binCovered = findViewById(R.id.checkBox11);
        range = findViewById(R.id.editText10);//
        minRating = findViewById(R.id.editText11);//

        loadPreferences();

        numberPOI = findViewById(R.id.textView16);
    }

    public void savePreferences(View view) {    //CALLED BY SAVE FILTERS BUTTON
        Log.d("PREF-LOAD","Save button hit trying to save prefs");
        pref = getSharedPreferences("com.example.greenmap", 0);
        try {
            userPref = new Preferences(
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
