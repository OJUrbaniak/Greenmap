package com.example.greenmap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;

public class SendPostWithReturnTask extends AsyncTask<String, Void, JSONArray> {

    DatabaseInterfaceDBI DBI = new DatabaseInterfaceDBI();

    PrintListener listener;

    SendPostWithReturnTask(PrintListener mPrintListener) {
        this.listener = mPrintListener;
    }

    @Override
    protected JSONArray doInBackground(String[] strings) {
        JSONArray jArray = null;
        try {
            DBI.trustAllCertificates();
            jArray = DBI.sendPost(strings[0], strings[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArray;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        Log.i("Json array before listener function", String.valueOf(result));
        this.listener.getResult(result);
    }
}