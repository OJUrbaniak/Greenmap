package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import android.graphics.Point;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText userNameField;
    EditText passwordField;
    Button loginButton;
    //Button signupButton;     -- a method already exists for this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameField = findViewById(R.id.editText);
        passwordField = findViewById(R.id.editText2);
        loginButton = findViewById(R.id.button2);
        //signupButton = findViewById(R.id.button);
    }

    public void goToMap(View view){
        //if log in is correct:

        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToSignup(View view){

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

}
