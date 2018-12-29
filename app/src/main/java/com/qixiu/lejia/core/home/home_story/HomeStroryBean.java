package com.qixiu.lejia.core.home.home_story;

import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;

/**
 * Created by my on 2018/7/20.
 */

public class HomeStroryBean extends BindableItem {
    private String title;
    private String imag;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getViewType() {
         return R.layout.item_story;
    }
}
