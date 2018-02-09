package com.huahao.serialport.bean;

import com.xuhao.android.libsocket.sdk.bean.ISendable;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by xuhao on 2017/5/22.
 */

public class HandShake implements ISendable {
    private String content = "";

    public HandShake(String content) {
        this.content = content;
    }

    public HandShake(Map<String, Object> content) {
        this.content = content.toString();
    }

    @Override
    public byte[] parse() {
        byte[] body = content.getBytes(Charset.defaultCharset());
//        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
//        bb.order(ByteOrder.BIG_ENDIAN);
//        bb.putInt(body.length);
//        bb.put(body);
        return body;
    }
}
