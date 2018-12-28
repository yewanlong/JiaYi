package com.huahao.serialport.bean;

import java.io.Serializable;
import java.util.List;

public class Ads implements Serializable {
    private int status;
    private List<Res> res;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Res> getRes() {
        return res;
    }

    public void setRes(List<Res> res) {
        this.res = res;
    }

    public static class Res {
        private String images;
        private String atype;
        private String title;

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getAtype() {
            return atype;
        }

        public void setAtype(String atype) {
            this.atype = atype;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
