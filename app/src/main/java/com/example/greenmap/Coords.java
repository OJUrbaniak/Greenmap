package com.example.greenmap;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Coords implements Serializable, Parcelable {

    double longitude;
    double latitude;

    public Coords(double lat, double lon){
        this.longitude = lon;
        this.latitude = lat;
    }

    protected Coords(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Coords> CREATOR = new Parcelable.Creator<Coords>() {
        @Override
        public Coords createFromParcel(Parcel in) {
            return new Coords(in);
        }

        @Override
        public Coords[] newArray(int size) {
            return new Coords[size];
        }
    };
}

//public class Coords implements Serializable {
//
//    double longitude;
//    double latitude;
//
//    public Coords(double lat, double lon){
//        this.longitude = lon;
//        this.latitude = lat;
//    }
//}
