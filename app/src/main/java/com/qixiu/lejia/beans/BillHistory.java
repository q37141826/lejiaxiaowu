package com.qixiu.lejia.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/6/5
 */
public class BillHistory {

    @SerializedName("time")
    private String month;

    @SerializedName("paytotal")
    private String total;

    @SerializedName("list")
    private List<Expenditure> expenditures;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Expenditure> getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(List<Expenditure> expenditures) {
        this.expenditures = expenditures;
    }

    public static class Expenditure {
        private  int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @SerializedName("hy_id")
        private String billId;

        @SerializedName("pa_id")
        private String payId;

        //缴费类型,1是房租,2是水费,3是电费
        @SerializedName("bi_type")
        private int type;

        //支付方式,1是支付宝,2是微信
        @SerializedName("bi_payment")
        private int payWay;

        @SerializedName("bi_money")
        private String sum;

        @SerializedName("bi_time")
        private String datetime;

        public String getTypeText() {
            String s;
            if (type == 1) {
                s = "房租";
            } else if (type == 2) {
                s = "水费";
            } else {
                s = "电费";
            }
            return s + "-充值";
        }

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPayWay() {
            return payWay;
        }

        public void setPayWay(int payWay) {
            this.payWay = payWay;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }
    }



}
