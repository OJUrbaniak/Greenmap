package com.example.greenmap;

import com.google.gson.JsonArray;
import androidx.appcompat.app.AppCompatActivity;

interface databaseInteracter {
    void  resultsReturned(JsonArray jArray);
}
