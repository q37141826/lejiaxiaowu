package com.qixiu.lejia.items;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.PayDetails;

/**
 * Created by Long on 2018/4/28 0028
 */
public class PayDetailsCaptionItem extends BindableItem {

    private String title;
    private String money;

    private PayDetailsCaptionItem(String title, String money) {
        this.title = title;
        this.money = money;
    }

    public static PayDetailsCaptionItem of(PayDetails details) {
        return new PayDetailsCaptionItem(details.getTitle(), details.getMoney());
    }


    @Override
    public int getViewType() {
        return R.layout.item_pay_details_caption;
    }

    public String getTitle() {
        return title;
    }

    public String getMoney() {
        return money;
    }
}
