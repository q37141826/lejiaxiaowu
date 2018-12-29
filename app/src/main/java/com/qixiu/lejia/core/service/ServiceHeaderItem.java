package com.qixiu.lejia.core.service;

import android.databinding.ObservableField;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/4/23
 * <pre>
 *     生活服务头部
 * </pre>
 */
public class ServiceHeaderItem extends BindableItem {

    private ObservableField<String> bannerField = new ObservableField<>();

    public static ServiceHeaderItem newInstance() {
        return new ServiceHeaderItem();
    }

    @Override
    public int getViewType() {
        return R.layout.item_service_header;
    }

    public String getBanner() {
        return bannerField.get();
    }

    public void setBanner(String newBanner) {
        bannerField.set(newBanner);
    }

}
