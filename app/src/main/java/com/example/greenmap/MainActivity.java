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

import java.util.Vector;

public class MainActivity<user1> extends AppCompatActivity {

    TextView mainHeader;
    EditText userNameField;
    EditText passwordField;
    Button loginButton;

    databaseInterface DBINT = new databaseInterface();


    User validUsers[] = {new User(1, "cockman", "ilovepeepee", 74, "cock@cock.com"),
                      new User(2, "hoboson", "manlovestrash", 91, "cock@cock.com"),
                      new User(3, "wadeeb", "showbob", 2, "cock@cock.com"),
                      new User(4,"test","", 99, "cock@cock.com")};


    //Button signupButton;     -- a method already exists for this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Users from DB and populate validUsers vector

        final String userParams[] = {"insertUser.php","peepeebutthole@poo.peepee", "shitwhore", "ilovepeepee", "74"};


        mainHeader = findViewById(R.id.textView3);
        userNameField = findViewById(R.id.editText);
        passwordField = findViewById(R.id.editText2);
        loginButton = findViewById(R.id.button2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBINT.insertAdmin(1, 2);
                //DBINT.insertBikeRack(69.69, 69.420, 1, "hobo bikes", 25, "hobos like this bike rack", true );
            }
        });
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
