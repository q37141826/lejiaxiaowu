package com.qixiu.lejia.beans.mine;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/7/18.
 */

public class MessageListBean extends BaseBean<List<MessageListBean.OBean>>{


    public static class OBean {
        private String ne_id;
        private String ne_title;
        private String ne_content;
        private String ne_content_title;
        private String ne_read;
        private String ne_createtime;
        private String st_house_img;
        private String st_house_tel;
        private String type;
        private String nc_name;
        private int num;

        public String getNc_name() {
            return nc_name;
        }

        public void setNc_name(String nc_name) {
            this.nc_name = nc_name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNe_id() {
            return ne_id;
        }

        public void setNe_id(String ne_id) {
            this.ne_id = ne_id;
        }

        public String getNe_title() {
            return ne_title;
        }

        public void setNe_title(String ne_title) {
            this.ne_title = ne_title;
        }

        public String getNe_content() {
            return ne_content;
        }

        public void setNe_content(String ne_content) {
            this.ne_content = ne_content;
        }

        public String getNe_content_title() {
            return ne_content_title;
        }

        public void setNe_content_title(String ne_content_title) {
            this.ne_content_title = ne_content_title;
        }

        public String getNe_read() {
            return ne_read;
        }

        public void setNe_read(String ne_read) {
            this.ne_read = ne_read;
        }

        public String getNe_createtime() {
            return ne_createtime;
        }

        public void setNe_createtime(String ne_createtime) {
            this.ne_createtime = ne_createtime;
        }

        public String getSt_house_img() {
            return st_house_img;
        }

        public void setSt_house_img(String st_house_img) {
            this.st_house_img = st_house_img;
        }

        public String getSt_house_tel() {
            return st_house_tel;
        }

        public void setSt_house_tel(String st_house_tel) {
            this.st_house_tel = st_house_tel;
        }
    }
}
