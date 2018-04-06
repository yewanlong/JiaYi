package com.kongqw.serialportlibrary.thread;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kongqw on 2017/11/14.
 * 串口消息读取线程
 */

public abstract class SerialPortReadThread extends Thread {

    public abstract void onDataReceived(byte[] bytes);

    private static final String TAG = SerialPortReadThread.class.getSimpleName();
    private InputStream mInputStream;
    private byte[] mReadBuffer;

    public SerialPortReadThread(InputStream inputStream) {
        mInputStream = inputStream;
        mReadBuffer = new byte[20];
    }

    @Override
    public void run() {
        super.run();

        while (!isInterrupted()) {
            try {

                if (null == mInputStream) {
                    return;
                }
                int size = mInputStream.read(mReadBuffer);

                if (size > 0) {
                    byte[] readBytes = new byte[size];
                    Log.i(TAG, "size: " + size);
                    System.arraycopy(mReadBuffer, 0, readBytes, 0, size);
                    onDataReceived(readBytes);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }


    /**
     * 关闭线程 释放资源
     */
    public void release() {
        interrupt();

        if (null != mInputStream) {
            try {
                mInputStream.close();
                mInputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
