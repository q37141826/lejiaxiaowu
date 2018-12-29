package com.qixiu.lejia.beans.mine.points;

import com.qixiu.lejia.api.request.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/7/26.
 */

public class PointsBean extends BaseBean<PointsBean.OBean>{


    public static class OBean {
        /**
         * ui_title : 完善个人资料
         * ui_createtime : 2018/06/20 18:24
         * ui_integral : 5
         * ui_type : 1
         */

        private List<ReduceBean> reduce;
        /**
         * ui_title : 完善个人资料
         * ui_createtime : 2018/06/20 18:24
         * ui_integral : 5
         * ui_type : 1
         */

        private List<AddBean> add;

        public List<ReduceBean> getReduce() {
            return reduce;
        }

        public void setReduce(List<ReduceBean> reduce) {
            this.reduce = reduce;
        }

        public List<AddBean> getAdd() {
            return add;
        }

        public void setAdd(List<AddBean> add) {
            this.add = add;
        }

        public static class ReduceBean {
            private String ui_title;
            private String ui_createtime;
            private String ui_integral;
            private String ui_type;

            public String getUi_title() {
                return ui_title;
            }

            public void setUi_title(String ui_title) {
                this.ui_title = ui_title;
            }

            public String getUi_createtime() {
                return ui_createtime;
            }

            public void setUi_createtime(String ui_createtime) {
                this.ui_createtime = ui_createtime;
            }

            public String getUi_integral() {
                return ui_integral;
            }

            public void setUi_integral(String ui_integral) {
                this.ui_integral = ui_integral;
            }

            public String getUi_type() {
                return ui_type;
            }

            public void setUi_type(String ui_type) {
                this.ui_type = ui_type;
            }
        }

        public static class AddBean {
            private String ui_title;
            private String ui_createtime;
            private String ui_integral;
            private String ui_type;

            public String getUi_title() {
                return ui_title;
            }

            public void setUi_title(String ui_title) {
                this.ui_title = ui_title;
            }

            public String getUi_createtime() {
                return ui_createtime;
            }

            public void setUi_createtime(String ui_createtime) {
                this.ui_createtime = ui_createtime;
            }

            public String getUi_integral() {
                return ui_integral;
            }

            public void setUi_integral(String ui_integral) {
                this.ui_integral = ui_integral;
            }

            public String getUi_type() {
                return ui_type;
            }

            public void setUi_type(String ui_type) {
                this.ui_type = ui_type;
            }
        }
    }
}
