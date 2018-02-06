package com.kongqw.serialport.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.kongqw.serialport.HttpUtils;
import com.kongqw.serialport.NoneReconnect;
import com.kongqw.serialport.R;
import com.kongqw.serialport.SelectSerialPortActivity;
import com.kongqw.serialport.SerialPortActivity;
import com.kongqw.serialport.bean.HandShake;
import com.kongqw.serialport.bean.MsgDataBean;
import com.kongqw.serialport.gpio.GpioActivity1;
import com.kongqw.serialport.gpio.GpioActivity2;
import com.kongqw.serialport.utils.CommonUtils;
import com.kongqw.serialport.utils.VToast;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.SerialPortManager2;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener2;
import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.IPulseSendable;
import com.xuhao.android.libsocket.sdk.bean.ISendable;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static com.xuhao.android.libsocket.sdk.OkSocket.open;

/**
 * Created by Lkn on 2018/1/29.
 */

public class MainActivity extends YBaseActivity implements View.OnClickListener, OnOpenSerialPortListener {
    private HomeFragment homeFragment;
    private SerialPortManager2 mSerialPortManager;
    private Device device;
    private IConnectionManager mManager;
    private ConnectionInfo mInfo;
    private OkSocketOptions mOkOptions;
    private Handler handler = new Handler();
    private Runnable mRunnable, mRunnableSub, mRunnableCSQ;
    private long msgId = 0;
    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
            socketSend(HttpUtils.getCheckIn(0, HttpUtils.IMEI));
            handler.postDelayed(mRunnableCSQ, 30000);
        }

        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
            if (e != null) {
                Log.i("ywl", "异常断开:" + e.getMessage());
                handler.postDelayed(mRunnable, 20000);
            } else {
                Log.i("ywl", "正常断开:" + e.getMessage());
            }
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            VToast.showLong("TCP连接失败");
            Log.i("ywl", "连接失败:" + e.getMessage());
            handler.postDelayed(mRunnable, 20000);
        }

        //接收
        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);

            String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
            Log.i("ywl", "onSocketReadResponse:" + str);
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
            String str = new String(data.parse(), Charset.forName("utf-8"));
            Log.i("ywl", "onSocketWriteResponse:" + str);
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
            String str = new String(data.parse(), Charset.forName("utf-8"));
            Log.i("ywl", "onPulseSend:" + str);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        device = (Device) getIntent().getSerializableExtra(SerialPortActivity.DEVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏的flag
        homeFragment = new HomeFragment();
        mSerialPortManager = new SerialPortManager2();
        mInfo = new ConnectionInfo(HttpUtils.TCP_IP, HttpUtils.TCP_PRO_IP);
        mOkOptions = new OkSocketOptions.Builder(OkSocketOptions.getDefault())
                .setReconnectionManager(new NoneReconnect())
                .setSinglePackageBytes(100)
                .build();
        mManager = open(mInfo, mOkOptions);
        mManager.registerReceiver(adapter);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mManager.connect();
            }
        };
        mRunnableSub = new Runnable() {
            @Override
            public void run() {
                homeFragment.subtractList();
            }
        };
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mManager.connect();
            }
        };
        mRunnableCSQ = new Runnable() {
            @Override
            public void run() {
                socketSend(HttpUtils.getCSQ(msgId, HttpUtils.IMEI));
                msgId++;
                handler.postDelayed(mRunnableCSQ, 30000);
            }
        };
        mManager.connect();
        Log.i("ywl", "size:" + mOkOptions.getSendSinglePackageBytes());
    }

    @Override
    protected void initData() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .show(homeFragment).commit();
    }

    @Override
    protected void initListener() {
        if (device != null) {
            mSerialPortManager.setOnOpenSerialPortListener(this)
                    .setOnSerialPortDataListener(new OnSerialPortDataListener2() {
                        @Override
                        public void onDataReceived(final byte[] bytes) {
                            Log.i("ywl", "接收到数据");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switchReceived(bytes);
                                }
                            });
                        }

                        @Override
                        public void onDataSent(final byte[] bytes, final int what) {
                        }
                    }).openSerialPort(new File("/dev/ttyS1"), 9600);
        }
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    private void switchReceived(byte[] bytes) {
        switch (bytes[1]) {
            case HttpUtils.SERIAL_TYPE: {
                String str = Tool.bytesToHexString(bytes);
                str = str.substring(14, 28);
                byte[] send_start = new byte[7];
                int start = 0;
                int end = 2;
                for (int i = 0; i < send_start.length; i++) {
                    send_start[i] = (byte) Integer.parseInt(str.substring(start, end), 16);
                    start = start + 2;
                    end = end + 2;
                }
                str = new String(send_start, Charset.forName("utf-8"));
                HttpUtils.IMEI = str;
                socketSend(HttpUtils.getCheckIn(0, HttpUtils.IMEI));
                handler.postDelayed(mRunnableCSQ, 300000);
            }
            break;
            case HttpUtils.SERIAL_TYPE_5: {
                String str = Tool.bytesToHexString(bytes);
                if (str.substring(4, 6).equals("00")) {
                    VToast.showLong("出货成功");
                } else {
                    VToast.showLong("出货失败");
                }
            }
            break;
        }

    }

    private void send05() {
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_5);
        map.put(Tool.MOTOR_NUMBER, 3);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_5);
    }

    private void socketSend(String tcpMap) {
        if (!mManager.isConnect()) {
            mManager.connect();
        } else {
            mManager.send(new HandShake(tcpMap));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    /**
     * 串口打开成后获取序列号
     *
     * @param device
     */
    @Override
    public void onSuccess(File device) {
        VToast.showLong("串口打开成功");
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_1);
    }

    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                VToast.showLong("没有读写权限");
                break;
            case OPEN_FAIL:
            default:
                //TODO 打开失败后向服务器发送信息
                VToast.showLong("串口打开失败");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (device != null) {
            mSerialPortManager.closeSerialPort();
            mManager.disConnect();
        }
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                handler.removeCallbacks(mRunnableSub);
            }
            case MotionEvent.ACTION_UP: {
                handler.postDelayed(mRunnableSub, 120000);
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
