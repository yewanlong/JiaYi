package com.huahao.serialport.bean;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/2/2.
 */

public class HomeOrder implements Serializable {
    private int status;
    private String reason;
    private Order result;

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

    public Order getResult() {
        return result;
    }

    public void setResult(Order result) {
        this.result = result;
    }
}
