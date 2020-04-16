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
    //Button saveFiltersButton;
    CheckBox rackCovered;
    TextView rackRange;
    TextView rackMinRating;
    CheckBox drinkingTap;
    CheckBox tapBottleRefill;
    TextView tapRange;
    TextView tapMinRating;

    SharedPreferences pref = this.getSharedPreferences("com.example.greenmap", Context.MODE_PRIVATE);
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

        loadPreferences();

        numberPOI = findViewById(R.id.textView16);
        //saveFiltersButton = findViewById(R.id.button4);
    }

    public void savePreferences(View view) {
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

    public class Preferences {
        boolean rackCovered;
        int rackRange;
        int rackMinRating;
        boolean drinkingTap;
        boolean tapBottleRefill;
        int tapRange;
        int tapMinRating;

        public Preferences() {
            this.rackCovered = false;       //defaults
            this.rackRange = 0;             //defaults
            this.rackMinRating = 0;         //defaults
            this.drinkingTap = false;       //defaults
            this.tapBottleRefill = false;   //defaults
            this.tapRange = 500;            //defaults
            this.tapMinRating = 0;          //defaults
        }

        public Preferences(boolean rackCovered, String rackRange, String rackMinRating, boolean drinkingTap, boolean tapBottleRefill, String tapRange, String tapMinRating) {
            try {
                this.rackCovered = rackCovered;
                this.rackRange = 0; try { Integer.parseInt(rackRange); } catch (NumberFormatException ex) {
                    Log.e("PrefError","Rackrange not a number"); };
                this.rackMinRating = 0; try { Integer.parseInt(rackMinRating); } catch (NumberFormatException ex) {Log.e("PrefError","rackMinRating not a number");};
                this.drinkingTap = false;
                this.tapBottleRefill = false;
                this.tapRange = 500; try { Integer.parseInt(tapRange); } catch (NumberFormatException ex) {Log.e("PrefError","tapRange not a number");};
                this.tapMinRating = 0; try { Integer.parseInt(tapMinRating); } catch (NumberFormatException ex) {Log.e("PrefError","tapMinRating not a number");};
            }
            catch (Exception ex) {
                Log.e("PrefError","ONE OR MORE SETTINGS NOT INTS");
            }
        }
    }
}
