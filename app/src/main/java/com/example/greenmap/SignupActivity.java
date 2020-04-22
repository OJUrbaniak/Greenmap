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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;
import java.security.MessageDigest;

public class SignupActivity extends AppCompatActivity implements databaseInteracter{

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    EditText emailField;
    Button signUpButton;
    String hashPass;
    DatabaseInterfaceDBI newDBI = new DatabaseInterfaceDBI();

    @Override
    public void resultsReturned(JsonArray jArray){ //Sign up functionality using communication with the DB
        if(jArray.size() == 0) {
            String username = userNameField.getText().toString();
            //String password = passwordField.getText().toString();


            String email = emailField.getText().toString();
            newDBI.insertUser(email, username, hashPass, 0);
            mainHeader.setText("Account Created!");
            Intent intent = new Intent(this, MainActivity.class);
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

        hashPass = new String(Hex.encodeHex(DigestUtils.md5(password)));

        Log.d("hashPass","hash for abcd is "+hashPass);
        String email = emailField.getText().toString();

        if(username.length() > 0 && password.length() > 0 && email.length() > 0) {
            newDBI.selectUserbyUsername(username, this);

            //GET USER FROM DB HERE


        } else {
            mainHeader.setText("Please enter valid credentials");
        }
    }
}
