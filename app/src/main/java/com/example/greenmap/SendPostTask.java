package com.example.greenmap;

import android.os.AsyncTask;
import android.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SendPostTask extends AsyncTask<String, Void, String> {
    DatabaseInterfaceDBI DBI = new DatabaseInterfaceDBI();
    @Override
    protected String doInBackground(String[] params) {
        try {
            DBI.trustAllCertificates();
            DBI.sendPost(params[0], params[1]);
            return "Sent";
        } catch (Exception ex) {
            Log.i("POST EXCEPTION", "INSERTION ERROR");
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return "Not sent";
        }
    }
}