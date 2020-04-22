package com.example.greenmap;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

public class FollowersActivity extends AppCompatActivity implements databaseInteracter {

    //TableLayout table;
    ArrayList<User> searchResults  = new ArrayList<User>();
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    User currentUser;

    Typeface tf; //casual
    Typeface tf2; //sans serif

    TableLayout tableInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");
        dbi.selectFollowing(currentUser.userID, this);

        tableInfo = findViewById(R.id.infoTable);

        //or to support all versions use
        tf = Typeface.create("casual", Typeface.NORMAL);
        tf2 = Typeface.create("sans-serif-light", Typeface.NORMAL);
    }



    @Override
    public void onResume() {
        super.onResume();
        dbi.selectFollowing(currentUser.userID, this);
    }



    private void showResults(){
        int count = tableInfo.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tableInfo.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        for (final User result: searchResults){

            String p = Integer.toString(result.carbon_saved_value);

            String username = result.username.toString().replaceAll("\"", "");

            //Table row
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams itemLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);

            //Name
            TextView name = new TextView(this);
            name.setText(username);
            name.setTypeface(tf);
            name.setTextColor(Color.parseColor("#F4F4F4"));

            //Carbon Points
            TextView cPoints = new TextView((this));
            cPoints.setText(p);
            cPoints.setTypeface(tf);
            cPoints.setTextColor(Color.parseColor("#F4F4F4"));

            //Unfollow button
            Button unfollowButton = new Button(this);
            unfollowButton.setText("Unfollow");
            unfollowButton.setTypeface(tf2);
            unfollowButton.setBackgroundColor(Color.parseColor("#777777"));
            unfollowButton.setTextColor(Color.parseColor("#F4F4F4"));
            unfollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unfollow(result);
                }
            });

            //Set layout for content
            name.setLayoutParams(itemLayout);
            cPoints.setLayoutParams(itemLayout);
            unfollowButton.setLayoutParams(itemLayout);

            //17 = Gravity.CENTER
            tr.setGravity(17);
            name.setGravity(17);
            cPoints.setGravity(17);
            unfollowButton.setGravity(17);

            tr.addView(name);
            tr.addView(cPoints);
            tr.addView(unfollowButton);
            tableInfo.addView(tr);
        }
    }

    public void goToSearch(View view){
        Intent intent = new Intent(this, AccountSearchActivity.class);
        intent.putExtra("User", (Parcelable) currentUser);
        startActivity(intent);
    }

    public void unfollow(User user){
        //add to followers
        Log.i("AccSearch", "unfollowed user "+ user.username);
        dbi.deleteFollow(user.userID, currentUser.userID);
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
