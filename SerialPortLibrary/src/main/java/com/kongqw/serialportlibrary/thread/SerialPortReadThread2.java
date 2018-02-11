package com.kongqw.serialportlibrary.thread;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kongqw on 2017/11/14.
 * 串口消息读取线程
 */

public abstract class SerialPortReadThread2 extends Thread {

    public abstract void onDataReceived(byte[] bytes, int what,String saleId);

    private static final String TAG = SerialPortReadThread2.class.getSimpleName();
    private InputStream mInputStream;
    private byte[] mReadBuffer;
    private int what;
    private String saleId;
    public SerialPortReadThread2(InputStream inputStream) {
        mInputStream = inputStream;

        mReadBuffer = new byte[1024];
    }
    public void setWhat(int what,String saleId){
        this.what = what;
        this.saleId = saleId;
    }
    @Override
    public void run() {
        super.run();

        while (!isInterrupted()) {
            try {
                if (null == mInputStream) {
                    return;
                }

                Log.i(TAG, "run: ");
                int size = mInputStream.read(mReadBuffer);

                if (-1 == size || 0 >= size) {
                    return;
                }

                byte[] readBytes = new byte[size];

                System.arraycopy(mReadBuffer, 0, readBytes, 0, size);
                onDataReceived(readBytes, what,saleId);

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
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
