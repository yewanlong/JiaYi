package com.huahao.serialport.utils;

/**
 * Created by blacKey on 2016/12/12.
 */
public interface UpdateProgressListener {
    /**
     * download start
     */
    public void start();

    /**
     * update download progress
     * @param progress
     */
    public void update(int progress);

    /**
     * download success
     */
    public void success();

    /**
     * download error
     */
    public void error();
}
