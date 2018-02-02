package com.kongqw.serialport.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lkn on 2018/1/31.
 */

public class HomeBean implements Serializable {
    private int status;
    private String reason;
    private List<HomeList> lists;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<HomeList> getLists() {
        return lists;
    }

    public void setLists(List<HomeList> lists) {
        this.lists = lists;
    }
}
