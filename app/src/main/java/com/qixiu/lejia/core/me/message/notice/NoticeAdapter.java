package com.qixiu.lejia.core.me.message.notice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.mine.notice.BillNoticeBean;
import com.qixiu.lejia.beans.mine.notice.DiscountBean;
import com.qixiu.lejia.beans.mine.notice.SystemNoticeBean;
import com.qixiu.lejia.core.service.rent.RentAct;
import com.qixiu.lejia.core.service.we.WaterAndElectricityAct;
import com.qixiu.widget.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by my on 2018/7/23.
 */

public class NoticeAdapter extends RecyclerBaseAdapter {

    private String systemNoticeType[] = {"签约成功", "预约成功", "维修已受理", "维修已拒理"};
    private String systemNoticeKey[] = {"1", "2", "3", "4"};
    private Map<String, String> systemNoticeMap = new HashMap<>();

    private String period[] = {"押一付一", "押一付三", "半年付", "全年付"};//账单周期 1=押一付一 2=押一付三 3=半年付 4=全年付
    private String periodKey[] = {"1", "2", "3", "4"};//账单周期 1=押一付一 2=押一付三 3=半年付 4=全年付
    private Map<String, String> peroidMap = new HashMap<>();
    //账单
    private String billType[] = {"房费账单", "水费账单", "电费账单"};//消息内容 "type": "5", //消息分类id 5=房费账单 6=水费账单 7=电费账单
    private String billKey[] = {"5", "6", "7"};
    private Map<String, String> billMap = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.item_notice;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        for (int i = 0; i < systemNoticeType.length; i++) {
            systemNoticeMap.put(systemNoticeKey[i], systemNoticeType[i]);
        }
        for (int i = 0; i < period.length; i++) {
            peroidMap.put(periodKey[i], period[i]);
        }
        for (int i = 0; i < billKey.length; i++) {
            billMap.put(billKey[i], billType[i]);
        }

