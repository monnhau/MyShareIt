package com.shareit.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class MenuEntity implements Serializable{
    private int id;
    private String name;
    private boolean selected;
    private boolean isParent;

    public MenuEntity(){}

    public MenuEntity(int catId, String name, boolean selected) {
        this.id = catId;
        this.name = name;
        this.selected = selected;
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
        return "MenuEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", selected=" + selected +
                ", isParent=" + isParent +
                '}';
    }
}
