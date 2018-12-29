package com.qixiu.lejia.beans.mine.notice;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/7/27.
 */

public class BillNoticeBean extends BaseBean<BillNoticeBean.OBean>{


    public static class OBean {
        private int page;
        /**
         * ne_createtime : 2018/07/26 18:32:36
         * ne_url : http://lj.lejiaxiaowu.com/public/images/2018072410494394026184.png
         * ne_content : 您居住的乐家小屋·第三季M310&nbsp&nbsp6-7月份房费账单
         * type : 5
         * payment : {"pa_createtime":"2018-07-26 18:41:43","pa_total_money":"0.00","cycletime":"06-26-07-26","latetime":"8-10"}
         */

        private List<ListBean> list;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String ne_createtime;
            private String ne_url;
            private String ne_content;
            private String type;
            /**
             * pa_createtime : 2018-07-26 18:41:43
             * pa_total_money : 0.00
             * cycletime : 06-26-07-26
             * latetime : 8-10
             */

            private PaymentBean payment;
            /**
             * hy_last_time : 01-01
             * hy_now_time : 02-02
             * hy_createtime : 2018-07-26 18:24:03
             * hy_pay_total : 20.00
             * latetime : 8-05
             */

            private WaterBean water;
            /**
             * hy_last_time : 01-01
             * hy_now_time : 02-02
             * hy_createtime : 2018-07-26 18:25:43
             * hy_pay_total : 6.75
             * latetime : 8-05
             */

            private ElectricBean electric;

           private String className;

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getNe_createtime() {
                return ne_createtime;
            }

            public void setNe_createtime(String ne_createtime) {
                this.ne_createtime = ne_createtime;
            }

            public String getNe_url() {
                return ne_url;
            }

            public void setNe_url(String ne_url) {
                this.ne_url = ne_url;
            }

            public String getNe_content() {
                return ne_content;
            }

            public void setNe_content(String ne_content) {
                this.ne_content = ne_content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public PaymentBean getPayment() {
                return payment;
            }

            public void setPayment(PaymentBean payment) {
                this.payment = payment;
            }

            public WaterBean getWater() {
                return water;
            }

            public void setWater(WaterBean water) {
                this.water = water;
            }

            public ElectricBean getElectric() {
                return electric;
            }

            public void setElectric(ElectricBean electric) {
                this.electric = electric;
            }

            public static class PaymentBean {
                private String pa_createtime;
                private String pa_total_money;
                private String cycletime;
                private String latetime;

                public String getPa_createtime() {
                    return pa_createtime;
                }

                public void setPa_createtime(String pa_createtime) {
                    this.pa_createtime = pa_createtime;
                }

                public String getPa_total_money() {
                    return pa_total_money;
                }

                public void setPa_total_money(String pa_total_money) {
                    this.pa_total_money = pa_total_money;
                }

                public String getCycletime() {
                    return cycletime;
                }

                public void setCycletime(String cycletime) {
                    this.cycletime = cycletime;
                }

                public String getLatetime() {
                    return latetime;
                }

                public void setLatetime(String latetime) {
                    this.latetime = latetime;
                }
            }

            public static class WaterBean {
                private String hy_last_time;
                private String hy_now_time;
                private String hy_createtime;
                private String hy_pay_total;
                private String latetime;

                public String getHy_last_time() {
                    return hy_last_time;
                }

                public void setHy_last_time(String hy_last_time) {
                    this.hy_last_time = hy_last_time;
                }

                public String getHy_now_time() {
                    return hy_now_time;
                }

                public void setHy_now_time(String hy_now_time) {
                    this.hy_now_time = hy_now_time;
                }

                public String getHy_createtime() {
                    return hy_createtime;
                }

                public void setHy_createtime(String hy_createtime) {
                    this.hy_createtime = hy_createtime;
                }

                public String getHy_pay_total() {
                    return hy_pay_total;
                }

                public void setHy_pay_total(String hy_pay_total) {
                    this.hy_pay_total = hy_pay_total;
                }

                public String getLatetime() {
                    return latetime;
                }

                public void setLatetime(String latetime) {
                    this.latetime = latetime;
                }
            }

            public static class ElectricBean {
                private String hy_last_time;
                private String hy_now_time;
                private String hy_createtime;
                private String hy_pay_total;
                private String latetime;

                public String getHy_last_time() {
                    return hy_last_time;
                }

                public void setHy_last_time(String hy_last_time) {
                    this.hy_last_time = hy_last_time;
                }

                public String getHy_now_time() {
                    return hy_now_time;
                }

                public void setHy_now_time(String hy_now_time) {
                    this.hy_now_time = hy_now_time;
                }

                public String getHy_createtime() {
                    return hy_createtime;
                }

                public void setHy_createtime(String hy_createtime) {
                    this.hy_createtime = hy_createtime;
                }

                public String getHy_pay_total() {
                    return hy_pay_total;
                }

                public void setHy_pay_total(String hy_pay_total) {
                    this.hy_pay_total = hy_pay_total;
                }

                public String getLatetime() {
                    return latetime;
                }

                public void setLatetime(String latetime) {
                    this.latetime = latetime;
                }
            }
        }
    }
}
