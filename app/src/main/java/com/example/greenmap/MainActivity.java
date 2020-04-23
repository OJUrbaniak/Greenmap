package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import android.graphics.Point;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements databaseInteracter {

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    Button loginButton;
    String hashPass;
    DatabaseInterfaceDBI newDBI = new DatabaseInterfaceDBI();

    @Override
    public void  resultsReturned(JsonArray jArray){ //Log In functionality using communication with the DB
        if(jArray.size() > 0) {
            JsonObject jObj = jArray.get(0).getAsJsonObject(); //Get the user object

            //Define attributes for passing user information around front end
            int userID = jObj.get("User_ID").getAsInt();
            String email = jObj.get("Email").getAsString();
            String username = jObj.get("Username").getAsString();
            String password = jObj.get("Password").getAsString();
            Log.d("Encryption","DBPASS: "+password);
            int carbon_saved_points = jObj.get("Carbon_Saved_Points").getAsInt();
            int permission_Level = 0;
            try {
                permission_Level = jObj.get("Permission_Level").getAsInt();
                Log.i("login","User is admin PL= " + permission_Level);
            } catch (Exception ex){
                //isnt admin
                Log.i("login","User is not an admin");
            }

            Log.i("dbi", "Trying to log in");
            if(userNameField.getText().toString().equals(username) && hashPass.equals(password)){
                mainHeader.setText("Welcome back " + username + "!");
                //Create user object and send to map
                User userLogin = new User(userID, username, password, carbon_saved_points, email, permission_Level);
                Log.i("login","Permission Level = " + userLogin.permissionLevel);
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("User", (Parcelable) userLogin);
                startActivity(intent);
            } else {
                mainHeader.setText("Incorrect credentials");
            }
        } else {
            mainHeader.setText("Incorrect credentials");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHeader = findViewById(R.id.textView3);
        userNameField = findViewById(R.id.editText);
        passwordField = findViewById(R.id.editText2);
        loginButton = findViewById(R.id.button2);
    }

    public void login(View view) throws ExecutionException, InterruptedException { //Login button
        //Check if log in is actually filled in:
        if(userNameField.getText().length() > 0 && passwordField.getText().length() > 0) {
            hashPass = new String(Hex.encodeHex(DigestUtils.md5(passwordField.getText().toString())));
            Log.d("Encryption","APPPASS: "+hashPass);
            newDBI.selectUserandAdminByLogin(userNameField.getText().toString(), hashPass, this);
        }
    }

    public void goToSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}
