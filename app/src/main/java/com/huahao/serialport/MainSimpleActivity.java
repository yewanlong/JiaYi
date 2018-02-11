package com.huahao.serialport;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huahao.serialport.adapter.LogAdapter;
import com.huahao.serialport.bean.HandShake;
import com.huahao.serialport.bean.LogBean;
import com.huahao.serialport.bean.MsgDataBean;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
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

import static android.widget.Toast.LENGTH_SHORT;
import static com.xuhao.android.libsocket.sdk.OkSocket.open;

/**
 * Created by Tony on 2017/10/24.
 */

public class MainSimpleActivity extends AppCompatActivity implements View.OnClickListener, OnOpenSerialPortListener {
    private SerialPortManager mSerialPortManager;
    private ConnectionInfo mInfo;
    private Button mConnect;
    private IConnectionManager mManager;
    private EditText mSendET;
    private OkSocketOptions mOkOptions;
    private Button mClearLog;
    private Button mSendBtn;
    private RecyclerView mSendList;
    private RecyclerView mReceList;
    private LogAdapter mSendLogAdapter = new LogAdapter();
    private LogAdapter mReceLogAdapter = new LogAdapter();
    private String content;
    private boolean isShow;
    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
            mManager.send(new HandShake(content));
            mConnect.setText("DisConnect");
        }

        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
            if (e != null) {
                logSend("异常断开:" + e.getMessage());
            } else {
                Toast.makeText(context, "正常断开", LENGTH_SHORT).show();
                logSend("正常断开");
            }
            mConnect.setText("Connect");
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            Toast.makeText(context, "连接失败" + e.getMessage(), LENGTH_SHORT).show();
            logSend("连接失败");
            mConnect.setText("Connect");
        }

        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);
            String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
            logRece(str);
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
            String str = new String(data.parse(), Charset.forName("utf-8"));
            logSend(str);
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
            String str = new String(data.parse(), Charset.forName("utf-8"));
            logSend(str);
        }
    };
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_main);
        findViews();
        initData();
        setListener();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void findViews() {
        device = (Device) getIntent().getSerializableExtra(SerialPortActivity.DEVICE);
        if (null == device) {
            finish();
            return;
        }
        mSendList = findViewById(R.id.send_list);
        mReceList = findViewById(R.id.rece_list);
        mClearLog = findViewById(R.id.clear_log);
        mConnect = findViewById(R.id.connect);
        mSendET = findViewById(R.id.send_et);
        mSendBtn = findViewById(R.id.send_btn);
        mSerialPortManager = new SerialPortManager();
    }

    private void initData() {
        LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSendList.setLayoutManager(manager1);
        mSendList.setAdapter(mSendLogAdapter);

        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReceList.setLayoutManager(manager2);
        mReceList.setAdapter(mReceLogAdapter);

        mInfo = new ConnectionInfo(HttpUtils.TCP_IP, HttpUtils.TCP_PRO_IP);
        mOkOptions = new OkSocketOptions.Builder(OkSocketOptions.getDefault())
                .setReconnectionManager(new NoneReconnect())
                .build();
        mManager = open(mInfo, mOkOptions);
    }

    private void setListener() {
        mManager.registerReceiver(adapter);
        mConnect.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mClearLog.setOnClickListener(this);
        mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        final String status = Tool.bytesToHexString(bytes);
                        Log.i("ywl", "onDataReceivedCopy [ byte[] ]: " + Arrays.toString(finalBytes));
                        Log.i("ywl", "status:" + status);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                socketSend(status, finalBytes);
                            }
                        });
                    }

                    @Override
                    public void onDataSent(byte[] bytes) {
                        final byte[] finalBytes = bytes;
                        final String status = Tool.bytesToHexString(bytes);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                socketSend(status, finalBytes);
                            }
                        });
                    }
                }).openSerialPort(device.getFile(), 9600);
    }


    private void socketSend(String status, byte[] bytes) {
        if (!mManager.isConnect()) {
            if (!isShow) {
                content = status;
                isShow = true;
            }
            mManager.connect();
        } else {
            if (!mManager.isConnect()) {
                Toast.makeText(getApplicationContext(), "未连接,请先连接", LENGTH_SHORT).show();
            } else {
                MsgDataBean msgDataBean = new MsgDataBean(status);
                mManager.send(msgDataBean);
                mSendET.setText("");
            }
        }
    }


    private void logSend(String log) {
        LogBean logBean = new LogBean(System.currentTimeMillis(), log);
        mSendLogAdapter.getDataList().add(0, logBean);
        mSendLogAdapter.notifyDataSetChanged();
    }

    private void logRece(String log) {
        LogBean logBean = new LogBean(System.currentTimeMillis(), log);
        mReceLogAdapter.getDataList().add(0, logBean);
        mReceLogAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mManager != null) {
            mManager.disConnect();
        }
        if (null != mSerialPortManager) {
            mSerialPortManager.closeSerialPort();
            mSerialPortManager = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect:
                if (mManager == null) {
                    return;
                }
                if (!mManager.isConnect()) {
                    mManager.connect();
                } else {
                    mConnect.setText("DisConnecting");
                    mManager.disConnect();
                }
                break;
            case R.id.send_btn:
                mSerialPortManager.sendBytes(Integer.valueOf(mSendET.getText().toString()));
                break;
            case R.id.clear_log:
                mReceLogAdapter.getDataList().clear();
                mSendLogAdapter.getDataList().clear();
                mReceLogAdapter.notifyDataSetChanged();
                mSendLogAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onSuccess(File device) {
        mSerialPortManager.sendBytes(HttpUtils.SERIAL_TYPE);
    }

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

    private Toast mToast;

    private void showToast(String content) {
        if (null == mToast) {
            mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        mToast.show();
    }
}
