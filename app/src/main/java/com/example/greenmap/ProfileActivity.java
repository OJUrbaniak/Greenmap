package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    User user;
    TextView usernameTextLabel;
    TextView carbonPointsLabel;
//    Button viewPOILabel;
//    Button signOutLabel;
//    Button tosButton;
//    Button ppButton;
//    Button aboutUsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");

        usernameTextLabel = findViewById(R.id.textView17);
        carbonPointsLabel = findViewById(R.id.textView20);

        usernameTextLabel.setText(user.username);
        carbonPointsLabel.setText("Carbon Points: " + user.carbon_saved_value);
    }

    public void signOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToCreatePOI(View view){
        Intent intent = new Intent (this,CreatePOIActivity.class);
        startActivity(intent);
    }

    public void goToCreated(View view){
        Intent intent = new Intent (this,CreatedListActivity.class);
        startActivity(intent);
    }

    public void goToTOS(View view){
        Intent intent = new Intent(this,TermsOfServiceActivity.class);
        startActivity(intent);
    }

    public void goToPP(View view){
        Intent intent = new Intent(this,PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    public void goToAU(View view){
        Intent intent = new Intent(this,AboutUsActivity.class);
        startActivity(intent);
    }
}
