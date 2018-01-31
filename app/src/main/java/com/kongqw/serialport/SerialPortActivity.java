package com.kongqw.serialport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.io.File;
import java.util.Arrays;

public class SerialPortActivity extends AppCompatActivity implements OnOpenSerialPortListener {

    public static final String DEVICE = "device";
    private SerialPortManager mSerialPortManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);
        Device device = (Device) getIntent().getSerializableExtra(DEVICE);
        if (null == device) {
            finish();
            return;
        }

        Log.i("ywl", "file:" + device.getFile().getPath());
//        File f = new File(file);
        mSerialPortManager = new SerialPortManager();
        mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        String status = Tool.bytesToHexString(bytes);
                        Log.i("ywl", "onDataReceivedCopy [ byte[] ]: "
                                + Arrays.toString(bytes));
                        Log.i("ywl", "status:" + status);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(String.format("接收\n%s", new String(finalBytes)));
                            }
                        });
                    }

                    @Override
                    public void onDataSent(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(String.format("发送\n%s", finalBytes));
                            }
                        });
                    }
                }).openSerialPort(device.getFile(), 9600);
    }


    @Override
    protected void onDestroy() {
        if (null != mSerialPortManager) {
            mSerialPortManager.closeSerialPort();
            mSerialPortManager = null;
        }
        super.onDestroy();
    }

    /**
     * 串口打开成功
     *
     * @param device 串口
     */
    @Override
    public void onSuccess(File device) {
        Toast.makeText(getApplicationContext(), String.format("串口 [%s] 打开成功", device.getPath()), Toast.LENGTH_SHORT).show();
    }

    /**
     * 串口打开失败
     *
     * @param device 串口
     * @param status status
     */
    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                showToast("没有读写权限");
                break;
            case OPEN_FAIL:
            default:
                showToast("串口打开失败");
                break;
        }
    }

    /**
     * 显示提示框
     *
     * @param message message
     */
    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }


    /**
     * 发送数据
     *
     * @param view view
     */
    public void onSend(View view) {
        EditText editTextSendContent = (EditText) findViewById(R.id.et_send_content);
        if (null == editTextSendContent) {
            return;
        }
        String sendContent = editTextSendContent.getText().toString().trim();
        if (TextUtils.isEmpty(sendContent)) {
            showDialog("onSend: 发送内容为 null");
            return;
        }

        boolean sendBytes = mSerialPortManager.sendBytes(
                Integer.valueOf(editTextSendContent.getText().toString()));
        showToast(sendBytes ? "发送成功" : "发送失败");
    }


    private Toast mToast;

    /**
     * Toast
     *
     * @param content content
     */
    private void showToast(String content) {
        if (null == mToast) {
            mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        mToast.show();
    }
}
