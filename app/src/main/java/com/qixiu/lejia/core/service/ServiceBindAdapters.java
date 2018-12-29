package com.qixiu.lejia.core.service;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/6/6
 * <pre>
 *     生活服务相关BindAdapter
 * </pre>
 */
public class ServiceBindAdapters {

    //设置支付状态对应的显示
    @BindingAdapter("servicePayStatus")
    public static void bindRentPayStatus(TextView view, int payStatus) {
        int strRes;         //文字
        int colorRes;       //颜色
        int backgroundRes;  //背景

        if (payStatus == 0) {
            //未生成账单
            strRes = R.string.rent_state_not;
            colorRes = R.color.color_bill_state_not;
            backgroundRes = R.drawable.bill_state_not;
        } else if (payStatus == 1) {
            //已缴费
            strRes = R.string.rent_state_payed;
            colorRes = R.color.color_bill_state_payed;
            backgroundRes = R.drawable.bill_state_payed;
        } else if (payStatus == 2) {
            //欠费
            strRes = R.string.rent_state_arrearage;
            colorRes = R.color.color_bill_state_arrearage;
            backgroundRes = R.drawable.bill_state_arrearage;

        } else {
            //代缴费
            strRes = R.string.rent_state_nopay;
            colorRes = R.color.color_bill_state_arrearage;
            backgroundRes = R.drawable.bill_state_arrearage;
        }

        view.setText(strRes);
        view.setTextColor(ContextCompat.getColor(view.getContext(), colorRes));
        view.setBackgroundResource(backgroundRes);

    }

}
