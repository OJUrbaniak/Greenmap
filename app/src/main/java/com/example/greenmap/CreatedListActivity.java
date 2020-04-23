package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class CreatedListActivity extends AppCompatActivity implements databaseInteracter {

    TableLayout table;
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    User currentUser;
    ArrayList<PointOfInterest> data = new ArrayList<PointOfInterest> ();
    Typeface tf; //casual
    Typeface tf2; //sans-serif-light


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_list);
        table = findViewById(R.id.createdPOITable);
        Intent i = getIntent();
        currentUser = (User)i.getSerializableExtra("User");
        dbi.selectPOIbyUser_ID(currentUser.userID, this);

        tf = Typeface.create("casual", Typeface.NORMAL);
        tf2 = Typeface.create("sans-serif-light", Typeface.NORMAL);
    }

    private void goToViewPOI(PointOfInterest currentPOI){
        Intent intent = new Intent(this,ViewPOIActivity.class);
        intent.putExtra("currentPOI", currentPOI);
        startActivity(intent);
    }

    private void deletePOI(int POI_ID){
        dbi.deletePOI(POI_ID);
    }

//    private void edit(PointOfInterest currentPOI){
//        Log.i("CreatedList", "type= "+currentPOI.type);
//        if(currentPOI.type == "w"){
//            Log.i("CreatedList", "type= "+currentPOI.type);
//            Intent intent = new Intent(CreatedListActivity.this,EditWaterActivity.class);
//            intent.putExtra("currentPOI", currentPOI);
//            startActivity(intent);
//        }else if (currentPOI.type == "r"){
//            Intent intent = new Intent(CreatedListActivity.this,EditBinActivity.class);
//            Log.i("CreatedList", "type= "+currentPOI.type);
//            intent.putExtra("currentPOI", currentPOI);
//            startActivity(intent);
//        } else if (currentPOI.type == "b"){
//            Log.i("CreatedList", "type= "+currentPOI.type);
//            Intent intent = new Intent(CreatedListActivity.this,EditBikeActivity.class);
//            intent.putExtra("currentPOI", currentPOI);
//            startActivity(intent);
//        } else {
//            return;
//        }
//    }


    public void resultsReturned(JsonArray jArray) { //Plot marker points after receiving them from the database
        if(jArray.size() > 0) {
            float rating;
            for(int n = 0; n < jArray.size(); n++) {
                Log.i("CreatedList", "POI found");
                JsonObject jObj = jArray.get(n).getAsJsonObject(); //Get the POI object
                //Define attributes for passing user information around front end
                rating = (float) jObj.get("Review_Rating").getAsInt() / jObj.get("No_Reviews").getAsInt();
                //searchResults.add(new PoiSearchInfo(jObj.get("Name").toString(), jObj.get("Description").toString(), jObj.get("Type").getAsString(), rating, jObj.get("Latitude").getAsFloat(), jObj.get("Longitude").getAsFloat(), jObj.get("POI_ID").getAsInt()));

                data.add(new PointOfInterest(
                        jObj.get("POI_ID").getAsInt(),
                        jObj.get("Name").toString(),
                        jObj.get("Description").toString(),
                        (double) jObj.get("Latitude").getAsFloat(),
                        (double) jObj.get("Longitude").getAsFloat(),
                        jObj.get("Type").getAsString()
                ));

                final int arrayListIndex = n + 1;
                Log.i("CreatedList", "added POI name= "+ jObj.get("Name").toString() + " type = "+ jObj.get("Type").getAsString()+ " index = "+ arrayListIndex+ " POID = "+ data.get(arrayListIndex).id);

                String poiName = jObj.get("Name").toString().replaceAll("\"", "");
                String poiDesc = jObj.get("Description").toString().replaceAll("\"", "");

                //Table row
                TableRow tr = new TableRow(this);
                TableRow.LayoutParams itemLayout;
                itemLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.25f);

                int leftMargin=10;
                int topMargin=4;
                int rightMargin=10;
                int bottomMargin=4;

                itemLayout.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                //Name
                TextView name = new TextView(this);
                name.setText(poiName);
                name.setTypeface(tf);
                name.setTextColor(Color.parseColor("#F4F4F4"));

                //Desc
                TextView desc = new TextView(this);
                desc.setText(poiDesc);
                desc.setTypeface(tf);
                desc.setTextColor(Color.parseColor("#F4F4F4"));

                //Set layout for content
                name.setLayoutParams(itemLayout);
                desc.setLayoutParams(itemLayout);

                //17 = Gravity.CENTER
                tr.setGravity(17);
                name.setGravity(17);

                tr.addView(name);
                tr.addView(desc);

                //ViewPOI button
                final Button viewButton = new Button(this);
                viewButton.setText("Edit POI");
                viewButton.setTypeface(tf2);
                viewButton.setBackgroundColor(Color.parseColor("#777777"));
                viewButton.setTextColor(Color.parseColor("#F4F4F4"));
                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editPressed(arrayListIndex, v);
                    }
                });
                viewButton.setLayoutParams(itemLayout);
                viewButton.setGravity(17);
                tr.addView(viewButton);

                //Admin buttons
                final Button deleteButton = new Button(this);
                deleteButton.setText("Delete POI");
                deleteButton.setTypeface(tf2);
                deleteButton.setBackgroundColor(Color.parseColor("#777777"));
                deleteButton.setTextColor(Color.parseColor("#F4F4F4"));
                deleteButton.setLayoutParams(itemLayout);
                deleteButton.setGravity(17);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePOI(data.get(arrayListIndex).id);
                        deleteButton.setTextColor(Color.parseColor("#910000"));
                        deleteButton.setText("Deleted");
                    }
                });
                tr.addView(deleteButton);

                //Add row to table
                table.addView(tr);
