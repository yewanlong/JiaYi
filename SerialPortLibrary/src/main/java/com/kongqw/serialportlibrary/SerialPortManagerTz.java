package com.kongqw.serialportlibrary;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.kongqw.serialportlibrary.thread.SerialPortReadThread;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Kongqw on 2017/11/13.
 * SerialPortManager
 */

public class SerialPortManagerTz extends SerialPort {

    private static final String TAG = "ywl";
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private FileDescriptor mFd;
    private OnOpenSerialPortListener mOnOpenSerialPortListener;
    private OnSerialPortDataListener mOnSerialPortDataListener;

    private HandlerThread mSendingHandlerThread;
    private Handler mSendingHandler;
    private SerialPortReadThread mSerialPortReadThread;

    /**
     * 打开串口
     *
     * @param device   串口设备
     * @param baudRate 波特率
     * @return 打开是否成功
     */
    public boolean openSerialPort(File device, int baudRate) {

        Log.i(TAG, "openSerialPort: " + String.format("打开串口 %s  波特率 %s", device.getPath(), baudRate));

        // 校验串口权限
        if (!device.canRead() || !device.canWrite()) {
            boolean chmod777 = chmod777(device);
            if (!chmod777) {
                Log.i(TAG, "openSerialPort: 没有读写权限");
                if (null != mOnOpenSerialPortListener) {
                    mOnOpenSerialPortListener.onFail(device, OnOpenSerialPortListener.Status.NO_READ_WRITE_PERMISSION);
                }
                return false;
            }
        }

        try {
            mFd = open(device.getAbsolutePath(), baudRate, 0);
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
            Log.i(TAG, "openSerialPort: 串口已经打开 " + mFd);
            if (null != mOnOpenSerialPortListener) {
                mOnOpenSerialPortListener.onSuccess(device);
            }
            // 开启发送消息的线程
            startSendThread();
            // 开启接收消息的线程
            startReadThread();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (null != mOnOpenSerialPortListener) {
                mOnOpenSerialPortListener.onFail(device, OnOpenSerialPortListener.Status.OPEN_FAIL);
            }
        }
        return false;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {

        if (null != mFd) {
            close();
            mFd = null;
        }
        // 停止发送消息的线程
        stopSendThread();
        // 停止接收消息的线程
        stopReadThread();

        if (null != mFileInputStream) {
            try {
                mFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFileInputStream = null;
        }

        if (null != mFileOutputStream) {
            try {
                mFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFileOutputStream = null;
        }

        mOnOpenSerialPortListener = null;

        mOnSerialPortDataListener = null;
    }

    /**
     * 添加打开串口监听
     *
     * @param listener listener
     * @return SerialPortManager
     */
    public SerialPortManagerTz setOnOpenSerialPortListener(OnOpenSerialPortListener listener) {
        mOnOpenSerialPortListener = listener;
        return this;
    }

    /**
     * 添加数据通信监听
     *
     * @param listener listener
     * @return SerialPortManager
     */
    public SerialPortManagerTz setOnSerialPortDataListener(OnSerialPortDataListener listener) {
        mOnSerialPortDataListener = listener;
        return this;
    }


    /**
     * 开启发送消息的线程
     */
    private void startSendThread() {
        // 开启发送消息的线程
        mSendingHandlerThread = new HandlerThread("mSendingHandlerThread");
        mSendingHandlerThread.start();
        // Handler
        mSendingHandler = new Handler(mSendingHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                byte[] m_zhiling = ((byte[]) msg.obj);
                if (null != mFileOutputStream && 0 < m_zhiling.length) {
                    try {
                        mFileOutputStream.write(m_zhiling);
                        if (null != mOnSerialPortDataListener) {
                            Log.d("sum_command_start", "m_zhiling=" + Tool.bytesToHexString(m_zhiling));
                            mOnSerialPortDataListener.onDataSent(m_zhiling);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    //称重命令
    public byte[] select() {
        byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x04, (byte) 0x00, (byte) 0x50, (byte) 0x55, (byte) 0x0D, (byte) 0x0A};
        return bytes;
    }

    //校验第一个传感器
    public byte[] select1() {
        byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x04, (byte) 0x01, (byte) 0x00, (byte) 0x06, (byte) 0x0D, (byte) 0x0A};
        return bytes;
    }

    //校验第二个传感器
    public byte[] select2() {
        byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x04, (byte) 0x02, (byte) 0x00, (byte) 0x07, (byte) 0x0D, (byte) 0x0A};
        return bytes;
    }

    //校验第三个传感器
    public byte[] select3() {
        byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x04, (byte) 0x03, (byte) 0x00, (byte) 0x08, (byte) 0x0D, (byte) 0x0A};
        return bytes;
    }

    //校验第四个传感器
    public byte[] select4() {
        byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x04, (byte) 0x04, (byte) 0x00, (byte) 0x09, (byte) 0x0D, (byte) 0x0A};
        return bytes;
    }

    //去皮操作
    public byte[] select5() {
        byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x04, (byte) 0x01, (byte) 0x04, (byte) 0xA0, (byte) 0x0D, (byte) 0x0A};
        return bytes;
    }

    /**
     * 停止发送消息线程
     */
    private void stopSendThread() {
        mSendingHandler = null;
        if (null != mSendingHandlerThread) {
            mSendingHandlerThread.interrupt();
            mSendingHandlerThread.quit();
            mSendingHandlerThread = null;
        }
    }

    /**
     * 开启接收消息的线程
     */
    private void startReadThread() {
        mSerialPortReadThread = new SerialPortReadThread(mFileInputStream) {
            @Override
            public void onDataReceived(byte[] bytes) {
                if (null != mOnSerialPortDataListener) {
                    mOnSerialPortDataListener.onDataReceived(bytes);
                }
            }
        };
        mSerialPortReadThread.start();
    }

    /**
     * 停止接收消息的线程
     */
    private void stopReadThread() {
        if (null != mSerialPortReadThread) {
            mSerialPortReadThread.release();
        }
    }

    /**
     * 发送数据
     *
     * @param sendBytes 发送数据
     * @return 发送是否成功
     */
    public boolean sendBytes(byte[] sendBytes) {
        if (null != mFd && null != mFileInputStream && null != mFileOutputStream) {
            if (null != mSendingHandler) {
                Message message = Message.obtain();
                message.obj = sendBytes;
                return mSendingHandler.sendMessage(message);
            }
        }
        return false;
    }
}
