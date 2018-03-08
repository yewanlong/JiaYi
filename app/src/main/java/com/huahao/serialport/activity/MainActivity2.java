package com.huahao.serialport.activity;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.huahao.serialport.R;
import com.huahao.serialport.utils.VToast;
import com.kongqw.serialportlibrary.SerialPortManager3;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.io.File;

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
                                edit_query.setText(Tool.bytesToHexString(bytes) + "\n");
                                getSend();
                            }
                        });
                    }

                    @Override
                    public void onDataSent(final byte[] bytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(Tool.bytesToHexString(bytes) + "\n");
                            }
                        });
                    }
                }).openSerialPort(new File("/dev/ttyS1"), 9600);

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
    }

    private void sendZbj() {
        mSerialPortManager3.sendBytes(number);
        mSerialPortManager3.setShow();
    }

    private void getSend() {
        switch (number) {
            case Tool.ZBJ_A:
                number++;
                sendZbj();
                break;
            case Tool.ZBJ_B:
                number++;
                sendZbj();
                break;
            case Tool.ZBJ_C:
                number++;
                sendZbj();
                break;
            case Tool.ZBJ_D:
                number++;
                sendZbj();
                break;
            case Tool.ZBJ_E:
                number++;
                sendZbj();
                break;
            case Tool.ZBJ_G:
                sendZbj();
                handler.postDelayed(mRunnable, 200);
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
