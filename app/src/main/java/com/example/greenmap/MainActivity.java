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

import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class MainActivity extends databaseInteracter {

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    Button loginButton;

    DatabaseInterfaceDBI newDBI = new DatabaseInterfaceDBI();

    //Button signupButton;     -- a method already exists for this
    @Override
    public void  resultsReturned(JsonArray jArray){ //Log In functionality using communication with the DB
        if(jArray.size() > 0) {
            JsonObject jObj = jArray.get(0).getAsJsonObject(); //Get the user object
            //Define attributes for passing user information around front end
            int userID = jObj.get("User_ID").getAsInt();
            String email = jObj.get("Email").getAsString();
            String username = jObj.get("Username").getAsString();
            String password = jObj.get("Password").getAsString();
            int carbon_saved_points = jObj.get("Carbon_Saved_Points").getAsInt();
            Log.i("dbi", "Trying to log in");
            if(userNameField.getText().toString().equals(username) && passwordField.getText().toString().equals(password)){
                mainHeader.setText("Welcome back " + username + "!");
                //Create user object and send to map
                User userLogin = new User(userID, username, password, carbon_saved_points, email);
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

//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //DBINT.insertUser("hadsonshooley@gmail.communist", "vladimir3000", "walay", 35);
//                //DBINT.insertAdmin(75, 9);
//                //DBINT.insertWaterFountain(100.00f, 100.50f, 1, "hot bby wortr founten", 15, "where all them swEEt hot babes drink", true, false,true);
//                //DBINT.insertBikeRack( 69.69f, 69.420f, 1, "hobo bikes", 25, "hobos like this bike rack", true );
//                //DBINT.insertRecyclingBin(50.04f, 50.05f, 15, "glass ting init", 20, "yeah this a gret bin", "glass");
//            }
//        });
    }

    public void goToMap(View view) throws ExecutionException, InterruptedException { //Login button
        //Check if log in is actually filled in:
        if(userNameField.getText().length() > 0 && passwordField.getText().length() > 0) {
            newDBI.selectUserByLogin(userNameField.getText().toString(), passwordField.getText().toString(), this);
        }
    }

    public void goToSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}
