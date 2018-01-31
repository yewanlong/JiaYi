package com.kongqw.serialport.activity;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
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
    private String tcpMap = "";

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
                .build();
        mManager = open(mInfo, mOkOptions);
        mManager.registerReceiver(adapter);
        mManager.connect();
        Log.i("ywl", "getLocalMacAddressFromIp:" + CommonUtils.getLocalMacAddressFromIp());
    }


    @Override
    protected void initData() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .show(homeFragment).commit();
    }

    @Override
    protected void initListener() {
        findViewById(R.id.btn_gpio).setOnClickListener(this);
        findViewById(R.id.btn_ck).setOnClickListener(this);
        findViewById(R.id.btn_gpio2).setOnClickListener(this);
        if (device != null) {
            mSerialPortManager.setOnOpenSerialPortListener(this)
                    .setOnSerialPortDataListener(new OnSerialPortDataListener2() {
                        @Override
                        public void onDataReceived(final byte[] bytes, final int what) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switchReceived(bytes, what);
                                }
                            });
                        }

                        @Override
                        public void onDataSent(final byte[] bytes, final int what) {
                        }
                    }).openSerialPort(device.getFile(), 9600, Tool.SERIAL_TYPE_WHAT_0);
        }
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
            if (!TextUtils.isEmpty(tcpMap)) {
                mManager.send(new HandShake(tcpMap));
            }
        }

        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
            if (e != null) {
                Log.i("ywl", "异常断开:" + e.getMessage());
            } else {
                Log.i("ywl", "正常断开:" + e.getMessage());
            }
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            VToast.showLong("TCP连接失败");
            Log.i("ywl", "连接失败:" + e.getMessage());
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

    private void switchReceived(byte[] bytes, int what) {
        String str;
        switch (what) {
            case Tool.SERIAL_TYPE_WHAT_0:
                break;
            case Tool.SERIAL_TYPE_WHAT_1:
                str = new String(bytes, Charset.forName("utf-8"));
                tcpMap = "Action=CheckIn&Imei=" + str;
                socketSend();
                break;
            case Tool.SERIAL_TYPE_WHAT_3:
                str = Tool.bytesToHexString(bytes);
                Log.i("ywl", "str:" + str);
                break;
            case Tool.SERIAL_TYPE_WHAT_5:
                str = Tool.bytesToHexString(bytes);
                Log.i("ywl", "str:" + str);
//                Map<String, Integer> map = new HashMap<>();
//                map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_3);
//                mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_3);
                //发起03
                break;
        }

    }

    private void socketSend() {
        if (!mManager.isConnect()) {
            mManager.connect();
        } else {
            mManager.send(new HandShake(tcpMap));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gpio:
                startActivity(new Intent(this, GpioActivity1.class));
                break;
            case R.id.btn_ck:
                startActivity(new Intent(this, SelectSerialPortActivity.class));
                break;
            case R.id.btn_gpio2:
                startActivity(new Intent(this, GpioActivity2.class));
                break;
        }
    }

    /**
     * 串口打开成后获取序列号
     *
     * @param device
     */
    @Override
    public void onSuccess(File device) {
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
        tcpMap = "Action=SerialError&Mac=" + CommonUtils.getLocalMacAddressFromIp();
        //TODO 获取成功向服务器发送TCP
        socketSend();
    }

    @Override
    protected void onDestroy() {
        if (device != null) {
            mSerialPortManager.closeSerialPort();
        }
        super.onDestroy();
    }

}
