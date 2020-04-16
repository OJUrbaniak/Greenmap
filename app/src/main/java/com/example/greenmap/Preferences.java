package com.example.greenmap;

import android.util.Log;

public class Preferences {
    boolean rackCovered;
    boolean drinkingTap;
    boolean tapBottleRefill;
    int range;
    int minRating;

    public Preferences() {
        this.rackCovered = false;       //defaults
        this.drinkingTap = false;       //defaults
        this.tapBottleRefill = false;   //defaults
        this.range = 500;            //defaults
        this.minRating = 0;          //defaults
    }

    public Preferences(boolean rackCovered, boolean drinkingTap, boolean tapBottleRefill, String tapRange, String tapMinRating) {
        try {
            this.rackCovered = rackCovered;
            this.drinkingTap = false;
            this.tapBottleRefill = false;
            this.range = 500; try { Integer.parseInt(tapRange); } catch (NumberFormatException ex) {Log.e("PrefError","tapRange not a number");};
            this.minRating = 0; try { Integer.parseInt(tapMinRating); } catch (NumberFormatException ex) {Log.e("PrefError","tapMinRating not a number");};
        }
        catch (Exception ex) {
            Log.e("PrefError","ONE OR MORE SETTINGS NOT INTS");
        }
    }
}
