package com.qixiu.lejia.core.me.sign;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/12/28.
 */

public class ElecPayBean extends BaseBean<List<ElecPayBean.OBean>>{

    public static class OBean{
        String name;
        String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
