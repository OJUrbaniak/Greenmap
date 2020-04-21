package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_search);
        nameTable = findViewById(R.id.nameTable);
        nameSearch = findViewById(R.id.nameSearch);
        searchButton = findViewById(R.id.searchButton);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");
    }

    public void search(View view){

        dbi.selectUserbyUsername(nameSearch.getText().toString() , this);


    }

    private void showResults(){
        int count = nameTable.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = nameTable.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        for (final User result: searchResults){

            final String userN = result.username;
            TableRow tr = new TableRow(this);

            TextView name = new TextView(this); name.setText(userN);
            name.setTextColor(Color.parseColor("#F4F4F4"));
            name.setWidth(500); name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            Button add = new Button(this);
            add.setText("Follow"); add.setBackground(getDrawable(R.drawable.button_rounded));
            add.setTextColor(Color.parseColor("#F4F4F4")); add.setWidth(500);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow(result);
                }
            });

            tr.addView(name);
            tr.addView(add);
            nameTable.addView(tr);

        }
    }

    public void follow(User user){
        //add to followers
        Log.i("AccSearch", "follow user "+ user.username);
        dbi.insertFollow(currentUser.userID, user.userID);
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
