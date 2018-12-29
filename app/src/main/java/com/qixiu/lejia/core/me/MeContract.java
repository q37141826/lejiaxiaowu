package com.qixiu.lejia.core.me;

import com.qixiu.lejia.beans.NoReadMessage;
import com.qixiu.lejia.beans.resp.MeResp;
import com.qixiu.lejia.mvp.BaseView;
import com.qixiu.lejia.mvp.CallPresenter;

/**
 * Created by Long on 2018/4/26
 */
interface MeContract {

    interface View extends BaseView {

        void showLoadFail();

        void showMeResp(MeResp resp, boolean checkProfileFull);

        void startLogin();

        void startSigned();

        void startAppointment();

        void startCredit();

        void startMessage();

        void startMeProfile();

        void startRoomie();

        void checkVersion();

        void showMeNoReadResp(NoReadMessage resp);
        void test();
    }

    interface Presenter extends CallPresenter {

        /**
         * 加载用户资料
         *
         * @param checkProfileFull 是否检查资料完整
         */
        void loadMeInfo(boolean checkProfileFull);

        void startLogin();

        void startSigned();

        void startAppointment();

        void startCredit();

        void startMessage();

        void startMeProfile();

        void startRoomie();

        void checkVersion();

        void loadMeNoRead();

        void test();
    }


}
