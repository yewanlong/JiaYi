package com.huahao.serialport.bean;

import java.io.Serializable;

/**
 * Created by Lkn on 2018/2/5.
 */

public class Order implements Serializable {
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
