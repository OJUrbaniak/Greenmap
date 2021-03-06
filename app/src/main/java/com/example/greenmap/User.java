package com.example.greenmap;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable, Parcelable {
    public int userID;
    public String username;
    public String password;
    public int carbon_saved_value;
    public String email;
    public int permissionLevel;

    public User(int userID, String username, String password, int carbon_saved_value, String email) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.carbon_saved_value = carbon_saved_value;
        this.email = email;
        this.permissionLevel = 0;
    }

    public User(int userID, String username, String password, int carbon_saved_value, String email, int permissionLevel) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.carbon_saved_value = carbon_saved_value;
        this.email = email;
        this.permissionLevel = permissionLevel;
    }

    public User(int userID, String username, int carbon_saved_value) {
        this.userID = userID;
        this.username = username;
        this.carbon_saved_value = carbon_saved_value;
    }

    protected User(Parcel in) {
        userID = in.readInt();
        username = in.readString();
        password = in.readString();
        carbon_saved_value = in.readInt();
        email = in.readString();
        permissionLevel = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeInt(carbon_saved_value);
        dest.writeString(email);
        dest.writeInt(permissionLevel);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
