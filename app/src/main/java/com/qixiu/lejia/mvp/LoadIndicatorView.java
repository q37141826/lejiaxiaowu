package com.qixiu.lejia.mvp;

/**
 * Created by Long on 2016/8/12
 * 加载指示器
 */
public interface LoadIndicatorView extends BaseView {

    /*显示加载指示*/
    void showLoadIndicator();

    /*dismiss 加载指示 */
    void dismissLoadIndicator();

    /* 加载错误时要显示的信息*/
    void showErrorMsg(String msg);

}
