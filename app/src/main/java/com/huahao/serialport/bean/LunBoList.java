package com.huahao.serialport.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/2/2.
 */

public class LunBoList implements Serializable {
    private String content;
    private String images;
    private int id;
    private int rank;
    private String title;

    public String getContent() {
        return content;
    }

    public String getImages() {
        if (TextUtils.isEmpty(images)) {
            images = "http://g.hiphotos.baidu.com/image/pic/item/10dfa9ec8a1363275cd315d09a8fa0ec08fac713.jpg";
        }
        return images;
    }

    public int getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
