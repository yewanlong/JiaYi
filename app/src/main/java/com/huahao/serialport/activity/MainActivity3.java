package com.huahao.serialport.activity;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huahao.serialport.R;
import com.huahao.serialport.utils.VToast;
import com.kongqw.serialportlibrary.SerialPortManagerYb;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.io.File;

/**
 * Created by Lkn on 2018/3/7.
 */

public class MainActivity3 extends YBaseActivity implements OnOpenSerialPortListener {
    private SerialPortManagerYb mSerialPortManager4;
    private TextView textView, textView2;
    private String statusYbj = "";
    private EditText et_money;

    @Override
    protected int getContentView() {
        return R.layout.activity_main3;
    }

    @Override
    protected void initView() {
        textView = $(R.id.textView);
        textView2 = $(R.id.textView2);
        et_money = $(R.id.et_money);
    }

    @Override
    protected void initData() {
        mSerialPortManager4 = new SerialPortManagerYb();
        mSerialPortManager4.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(final byte[] bytes) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getSendYbj(Tool.bytesToHexString(bytes));
                            }
                        });
                    }

                    @Override
                    public void onDataSent(final byte[] bytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(textView.getText().toString() + Tool.bytesToHexString(bytes) + " ");
                            }
                        });
                    }
                }).openSerialPort(new File("/dev/ttyS3"), 9600);

    }


    @Override
    protected void initListener() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText().toString() + Tool.bytesToHexString(mSerialPortManager4.selectYB()) + " ");
                mSerialPortManager4.sendBytes(mSerialPortManager4.selectYB());
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText().toString() + Tool.bytesToHexString(mSerialPortManager4.goYB(Integer.valueOf(et_money.getText().toString()))) + " ");
                mSerialPortManager4.sendBytes(mSerialPortManager4.goYB(Integer.valueOf(et_money.getText().toString())));
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(textView.getText().toString() + Tool.bytesToHexString(mSerialPortManager4.selectNoYB()) + " ");
                mSerialPortManager4.sendBytes(mSerialPortManager4.selectNoYB());
            }
        });
    }

    private void getSendYbj(String str) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        statusYbj = statusYbj + str;
        if (str.length() == 12) {
            textView2.setText(textView2.getText().toString() + statusYbj + " ");
            statusYbj = "";
        }
    }


    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    @Override
    public void onSuccess(File device) {
        VToast.showLong("串口打开成功");
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
}
