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
    EditText emailField;
    Button signUpButton;

    databaseInterface DBI = new databaseInterface();

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

    public void createAccount(View view){
        //get values from inputs, validate and create account for database
        String username = userNameField.getText().toString();
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString();
        if(username.length() > 0 && password.length() > 0 && email.length() > 0) {
            mainHeader.setText("Account created!");
            DBI.insertUser(email, username, password, 0);

            //GET USER FROM DB HERE

            User user = new User(1, username, password, 0, email);
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("User", user);
            startActivity(intent);
        } else {
            mainHeader.setText("Please enter valid credentials");
        }
    }
}
