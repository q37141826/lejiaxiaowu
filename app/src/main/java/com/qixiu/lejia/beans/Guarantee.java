package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/6/12
 * <pre>
 *     品质保障
 * </pre>
 */
public class Guarantee {

    @SerializedName("url")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
