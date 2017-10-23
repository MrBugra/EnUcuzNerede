package com.example.mrbug.enucuznerede.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mrbug.enucuznerede.App;
import com.parse.ParseGeoPoint;

import java.io.Serializable;

/**
 * Created by MrBug on 16.10.2017.
 */

public class AppShares  {

    public String getObjectID() {
        return objectID;
    }

    public String getLongAdress() {
        return longAdress;
    }

    public void setLongAdress(String longAdress) {
        this.longAdress = longAdress;
    }

    private String longAdress;

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    private String objectID;
    private String User;

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Double discounted) {
        this.discounted = discounted;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String title;
    private Double price;
    private Double discounted;
    private Double locationLatitude;
    private Double locationLongitude;
    private Integer id;

    public  AppShares(){

    }


}
