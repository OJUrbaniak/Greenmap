package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

public class FiltersActivity extends AppCompatActivity {

    TextView numberPOI;
    CheckBox rackCovered;
    TextView rackRange;
    TextView rackMinRating;
    CheckBox drinkingTap;
    CheckBox tapBottleRefill;
    TextView tapRange;
    TextView tapMinRating;

    SharedPreferences pref = null;
    Preferences userPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        rackCovered = findViewById(R.id.checkBox);
        rackRange = findViewById(R.id.textView7);
        rackMinRating = findViewById(R.id.textView2);
        drinkingTap = findViewById(R.id.checkBox2);
        tapBottleRefill = findViewById(R.id.checkBox3);
        tapRange = findViewById(R.id.textView14);
        tapMinRating = findViewById(R.id.textView15);

        //loadPreferences();

        numberPOI = findViewById(R.id.textView16);
    }

    public void savePreferences(View view) {    //CALLED BY SAVE FILTERS BUTTON
        pref = this.getSharedPreferences("com.example.greenmap", Context.MODE_PRIVATE);
        Preferences userPref = new Preferences(
                rackCovered.isChecked(),
                rackRange.getText().toString(),
                rackMinRating.getText().toString(),
                drinkingTap.isChecked(),
                tapBottleRefill.isChecked(),
                tapRange.getText().toString(),
                tapMinRating.getText().toString()
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
        rackRange.setText(p.rackRange);
        rackMinRating.setText(p.rackMinRating);
        drinkingTap.setChecked(p.drinkingTap);
        tapBottleRefill.setChecked(p.tapBottleRefill);
        tapRange.setText(p.tapRange);
        tapMinRating.setText(p.tapMinRating);
    }
}
