package com.huahao.serialport.bean;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/2/9.
 */

public class EventMp4 implements Serializable {
    private String path;

    public EventMp4(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
