package com.huahao.serialportlibrary.listener;

/**
 * Created by Kongqw on 2017/11/14.
 * 串口消息监听
 */

public interface OnSerialPortDataListener2 {

    /**
     * 数据接收
     *
     * @param bytes 接收到的数据
     */
    void onDataReceived(byte[] bytes,int id,String saleId);

    /**
     * 数据发送
     *
     * @param bytes 发送的数据
     */
    void onDataSent(byte[] bytes, int what);
}
