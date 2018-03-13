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
    private TextView edit_query, textView,textView2;
    private SerialPortManager3 mSerialPortManager3;
    private int number = 1;
    private Handler handler = new Handler();
    private Runnable mRunnable;
    private String statusStr = "";
    @Override
    protected int getContentView() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initView() {
        edit_query = $(R.id.edit_query);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
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
                                getSend(Tool.bytesToHexString(bytes), true);
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
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(mRunnable);
            }
        });
    }

    private void sendZbj() {
        mSerialPortManager3.sendBytes(number);
        mSerialPortManager3.setShow();
    }

    private void getSend(String str, boolean isStatus) {
        if (isStatus) {
            statusStr = statusStr + str;
        }
        if (statusStr.length() < 6) {
            sendSwitch(true);
            return;
        }
        int length = (Tool.toInt(statusStr.substring(4, 6))) * 2 + 10;
        if (statusStr.length() < length) {
            sendSwitch(true);
            return;
        }
        String newStr = statusStr;
        if (statusStr.length() > length) {
            newStr = statusStr.substring(0, length);
            statusStr = statusStr.substring(length, statusStr.length());
            sendSwitch(false);
        } else {
            statusStr = "";
            sendSwitch(true);
        }
        edit_query.setText(edit_query.getText().toString() + newStr + " ");
        if (newStr.contains("F0EF")) {
            Log.i("ywl", "有纸币进入：" + newStr);
            textView2.setText(textView2.getText().toString()+ newStr +" ");
        }
    }

    private void sendSwitch(boolean isStatus) {
        switch (number) {
            case Tool.ZBJ_G:
                if (statusStr.length() != 0 && !isStatus) {
                    getSend(statusStr, false);
                } else {
                    handler.postDelayed(mRunnable, 350);
                }
                break;
            default:
                number++;
                sendZbj();
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
