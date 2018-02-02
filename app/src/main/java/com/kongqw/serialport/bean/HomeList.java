package com.kongqw.serialport.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lkn on 2018/2/2.
 */

public class HomeList implements Serializable {
    private String p_type;
    private int cnt;
    private List<HomeListData> p_list;

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<HomeListData> getP_list() {
        return p_list;
    }

    public void setP_list(List<HomeListData> p_list) {
        this.p_list = p_list;
    }
}
