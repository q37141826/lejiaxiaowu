package com.qixiu.lejia.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2018/5/8
 */
public class ImagesPreview {

    private List<String> images;

    private int startPos;

    public static ImagesPreview of(String json) {
        ImagesPreview o = new ImagesPreview();
        List<String> images = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray imgs = object.getJSONArray("imgs");
            for (int i = 0; i < imgs.length(); i++) {
                String link = imgs.getString(i);
                images.add(link);
            }
            o.setImages(images);
            o.setStartPos(object.getInt("index"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return o;
    }


    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }
}
