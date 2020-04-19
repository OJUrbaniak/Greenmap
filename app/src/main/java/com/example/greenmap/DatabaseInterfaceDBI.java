
package com.example.greenmap;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.google.gson.*;

import androidx.appcompat.app.AppCompatActivity;


class DatabaseInterfaceDBI{

    User returnedUser;

    private static final String domain = "http://192.168.0.27/";

    public void databaseInterface(){
    }

    private void runHTML(final String[] strings, final databaseInteracter interacter){

         class SendPostWithReturnTask extends AsyncTask<Void, Void, String> {

            DatabaseInterfaceDBI DBI = new DatabaseInterfaceDBI();

            //PrintListener listener;

            //SendPostWithReturnTask(PrintListener mPrintListener) {
                //this.listener = mPrintListener;
            //}

            @Override
            protected String doInBackground(Void... voids)  {
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
                    interacter.resultsReturned(result);

                } catch (Exception e) {
                    Log.i("dbi", "Error");
                    e.printStackTrace();
                }
                //this.listener.getResult(result);
            }
        }
        SendPostWithReturnTask sendP = new SendPostWithReturnTask();
         sendP.execute();
    }

    private void returnPOIs(String POIdata){
        JsonArray jArray = jsonFromString(POIdata);
        JsonObject jObj= jArray.get(0).getAsJsonObject();
        Log.i("dbi", jObj.get("Email").getAsString());
    }
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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

    private static JsonArray jsonFromString(String jsonObjectStr) {

        try {
            //JsonReader jsonReader = new JsonReader(new StringReader(jsonObjectStr));
            //String json = gson.toJson(obj);
            //jsonReader.close();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(jsonObjectStr);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Log.i("jsonfromstring jsonarray", String.valueOf(jsonArray));
            return jsonArray;
        } catch (Exception ex){
            Log.i("WADHOLHEK", "MY ASS HAS NEVER FELT CRUNCHIER");
            return null;
        }
    }

    String sendPost(String params, String path) throws Exception {

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

        String jsonReply = null;
        if(httpClient.getResponseCode()==201 || httpClient.getResponseCode()==200)
        {
            InputStream response = httpClient.getInputStream();
            jsonReply = convertStreamToString(response);
            jsonReply = jsonReply.replaceAll("\r?\n", "");

            System.out.println("jsonReply= "+jsonReply);

        }
        if (jsonReply == null){
            return null;
        }
        else {
            JsonArray jArray = jsonFromString(jsonReply);
            Log.i("dbi", "jArray= " + jArray);
            return jsonReply;
        }


    }

    //------insert statements-------
    public boolean insertUser(String Email, String Username, String Password, float Carbon_Saved_Points){
        String urlParameters  = "email= "+Email+"&username="+Username+"&password="+Password+"&carbon_saved_points="+Float.toString(Carbon_Saved_Points)+"f";;
        try {
            sendPost(urlParameters, "insertUser.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertAdmin(int User_ID, int Permission_Level){
        String urlParameters  = "user_id="+Integer.toString(User_ID)+"&permission_level="+Integer.toString(Permission_Level);
        try {
            sendPost(urlParameters, "insertAdmin.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertFollow(int Followed_User_ID, int Follower_User_ID){
        String urlParameters  = "&followed_user_ID="+Integer.toString(Followed_User_ID)+"&follower_user_id="+Integer.toString(Follower_User_ID);
        try {
            sendPost(urlParameters, "insertFollow.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

	/*public boolean insertPOI(char Type, float lat, float lng, int User_ID){
		String urlParameters  = "type="+Type+"lat="+Float.toString(lat)+"&long="+Float.toString(lng)+"&userid="+Integer.toString(User_ID);
		try {
			sendPost(urlParameters, "insertPOI.php");
			return true;
		} catch (Exception ex) {
			Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}*/

    public boolean insertWaterFountain(float lat, float lng, String Name, int Carbon_Saved_Value, String Description, int userID, boolean Drink_Straight_Tap, boolean Bottle_Filling_Tap, boolean Filtered){
        String urlParameters  = "lat="+Float.toString(lat)+"&long="+Float.toString(lng)+"&user_ID="+Integer.toString(userID)+"&name="+Name+"&carbon_saved_value="+Integer.toString(Carbon_Saved_Value)+"&description="+Description+"&drink_straight_tap="+Boolean.toString(Drink_Straight_Tap)+"&bottle_filling_tap="+Boolean.toString(Bottle_Filling_Tap)+"&filtered="+Boolean.toString(Filtered)+"&no_reviews=";
        try {
            sendPost(urlParameters, "insertWaterFountain.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertBikeRack(float lat, float lng, String Name, int Carbon_Saved_Value, String Description, int userID,  boolean Covered){
        String urlParameters  = "lat="+Float.toString(lat)+"&long="+Float.toString(lng)+"&user_ID="+Integer.toString(userID)+"&name="+Name+"&carbon_saved_value="+Integer.toString(Carbon_Saved_Value)+"&description="+Description+"&covered="+Boolean.toString(Covered);
        try {
            sendPost(urlParameters, "insertBikeRack.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean insertRecyclingBin(float lat, float lng, String Name, int Carbon_Saved_Value, String Description, int userID, String Recycling_Bin_Type){
        String urlParameters  = "lat="+Float.toString(lat)+"&long="+Float.toString(lng)+"&user_ID="+Integer.toString(userID)+"&name="+Name+"&carbon_saved_value="+Integer.toString(Carbon_Saved_Value)+"&description="+Description+"&recycling_bin_type="+Recycling_Bin_Type;
        try {
            sendPost(urlParameters, "insertRecyclingBin.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }



    //--------select statements------

    //userTable

    public User[] selectUserbyID(int userID){
        /*String SQLquery  = "Select User From GreenMap.`User`";// WHERE User_ID = *" + Integer.toString(userID);
        try {
            JsonArray jArray = sendPost("query="+SQLquery, "select.php");
            JsonObject obj;
            JsonElement ele;
            User[] userArray = new User[jArray.size()];
            for(int n = 0; n < jArray.size(); n++)
            {
                ele = jArray.get(n);
                obj = ele.getAsJsonObject();
                userArray[n] = new User(Integer.parseInt(obj.getAsString("User_ID").replaceAll("[\\D]", "")), obj.getString("Username"), obj.getString("Password"), Integer.parseInt(obj.getString("Carbon_Saved_Points").replaceAll("[\\D]", "")), obj.getString("Email"));
            }
            return userArray;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);*/
            return null;
        //}
    }

//    String urlParameters  = "email= "+Email+"&username="+Username+"&password="+Password+"&carbon_points_saved="+Float.toString(Carbon_Saved_Points)+"f";;
//    String[] params = {urlParameters, "insertUser.php"};
//        try {
//        SendPostTask insertUser = new SendPostTask();
//        insertUser.execute(params);
//        return true;

    public User selectUserByLogin(String user, String pass, databaseInteracter dbInteracter) throws ExecutionException, InterruptedException {
        String SQLquery  = "SELECT * " + "FROM GreenMap.User" + " WHERE (\""+ user + "\" = User.Username) AND (\""+ pass + "\" = User.Password)";
        String[] params = {"query=" + SQLquery, "select.php"};
        runHTML(params, dbInteracter);
        //this.valueListener = new ListenerImplementation();
        //SendPostWithReturnTask selectUserByLogin = new SendPostWithReturnTask(this.valueListener);
        //selectUserByLogin.execute(params).get();
        //return this.valueListener.returnedUser;
        return null;
    }

    //following table

    public boolean selectFollowers(int userID){
        String SQLquery  = "SELECT User.User_ID, Follow.Followed_User_ID, Follow.Follower_User_ID, User.Carbon_Saved_Points " +
                "FROM GreenMap.Follow " + "INNER JOIN GreenMap.User" +  "WHERE Followed_User_ID = \" + Integer.toString(userID)";
        return false;
    }

    public boolean searchFollowers(String userName){
        String SQLquery = "SELECT User.Name, User.Carbon_Saved_Points" +
                "FROM GreenMap.User" + "WHERE userName = User.Name";
        return false;
    }

    public boolean poiTest(int distance, double lat, double lng){
        String SQLquery  = "SELECT WaterFountainPOI.Name, WaterFountainPOI.Carbon_Saved_Value, WaterFountainPOI.Description, WaterFountainPOI.Review_Rating, Bottle_Filling_Tap, Filtered, Drink_Straight_Tap ,WaterFountainPOI.No_Reviews, POI.Location" +
                "FROM GreenMap.WaterFountainPOI INNER JOIN GreenMap.POI WHERE (type = 'w') AND (Distance >=  POINT("+Double.toString(lat)+", "+ Double.toString(lng)+"), POI.Location))'";
        try {
            sendPost("query="+SQLquery, "select.php");
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

//Select statements for GreenMap

    public boolean selectCoveredBikeOnly(boolean covered, float lat, float lon, int numberOfPOIs){
        String SQLquery  = "SELECT BikeRackPOI.Name, BikeRackPOI.Carbon_Saved_Value, BikeRackPOI.Description, BikeRackPOI.Review_Rating, BikeRackPOI.Covered, BikeRackPOI.No_Reviews, POI.Location " +
                "FROM GreenMap.BikeRackPOI INNER JOIN GreenMap.POI " +
                "WHERE Covered = " + Boolean.toString(covered) + " AND Type = 'b' AND (ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) IS NOT NULL ORDER BY ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) limit "+ Integer.toString(numberOfPOIs);
        return false;
    }


    public boolean selectBikesOnly(float lat, float lon, int numberOfPOIs){
        String SQLquery  = "SELECT BikeRackPOI.Name, BikeRackPOI.Carbon_Saved_Value, BikeRackPOI.Description, BikeRackPOI.Review_Rating, BikeRackPOI.No_Reviews, POI.Location, BikeRackPOI.Covered" +
                "FROM GreenMap.BikeRackPOI INNER JOIN GreenMap.POI " +
                "WHERE Type = 'b' AND (Distance =  +' Integer.toString(ditsance)'";
        return false;
    }

    public boolean selectWaterOnly(float lat, float lon, int numberOfPOIs){
        String SQLquery  = "SELECT WaterFountainPOI.Name, WaterFountainPOI.Carbon_Saved_Value, WaterFountainPOI.Description, WaterFountainPOI.Review_Rating, WaterFountainPOI.No_Reviews, POI.Location, WaterFountainPOI.Bottle_Filling_Tap, WaterFountainPOI.Filtered, WaterFountainPOI.Drink_Straight_Tap  " +
                "FROM GreenMap.WaterFountainPOI INNER JOIN GreenMap.POI " +
                "WHERE Type = 'w' AND (ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) IS NOT NULL ORDER BY ST_Distance(POINT(-121.626, 47.8315), Location) limit "+ Integer.toString(numberOfPOIs);
        return false;
    }

    public boolean selectBinOnly(float lat, float lon, int numberOfPOIs){
        /*String SQLquery  = "SELECT RecyclingBinPOI.Name, RecyclingBinPOI.Carbon_Saved_Value, RecyclingBinPOI.Description, RecyclingBinPOI.Review_Rating, RecyclingBinPOI.No_Reviews, ST_X(POI.Location) AS Latitude, ST_Y(POI.Location) AS Longitude " +
                " FROM GreenMap.RecyclingBinPOI INNER JOIN GreenMap.POI " +
                "WHERE (Type = 'r') AND (ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), POI.Location) IS NOT NULL) ORDER BY ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), POI.Location) limit "+ Integer.toString(numberOfPOIs);
        try {
            JSONArray jArray = sendPost("query="+SQLquery, "select.php");
            JSONObject obj;
            User[] userArray = new User[jArray.length()];
            for(int n = 0; n < jArray.length(); n++)
            {
                obj = jArray.getJSONObject(n);
                userArray[n] = new User(Integer.parseInt(obj.getString("User_ID").replaceAll("[\\D]", "")), obj.getString("Username"), obj.getString("Password"), Integer.parseInt(obj.getString("Carbon_Saved_Points").replaceAll("[\\D]", "")), obj.getString("Email"));
            }
            //if (userArray[0] != null){
            //return true;
            //}
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }*/
        return false;
    }


    public boolean filteredWaterFountainOnly(boolean filtered, float lat, float lon, int numberOfPOIs){
        String SQLquery  = "SELECT WaterFountainPOI.Name, WaterFountainPOI.Carbon_Saved_Value, WaterFountainPOI.Description, WaterFountainPOI.Review_Rating, WaterFountainPOI.Bottle_Filling_Tap, WaterFountainPOI.Filtered, WaterFountainPOI.Drink_Straight_Tap ,WaterFountainPOI.No_Reviews, POI.Location" +
                "FROM GreenMap.'WaterFountainPOI' INNER JOIN GreenMap.'POI'" +
                "WHERE Type = 'w' AND Filtered = " + Boolean.toString(filtered) + " AND (ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) IS NOT NULL ORDER BY ST_Distance(POINT(-121.626, 47.8315), Location) limit "+ Integer.toString(numberOfPOIs);
        return false;
    }

    public boolean drinkWaterFountainOnly(boolean filtered, boolean drink, float lat, float lon, int numberOfPOIs){
        String SQLquery  = "SELECT WaterFountainPOI.Name, WaterFountainPOI.Carbon_Saved_Value, WaterFountainPOI.Description, WaterFountainPOI.Review_Rating, WaterFountainPOI.Bottle_Filling_Tap, WaterFountainPOI.Filtered, WaterFountainPOI.Drink_Straight_Tap ,WaterFountainPOI.No_Reviews, POI.Location" +
                "FROM GreenMap.'WaterFountainPOI' INNER JOIN GreenMap.'POI'" +
                "WHERE Type = 'w' AND Filtered = " + Boolean.toString(filtered) + " AND Drink_Straight_Tap  = " + Boolean.toString(drink) + " AND (ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) IS NOT NULL ORDER BY ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) limit "+ Integer.toString(numberOfPOIs);
        return false;
    }

    public boolean bottledWaterFountainOnly(boolean filtered, boolean bottle, float lat, float lon, int numberOfPOIs){
        String SQLquery  = "SELECT WaterFountainPOI.Name, WaterFountainPOI.Carbon_Saved_Value, WaterFountainPOI.Description, WaterFountainPOI.Review_Rating, WaterFountainPOI.Bottle_Filling_Tap, WaterFountainPOI.Filtered, WaterFountainPOI.Drink_Straight_Tap ,WaterFountainPOI.No_Reviews, POI.Location" +
                "FROM GreenMap.'WaterFountainPOI' INNER JOIN GreenMap.'POI'" +
                "WHERE Type = 'w' AND Filtered = " + Boolean.toString(filtered) + " AND Bottle_Filling_Tap = " + Boolean.toString(bottle) + " AND (ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) IS NOT NULL ORDER BY ST_Distance(POINT(" + Float.toString(lat) +"," + Float.toString(lon)+ "), Location) limit "+ Integer.toString(numberOfPOIs);
        return false;
    }


    //--------update statements------
    public boolean updatePOI(int POI_ID, char Type, float lat, float lng, int User_ID){ //Might need to swap the lat and long around for it to work in mySQL.
        String SQLquery  = "UPDATE GreenMap.`User` SET Type = \" " + Type +
                ", Location = " + "ST_SRID(POINT(" + Float.toString(lat) + "," + Float.toString(lng) + "), 3857)" + ", User_ID = " + User_ID +
                "\" WHERE POI_ID = " + Integer.toString(POI_ID);
        try {
            sendPost("query="+SQLquery, "update.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    //--------delete statements------

    public boolean deleteUser(int User_ID){
        String SQLquery  = "DELETE FROM GreenMap.`User` WHERE User_ID =  " + Integer.toString(User_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteFollow(int Follow_ID){
        String SQLquery  = "DELETE FROM GreenMap.`Follow` WHERE Follow_ID =  " + Integer.toString(Follow_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteAdmin(int User_ID){
        String SQLquery  = "DELETE FROM GreenMap.`Admin` WHERE User_ID =  " + Integer.toString(User_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deletePOI(int POI_ID){
        String SQLquery = "DELETE FROM GreenMap.`POI` WHERE POI_ID =  " + Integer.toString(POI_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteWaterFountainPOI(int POI_ID){
        String SQLquery = "DELETE FROM GreenMap.`WaterFountainPOI` WHERE POI_ID =  " + Integer.toString(POI_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteRecyclingBinPOI(int POI_ID){
        String SQLquery = "DELETE FROM GreenMap.`RecyclingBinPOI` WHERE POI_ID =  " + Integer.toString(POI_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteBikeRackPOI(int POI_ID){
        String SQLquery = "DELETE FROM GreenMap.`BikeRackPOI` WHERE POI_ID =  " + Integer.toString(POI_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }


    //ADDITIONAL DELETES THAT MAY BE NEEDED
    //Removes a POI based off the user_id instead of poi_id.
    public boolean deletePOIoffUserID(int User_ID){
        String SQLquery = "DELETE FROM GreenMap.`POI` WHERE User_ID =  " + Integer.toString(User_ID);
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //Laurence asked for these in SQL so they're here in Java too.
    //Not entirely sure if these'll work the same way they do in mySQL.
    public boolean deletePOIoffLastIncrem(){
        String SQLquery = "DELETE FROM GreenMap.`POI` WHERE POI_ID = last_insert_id()";
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteUseroffLastIncrem(){
        String SQLquery = "DELETE FROM GreenMap.`User` WHERE User_ID = last_insert_id()";
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteFollowoffLastIncrem(){
        String SQLquery = "DELETE FROM GreenMap.`Follow` WHERE Follow_ID = last_insert_id()";
        try {
            sendPost("query="+SQLquery, "delete.php");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(databaseInterface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
