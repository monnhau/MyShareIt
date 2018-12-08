package com.shareit.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class PostEntity implements Serializable {
    private int id;
    private String title;
    private String desc;
    private String thumb;
    private String content;
    private String link;
    private String text;

    public PostEntity(){}

    public PostEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("post_id");
        this.title = jsonObject.optString("title");
        this.desc = jsonObject.optString("desc");
        this.thumb = jsonObject.optString("thumb");
        this.content = jsonObject.optString("detail");
        this.link = jsonObject.optString("link");
    }

    public PostEntity(JSONObject jsonObject, boolean isShareit) {
        this.id = jsonObject.optInt("tintuc_id");
        this.title = jsonObject.optString("tentintuc");
        this.desc = jsonObject.optString("gioithieu");
        this.thumb = jsonObject.optString("hinhanh");
        this.content = jsonObject.optString("chitiet");
        this.text = jsonObject.optString("text");
    }

    public String getLink() {
        return link;
    }

    public String getText() {
        return text;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
