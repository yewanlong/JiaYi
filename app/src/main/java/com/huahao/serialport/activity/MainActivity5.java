package com.huahao.serialport.activity;

import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.huahao.serialport.R;
import com.huahao.serialport.utils.VToast;
import com.kongqw.serialportlibrary.SerialPortManagerSjj;
import com.kongqw.serialportlibrary.SerialPortManagerTz;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.io.File;

/**
 * Created by Lkn on 2018/3/7.
 * 升降机
 */

public class MainActivity5 extends YBaseActivity implements OnOpenSerialPortListener {
    private SerialPortManagerSjj mSerialPortManager;
    private EditText textView, textView2;

    @Override
    protected int getContentView() {
        return R.layout.activity_main4;
    }

    @Override
    protected void initView() {
        textView = $(R.id.textView);
        textView2 = $(R.id.textView2);
    }

    @Override
    protected void initData() {
        mSerialPortManager = new SerialPortManagerSjj();
        mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(final byte[] bytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView2.setText(textView2.getText().toString() + new String(bytes));
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
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select());
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select1());
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select2());
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select3());
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select4());
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select5());
            }
        });
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select6());
            }
        });
        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select7());
            }
        });
        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select8());
            }
        });
        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemClock.sleep(350);
                mSerialPortManager.sendBytes(mSerialPortManager.select9());
            }
        });
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

    @Override
    protected void onDestroy() {
        mSerialPortManager.closeSerialPort();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }
}
