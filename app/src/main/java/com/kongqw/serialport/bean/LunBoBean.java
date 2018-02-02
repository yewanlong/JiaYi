package com.kongqw.serialport.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lkn on 2018/2/2.
 */

public class LunBoBean implements Serializable {
    private int status;
    private String reason;
    private List<LunBoList> lunbo;

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

    public List<LunBoList> getLunbo() {
        return lunbo;
    }

    public void setLunbo(List<LunBoList> lunbo) {
        this.lunbo = lunbo;
    }
}
