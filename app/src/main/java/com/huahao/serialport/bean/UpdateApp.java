package com.huahao.serialport.bean;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/2/5.
 */

public class UpdateApp implements Serializable {
    private int status;
    private String reason;
    private String apk_url;

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

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }
}
