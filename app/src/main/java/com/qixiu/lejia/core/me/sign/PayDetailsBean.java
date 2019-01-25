package com.qixiu.lejia.core.me.sign;

import com.qixiu.lejia.api.request.BaseBean;

//
public class PayDetailsBean  extends BaseBean<PayDetailsBean.OBean> {


    public static class OBean {
        /**
         * store_name : 乐家小屋·第五季
         * ro_number : 电表111
         * left : -8.8
         * electricity_fees : 1.00
         * store_id : 38
         */

        private String store_name;
        private String ro_number;
        private double left;
        private String electricity_fees;
        private String store_id;

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getRo_number() {
            return ro_number;
        }

        public void setRo_number(String ro_number) {
            this.ro_number = ro_number;
        }

        public double getLeft() {
            return left;
        }

        public void setLeft(double left) {
            this.left = left;
        }

        public String getElectricity_fees() {
            return electricity_fees;
        }

        public void setElectricity_fees(String electricity_fees) {
            this.electricity_fees = electricity_fees;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }
    }
}
