package com.kongqw.serialport.bean;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/2/5.
 */

public class OrderData implements Serializable {
    private int status;
    private String reason;
    private String qr_code;

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

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
}