        return new NoticeHolder(itemView, context, this);
    }

    public class NoticeHolder extends RecyclerBaseHolder {
        //通知的item部分
        LinearLayout linearLayout_Lease;
        TextView textView_time_notice_item, textView_Lease_title, textView_lease_content,
                textView_lease_money, textView_lease_time_start, textView_lease_time_last,
                textView_lease_time_period, textView_lease_time_end;
        RoundedImageView imageView_lease_pic;
        //通知的item部分
        LinearLayout linearLayout_appointment;
        TextView textView_appointment_title, textView_appoint_content, textView_appointment_where, textView_appointment_time;
        RoundedImageView imageView_appoint_content;


        public NoticeHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            linearLayout_Lease = itemView.findViewById(R.id.linearLayout_Lease);
            textView_time_notice_item = itemView.findViewById(R.id.textView_time_notice_item);
            textView_Lease_title = itemView.findViewById(R.id.textView_Lease_title);
            textView_lease_content = itemView.findViewById(R.id.textView_lease_content);
            textView_lease_money = itemView.findViewById(R.id.textView_lease_money);
            textView_lease_time_start = itemView.findViewById(R.id.textView_lease_time_start);
            textView_lease_time_last = itemView.findViewById(R.id.textView_lease_time_last);
            textView_lease_time_period = itemView.findViewById(R.id.textView_lease_time_period);
            textView_lease_time_end = itemView.findViewById(R.id.textView_lease_time_end);
            imageView_lease_pic = itemView.findViewById(R.id.imageView_lease_pic);
            textView_appointment_title = itemView.findViewById(R.id.textView_appointment_title);
            linearLayout_appointment = itemView.findViewById(R.id.linearLayout_appointment);
            textView_appoint_content = itemView.findViewById(R.id.textView_appoint_content);
            textView_appointment_where = itemView.findViewById(R.id.textView_appointment_where);
            textView_appointment_time = itemView.findViewById(R.id.textView_appointment_time);
            imageView_appoint_content = itemView.findViewById(R.id.imageView_appoint_content);

        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof SystemNoticeBean.OBean.ListBean) {
                //消息内容 "type": "1", //通知类消息分类id 1=签约成功 2=预约看房 3=维修受理 4=维修拒理 "
                SystemNoticeBean.OBean.ListBean listBean = (SystemNoticeBean.OBean.ListBean) mData;
                textView_time_notice_item.setText(listBean.getNe_createtime());

                textView_Lease_title.setText(systemNoticeMap.get(listBean.getType()));
                textView_appointment_title.setText(systemNoticeMap.get(listBean.getType()));
                textView_lease_content.setText(listBean.getNe_content());
                textView_appoint_content.setText(listBean.getNe_content());

                Glide.with(mContext).load(listBean.getNe_url()).error(R.drawable.holder_home_banner).into(imageView_lease_pic);
                Glide.with(mContext).load(listBean.getNe_url()).error(R.drawable.holder_home_banner).into(imageView_appoint_content);
                if (systemNoticeMap.get(listBean.getType()).contains(systemNoticeType[0])) {
                    linearLayout_Lease.setVisibility(View.VISIBLE);
                    linearLayout_appointment.setVisibility(View.GONE);
                    textView_lease_money.setText("RMB    " + listBean.getSign().getSd_surplus_pay() + "/月");
                    textView_lease_time_start.setText("租期开始        " + listBean.getSign().getSd_starttime());
                    textView_lease_time_last.setText("租        期        " + listBean.getSign().getSd_time());
                    textView_lease_time_period.setText("账单周期        " + peroidMap.get(listBean.getSign().getSd_money_type()));
                    textView_lease_time_end.setText("签约截止        " + listBean.getSign().getSd_endtime());
                } else {
                    linearLayout_Lease.setVisibility(View.GONE);
                    linearLayout_appointment.setVisibility(View.VISIBLE);
                }
                if (systemNoticeMap.get(listBean.getType()).contains(systemNoticeType[1])) {
                    textView_appointment_where.setText("预约门店        " + listBean.getReserve().getSt_name());
                    textView_appointment_time.setText("预约时间        " + listBean.getReserve().getRe_time());
                }
                if (systemNoticeMap.get(listBean.getType()).contains(systemNoticeType[2])) {
                    textView_appointment_where.setText("提交时间        " + listBean.getRepair().getRi_createtime());
                    textView_appointment_time.setText("受理时间        " + listBean.getRepair().getRi_handle_time());
                }
                if (systemNoticeMap.get(listBean.getType()).contains(systemNoticeType[3])) {
                    textView_appointment_where.setText("提交时间        " + listBean.getRefuse().getRi_createtime());
                    textView_appointment_time.setText("受理时间        " + listBean.getRefuse().getRi_handle_time());
                }

            }
            if (mData instanceof BillNoticeBean.OBean.ListBean) {
                linearLayout_Lease.setVisibility(View.VISIBLE);
                linearLayout_appointment.setVisibility(View.GONE);
                BillNoticeBean.OBean.ListBean listBean = (BillNoticeBean.OBean.ListBean) mData;
                textView_time_notice_item.setText(listBean.getNe_createtime());
                Glide.with(mContext).load(listBean.getNe_url()).error(R.drawable.holder_home_banner).into(imageView_lease_pic);
                Glide.with(mContext).load(listBean.getNe_url()).error(R.drawable.holder_home_banner).into(imageView_appoint_content);
                textView_lease_content.setText(listBean.getNe_content());
                textView_lease_time_start.setVisibility(View.GONE);
                textView_lease_time_last.setVisibility(View.GONE);
                textView_Lease_title.setText(billMap.get(listBean.getType()));
                if (billMap.get(listBean.getType()).contains(billType[0])) {
                    //设置跳转的位置RentAct
                    listBean.setClassName(RentAct.class.getSimpleName());
                    textView_lease_time_period.setText("账单周期        " + listBean.getPayment().getCycletime());
                    textView_lease_time_end.setText("缴费截止        " + listBean.getPayment().getLatetime());
                    textView_lease_money.setText("RMB  "+listBean.getPayment().getPa_total_money());
                }
                if (billMap.get(listBean.getType()).contains(billType[1])) {
                    //设置跳转的位置  WaterAndElectricityAct
                    listBean.setClassName(WaterAndElectricityAct.class.getSimpleName());
                    textView_lease_time_period.setText("账单周期        " + listBean.getWater().getHy_last_time()+"--"+listBean.getWater().getHy_now_time());
                    textView_lease_time_end.setText("缴费截止        " + listBean.getWater().getLatetime());
                    textView_lease_money.setText("RMB  "+listBean.getWater().getHy_pay_total());
                }
                if (billMap.get(listBean.getType()).contains(billType[2])) {
                    //设置跳转的位置  WaterAndElectricityAct
                    listBean.setClassName(WaterAndElectricityAct.class.getSimpleName());
                    textView_lease_time_period.setText("账单周期        " + listBean.getElectric().getHy_last_time()+"--"+listBean.getElectric().getHy_now_time());
                    textView_lease_time_end.setText("缴费截止        " + listBean.getElectric().getLatetime());
                    textView_lease_money.setText("RMB  "+listBean.getElectric().getHy_pay_total());
                }
            }

            if(mData instanceof DiscountBean.OBean.ListBean){
                linearLayout_Lease.setVisibility(View.GONE);
                linearLayout_appointment.setVisibility(View.VISIBLE);
                DiscountBean.OBean.ListBean listBean= (DiscountBean.OBean.ListBean) mData;
                textView_time_notice_item.setText(listBean.getNe_createtime());
                textView_appointment_title.setText(listBean.getNe_title());
                Glide.with(mContext).load(listBean.getNe_url()).error(R.drawable.holder_home_banner).into(imageView_lease_pic);
                Glide.with(mContext).load(listBean.getNe_url()).error(R.drawable.holder_home_banner).into(imageView_appoint_content);
                textView_appoint_content.setVisibility(View.GONE);
                textView_appointment_where.setText(listBean.getNe_content());
                textView_appointment_time.setVisibility(View.GONE);
            }
        }
    }
}
