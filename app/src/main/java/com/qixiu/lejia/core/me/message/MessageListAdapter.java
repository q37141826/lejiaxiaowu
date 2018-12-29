package com.qixiu.lejia.core.me.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.mine.MessageListBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by my on 2018/7/19.
 */

public class MessageListAdapter extends RecyclerBaseAdapter {


    @Override
    public int getLayoutId() {
        return R.layout.item_message;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {

        return new MessageHolder(itemView, context, this);
    }

    public class MessageHolder extends RecyclerBaseHolder {
        //        ImageView imageView_item_message;
        ImageView imageView_item_message;
        TextView textView_time_message,
                text_item_message_content, textView_title_message, textView_unread_notice;
        private String key[]={"账单提醒","活动优惠","通知提醒"};
        private int image[]={R.mipmap.message_bill2x,R.mipmap.message_gift2x,R.mipmap.message_system2x};

        public MessageHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
//            imageView_item_message=itemView.findViewById(R.id.imageView_item_message);
            imageView_item_message = itemView.findViewById(R.id.imageView_item_message);
            textView_time_message = itemView.findViewById(R.id.textView_time_message);
            text_item_message_content = itemView.findViewById(R.id.text_item_message_content);
            textView_title_message = itemView.findViewById(R.id.textView_title_message);
            textView_unread_notice = itemView.findViewById(R.id.textView_unread_notice);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof MessageListBean.OBean) {
                Map<String,Integer> mapImage=new HashMap<>();
                for (int i = 0; i <image.length ; i++) {
                    mapImage.put(key[i],image[i]);
                }
                MessageListBean.OBean bean = (MessageListBean.OBean) mData;
//                Glide.with(mContext).load(bean.getSt_house_img()).into(imageView_item_message);
                textView_time_message.setText(bean.getNe_createtime());
                text_item_message_content.setText(bean.getNe_content());
                textView_title_message.setText(bean.getNc_name());
                textView_unread_notice.setText(bean.getNum() + "");
                imageView_item_message.setImageResource(mapImage.get(bean.getNc_name()));
                if (bean.getNum() == 0) {
                    textView_unread_notice.setVisibility(View.GONE);
                } else {
                    textView_unread_notice.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
