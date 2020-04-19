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
import com.google.gson.JsonParser;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class MainActivity<user1> extends databaseInteracter {

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    Button loginButton;

    DatabaseInterfaceDBI newDBI = new DatabaseInterfaceDBI();

   // databaseInterface DBINT = new databaseInterface();


    User validUsers[] = {new User(1, "cockman", "ilovepeepee", 74, "cock@cock.com"),
                      new User(2, "hoboson", "manlovestrash", 91, "cock@cock.com"),
                      new User(3, "wadeeb", "showbob", 2, "cock@cock.com"),
                      new User(4,"test","", 99, "cock@cock.com")};


    //Button signupButton;     -- a method already exists for this
    @Override
    public void  resultsReturned(String results){
        Log.i("dbi", "from Main results= "+ results);
        //new user(Integer.parseInt(obj.getString("User_ID").replaceAll("[\\D]", "")), obj.getString("Username"), obj.getString("Password"), Integer.parseInt(obj.getString("Carbon_Saved_Points").replaceAll("[\\D]", "")), obj.getString("Email"));
        JsonParser parser = new JsonParser();
        JsonElement jEle = parser.parse(results);
        Log.i("dbi", String.valueOf(jEle));
        //User loginUser = new User(Integer.parseInt(jEle.getString("User_ID").replaceAll("[\\D]", "")), obj.getString("Username"), obj.getString("Password"), Integer.parseInt(obj.getString("Carbon_Saved_Points").replaceAll("[\\D]", "")), obj.getString("Email"));
        //    JsonArray jsonArray = jsonElement.getAsJsonArray();
        //Log.i("dbi", String.valueOf(jsonArray));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Users from DB and populate validUsers vector

        final String userParams[] = {"insertUser.php","peepeebutthole@poo.peepee", "shitwhore", "ilovepeepee", "74"};

        try {
            Log.i("dbi", "calling selectUserByLogin");
            User loginUser = newDBI.selectUserByLogin("user", "pass", this);
            //Log.i("dbi", loginUser.email);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i("dbi",e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("dbi", e.getMessage());
        }


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
        //signupButton = findViewById(R.id.button);
    }

    public void goToMap(View view){ //Login button
        //check if log in is correct:
        for (int i = 0; i < validUsers.length; i++) {
            if(userNameField.getText().toString().equals(validUsers[i].username) && passwordField.getText().toString().equals(validUsers[i].password)) {
                //Log.i("EMAIL RETRIEVED ",loginUser.email);
                mainHeader.setText("Welcome back, " + validUsers[i].username + "!");
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("User", (Parcelable) validUsers[i]);
                startActivity(intent);
                break;
            } else {
                mainHeader.setText("Incorrect credentials!");
            }
        }
    }

    public void goToSignup(View view){

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

}
