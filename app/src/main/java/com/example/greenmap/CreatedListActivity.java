package com.example.greenmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class CreatedListActivity extends AppCompatActivity implements databaseInteracter, SearchView.OnQueryTextListener {

    TableLayout table;
    DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
    User currentUser;
    ArrayList<PointOfInterest> data = new ArrayList<PointOfInterest> ();
    Typeface tf; //casual
    Typeface tf2; //sans-serif-light

    int arrayListIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_list);
        //android.widget.SearchView search = findViewById(R.id.sea)
        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null,
                null);
        TextView searchTextView = (TextView) searchView.findViewById(id);
        searchTextView.setTextColor(Color.WHITE);

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
            }

            for(final PointOfInterest currentItem : data){

                //Log.i("CreatedList", "added POI name= "+ jObj.get("Name").toString() + " type = "+ jObj.get("Type").getAsString()+ " index = "+ arrayListIndex+ " POID = "+ data.get(arrayListIndex).id);

//                String poiName = jObj.get("Name").toString().replaceAll("\"", "");
//                String poiDesc = jObj.get("Description").toString().replaceAll("\"", "");

                String poiName = currentItem.name.replaceAll("\"", "");
                String poiDesc = currentItem.desc.replaceAll("\"", "");

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
                        Log.i("Edit Button", "clicked");
//                        editPressed(arrayListIndex, v);
                        Log.i("Edit button:", currentItem.name);
                        if(currentItem.type.equals('w')){
                            Log.i("CreatedList", "type= "+currentItem.type);
                            Intent intent = new Intent(v.getContext(),EditWaterActivity.class);
                            intent.putExtra("currentPOI", currentItem);
                            startActivityForResult(intent, 1);
                        }else if (currentItem.type.equals('r')){
                            Intent intent = new Intent(v.getContext(),EditBinActivity.class);
                            Log.i("CreatedList", "type= "+currentItem.type);
                            intent.putExtra("currentPOI", currentItem);
                            startActivityForResult(intent, 1);
                        } else if (currentItem.type.equals('b')){
                            Log.i("CreatedList", "type= "+currentItem.type);
                            Intent intent = new Intent(v.getContext(),EditBikeActivity.class);
                            intent.putExtra("currentPOI", currentItem);
                            startActivityForResult(intent, 1);
                        } else {
                            return;
                        }
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
                        deleteButton.setText("Deleting...");
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                tr.addView(deleteButton);

                //Add row to table
                table.addView(tr);
            }

        } else {
            //no POIs found
        }
    }

    private void editPressed(int index, View v){

        Log.i("Edit Button", "index: "+ index);
        Log.i("Edit button:", data.get(index).name);

        if(data.get(index).type.equals('w')){
            Log.i("CreatedList", "type= "+data.get(index).type);
            Intent intent = new Intent(v.getContext(),EditWaterActivity.class);
            intent.putExtra("currentPOI", data.get(index));
            startActivityForResult(intent, 1);
        }else if (data.get(index).type.equals('r')){
            Intent intent = new Intent(v.getContext(),EditBinActivity.class);
            Log.i("CreatedList", "type= "+data.get(index).type);
            intent.putExtra("currentPOI", data.get(index));
            startActivityForResult(intent, 1);
        } else if (data.get(index).type.equals('b')){
            Log.i("CreatedList", "type= "+data.get(index).type);
            Intent intent = new Intent(v.getContext(),EditBikeActivity.class);
            intent.putExtra("currentPOI", data.get(index));
            startActivityForResult(intent, 1);
        } else {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Log.i("onActivityResult", "RESULT OK");
            Intent refresh = new Intent(this, CreatedListActivity.class);
            refresh.putExtra("User", (Parcelable) currentUser);
            startActivity(refresh);
            this.finish();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        for (int i = 0; i < table.getChildCount(); i++) {
            View v = table.getChildAt(i);
            if (v instanceof TableRow) {
                View nameView = ((TableRow) v).getChildAt(0);
                if (nameView instanceof TextView) {
                    //Log.d("SEARCHD",((TextView) nameView).getText().toString()+"  searchString: "+s);
                    String name = ((TextView) nameView).getText().toString();
                    if (!name.toUpperCase().contains(s.toUpperCase())) {
                        v.setVisibility(View.GONE);
                    }
                    else {
                        v.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        return false;
    }
}
