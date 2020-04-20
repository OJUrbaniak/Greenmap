package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.ExecutionException;


public class SignupActivity extends databaseInteracter{

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    EditText emailField;
    Button signUpButton;

    DatabaseInterfaceDBI newDBI = new DatabaseInterfaceDBI();

    @Override
    public void resultsReturned(String results){ //Sign up functionality using communication with the DB
        Log.i("dbi", "from sign up results= "+ results);
        JsonParser parser = new JsonParser();
        JsonElement jEle = parser.parse(results); //Parse the results from the DBI Post
        Log.i("dbi", String.valueOf(jEle));
        JsonArray jArray = jEle.getAsJsonArray();
        if(jArray.size() == 0) {
            String username = userNameField.getText().toString();
            String password = passwordField.getText().toString();
            String email = emailField.getText().toString();
            newDBI.insertUser(email, username, password, 0);
            mainHeader.setText("Account Created!");
            User user = new User(1, username, password, 0, email);
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("User", (Parcelable) user);
            startActivity(intent);
        } else {
            mainHeader.setText("Sorry, that user already exists");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mainHeader = findViewById(R.id.createAccountLabel);
        userNameField = findViewById(R.id.signUpUserNameLabel);
        passwordField = findViewById(R.id.signUpPWordLabel);
        emailField = findViewById(R.id.signUpEmailAddress);
        signUpButton = findViewById(R.id.createAccountButton);
    }

    public void createAccount(View view) throws ExecutionException, InterruptedException {
        //get values from inputs, validate and create account for database
        String username = userNameField.getText().toString();
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString();
        if(username.length() > 0 && password.length() > 0 && email.length() > 0) {
            newDBI.selectUserByUserName(username, this);

            //GET USER FROM DB HERE


        } else {
            mainHeader.setText("Please enter valid credentials");
        }
    }
}
