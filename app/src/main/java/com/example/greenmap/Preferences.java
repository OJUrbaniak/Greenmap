package com.example.greenmap;

import android.util.Log;

public class Preferences {
    boolean showRacks;
    boolean rackCovered;
    boolean showTaps;
    boolean drinkingTap;
    boolean tapBottleRefill;
    boolean tapFiltered;
    boolean showBins;
    int range;
    int minRating;

    public Preferences() {
        this.showRacks = true;
        this.showTaps = true;
        this.showBins = true;
        this.rackCovered = false;       //defaults
        this.drinkingTap = false;       //defaults
        this.tapBottleRefill = false;   //defaults
        this.tapFiltered = false;       //defaults
        this.range = 500;            //defaults
        this.minRating = 0;          //defaults
    }

    public Preferences(boolean showRacks, boolean showTaps, boolean showBins, boolean rackCovered, boolean drinkingTap, boolean tapBottleRefill, boolean tapFiltered, int tapRange, int tapMinRating) {
        this.showRacks = showRacks;
        this.showTaps = showTaps;
        this.showBins = showBins;
        this.rackCovered = rackCovered;
        this.drinkingTap = drinkingTap;
        this.tapBottleRefill = tapBottleRefill;
        this.tapFiltered = tapFiltered;
        this.range = tapRange;
        this.minRating = tapMinRating;
    }
}
