package com.qixiu.lejia.beans.home;

import com.qixiu.lejia.api.request.BaseBean;

/**
 * Created by my on 2018/8/13.
 */

public class MessageBean extends BaseBean<MessageBean.OBean>{

    public static class OBean {
        private double noread;

        public double getNoread() {
            return noread;
        }

        public void setNoread(double noread) {
            this.noread = noread;
        }
    }
}
