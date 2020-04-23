package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;

public class ProfileActivity extends AppCompatActivity implements databaseInteracter {

    User user;
    TextView usernameTextLabel;
    TextView carbonPointsLabel;
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");
        dbi.selectUserbyID(user.userID, this);

        usernameTextLabel = findViewById(R.id.textView17);
        carbonPointsLabel = findViewById(R.id.textView20);

        usernameTextLabel.setText(user.username);
        carbonPointsLabel.setText("Carbon Points: " + user.carbon_saved_value);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        dbi.selectUserbyID(user.userID, this);
    }

    public void signOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToCreated(View view){
        Intent intent = new Intent (this,CreatedListActivity.class);
        intent.putExtra("User", (Parcelable) user);
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

    public void goToFollowers(View view){
        Intent intent = new Intent(this,FollowersActivity.class);
        intent.putExtra("User", (Parcelable) user);
        startActivity(intent);
    }


    @Override
    public void resultsReturned(JsonArray jArray) {
        if(jArray.size() > 0){
            Log.i("carbon", "returned");
            int carbonPoints = jArray.get(0).getAsJsonObject().get("Carbon_Saved_Points").getAsInt();
            carbonPointsLabel.setText("Carbon Points: " + carbonPoints);
            user.carbon_saved_value = carbonPoints;
        }
    }
}
