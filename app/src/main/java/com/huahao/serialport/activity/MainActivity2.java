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
    private String statusStr = "";
    private byte[] statusByte = new byte[1024];

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
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSerialPortManager3.closeSerialPort();
            }
        });
    }

    private void sendZbj() {
        mSerialPortManager3.sendBytes(number);
        mSerialPortManager3.setShow();
    }

    private void getSend(final byte[] bytes) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        statusStr = statusStr + Tool.bytesToHexString(bytes);
        if (statusStr.length() < 6) {
            return;
        }
//        int length = Tool.toInt(statusStr.substring(4, 6));
//        if (statusStr.length() < 10 + length) {
//            return;
//        }
//        length = 6 + length + 4;
        String newStr = statusStr;
//        if (statusStr.length() != length) {
//            newStr = statusStr.substring(0, length);
//            statusStr = statusStr.substring(length, statusStr.length());
//        } else {
//            statusStr = "";
//        }
//        statusStr = Tool.bytesToHexString(contentBytes);
        edit_query.setText(edit_query.getText().toString() + newStr + "\t");
        Log.i("ywl", edit_query.getText().toString() + newStr + "\t");
        switch (number) {
            case Tool.ZBJ_A:
//                if (statusStr.substring(0, 2).equals("F0")) {
                number++;
                sendZbj();
//                }
                break;
            case Tool.ZBJ_B:
//                if (statusStr.substring(0, 2).equals("F0")) {
                number++;
                sendZbj();
//                }
                break;
            case Tool.ZBJ_C:
//                if (statusStr.substring(0, 2).equals("F0")) {
                number++;
                sendZbj();
//                }
                break;
            case Tool.ZBJ_D:
//                if (statusStr.substring(0, 2).equals("F0")) {
                number++;
                sendZbj();
//                }
                break;
            case Tool.ZBJ_E:
//                if (statusStr.substring(0, 2).equals("F0")) {
                number++;
                sendZbj();
//                }
                break;
            case Tool.ZBJ_F:
//                if (statusStr.substring(0, 2).equals("F0")) {
                number++;
                sendZbj();
//                }
                break;
            case Tool.ZBJ_G:
//                if (statusStr.substring(0, 2).equals("F0")) {
                if (!statusStr.equals("")) {
                    sendZbj();
                    handler.postDelayed(mRunnable, 2000);
                } else {
                    edit_query.setText(edit_query.getText().toString() + "---------stop  " + statusStr);
                }
//                }
                break;
        }
        statusStr = "";
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
