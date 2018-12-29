package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     门店
 * </pre>
 */
public class Shop {

    /*ID*/
    @SerializedName("st_id")
    private String id;

    /*标题*/
    @SerializedName("st_name")
    private String title;

    /*地址*/
    @SerializedName("st_address")
    private String address;

    /*图片*/
    @SerializedName("si_url")
    private String image;

    /*价格*/
    @SerializedName("minmoney")
    private String price;

    /*打折率*/
    @SerializedName("st_off")
    private String discount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    //是否打折
    public boolean hasDiscount() {
        Float value = Float.valueOf(discount);
        return value > 0;
    }

    @Override
    public String toString() {
        return title;
    }

}
