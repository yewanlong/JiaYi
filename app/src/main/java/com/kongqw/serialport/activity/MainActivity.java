package com.kongqw.serialport.activity;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.kongqw.serialport.HttpUtils;
import com.kongqw.serialport.R;
import com.kongqw.serialport.SelectSerialPortActivity;
import com.kongqw.serialport.SerialPortActivity;
import com.kongqw.serialport.gpio.GpioActivity1;
import com.kongqw.serialport.gpio.GpioActivity2;
import com.kongqw.serialport.utils.VToast;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.SerialPortManager2;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener2;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lkn on 2018/1/29.
 */

public class MainActivity extends YBaseActivity implements View.OnClickListener, OnOpenSerialPortListener {

    private HomeFragment homeFragment;
    private SerialPortManager2 mSerialPortManager;
    private Device device;

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

    private void switchReceived(byte[] bytes, int what) {
        switch (what) {
            case Tool.SERIAL_TYPE_WHAT_0:
                //TODO 获取成功向服务器发送TCP
//                socketSend(status, finalBytes);
                break;
            case Tool.SERIAL_TYPE_WHAT_1:
                break;
            case Tool.SERIAL_TYPE_WHAT_5:
                break;
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
     * 串口打开成获取序列号
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
    }

    @Override
    protected void onDestroy() {
        if (device != null) {
            mSerialPortManager.closeSerialPort();
        }
        super.onDestroy();
    }

}
