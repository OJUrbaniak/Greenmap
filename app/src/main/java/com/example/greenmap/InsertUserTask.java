package com.example.greenmap;

import android.os.AsyncTask;
import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertUserTask extends AsyncTask<String, Void, String> {
    databaseInterface DBI = new databaseInterface();
    @Override
    protected String doInBackground(String[] params) {
        String urlParameters  = "email= "+params[0]+"&username="+params[1]+"&password="+params[2]+"&carbon_points_saved="+Float.toString(Float.parseFloat(params[3]))+"f";;
        try {
            DBI.trustAllCertificates();
            DBI.sendPost(urlParameters, "insertUser.php");
            return "Sent";
        } catch (Exception ex) {
            Log.i("INSERT USER EXCEPTION", "INSERTION ERROR");
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return "Not sent";
        }
    }
}