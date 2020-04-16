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

public class MainActivity extends AppCompatActivity {

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    Button loginButton;

    User[] validUsers = {new User(1, "cockman", "ilovepeepee", 74),
                         new User(2, "hoboson", "manlovestrash", 91),
                         new User(3, "wadeeb", "showbob", 2),};

    //Button signupButton;     -- a method already exists for this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHeader = findViewById(R.id.textView3);
        userNameField = findViewById(R.id.editText);
        passwordField = findViewById(R.id.editText2);
        loginButton = findViewById(R.id.button2);
        //signupButton = findViewById(R.id.button);
    }

    public void goToMap(View view){
        //check if log in is correct:
        for (int i = 0; i < validUsers.length; i++) {
            if(userNameField.getText().toString().equals(validUsers[i].username) && passwordField.getText().toString().equals(validUsers[i].password)) {
                mainHeader.setText("Welcome back, " + validUsers[i].username + "!");
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("User", validUsers[i]);
                startActivity(intent);
                break;
            } else {
                mainHeader.setText("Incorrect credentials! (whore)");
            }
        }
    }

    public void goToSignup(View view){

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

}
