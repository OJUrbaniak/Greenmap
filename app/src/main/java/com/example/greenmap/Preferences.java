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

    public Preferences(boolean rackCovered, boolean drinkingTap, boolean tapBottleRefill, int tapRange, int tapMinRating) {
        this.rackCovered = rackCovered;
        this.drinkingTap = drinkingTap;
        this.tapBottleRefill = tapBottleRefill;
        this.range = tapRange;
        this.minRating = tapMinRating;
    }
}
