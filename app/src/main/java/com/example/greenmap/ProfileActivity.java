package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void goToCreatePOI(View view){
        Intent intent = new Intent (this,CreatePOIActivity.class);
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
