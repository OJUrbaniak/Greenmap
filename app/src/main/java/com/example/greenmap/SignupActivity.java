package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SignupActivity extends AppCompatActivity {

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mainHeader = findViewById(R.id.createAccountLabel);
        userNameField = findViewById(R.id.signUpUserNameLabel);
        passwordField = findViewById(R.id.signUpPWordLabel);
        signUpButton = findViewById(R.id.createAccountButton);
    }

    public void createAccount(View view){
        //get values from inputs, validate and create account for database

    }
}
