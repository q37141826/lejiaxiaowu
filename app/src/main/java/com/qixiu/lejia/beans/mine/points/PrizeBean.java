package com.qixiu.lejia.beans.mine.points;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/7/26.
 */

public class PrizeBean extends BaseBean<List<PrizeBean.OBean>> {




    public static class OBean {
        private String time;
        /**
         * dr_name : 价值¥39全套指甲剪
         * dr_url : http://lj.lejiaxiaowu.com/public/images/2018071917310922918291.png
         * di_createtime : 2018-07-26 14:00:59
         * di_code : Qej71BEZzL823
         * di_status : 0
         * di_date : 2018-07-27
         */

        private List<ListBean> list;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String dr_name;
            private String dr_url;
            private String di_createtime;
            private String di_code;
            private String di_status;
            private String di_date;

            public String getDr_name() {
                return dr_name;
            }

            public void setDr_name(String dr_name) {
                this.dr_name = dr_name;
            }

            public String getDr_url() {
                return dr_url;
            }

            public void setDr_url(String dr_url) {
                this.dr_url = dr_url;
            }

            public String getDi_createtime() {
                return di_createtime;
            }

            public void setDi_createtime(String di_createtime) {
                this.di_createtime = di_createtime;
            }

            public String getDi_code() {
                return di_code;
            }

            public void setDi_code(String di_code) {
                this.di_code = di_code;
            }

            public String getDi_status() {
                return di_status;
            }

            public void setDi_status(String di_status) {
                this.di_status = di_status;
            }

            public String getDi_date() {
                return di_date;
            }

            public void setDi_date(String di_date) {
                this.di_date = di_date;
            }
        }
    }
}
