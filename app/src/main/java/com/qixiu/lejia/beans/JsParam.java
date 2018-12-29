package com.qixiu.lejia.beans;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Long on 2018/5/7
 */
public class JsParam {

    private String title;

    private String link;

    private JsParam(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public static JsParam of(String json) {
        try {
            JSONObject object = new JSONObject(json);
            String title = object.getString("subTitle");
            String link = object.getString("subUrl");
            return new JsParam(title, link.replace("/", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String toString() {
        return "JsParam{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
