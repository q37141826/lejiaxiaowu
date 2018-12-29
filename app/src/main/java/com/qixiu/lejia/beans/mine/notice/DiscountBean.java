package com.qixiu.lejia.beans.mine.notice;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/7/30.
 */

public class DiscountBean extends BaseBean<DiscountBean.OBean> {



    public static class OBean {
        /**
         * ne_id : 1229
         * ne_user_id : 2400,2399,2398,2397
         * ne_createtime :
         * ne_title : 签约成功
         * ne_content : 核事故后价格带敬爱个点击
         * ne_url : http://lj.lejiaxiaowu.com/public/images/2018072715462999524841.png
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String ne_id;
            private String ne_user_id;
            private String ne_createtime;
            private String ne_title;
            private String ne_content;
            private String ne_url;

            public String getNe_id() {
                return ne_id;
            }

            public void setNe_id(String ne_id) {
                this.ne_id = ne_id;
            }

            public String getNe_user_id() {
                return ne_user_id;
            }

            public void setNe_user_id(String ne_user_id) {
                this.ne_user_id = ne_user_id;
            }

            public String getNe_createtime() {
                return ne_createtime;
            }

            public void setNe_createtime(String ne_createtime) {
                this.ne_createtime = ne_createtime;
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

            public String getNe_url() {
                return ne_url;
            }

            public void setNe_url(String ne_url) {
                this.ne_url = ne_url;
            }
        }
    }
}
