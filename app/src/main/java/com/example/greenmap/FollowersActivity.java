package com.example.greenmap;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FollowersActivity extends AppCompatActivity implements databaseInteracter {

    TableLayout table;
    TextView followingCount;
    TextView followerCount;
    ArrayList<User> searchResults  = new ArrayList<User>();
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        table = findViewById(R.id.FollowingTable);
        followerCount = findViewById(R.id.followerCount);
        followingCount = findViewById(R.id.followingCount);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");
        dbi.selectFollowing(currentUser.userID, this);

    }



    @Override
    public void onResume() {
        super.onResume();
        dbi.selectFollowing(currentUser.userID, this);
    }



    private void showResults(){
        int count = table.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = table.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        for (final User result: searchResults){

            String p = Integer.toString(result.carbon_saved_value);
            TableRow tr = new TableRow(this);

            TextView name = new TextView(this); name.setText(result.username);
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
        intent.putExtra("User", (Parcelable) currentUser);
        startActivity(intent);
    }

    @Override
    public void resultsReturned(JsonArray jArray) {
        searchResults.clear();
        if(jArray.size() > 0) {
            for(int n = 0; n < jArray.size(); n++) {
                Log.i("AccSearch", "results returned= " + String.valueOf(jArray.size()));
                Log.i("AccSearch", "user found");
                JsonObject jObj = jArray.get(n).getAsJsonObject(); //Get the POI object
                //Define attributes for passing user information around front end
                searchResults.add(new User(jObj.get("User_ID").getAsInt(), jObj.get("Username").toString(), jObj.get("Carbon_Saved_Points").getAsInt()));
            }
            Log.i("AccSearch", String.valueOf(searchResults.size()));

        } else {
            //no users found
            Log.i("AccSearch", "no users found");
        }
        showResults();
    }
}
