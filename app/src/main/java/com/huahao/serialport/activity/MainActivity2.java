package com.huahao.serialport.activity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.huahao.serialport.R;
import com.huahao.serialport.utils.VToast;
import com.kongqw.serialportlibrary.SerialPortManager3;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by Lkn on 2018/3/7.
 */

public class MainActivity2 extends YBaseActivity implements OnOpenSerialPortListener {
    private TextView edit_query, textView;
    private SerialPortManager3 mSerialPortManager3;
    private int number = 1;
    private Handler handler = new Handler();
    private Runnable mRunnable;

    @Override
    protected int getContentView() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initView() {
        edit_query = $(R.id.edit_query);
        textView = findViewById(R.id.textView);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                sendZbj();
            }
        };
    }


    @Override
    protected void initData() {
        mSerialPortManager3 = new SerialPortManager3();
        mSerialPortManager3.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(final byte[] bytes) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edit_query.setText(edit_query.getText().toString() + Tool.bytesToHexString(bytes) + "\t");
                                getSend(bytes);
                            }
                        });
                    }

                    @Override
                    public void onDataSent(final byte[] bytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(textView.getText().toString() + Tool.bytesToHexString(bytes) + "\t");
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
                handler.removeCallbacks(mRunnable);
                number = 1;
                sendZbj();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                edit_query.setText("");
            }
        });
    }

    private void sendZbj() {
        mSerialPortManager3.sendBytes(number);
        mSerialPortManager3.setShow();
    }

    private void getSend(final byte[] bytes) {
        String statusStr = Tool.bytesToHexString(bytes);
        int length = Tool.toInt(bytes[2]);
        byte[] contentBytes = new byte[length];
        for (int i = 3; i < length + 3; i++) {
            contentBytes[i - 3] = bytes[i];
        }
        statusStr = Tool.bytesToHexString(contentBytes);
        switch (number) {
            case Tool.ZBJ_A:
                if (statusStr.substring(0, 2).equals("F0")) {
                    number++;
                    sendZbj();
                }
                break;
            case Tool.ZBJ_B:
                if (statusStr.substring(0, 2).equals("F0")) {
                    number++;
                    sendZbj();
                }
                break;
            case Tool.ZBJ_C:
                if (statusStr.substring(0, 2).equals("F0")) {
                    number++;
                    sendZbj();
                }
                break;
            case Tool.ZBJ_D:
                if (statusStr.substring(0, 2).equals("F0")) {
                    number++;
                    sendZbj();
                }
                break;
            case Tool.ZBJ_E:
                if (statusStr.substring(0, 2).equals("F0")) {
                    number++;
                    sendZbj();
                }
                break;
            case Tool.ZBJ_G:
                if (statusStr.substring(0, 2).equals("F0")) {
                    sendZbj();
                    handler.postDelayed(mRunnable, 200);
                }
                break;

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
