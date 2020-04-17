package com.example.greenmap;

import java.io.Serializable;

public class User implements Serializable {
    public int userID;
    public String username;
    public String password;
    public int carbon_saved_value;
    public String email;

    public User(int userID, String username, String password, int carbon_saved_value, String email) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.carbon_saved_value = carbon_saved_value;
        this.email = email;
    }
}
