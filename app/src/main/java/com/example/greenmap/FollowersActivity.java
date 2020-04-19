package com.example.greenmap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

public class FollowersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
    }

    public void goToSearch(View view){
        Intent intent = new Intent(this, AccountSearchActivity.class);
        startActivity(intent);
    }
}
