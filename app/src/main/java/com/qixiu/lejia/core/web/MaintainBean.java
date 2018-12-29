package com.qixiu.lejia.core.web;

import com.qixiu.lejia.api.request.BaseBean;

/**
 * Created by my on 2018/12/17.
 */

public class MaintainBean  extends BaseBean<MaintainBean.OBean>{


    /**
     * c : 1
     * m : 查询成功
     * o : {"sd_endtime":"2019-05-14 14:38:06"}
     * e :
     */


    public static class OBean {
        private String sd_endtime;

        public String getSd_endtime() {
            return sd_endtime;
        }

        public void setSd_endtime(String sd_endtime) {
            this.sd_endtime = sd_endtime;
        }
    }
}
