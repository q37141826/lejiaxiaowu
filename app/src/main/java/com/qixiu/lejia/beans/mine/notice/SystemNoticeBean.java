package com.qixiu.lejia.beans.mine.notice;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/7/27.
 */

public class SystemNoticeBean extends BaseBean<SystemNoticeBean.OBean> {

    public static class OBean {
        private int page;
        /**
         * ne_createtime : 2018/07/26 18:10:00
         * ne_url : http://lj.lejiaxiaowu.com/public/images/2018072419114324309916.jpg
         * ne_content : 您已成功签约乐家小屋·第三季--M310房间
         * type : 1
         * sign : {"sd_surplus_pay":"0.01","sd_starttime":"2018/05/13","sd_time":"6","sd_money_type":"2","sd_endtime":"2018/11/13"}
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
             * sd_surplus_pay : 0.01
             * sd_starttime : 2018/05/13
             * sd_time : 6
             * sd_money_type : 2
             * sd_endtime : 2018/11/13
             */




            private SignBean sign;
            /**
             * re_time : 2018/07/31
             * st_name : 乐家小屋·第三季
             */

            private ReserveBean reserve;
            /**
             * ri_createtime : 2018/07/26 18:28:59
             * ri_handle_time : 2018/07/26 18:29:29
             */

            private RefuseBean refuse;
            /**
             * ri_createtime : 2018/07/26 18:30:14
             * ri_handle_time : 2018/07/26 18:30:24
             */

            private RepairBean repair;


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

            public SignBean getSign() {
                return sign;
            }

            public void setSign(SignBean sign) {
                this.sign = sign;
            }

            public ReserveBean getReserve() {
                return reserve;
            }

            public void setReserve(ReserveBean reserve) {
                this.reserve = reserve;
            }

            public RefuseBean getRefuse() {
                return refuse;
            }

            public void setRefuse(RefuseBean refuse) {
                this.refuse = refuse;
            }

            public RepairBean getRepair() {
                return repair;
            }

            public void setRepair(RepairBean repair) {
                this.repair = repair;
            }

            public static class SignBean {
                private String sd_surplus_pay;
                private String sd_starttime;
                private String sd_time;
                private String sd_money_type;
                private String sd_endtime;

                public String getSd_surplus_pay() {
                    return sd_surplus_pay;
                }

                public void setSd_surplus_pay(String sd_surplus_pay) {
                    this.sd_surplus_pay = sd_surplus_pay;
                }

                public String getSd_starttime() {
                    return sd_starttime;
                }

                public void setSd_starttime(String sd_starttime) {
                    this.sd_starttime = sd_starttime;
                }

                public String getSd_time() {
                    return sd_time;
                }

                public void setSd_time(String sd_time) {
                    this.sd_time = sd_time;
                }

                public String getSd_money_type() {
                    return sd_money_type;
                }

                public void setSd_money_type(String sd_money_type) {
                    this.sd_money_type = sd_money_type;
                }

                public String getSd_endtime() {
                    return sd_endtime;
                }

                public void setSd_endtime(String sd_endtime) {
                    this.sd_endtime = sd_endtime;
                }
            }

            public static class ReserveBean {
                private String re_time;
                private String st_name;

                public String getRe_time() {
                    return re_time;
                }

                public void setRe_time(String re_time) {
                    this.re_time = re_time;
                }

                public String getSt_name() {
                    return st_name;
                }

                public void setSt_name(String st_name) {
                    this.st_name = st_name;
                }
            }

            public static class RefuseBean {
                private String ri_createtime;
                private String ri_handle_time;

                public String getRi_createtime() {
                    return ri_createtime;
                }

                public void setRi_createtime(String ri_createtime) {
                    this.ri_createtime = ri_createtime;
                }

                public String getRi_handle_time() {
                    return ri_handle_time;
                }

                public void setRi_handle_time(String ri_handle_time) {
                    this.ri_handle_time = ri_handle_time;
                }
            }

            public static class RepairBean {
                private String ri_createtime;
                private String ri_handle_time;

                public String getRi_createtime() {
                    return ri_createtime;
                }

                public void setRi_createtime(String ri_createtime) {
                    this.ri_createtime = ri_createtime;
                }

                public String getRi_handle_time() {
                    return ri_handle_time;
                }

                public void setRi_handle_time(String ri_handle_time) {
                    this.ri_handle_time = ri_handle_time;
                }
            }
        }
    }
}
