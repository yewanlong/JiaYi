package com.xuhao.android.libsocket.sdk.connection.abilities;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IDisConnectable {
    void disConnect(Exception e);

    void disConnect();
}
