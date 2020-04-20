package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AccountSearchActivity extends AppCompatActivity {
    EditText nameSearch;
    ImageButton searchButton;
    TableLayout nameTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_search);
    }

    public void search(View view){
        nameTable = findViewById(R.id.nameTable);
        nameSearch = findViewById(R.id.nameSearch);
        searchButton = findViewById(R.id.searchButton);

        String[] foundnames = {"Jim","Pete"}; //find from db using nameSearch

        for (String currUser: foundnames){
            final String user = currUser;
            TableRow tr = new TableRow(this);

            TextView name = new TextView(this); name.setText(user);
            name.setTextColor(Color.parseColor("#F4F4F4"));
            name.setWidth(270); name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            Button add = new Button(this);
            add.setText("Follow"); add.setBackground(getDrawable(R.drawable.button_rounded));
            add.setTextColor(Color.parseColor("#F4F4F4")); add.setWidth(142);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow(user);
                }
            });

            tr.addView(name);
            tr.addView(add);
            nameTable.addView(tr);

        }



    }

    public void follow(String username){
        //add to followers
    }
}
