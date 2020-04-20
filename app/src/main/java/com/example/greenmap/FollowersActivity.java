package com.example.greenmap;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.TableLayout;
import java.util.List;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;

public class FollowersActivity extends AppCompatActivity implements databaseInteracter {

    TableLayout table;
    TextView followingCount;
    TextView followerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        table = findViewById(R.id.FollowingTable);
        followerCount = findViewById(R.id.followerCount);
        followingCount = findViewById(R.id.followingCount);

        //needs to draw following from db
        User[] searchresults = {
                new User(1, "cockman", "ilovepeepee", 74, "cock@cock.com"),
                new User(2, "hoboson", "manlovestrash", 91, "cock@cock.com"),
                new User(3, "wadeeb", "showbob", 2, "cock@cock.com"),
                new User(4,"test","", 99, "cock@cock.com") };

        String l = Integer.toString(searchresults.length);
        followingCount.setText(l);
        followerCount.setText("0"); //-------------------- needs implementing with db


        for (User currUser: searchresults){
            String p = Integer.toString(currUser.carbon_saved_value);
            TableRow tr = new TableRow(this);

            TextView name = new TextView(this); name.setText(currUser.username);
            name.setTextColor(Color.parseColor("#F4F4F4"));
            name.setWidth(205); name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView points = new TextView(this); points.setText(p);
            points.setTextColor(Color.parseColor("#F4F4F4"));
            points.setWidth(205); points.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            tr.addView(name);
            tr.addView(points);
            table.addView(tr);
        }

    }

    public void goToSearch(View view){
        Intent intent = new Intent(this, AccountSearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void resultsReturned(JsonArray jArray) {

    }
}
