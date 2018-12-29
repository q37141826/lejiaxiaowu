package com.qixiu.lejia.beans.resp;

import com.google.gson.annotations.SerializedName;
import com.qixiu.lejia.beans.Banner;
import com.qixiu.lejia.beans.Event;
import com.qixiu.lejia.beans.Guarantee;
import com.qixiu.lejia.beans.Shop;
import com.qixiu.lejia.beans.home.story.StoryBean;

import java.util.List;

/**
 * Created by Long on 2018/4/25
 * <pre>
 *     首页数据响应
 * </pre>
 */
public class HomeResp {

    @SerializedName("home_img")
    private BannerResp bannerResp;

    @SerializedName("storelist")
    private List<Shop> shops;

    public List<StoryBean.ListBean> getStoryBeans() {
        return storyBeans;
    }

    public void setStoryBeans(List<StoryBean.ListBean> storyBeans) {
        this.storyBeans = storyBeans;
    }

    private List<StoryBean.ListBean> storyBeans;

    @SerializedName("activity")
    private List<Event> events;

    @SerializedName("serverinfo")
    private List<Guarantee> guarantees;

    public BannerResp getBannerResp() {
        return bannerResp;
    }

    public void setBannerResp(BannerResp bannerResp) {
        this.bannerResp = bannerResp;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Guarantee> getGuarantees() {
        return guarantees;
    }

    public void setGuarantees(List<Guarantee> guarantees) {
        this.guarantees = guarantees;
    }

    public static class BannerResp {

        @SerializedName("top")
        private List<Banner> topBanner;

        @SerializedName("carousel")
        private List<Banner> middleBanners;

        public List<Banner> getTopBanner() {
            return topBanner;
        }

        public void setTopBanner(List<Banner> topBanner) {
            this.topBanner = topBanner;
        }

        public List<Banner> getMiddleBanners() {
            return middleBanners;
        }

        public void setMiddleBanners(List<Banner> middleBanners) {
            this.middleBanners = middleBanners;
        }
    }

}
