package com.example.trading_android.model;

public class PostSort {

    private int id;
    private String sortName;
    private String iconPath;
    private String parentSort;
    private String isShow;


    @Override
    public String toString() {
        return "PostSort{" +
                "id=" + id +
                ", sortName='" + sortName + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", parentSort='" + parentSort + '\'' +
                ", isShow='" + isShow + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getParentSort() {
        return parentSort;
    }

    public void setParentSort(String parentSort) {
        this.parentSort = parentSort;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
