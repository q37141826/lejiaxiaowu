package com.qixiu.lejia.beans.mine;

import com.qixiu.lejia.api.request.BaseBean;

/**
 * Created by my on 2018/7/19.
 */

public class MessageDetailsBean extends BaseBean<MessageDetailsBean.OBean> {



    public static class OBean {
        private String ne_title;
        private String ne_content;
        private String ne_content_title;
        private String ne_createtime;
        private String st_house_tel;

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

        public String getNe_createtime() {
            return ne_createtime;
        }

        public void setNe_createtime(String ne_createtime) {
            this.ne_createtime = ne_createtime;
        }

        public String getSt_house_tel() {
            return st_house_tel;
        }

        public void setSt_house_tel(String st_house_tel) {
            this.st_house_tel = st_house_tel;
        }
    }
}
