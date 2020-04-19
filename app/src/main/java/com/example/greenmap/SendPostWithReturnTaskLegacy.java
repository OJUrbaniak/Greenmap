package com.example.greenmap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.gson.*;
//import org.json.JSONArray;

public class SendPostWithReturnTaskLegacy extends AsyncTask<String, databaseInterface, String> {

    DatabaseInterfaceDBI DBI = new DatabaseInterfaceDBI();

    PrintListener listener;

    SendPostWithReturnTaskLegacy(PrintListener mPrintListener) {
        this.listener = mPrintListener;
    }

    @Override
    protected String doInBackground(String[] strings) {
        String jStr = null;
        try {
            DBI.trustAllCertificates();
            jStr = DBI.sendPost(strings[0], strings[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jStr;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("Json array before listener function", String.valueOf(result));
            try {
                Log.i("dbi", "result= "+result);
            } catch (Exception e) {
                Log.i("dbi", "Error");
                e.printStackTrace();
            }
        //this.listener.getResult(result);
    }
}