/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.greenmap;

/**
 *
 * @author Laurence
 */
public class poiSearchInfo {
    public String Name;
    public String Description;
    public char Type;
    public double Rating;
    public float Lng;
    public float Lat;
    public int PoiID;
    
    public poiSearchInfo(String name, String description, char type, float rating, float lat, float lng, int poiID){
        this.Name = name;
        this.Description = description;
        this.Type = type;
        this.Rating = rating;
        this.Lat = lat;
        this.Lng = lng;
        this.PoiID = poiID;
    }
}
