package com.qixiu.lejia.core.home;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.beans.Banner;
import com.qixiu.lejia.beans.Guarantee;

import java.util.List;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     品质保障条目
 * </pre>
 */
public class HomeGuaranteeItem extends BindableItem {

    private List<Banner>    banners;
    private List<Guarantee> guarantees;

    private HomeGuaranteeItem(List<Banner> banners, List<Guarantee> guarantees) {
        this.banners = banners;
        this.guarantees = guarantees;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public List<Guarantee> getGuarantees() {
        return guarantees;
    }

    @Override
    public int getViewType() {
        return R.layout.item_home_guarantee;
    }

    public static HomeGuaranteeItem newInstance(List<Banner> banners, List<Guarantee> guarantees) {
        return new HomeGuaranteeItem(banners, guarantees);
    }

}
