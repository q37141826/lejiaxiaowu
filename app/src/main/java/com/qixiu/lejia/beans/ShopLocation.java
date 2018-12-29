package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/6/1
 */
public class ShopLocation {

    @SerializedName("st_y")
    private double lat;
    @SerializedName("st_x")
    private double lng;

    @SerializedName("detail")
    private Shop shop;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

}
