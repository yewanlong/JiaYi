package com.huahao.serialport.activity;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.huahao.serialport.HttpUtils;
import com.huahao.serialport.NoneReconnect;
import com.huahao.serialport.R;
import com.huahao.serialport.bean.EventApk;
import com.huahao.serialport.bean.HandShake;
import com.huahao.serialport.utils.CommonUtils;
import com.huahao.serialport.utils.SilentInstall;
import com.huahao.serialport.utils.VToast;
import com.kongqw.serialportlibrary.SerialPortManager2;
import com.kongqw.serialportlibrary.Tool;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener2;
import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.IPulseSendable;
import com.xuhao.android.libsocket.sdk.bean.ISendable;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.xuhao.android.libsocket.sdk.OkSocket.open;

/**
 * Created by Lkn on 2018/1/29.
 */

public class MainActivity extends YBaseActivity implements View.OnClickListener, OnOpenSerialPortListener {
    private HomeFragment homeFragment;
    private SerialPortManager2 mSerialPortManager;
    private IConnectionManager mManager;
    private ConnectionInfo mInfo;
    private OkSocketOptions mOkOptions;
    private Handler handler = new Handler();
    private long msgId = 0;
    private int channelLenght = 0;
    private String channelStr = "", statusStr = "";
    private String[] channelId = new String[0];
    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
            checkPermission(SELFPERMISSIONS, 199);
        }

        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
            if (e != null) {
                handler.postDelayed(mRunnable, 20000);
            }
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            handler.postDelayed(mRunnable, 20000);
        }

        //接收
        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);
            String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
            ReadResponse(str);
            Log.i("ywl", "onSocketReadResponse:" + str);
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        Log.i("ywl", "ssssss");
//        app.addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏的flag
        homeFragment = new HomeFragment();
        mSerialPortManager = new SerialPortManager2();
        mInfo = new ConnectionInfo(HttpUtils.TCP_IP, HttpUtils.TCP_PRO_IP);
        mOkOptions = new OkSocketOptions.Builder(OkSocketOptions.getDefault())
                .setReconnectionManager(new NoneReconnect())
                .setSinglePackageBytes(1024)
                .build();
        mManager = open(mInfo, mOkOptions);
        mManager.registerReceiver(adapter);
        mManager.connect();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mManager.connect();
        }
    };

    private Runnable mRunnableSub = new Runnable() {
        @Override
        public void run() {
            homeFragment.subtractList();
        }
    };

    private Runnable mRunnableCSQ = new Runnable() {
        @Override
        public void run() {
            socketSend(HttpUtils.getCSQ(msgId, HttpUtils.IMEI));
            msgId++;
            handler.postDelayed(mRunnableCSQ, 300000);
        }
    };

    @Override
    protected void initData() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .show(homeFragment).commit();
    }

    @Override
    protected void initListener() {
        mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener2() {
                    @Override
                    public void onDataReceived(final byte[] bytes, final int id, final String saleId) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchReceived(bytes, id, saleId);
                            }
                        });
                    }

                    @Override
                    public void onDataSent(final byte[] bytes, final int what) {
                        Log.i("ywl", "onDataSent:" + Tool.bytesToHexString(bytes));
                    }
                }).openSerialPort(new File("/dev/ttyS3"), 9600);
    }

    @Override
    protected boolean isApplyEventBus() {
        return true;
    }

    private void switchReceived(byte[] bytes, int id, String SaleId) {
        statusStr = statusStr + Tool.bytesToHexString(bytes);
        if (statusStr.length() == 40) {
            Log.i("ywl", "statusStr:" + Tool.bytesToHexString(bytes));
            switch (statusStr.substring(2, 4)) {
                case "0" + HttpUtils.SERIAL_TYPE_4: {
                    if (statusStr.contains("AA")) {
                        setChannelStr("0");
                    } else if (statusStr.contains("BB")) {
                        setChannelStr("1");
                    } else {
                        setChannelStr("2");
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    channelLenght++;
                    if (channelLenght < channelId.length) {
                        send04(Integer.valueOf(channelId[channelLenght]));
                    } else {
                        dismissProgressDialog();
                        socketSend(HttpUtils.getChannelStatus(HttpUtils.IMEI, channelStr));
                    }
                    break;
                }
                case "0" + HttpUtils.SERIAL_TYPE_5: {
                    if (statusStr.substring(4, 6).equals("00")) {
                        socketSend(HttpUtils.getDelive(HttpUtils.IMEI, 1, id, SaleId, 1));
                        homeFragment.initList2();
                        VToast.showLong("出货成功");
                    } else if (statusStr.substring(4, 6).equals("02") || statusStr.substring(4, 6).equals("32")) {
                        send05(id, SaleId);
                        VToast.showLong("电机正在运行..请稍后");
                    } else {
                        VToast.showLong("出货失败，正在退款，请稍后...");
                        socketSend(HttpUtils.getDelive(HttpUtils.IMEI, 0, id, SaleId, 0));
                    }
                    break;
                }

            }
            statusStr = "";
        }
    }

    private void setChannelStr(String id) {
        if (channelLenght == (channelId.length - 1))
            channelStr = channelStr + id;
        else
            channelStr = channelStr + id + ",";
    }

    private void send05(int id, String SaleId) {
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_5);
        map.put(Tool.MOTOR_NUMBER, id);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_5);
        mSerialPortManager.setWhat(id, SaleId);
    }

    private void send04(int id) {
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_4);
        map.put(Tool.MOTOR_NUMBER, id);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_4);
        mSerialPortManager.setWhat(id, "");
    }

    private void socketSend(String tcpMap) {
        if (!mManager.isConnect()) {
            mManager.connect();
        } else {
            mManager.send(new HandShake(tcpMap));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    /**
     * 串口打开成后获取序列号
     *
     * @param device
     */
    @Override
    public void onSuccess(File device) {
        VToast.showLong("串口打开成功");
    }

    public void checkPermission(String[] permissions, int REQUEST_FOR_PERMISSIONS) {
        if (lacksPermissions(permissions)) {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_FOR_PERMISSIONS);
        } else {
            initImei();
        }
    }

    private void initImei() {
        HttpUtils.IMEI = CommonUtils.getSubscriberId(this);
        socketSend(HttpUtils.getCheckIn(0, HttpUtils.IMEI));
        handler.postDelayed(mRunnableCSQ, 1000);
        homeFragment.getLunbo();
        homeFragment.initList();
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
        handler.removeCallbacks(mRunnableCSQ);
        handler.removeCallbacks(mRunnable);
        handler.removeCallbacks(mRunnableSub);
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                handler.removeCallbacks(mRunnableSub);
            }
            case MotionEvent.ACTION_UP: {
                handler.postDelayed(mRunnableSub, 70000);
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void ReadResponse(String str) {
        JSONObject jsonObject = new JSONObject();
        String[] sourceStrArray = str.split("&");
        for (int i = 0; i < sourceStrArray.length; i++) {
            String[] jsonStr = sourceStrArray[i].split("=");
            if (jsonStr.length == 2) {
                jsonObject.put(jsonStr[0], jsonStr[1]);
            }
        }
        switch (jsonObject.getString("Action")) {
            case "GoodsNotice":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeFragment.dialogDismiss();
                        VToast.showLong("正在出货，请稍后...");
                    }
                });
                send05(Integer.valueOf(jsonObject.getString("ChannelIndex")), jsonObject.getString("SaleId"));
                break;
            case "Reboot":
                SilentInstall.reboot();
                break;
            case "GetGoods":
                homeFragment.initList();
                break;
            case "ChannelStatus":
                setProgressDialog("正在检查电机是否正常,请稍后...");
                channelStr = "";
                channelLenght = 0;
                channelId = jsonObject.getString("ChannelStatus").split(",");
                send04(Integer.valueOf(channelId[channelLenght]));
                break;
        }
    }

    static final String[] SELFPERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    @Subscribe
    public void onEventMainThread(EventApk event) {
        Intent ite = new Intent(this, StartReceiver.class);
        ite.setAction("install_and_start");
        PendingIntent SENDER = PendingIntent.getBroadcast(this, 0, ite,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager ALARM = (AlarmManager) getSystemService(ALARM_SERVICE);
        ALARM.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000,
                SENDER);
        SilentInstall.install(event.getPath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 199:
                for (int i = 0; i < grantResults.length; i++) {
                    if (i == 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        initImei();
                    }
                }
                break;
            default:
                break;
        }
    }
}
