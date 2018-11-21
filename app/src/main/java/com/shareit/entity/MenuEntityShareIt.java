package com.shareit.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class MenuEntityShareIt implements Serializable{
    private int id;
    private String name;
    private boolean selected;
    private int parentId;

    public MenuEntityShareIt(){}

    public MenuEntityShareIt(JSONObject jsonObject, boolean selected) {
        this.id = jsonObject.optInt("danhmuc_id");
        this.name = jsonObject.optString("tendanhmuc");
        this.selected = selected;
        this.parentId = jsonObject.optInt("parent_id");
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "MenuEntityShareIt{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", selected=" + selected +
                ", parentId=" + parentId +
                '}';
    }
}
