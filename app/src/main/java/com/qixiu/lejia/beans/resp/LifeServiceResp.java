package com.qixiu.lejia.beans.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/4/27
 */
public class LifeServiceResp {

    @SerializedName("banner")
    private TopBanner topBanner;

    @SerializedName("recommend")
    private List<RecommendService>recommendServices;

    public static class TopBanner {
        @SerializedName("lf_url")
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class RecommendService {

        @SerializedName("rd_id")
        private String id;
        @SerializedName("rd_title")
        private String title;
        @SerializedName("rd_url")
        private String image;
        @SerializedName("rd_link")
        private String link;

        @SerializedName("rd_type")
        private int type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public TopBanner getTopBanner() {
        return topBanner;
    }

    public void setTopBanner(TopBanner topBanner) {
        this.topBanner = topBanner;
    }

    public List<RecommendService> getRecommendServices() {
        return recommendServices;
    }

    public void setRecommendServices(List<RecommendService> recommendServices) {
        this.recommendServices = recommendServices;
    }

}
