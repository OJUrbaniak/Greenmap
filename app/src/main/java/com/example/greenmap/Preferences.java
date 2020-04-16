package com.example.greenmap;

import android.util.Log;

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
