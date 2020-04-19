package com.example.greenmap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenerImplementation  implements  PrintListener{
    User returnedUser;
    @Override
    public void getResult(JSONArray jArray) {
        Log.i("LISTENER IMNPLEMENTATION GET RESULT", "CALLED");
        try {
            JSONObject obj;
            Log.i("shit", String.valueOf(jArray));
            User[] userArray = new User[jArray.length()];
            for(int n = 0; n < jArray.length(); n++)
            {
                obj = jArray.getJSONObject(n);
                userArray[n] = new User(Integer.parseInt(obj.getString("User_ID").replaceAll("[\\D]", "")), obj.getString("Username"), obj.getString("Password"), Integer.parseInt(obj.getString("Carbon_Saved_Points").replaceAll("[\\D]", "")), obj.getString("Email"));
            }
            if (userArray[0] != null){
                Log.i("USER ARRAY", userArray[0].password);
                returnedUser = userArray[0];
            }
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            Log.i("FAILED", "YOU SUCK");
        }
    }
}
