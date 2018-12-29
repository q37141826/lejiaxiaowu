package com.qixiu.lejia.beans.resp;

import com.qixiu.lejia.beans.ComplaintTag;
import com.qixiu.lejia.beans.Shop;

import java.util.List;

/**
 * Created by Long on 2018/6/8
 */
public class ComplaintResp {

    private List<ComplaintTag> tags;

    private List<Shop> shops;

    public ComplaintResp(List<ComplaintTag> tags, List<Shop> shops) {
        this.tags = tags;
        this.shops = shops;
    }

    public List<ComplaintTag> getTags() {
        return tags;
    }

    public List<Shop> getShops() {
        return shops;
    }
}
