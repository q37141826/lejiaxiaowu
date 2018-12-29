package com.qixiu.lejia.beans.home.story;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class StoryBean {


    /**
     * list : [{"id":"3","url":"http://lj.lejiaxiaowu.com/public/images/2018071810493260421781.png","title":"陌生城市里的我","read":"陌生城市里的我很寂寞"},{"id":"2","url":"http://lj.lejiaxiaowu.com/public/images/2018053016211244031407.png","title":"加快速度","read":"个发广告"},{"id":"1","url":"http://lj.lejiaxiaowu.com/public/images/2018053010425292888502.jpg","title":"加快速度","read":""}]
     * page : 1
     */

    public int page;
    /**
     * id : 3
     * url : http://lj.lejiaxiaowu.com/public/images/2018071810493260421781.png
     * title : 陌生城市里的我
     * read : 陌生城市里的我很寂寞
     */

    public List<ListBean> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        public String id;
        public String url;
        public String title;
        public String read;
        public String link;
        public String content;

        public String getLink() {
            return link;
        }

        public String getContent() {
            return content;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRead() {
            return read;
        }

        public void setRead(String read) {
            this.read = read;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.url);
            dest.writeString(this.title);
            dest.writeString(this.read);
            dest.writeString(this.link);
            dest.writeString(this.content);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.id = in.readString();
            this.url = in.readString();
            this.title = in.readString();
            this.read = in.readString();
            this.link = in.readString();
            this.content = in.readString();
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }
}
