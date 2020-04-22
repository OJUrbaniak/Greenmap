package com.example.greenmap;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class NearbyListActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        table = findViewById(R.id.nearbyPOITable);
        //or to support all versions use
        Typeface tf = Typeface.create("casual", Typeface.NORMAL);
        TableRow tr = new TableRow(this);
//        TextView name = new TextView(this); name.setText("Name"); name.setTextColor(Color.parseColor("#F4F4F4")); name.setWidth(130); name.setTypeface(tf);
//        TextView desc = new TextView(this); desc.setText("Description"); desc.setTextColor(Color.parseColor("#F4F4F4")); desc.setWidth(130); desc.setTypeface(tf);
//        TextView viewPOI = new TextView(this); viewPOI.setText("View POI"); viewPOI.setTextColor(Color.parseColor("#F4F4F4")); viewPOI.setWidth(130); viewPOI.setTypeface(tf);
        //if admin
        /*
        TextView delete = new TextView(this); delete.setText("View POI"); delete.setTextColor(Color.parseColor("#F4F4F4")); delete.setWidth(130); delete.setTypeface(tf);
        viewPOI.setWidth(98);
        desc.setWidth(98);
        name.setWidth(98);
        tr.addView(delete);
         */
//        tr.addView(name);
//        tr.addView(desc);
//        tr.addView(viewPOI);
//        table.addView(tr);

        ArrayList<PointOfInterest> data = new ArrayList<>();
        data = (ArrayList<PointOfInterest>) getIntent().getSerializableExtra("dataArray");

        for (final PointOfInterest currItem : data) {
            tr = new TableRow(this);

            TextView name = new TextView(this);
            TextView desc = new TextView(this);
            Button viewButton = new Button(this);

            name.setText(currItem.name.replace("\"",""));
            desc.setText(currItem.desc.replace("\"",""));

            viewButton.setText("View POI"); viewButton.setBackgroundColor(Color.parseColor("#777777"));
            viewButton.setTextColor(Color.parseColor("#F4F4F4")); viewButton.setBackground(getDrawable(R.drawable.button_rounded));
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToViewPOI(currItem);
                }
            });
            //if admin
            Button delete = new Button(this);
            final int user_id= currItem.id;
            delete.setText("Delete"); delete.setBackgroundColor(Color.parseColor("#777777"));
            delete.setTextColor(Color.parseColor("#922a31")); delete.setBackground(getDrawable(R.drawable.button_rounded));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePOI(user_id);
                }
            });

            TableRow.LayoutParams buttonLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            TableRow.LayoutParams itemLayout = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.4f);

            name.setLayoutParams(itemLayout);
            desc.setLayoutParams(itemLayout);

            buttonLayout.setMargins(0,0,0,10);
            viewButton.setLayoutParams(buttonLayout);

            name.setTextColor(Color.parseColor("#FFFFFF"));
            desc.setTextColor(Color.parseColor("#FFFFFF"));

            //17 = Gravity.CENTER
            tr.setGravity(17);
            name.setGravity(17);
            desc.setGravity(17);
            viewButton.setGravity(17);

            tr.addView(name);
            tr.addView(desc);
            tr.addView(viewButton);
            table.addView(tr);
        }
    }

    private void deletePOI(int POI_ID){
        DatabaseInterfaceDBI dbi = new DatabaseInterfaceDBI();
        dbi.deletePOI(POI_ID);
    }

    public void backToMap(View view){
        finish();
    }

    public void goToViewPOI(PointOfInterest currentPOI){
        Intent intent = new Intent(this,ViewNearbyPOIActivity.class);
        intent.putExtra("currentPOI", currentPOI);
        startActivity(intent);
    }
}
