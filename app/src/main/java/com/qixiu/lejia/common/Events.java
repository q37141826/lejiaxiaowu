package com.qixiu.lejia.common;

/**
 * Created by Long on 2018/5/19
 * EventBus Events.
 */
public interface Events {

    /*H5界面支付成功事件*/
    class H5PaySuccessEvent {}

    /*用户签约成功事件*/
    class SignedSuccessEvent {}

    /*企业员工首页跳转入住提醒*/
    class CorporateHomePageEvent {}
}
