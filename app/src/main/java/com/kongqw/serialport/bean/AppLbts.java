package com.kongqw.serialport.bean;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/1/29.
 */

public class AppLbts implements Serializable {
    private String image;
    private String link;

    public void setImage(String image) {
        this.image = image;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {

        return image;
    }

    public String getLink() {
        return link;
    }
}
