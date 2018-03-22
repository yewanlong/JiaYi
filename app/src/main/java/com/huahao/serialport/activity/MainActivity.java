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
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.huahao.serialport.HttpUtils;
import com.huahao.serialport.NoneReconnect;
import com.huahao.serialport.R;
import com.huahao.serialport.bean.EventApk;
import com.huahao.serialport.bean.GoodsNotice;
import com.huahao.serialport.bean.HandShake;
import com.huahao.serialport.utils.CommonUtils;
import com.huahao.serialport.utils.SilentInstall;
import com.huahao.serialport.utils.VToast;
import com.kongqw.serialportlibrary.SerialPortManager2;
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

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<GoodsNotice> goodsNotices = new ArrayList<>();
    private Button button;
    private ArrayList<String> list = new ArrayList<>();
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
//            list.add("TCP：" + str);

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
        app.addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏的flag
        button = findViewById(R.id.button);
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
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(final byte[] bytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchReceived(bytes);
                            }
                        });
                    }

                    @Override
                    public void onDataSent(final byte[] bytes) {
                        Log.i("ywl", "onDataSent:" + Tool.bytesToHexString(bytes));
//                        list.add("发送：" + Tool.bytesToHexString(bytes));
                    }
                }).openSerialPort(new File("/dev/ttyS3"), 9600);
        button.setOnClickListener(this);
    }

    @Override
    protected boolean isApplyEventBus() {
        return true;
    }

    private void switchReceived(byte[] bytes) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        statusStr = statusStr + Tool.bytesToHexString(bytes);
        if (statusStr.length() == 40) {
            Log.i("ywl", "switchReceived:" + statusStr);
//            list.add("接收：" + statusStr);
            switch (statusStr.substring(2, 4)) {
                case "0" + HttpUtils.SERIAL_TYPE_4: {
                    if (statusStr.contains("AA")) {
                        setChannelStr("0");
                    } else if (statusStr.contains("BB")) {
                        setChannelStr("1");
                    } else {
                        setChannelStr("2");
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
                        VToast.showLong("出货成功");
                        socketSend(HttpUtils.getDelive(HttpUtils.IMEI, 1, Integer.valueOf(goodsNotices.get(0).getChannelIndex()),
                                goodsNotices.get(0).getSaleId(), 1));
                        goodsNotices.remove(0);
                        send03();
                    } else if (statusStr.substring(4, 6).equals("02") || statusStr.substring(4, 6).equals("32")) {
                        VToast.showLong("正在运行..请稍后");
                        send03();
                    } else {
                        VToast.showLong("出货失败，正在退款，请稍后...");
                        socketSend(HttpUtils.getDelive(HttpUtils.IMEI, 0, Integer.valueOf(goodsNotices.get(0).getChannelIndex()),
                                goodsNotices.get(0).getSaleId(), 0));
                        goodsNotices.remove(0);
                        if (goodsNotices.size() == 0) {
                            homeFragment.initList2();
                        } else {
                            send05(Integer.valueOf(goodsNotices.get(0).getChannelIndex()));
                        }
                    }
                    break;
                }
                case "0" + HttpUtils.SERIAL_TYPE_3: {
                    if (statusStr.substring(4, 6).equals("00")) {//空闲
                        send();
                    } else if (statusStr.substring(4, 6).equals("02")) {
                        send();
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        send03();
                    }
                    break;
                }
            }
            statusStr = "";
        }
    }

    private void send() {
        if (goodsNotices.size() == 0) {
            homeFragment.initList2();
        } else {
            send05(Integer.valueOf(goodsNotices.get(0).getChannelIndex()));
        }
    }

    private void setChannelStr(String id) {
        if (channelLenght == (channelId.length - 1))
            channelStr = channelStr + id;
        else
            channelStr = channelStr + id + ",";
    }

    private void send05(int id) {
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_5);
        map.put(Tool.MOTOR_NUMBER, id);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_5);
    }

    private void send03() {
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_3);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_3);
    }

    private void send04(int id) {
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_4);
        map.put(Tool.MOTOR_NUMBER, id);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_4);
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
            case R.id.button:
                if (button.getText().toString().equals("查看log")) {
                    homeFragment.initLog(list);
                    button.setText("关闭");
                } else {
                    homeFragment.initLog();
                    button.setText("查看log");
                }
                break;
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
                    }
                });
                String[] ChannelIndexArray = jsonObject.getString("ChannelIndex").split(",");
                String[] SaleId = jsonObject.getString("SaleId").split(",");
                if (ChannelIndexArray.length == SaleId.length) {
                    for (int i = 0; i < ChannelIndexArray.length; i++) {
                        GoodsNotice goodsNotice = new GoodsNotice();
                        goodsNotice.setChannelIndex(ChannelIndexArray[i]);
                        goodsNotice.setSaleId(SaleId[i]);
                        goodsNotices.add(goodsNotice);
                    }
                }
                send03();
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
        if (SilentInstall.install(event.getPath())) {
            Intent ite = new Intent(this, StartReceiver.class);
            ite.setAction("install_and_start");
            PendingIntent SENDER = PendingIntent.getBroadcast(this, 0, ite,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager ALARM = (AlarmManager) getSystemService(ALARM_SERVICE);
            ALARM.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100,
                    SENDER);
            VToast.showLong("安装成功");
        } else {
            VToast.showLong("安装失败");
        }
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
