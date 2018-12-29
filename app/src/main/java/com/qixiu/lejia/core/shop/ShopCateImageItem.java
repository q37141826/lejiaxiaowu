package com.qixiu.lejia.core.shop;

import android.databinding.ObservableBoolean;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.ShopDetail;

/**
 * Created by Long on 2018/5/31
 */
public class ShopCateImageItem extends BindableItem {

    public ObservableBoolean checked = new ObservableBoolean();
    private ShopDetail.ShopImages images;

    ShopCateImageItem(ShopDetail.ShopImages images) {
        this.images = images;
    }

    public ShopDetail.ShopImages getImages() {
        return images;
    }

    public void setChecked(boolean checked){
        this.checked.set(checked);
    }

    @Override
    public int getViewType() {
        return R.layout.item_shop_cate_image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopCateImageItem item = (ShopCateImageItem) o;

        return images != null ? images.equals(item.images) : item.images == null;
    }

    @Override
    public int hashCode() {
        return images != null ? images.hashCode() : 0;
    }
}
