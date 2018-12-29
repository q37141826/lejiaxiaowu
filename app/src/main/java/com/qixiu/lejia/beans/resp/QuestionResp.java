package com.qixiu.lejia.beans.resp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.qixiu.lejia.beans.Question;

import java.util.List;

/**
 * Created by Long on 2018/6/7
 */
public class QuestionResp {

    @SerializedName("list")
    private List<Question>    questions;
    @SerializedName("storetel")
    private List<ShopContact> shopContacts;


    public static class ShopContact implements Parcelable {
        @SerializedName("st_name")
        private String shopName;
        @SerializedName("st_house_tel")
        private String contact;

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.shopName);
            dest.writeString(this.contact);
        }

        public ShopContact() {
        }

        protected ShopContact(Parcel in) {
            this.shopName = in.readString();
            this.contact = in.readString();
        }

        public static final Parcelable.Creator<ShopContact> CREATOR = new Parcelable.Creator<ShopContact>() {
            @Override
            public ShopContact createFromParcel(Parcel source) {
                return new ShopContact(source);
            }

            @Override
            public ShopContact[] newArray(int size) {
                return new ShopContact[size];
            }
        };
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<ShopContact> getShopContacts() {
        return shopContacts;
    }

    public void setShopContacts(List<ShopContact> shopContacts) {
        this.shopContacts = shopContacts;
    }

}