//                //add the POIs to the table
//                TableRow tr = new TableRow(this);
//                TextView name = new TextView(this);
//                TextView desc = new TextView(this);
//
//                name.setText(jObj.get("Name").toString().replace("\"",""));
//                desc.setText(jObj.get("Description").toString().replace("\"",""));
//
//                name.setTextColor(Color.parseColor("#F4F4F4"));
//                desc.setTextColor(Color.parseColor("#F4F4F4"));
//
//                Button viewButton = new Button(this);
//                Button delete = new Button(this);
//
//                viewButton.setText("Edit POI"); viewButton.setBackgroundColor(Color.parseColor("#777777"));
//                viewButton.setTextColor(Color.parseColor("#F4F4F4")); viewButton.setBackground(getDrawable(R.drawable.button_rounded));
//
//                viewButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Log.i("CreatedList", "type= "+data.type);
//                        if(data.type.equals("w")){
//                            Log.i("CreatedList", "type= "+data.type);
//                            Intent intent = new Intent(v.getContext(),EditWaterActivity.class);
//                            intent.putExtra("currentPOI", data);
//                            startActivity(intent);
//                        }else if (data.type.equals("r")){
//                            Intent intent = new Intent(v.getContext(),EditBinActivity.class);
//                            Log.i("CreatedList", "type= "+data.type);
//                            intent.putExtra("currentPOI", data);
//                            startActivity(intent);
//                        } else if (data.type.equals("b")){
//                            Log.i("CreatedList", "type= "+data.type);
//                            Intent intent = new Intent(v.getContext(),EditBikeActivity.class);
//                            intent.putExtra("currentPOI", data);
//                            startActivity(intent);
//                        } else {
//                            return;
//                        }
//                        Log.i("VIEW", String.valueOf(v));
//                    }
//                });
//
//                final int user_id= data.id;
//                delete.setText("Delete"); delete.setBackgroundColor(Color.parseColor("#777777"));
//                delete.setTextColor(Color.parseColor("#922a31")); delete.setBackground(getDrawable(R.drawable.button_rounded));
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        deletePOI(user_id);
//                    }
//                });
//
//                TableRow.LayoutParams itemLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.30f);
//                TableRow.LayoutParams editButtonLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.20f);
//                TableRow.LayoutParams deleteButtonLayout = editButtonLayout;
//
//                editButtonLayout.setMargins(5,0,0,10);
//                deleteButtonLayout.setMargins(10,0,0,10);
//
//                name.setLayoutParams(itemLayout);
//                desc.setLayoutParams(itemLayout);
//                viewButton.setLayoutParams(editButtonLayout);
//                delete.setLayoutParams(deleteButtonLayout);
//
//                tr.setGravity(17);
//                name.setGravity(17);
//                desc.setGravity(17);
//                viewButton.setGravity(17);
//                delete.setGravity(17);
//
//                tr.addView(name);
//                tr.addView(desc);
//                tr.addView(viewButton);
//                tr.addView(delete);
//                table.addView(tr);
            }

        } else {
            //no POIs found
        }
    }

    private void editPressed(int index, View v){

        if(data.get(index).type.equals("w")){
            Log.i("CreatedList", "type= "+data.get(index).type);
            Intent intent = new Intent(v.getContext(),EditWaterActivity.class);
            intent.putExtra("currentPOI", data.get(index));
            startActivity(intent);
        }else if (data.get(index).type.equals("r")){
            Intent intent = new Intent(v.getContext(),EditBinActivity.class);
            Log.i("CreatedList", "type= "+data.get(index).type);
            intent.putExtra("currentPOI", data.get(index));
            startActivity(intent);
        } else if (data.get(index).type.equals("b")){
            Log.i("CreatedList", "type= "+data.get(index).type);
            Intent intent = new Intent(v.getContext(),EditBikeActivity.class);
            intent.putExtra("currentPOI", data.get(index));
            startActivity(intent);
        } else {
            return;
        }
    }
}
