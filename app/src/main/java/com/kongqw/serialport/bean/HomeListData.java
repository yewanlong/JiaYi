package com.kongqw.serialport.bean;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/2/2.
 */

public class HomeListData implements Serializable {
    private double ori_price;
    private String subtitle;
    private String title;
    private String pid;
    private String images;
    private double price;
    private int num;
    private int left_num;
    private int titlePosition;
    private int contentPosition;

    public int getTitlePosition() {
        return titlePosition;
    }

    public void setTitlePosition(int titlePosition) {
        this.titlePosition = titlePosition;
    }

    public int getContentPosition() {
        return contentPosition;
    }

    public void setContentPosition(int contentPosition) {
        this.contentPosition = contentPosition;
    }

    public int getLeft_num() {
        return left_num;
    }

    public void setLeft_num(int left_num) {
        this.left_num = left_num;
    }

    public double getOri_price() {
        return ori_price;
    }

    public void setOri_price(double ori_price) {
        this.ori_price = ori_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
