package com.qixiu.lejia.core.service.qa;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.resp.QuestionResp;

/**
 * Created by Long on 2018/6/7
 */
public class ShopContactItem extends BindableItem {

    private QuestionResp.ShopContact contact;

    ShopContactItem(QuestionResp.ShopContact contact) {
        this.contact = contact;
    }

    public QuestionResp.ShopContact getContact() {
        return contact;
    }

    @Override
    public int getViewType() {
        return R.layout.item_shop_contact;
    }
}
