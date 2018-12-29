package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/4/28
 */
public class PayDetails {

    @SerializedName("sd_name")
    private String title;
    @SerializedName("sd_pay")
    private String money;
    @SerializedName("list")
    private List<Cost> costs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
    }

    public static class Cost {

        private String name;
        private String money;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }

}
