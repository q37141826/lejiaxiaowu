package com.qixiu.lejia.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/5/31
 */
public class ShopDetail {

    @SerializedName("list")
    private ShopInfo shopInfo;

    @SerializedName("recommend")
    private List<RecommendRoom> rooms;

    @SerializedName("toptype")
    private List<ShopImages> shopImages;


    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public List<RecommendRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<RecommendRoom> rooms) {
        this.rooms = rooms;
    }

    public List<ShopImages> getShopImages() {
        return shopImages;
    }

    public void setShopImages(List<ShopImages> shopImages) {
        this.shopImages = shopImages;
    }

    public static class ShopInfo {

        @SerializedName("st_name")
        private String name;
        @SerializedName("st_introduction")
        private String intro;
        @SerializedName("apartmentnum")
        private int    roomSpecNum;     //户型
        @SerializedName("minroom")
        private int    area;            //最小面积
        @SerializedName("roomnum")
        private int    source;          //房源

        @SerializedName("st_house_name")
        private String manager;
        @SerializedName("st_house_img")
        private String managerAvatar;
        @SerializedName("st_house_introduction")
        private String managerIntro;
        @SerializedName("st_house_tel")
        private String managerPhone;

        @SerializedName("st_y")
        private double lat;
        @SerializedName("st_x")
        private double lng;
        @SerializedName("st_address")
        private String location;        //地址

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

        public int getRoomSpecNum() {
            return roomSpecNum;
        }

        public void setRoomSpecNum(int roomSpecNum) {
            this.roomSpecNum = roomSpecNum;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public String getManagerIntro() {
            return managerIntro;
        }

        public void setManagerIntro(String managerIntro) {
            this.managerIntro = managerIntro;
        }

        public String getManagerPhone() {
            return managerPhone;
        }

        public void setManagerPhone(String managerPhone) {
            this.managerPhone = managerPhone;
        }

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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getManagerAvatar() {
            return managerAvatar;
        }

        public void setManagerAvatar(String managerAvatar) {
            this.managerAvatar = managerAvatar;
        }

    }

    public static class ShopImages {

        @SerializedName(value = "id", alternate = {"sy_id", "at_id"})
        private String id;

        @SerializedName(value = "name", alternate = {"sy_name", "at_name"})
        private String name;

        @SerializedName("img")
        private List<String> images;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ShopImages that = (ShopImages) o;

            return id != null ? id.equals(that.id) : that.id == null;
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }

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

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

    }

    /*public static class ShopImage {

        @SerializedName(value = "image", alternate = {"si_url", "ai_url"})
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }*/

    public static class RecommendRoom implements Parcelable {

        @SerializedName("ap_id")
        private String id;
        @SerializedName("ai_url")
        private String image;
        @SerializedName("ap_name")
        private String name;
        @SerializedName("ap_size")
        private String spec;
        @SerializedName("ap_room")
        private String area;

        @SerializedName("ap_money")
        private String lowestPrice;     //最低价
        @SerializedName("ap_short_money")
        private String highestPrice;    //最高价

        @SerializedName("st_startofftime")
        private long startTime;
        @SerializedName("st_endofftime")
        private long endTime;

        @SerializedName("st_off")
        private float discount;         //折扣


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
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

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public float getDiscount() {
            return discount;
        }

        public void setDiscount(float discount) {
            this.discount = discount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.image);
            dest.writeString(this.name);
            dest.writeString(this.spec);
            dest.writeString(this.area);
            dest.writeString(this.lowestPrice);
            dest.writeString(this.highestPrice);
            dest.writeLong(this.startTime);
            dest.writeLong(this.endTime);
            dest.writeFloat(this.discount);
        }

        public RecommendRoom() {
        }

        protected RecommendRoom(Parcel in) {
            this.id = in.readString();
            this.image = in.readString();
            this.name = in.readString();
            this.spec = in.readString();
            this.area = in.readString();
            this.lowestPrice = in.readString();
            this.highestPrice = in.readString();
            this.startTime = in.readLong();
            this.endTime = in.readLong();
            this.discount = in.readFloat();
        }

        public static final Parcelable.Creator<RecommendRoom> CREATOR = new Parcelable.Creator<RecommendRoom>() {
            @Override
            public RecommendRoom createFromParcel(Parcel source) {
                return new RecommendRoom(source);
            }

            @Override
            public RecommendRoom[] newArray(int size) {
                return new RecommendRoom[size];
            }
        };
    }

}
