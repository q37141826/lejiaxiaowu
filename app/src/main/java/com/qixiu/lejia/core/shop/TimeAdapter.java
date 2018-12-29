package com.qixiu.lejia.core.shop;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.lejia.R;
import com.qixiu.lejia.databinding.ItemRecommendRoomBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:武汉企秀网络科技有限公司 Copyright(c)20XX
 * author:xuchi
 * date: 2018/6/20 0020
 * version 1.0
 */
public class TimeAdapter extends RecyclerView.Adapter {
    private Context                 context;
    private List<RecommendRoomItem> items;

//    private LinearLayout linearLayout;
//    // 倒计时
//    private TextView     daysTv, hoursTv, minutesTv;

    private boolean isRun = true;

    public TimeAdapter(Context context, List<RecommendRoomItem> list) {
        this.context = context;
        this.items = list;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRecommendRoomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.item_recommend_room, parent, false);
        return new UserViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemRecommendRoomBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setItem(this.items.get(position));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = stampToDate(items.get(position).getRoom().getStartTime() * 1000);
        String endTime = stampToDate(items.get(position).getRoom().getEndTime() * 1000);
        String nowtime = stampToDate(System.currentTimeMillis());
//        long mDay = 1;
//        final long mHour = 1;
//        final long mMin = 1;
//        final long mSecond = 0;// 天 ,小时,分钟,秒
        try {
            Date startTimeData = format.parse(startTime);
            Date endTimeData = format.parse(endTime);
            Date nowtimeData = format.parse(nowtime);
            if (nowtimeData.getTime() > startTimeData.getTime() && nowtimeData.getTime() < endTimeData.getTime()) {
                Long diff = endTimeData.getTime() - nowtimeData.getTime();
                long mDay = diff / (1000 * 60 * 60 * 24);   //以天数为单位取整
                long mHour = (diff / (60 * 60 * 1000) - mDay * 24);    //以小时为单位取整
                long mMin = ((diff / (60 * 1000)) - mDay * 24 * 60 - mHour * 60); //以分钟为单位取整
                long mSecond = (diff / 1000 - mDay * 24 * 60 * 60 - mHour * 60 * 60 - mMin * 60);//秒
                startRun(new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            computeTime(mSecond, mMin, mHour, mDay);
                            binding.daysTv.setText(mDay + "");
                            binding.hoursTv.setText(mHour + "");
                            binding.minutesTv.setText(mMin + "");
//                secondsTv.setText(mSecond + "");
                            if (mDay == 0 && mHour == 0 && mMin == 0 && mSecond == 0) {
                                binding.linearLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                });
                if (items.get(position).getRoom().getDiscount() > 0) {
                    binding.linearLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.linearLayout.setVisibility(View.GONE);
                }
            } else {
                binding.linearLayout.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.setActionHandler(o -> {
            //房间详情
            RecommendRoomItem item = (RecommendRoomItem) o;
            RoomDetailAct.start(context, item.getRoom().getId());
        });
        binding.executePendingBindings();
//
//        Handler timeHandler = new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == 1) {
//                    computeTime();
//                    binding.daysTv.setText(mDay + "");
//                    binding.hoursTv.setText(mHour + "");
//                    binding.minutesTv.setText(mMin + "");
////                secondsTv.setText(mSecond + "");
//                    if (mDay == 0 && mHour == 0 && mMin == 0 && mSecond == 0) {
//                        binding.linearLayout.setVisibility(View.GONE);
//                    }
//                }
//            }
//        };

    }


    /**
     * 将时间戳转换为时间
     */
    public String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(View itemView) {
            super(itemView);
        }
    }

//    private Handler timeHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                computeTime();
//                daysTv.setText(mDay + "");
//                hoursTv.setText(mHour + "");
//                minutesTv.setText(mMin + "");
////                secondsTv.setText(mSecond + "");
//                if (mDay == 0 && mHour == 0 && mMin == 0 && mSecond == 0) {
//                    linearLayout.setVisibility(View.GONE);
//                }
//            }
//        }
//    };

    /**
     * 开启倒计时
     */
    private void startRun(Handler timeHandler) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime(long mSecond, long mMin, long mHour, long mDay) {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                }
            }
        }
    }

}
