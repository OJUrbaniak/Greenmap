package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

public class FiltersActivity extends AppCompatActivity {

    TextView numberPOI;
    CheckBox rackCovered;
    CheckBox drinkingTap;
    CheckBox tapBottleRefill;
    TextView range;
    TextView minRating;

    SharedPreferences pref = null;
    Preferences userPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        rackCovered = findViewById(R.id.checkBox);
        drinkingTap = findViewById(R.id.checkBox2);
        tapBottleRefill = findViewById(R.id.checkBox3);
        range = findViewById(R.id.textView14);//
        minRating = findViewById(R.id.textView15);//

        //loadPreferences();

        numberPOI = findViewById(R.id.textView16);
    }

    public void savePreferences(View view) {    //CALLED BY SAVE FILTERS BUTTON
        pref = this.getSharedPreferences("com.example.greenmap", Context.MODE_PRIVATE);
        Preferences userPref = new Preferences(
                rackCovered.isChecked(),
                drinkingTap.isChecked(),
                tapBottleRefill.isChecked(),
                range.getText().toString(),
                minRating.getText().toString()
        );
        SharedPreferences.Editor prefsEdit = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userPref);
        prefsEdit.putString("userPref", json);
        prefsEdit.commit();
    }

    private void loadPreferences() {
        if (this.getSharedPreferences("com.example.greenmap", Context.MODE_PRIVATE) != null) {
            pref = this.getSharedPreferences("com.example.greenmap", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            if (pref.contains("userPref")) {
                String json = pref.getString("userPref","");
                Preferences deviceUserPref = gson.fromJson(json, Preferences.class);
                userPref = deviceUserPref;
            }
            else {
                Log.d("PREF-LOAD","No preferences detected");
                userPref = new Preferences();
            }
        }
        else {
            userPref = new Preferences();
        }
        showPreferences(userPref);
    }

    private void showPreferences(Preferences p) {
        rackCovered.setChecked(p.rackCovered);
        drinkingTap.setChecked(p.drinkingTap);
        tapBottleRefill.setChecked(p.tapBottleRefill);
        range.setText(p.range);
        minRating.setText(p.minRating);
    }
}
