package com.example.greenmap;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


class databaseInterface{

    private static final String domain = "http://192.168.1.177/";

    public void databaseInterface(){
    }

    public void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }

    void sendPost(String params, String path) throws Exception {

		// url is missing?
        //String url = "https://selfsolve.apple.com/wcResults.do";
        String url = domain + path;


        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

        //add reuqest header
        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");



        // Send post request
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(params);
            wr.flush();
        }

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + params);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            System.out.println(response.toString());

        }
    }

    //------insert statements-------
    public boolean insertTemplate(){
        return true;
    }

    public boolean insertUser(String Email, String Username, String Password, float Carbon_Saved_Points){
        String urlParameters  = "email= "+Email+"&username="+Username+"&password="+Password+"&carbon_points_saved="+Float.toString(Carbon_Saved_Points)+"f";;
        String[] params = {urlParameters, "insertUser.php"};
        try {
            SendPostTask insertUser = new SendPostTask();
            insertUser.execute(params);
            return true;
        } catch (Exception ex) {
            Log.i("INSERT USER EXCEPTION", "INSERTION ERROR");
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertAdmin(int User_ID, int Permission_Level){
        String urlParameters  = "userID="+Integer.toString(User_ID)+"&permissionLevel="+Integer.toString(Permission_Level);
        String[] params = {urlParameters, "insertAdmin.php"};
        try {
            SendPostTask insertAdmin = new SendPostTask();
            insertAdmin.execute(params);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertWaterFountain(float lat, float lng, int userID, String Name, int Carbon_Saved_Value, String Description, boolean Drink_Straight_Tap, boolean Bottle_Filling_Tap, boolean Filtered){
        String urlParameters  = "lat="+Float.toString(lat)+"&long="+Float.toString(lng)+"&user_ID="+Integer.toString(userID)+"&name="+Name+"&carbon_saved_value="+Integer.toString(Carbon_Saved_Value)+"&description="+Description+"&drink_straight_tap="+Boolean.toString(Drink_Straight_Tap)+"&bottle_filling_tap="+Boolean.toString(Bottle_Filling_Tap)+"&filtered="+Boolean.toString(Filtered);
        String[] params = {urlParameters, "insertWaterFountain.php"};
        try {
            SendPostTask insertWaterFountain = new SendPostTask();
            insertWaterFountain.execute(params);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertBikeRack(float lat, float lng, int userID, String Name, int Carbon_Saved_Value, String Description,  boolean Covered){
        String urlParameters  = "lat="+Float.toString(lat)+"&long="+Float.toString(lng)+"&user_ID="+Integer.toString(userID)+"&name="+Name+"&carbon_saved_value="+Integer.toString(Carbon_Saved_Value)+"&description="+Description+"&covered="+Boolean.toString(Covered);
        String[] params = {urlParameters, "insertBikeRack.php"};
        try {
            SendPostTask insertBikeRack = new SendPostTask();
            insertBikeRack.execute(params);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertRecyclingBin(float lat, float lng, int userID, String Name, int Carbon_Saved_Value, String Description, String Recycling_Bin_Type){
        String urlParameters  = "lat="+Float.toString(lat)+"&long="+Float.toString(lng)+"&user_ID="+Integer.toString(userID)+"&name="+Name+"&carbon_saved_value="+Integer.toString(Carbon_Saved_Value)+"&description="+Description+"&recycling_bin_type="+Recycling_Bin_Type;
        String[] params = {urlParameters, "insertRecyclingBin.php"};
        try {
            SendPostTask insertRecyclingBin = new SendPostTask();
            insertRecyclingBin.execute(params);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //--------get statements------

    public boolean selectUserbyID(String Email, int userID){
        String SQLquery  = "Select GreenMap.`User` WHERE User_ID = " + Integer.toString(userID);
        try {
            sendPost("query="+SQLquery, "update.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean getTemplate(){
 return true;
    }

    public boolean getPOI(){
 return true;
    }

    public boolean getUserFullDetails(String Username){
 return true;
    }

    public boolean getLocalPOIs(int distance, char type, float positionLat, float positionLong){
         return true;
    }

    //--------update statements------
    public boolean updateUserEmail(String Email, int userID){
        String SQLquery  = "UPDATE GreenMap.`User`  SET Email = \""+ Email + "\" WHERE User_ID = " + Integer.toString(userID);
        try {
            sendPost("query="+SQLquery, "update.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean updateUserUsername(String Email, int userID){
        String SQLquery  = "UPDATE GreenMap.`User`  SET Email = \""+ Email + "\" WHERE User_ID = " + Integer.toString(userID);
        try {
            sendPost("query="+SQLquery, "update.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
