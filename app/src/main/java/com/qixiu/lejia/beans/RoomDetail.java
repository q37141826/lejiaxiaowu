package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/6/1
 */
public class RoomDetail {

    @SerializedName("apartment")
    private RoomInfo roomInfo;

    @SerializedName("apartment_type")
    private List<ShopDetail.ShopImages> roomImages;

    @SerializedName("apartment_configimg")
    private List<RoomConfig> configs;

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public List<ShopDetail.ShopImages> getRoomImages() {
        return roomImages;
    }

    public void setRoomImages(List<ShopDetail.ShopImages> roomImages) {
        this.roomImages = roomImages;
    }

    public List<RoomConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<RoomConfig> configs) {
        this.configs = configs;
    }

    public static class RoomInfo {

        @SerializedName("ap_name")
        private String name;
        @SerializedName("ap_introduction")
        private String intro;
        @SerializedName("ap_size")
        private String style;    //风格
        @SerializedName("st_name")
        private String shopName;
        @SerializedName("st_id")
        private String shopId;
        @SerializedName("ap_room")
        private String area;     //面积


        @SerializedName("ap_money")
        private String lowestPrice;     //最低价
        @SerializedName("ap_short_money")
        private String highestPrice;    //最高价

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(String lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public String getHighestPrice() {
            return highestPrice;
        }

        public void setHighestPrice(String highestPrice) {
            this.highestPrice = highestPrice;
        }
    }

    public static class RoomConfig {
        @SerializedName("ac_id")
        private String id;
        @SerializedName("ac_title")
        private String name;
        @SerializedName("ac_url")
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
