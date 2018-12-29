package com.qixiu.lejia.beans.home.event;

import com.qixiu.lejia.api.request.BaseBean;

/**
 * Created by my on 2018/7/27.
 */

public class EventDetailsBean extends BaseBean<EventDetailsBean.OBean> {


    public static class OBean {
        private String ay_id;
        private String ay_title;
        private String ay_address;
        private String ay_starttime;
        private String ay_endtime;
        private String ay_link;
        private String ay_detailurl;
        private String ay_post_content;
        private int ay_status;

        public String getAy_id() {
            return ay_id;
        }

        public void setAy_id(String ay_id) {
            this.ay_id = ay_id;
        }

        public String getAy_title() {
            return ay_title;
        }

        public void setAy_title(String ay_title) {
            this.ay_title = ay_title;
        }

        public String getAy_address() {
            return ay_address;
        }

        public void setAy_address(String ay_address) {
            this.ay_address = ay_address;
        }

        public String getAy_starttime() {
            return ay_starttime;
        }

        public void setAy_starttime(String ay_starttime) {
            this.ay_starttime = ay_starttime;
        }

        public String getAy_endtime() {
            return ay_endtime;
        }

        public void setAy_endtime(String ay_endtime) {
            this.ay_endtime = ay_endtime;
        }

        public String getAy_link() {
            return ay_link;
        }

        public void setAy_link(String ay_link) {
            this.ay_link = ay_link;
        }

        public String getAy_detailurl() {
            return ay_detailurl;
        }

        public void setAy_detailurl(String ay_detailurl) {
            this.ay_detailurl = ay_detailurl;
        }

        public String getAy_post_content() {
            return ay_post_content;
        }

        public void setAy_post_content(String ay_post_content) {
            this.ay_post_content = ay_post_content;
        }

        public int getAy_status() {
            return ay_status;
        }

        public void setAy_status(int ay_status) {
            this.ay_status = ay_status;
        }
    }
}
