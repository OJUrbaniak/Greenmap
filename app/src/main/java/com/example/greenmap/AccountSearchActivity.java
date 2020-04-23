package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class AccountSearchActivity extends AppCompatActivity implements databaseInteracter {
    EditText nameSearch;
    ImageButton searchButton;
    TableLayout nameTable;
    ArrayList<User> searchResults  = new ArrayList<User>();
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    User currentUser;

    TextView banLabel;
    TextView makeAdminLabel;

    Typeface tf; //casual
    Typeface tf2; //sans-serif-light

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_search);
        nameTable = findViewById(R.id.POITable);
        nameSearch = findViewById(R.id.nameSearch);
        searchButton = findViewById(R.id.searchButton);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");

        banLabel = findViewById(R.id.actionLabel);
        makeAdminLabel = findViewById(R.id.makeAdminLabel);

        tf = Typeface.create("casual", Typeface.NORMAL);
        tf2 = Typeface.create("sans-serif-light", Typeface.NORMAL);

        TableRow tr = new TableRow(this);
        nameTable.removeAllViews();
//        TextView name = new TextView(this); name.setText("Name"); name.setTextColor(Color.parseColor("#F4F4F4")); name.setWidth(400); name.setTypeface(tf);
//        TextView add = new TextView(this); add.setText("Follow"); add.setTextColor(Color.parseColor("#F4F4F4")); add.setWidth(400); add.setTypeface(tf);
        Log.i("nearbyList", "currentuser permissionlevel  is "+ currentUser.permissionLevel);
        if (currentUser.permissionLevel > 0){
            Log.i("nearbyList", "user is admin");
            banLabel.setVisibility(View.VISIBLE);
            makeAdminLabel.setVisibility(View.VISIBLE);

//            TextView ban = new TextView(this); ban.setText("Ban User"); ban.setTextColor(Color.parseColor("#F4F4F4")); ban.setWidth(400); ban.setTypeface(tf);
//            TextView admin = new TextView(this); admin.setText("Make Admin"); admin.setTextColor(Color.parseColor("#F4F4F4")); admin.setWidth(400); admin.setTypeface(tf);
//            add.setWidth(400);
//            name.setWidth(400);
//            tr.addView(name);
//            tr.addView(add);
//            tr.addView(ban);
//            tr.addView(admin);
        } else{
            banLabel.setVisibility(View.GONE);
            makeAdminLabel.setVisibility(View.GONE);

//            tr.addView(name);
//            tr.addView(add);
        }
        //nameTable.addView(tr);
    }

    public void search(View view){

        dbi.selectUserbyUsername(nameSearch.getText().toString() , this);


    }

    private void showResults(){
//        int count = nameTable.getChildCount();
//        for (int i = 1; i < count; i++) {
//            View child = nameTable.getChildAt(i);
//            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
//        }
        for (final User result: searchResults) {

            String username = result.username.toString().replaceAll("\"", "");

            //Table row
            TableRow.LayoutParams itemLayout;
            TableRow tr = new TableRow(this);
            if(currentUser.permissionLevel > 0){
                itemLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
            } else{
                itemLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
            }


            int leftMargin=10;
            int topMargin=4;
            int rightMargin=10;
            int bottomMargin=4;

            itemLayout.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

            //Name
            TextView name = new TextView(this);
            name.setText(username);
            name.setTypeface(tf);
            name.setTextColor(Color.parseColor("#F4F4F4"));

            //Follow button
            final Button followButton = new Button(this);
            followButton.setText("Follow");
            followButton.setTypeface(tf2);
            followButton.setBackgroundColor(Color.parseColor("#777777"));
            followButton.setTextColor(Color.parseColor("#F4F4F4"));
            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    follow(result);
                    followButton.setText("Followed!");
                }
            });

            //Admin buttons
            final Button banButton = new Button(this);
            final Button makeAdminButton = new Button(this);
            if(currentUser.permissionLevel > 0){
                banButton.setText("Ban User");
                banButton.setTypeface(tf2);
                banButton.setBackgroundColor(Color.parseColor("#777777"));
                banButton.setTextColor(Color.parseColor("#F4F4F4"));
                banButton.setLayoutParams(itemLayout);
                banButton.setGravity(17);
                banButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ban(result);
                        banButton.setTextColor(Color.parseColor("#910000"));
                        banButton.setText("User Banned");
                    }
                });

                makeAdminButton.setText("Make Admin");
                makeAdminButton.setTypeface(tf2);
                makeAdminButton.setBackgroundColor(Color.parseColor("#777777"));
                makeAdminButton.setTextColor(Color.parseColor("#F4F4F4"));
                makeAdminButton.setLayoutParams(itemLayout);
                makeAdminButton.setGravity(17);
                makeAdminButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        follow(result);
                        makeAdminButton.setText("Permissions Given");
                    }
                });
            }

            //Set layout for content
            tr.setLayoutParams(itemLayout);
            name.setLayoutParams(itemLayout);
            followButton.setLayoutParams(itemLayout);

            //17 = Gravity.CENTER
            tr.setGravity(17);
            name.setGravity(17);
            followButton.setGravity(17);


            tr.addView(name);
            tr.addView(followButton);
            if(currentUser.permissionLevel > 0){
                tr.addView(banButton);
                tr.addView(makeAdminButton);
            }
            nameTable.addView(tr);

//            final String userN = result.username;
//            TableRow tr = new TableRow(this);
//
//            TextView name = new TextView(this);
//            name.setText(userN);
//            name.setTextColor(Color.parseColor("#F4F4F4"));
//            name.setWidth(500);
//            name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
//            Button add = new Button(this);
//            add.setText("Follow");
//            add.setBackground(getDrawable(R.drawable.button_rounded));
//            add.setTextColor(Color.parseColor("#F4F4F4"));
//            add.setWidth(300);
//            add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    follow(result);
//                }
//            });
//
//            if (currentUser.permissionLevel > 0) {
//            Button admin = new Button(this);
//            admin.setText("admin");
//            admin.setBackground(getDrawable(R.drawable.button_rounded));
//            admin.setTextColor(Color.parseColor("#F4F4F4"));
//            admin.setWidth(300);
//            admin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    admin(result);
//                }
//            });
//
//
//            Button ban = new Button(this);
//            ban.setText("Ban");
//            ban.setBackground(getDrawable(R.drawable.button_rounded));
//            ban.setTextColor(Color.parseColor("#922a31"));
//            ban.setWidth(300);
//            ban.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ban(result);
//                }
//            });
//
//            tr.addView(name);
//            tr.addView(add);
//            tr.addView(ban);
//            tr.addView(admin);
//            } else {
//                tr.addView(name);
//                tr.addView(add);
//            }
//            nameTable.addView(tr);

        }
    }

    public void follow(User user){
        //add to followers
        Log.i("AccSearch", "followed user "+ user.username);
        dbi.insertFollow(user.userID, currentUser.userID);
    }

    public void ban(User user){
        //add to followers
        Log.i("AccSearch", "banned user "+ user.username);
        dbi.banUser(user.userID);
    }

    public void admin(User user){
        //add to followers
        Log.i("AccSearch", "admined user "+ user.username);
        dbi.insertAdmin(user.userID, 1);
    }

    @Override
    public void resultsReturned(JsonArray jArray) {
        //returns search results
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
