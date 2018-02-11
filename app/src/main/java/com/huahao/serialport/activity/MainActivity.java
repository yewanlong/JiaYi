package com.huahao.serialport.activity;


import android.content.Context;
import android.os.Handler;
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
    private Runnable mRunnable, mRunnableSub, mRunnableCSQ;
    private long msgId = 0;
    private int channelLenght = 0;
    private String channelStr = "";
    private String[] channelId = new String[0];
    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
//            socketSend(HttpUtils.getCheckIn(0, HttpUtils.IMEI));
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
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mManager.connect();
            }
        };
        mRunnableSub = new Runnable() {
            @Override
            public void run() {
                homeFragment.subtractList();
            }
        };
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mManager.connect();
            }
        };
        mRunnableCSQ = new Runnable() {
            @Override
            public void run() {
                socketSend(HttpUtils.getCSQ(msgId, HttpUtils.IMEI));
                msgId++;
                handler.postDelayed(mRunnableCSQ, 30000);
            }
        };
        mManager.connect();
    }

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
                    }
                }).openSerialPort(new File("/dev/ttyS1"), 9600);
    }

    @Override
    protected boolean isApplyEventBus() {
        return true;
    }

    private void switchReceived(byte[] bytes, int id, String SaleId) {
        switch (bytes[1]) {
            case HttpUtils.SERIAL_TYPE: {
                String str = Tool.bytesToHexString(bytes);
                str = str.substring(14, 28);
                byte[] send_start = new byte[7];
                int start = 0;
                int end = 2;
                for (int i = 0; i < send_start.length; i++) {
                    send_start[i] = (byte) Integer.parseInt(str.substring(start, end), 16);
                    start = start + 2;
                    end = end + 2;
                }
                str = new String(send_start, Charset.forName("utf-8"));
                HttpUtils.IMEI = str;
                socketSend(HttpUtils.getCheckIn(0, HttpUtils.IMEI));
                handler.postDelayed(mRunnableCSQ, 300000);
                homeFragment.getLunbo();
                break;
            }
            case HttpUtils.SERIAL_TYPE_4: {
                String str = Tool.bytesToHexString(bytes);
                if (str.contains("AA")) {
                    setChannelStr("0");
                } else if (str.contains("BB")) {
                    setChannelStr("1");
                } else {
                    setChannelStr("2");
                }
                channelLenght++;
                if (channelLenght < channelId.length) {
                    send04(Integer.valueOf(channelId[channelLenght]));
                } else {
                    socketSend(HttpUtils.getChannelStatus(HttpUtils.IMEI, channelStr));
                }
                break;
            }
            case HttpUtils.SERIAL_TYPE_5: {
                String str = Tool.bytesToHexString(bytes);
                if (str.substring(4, 6).equals("00")) {
                    socketSend(HttpUtils.getDelive(HttpUtils.IMEI, 1, id, SaleId, 1));
                    homeFragment.subtractList();
                    VToast.showLong("出货成功啦");
                } else if (str.substring(4, 6).equals("02") || str.substring(4, 6).equals("32")) {
                    send05(id, SaleId);
                    VToast.showLong("电机正在运行..请稍后");
                } else {
                    VToast.showLong("出货失败，正在退款，请稍后...");
                    socketSend(HttpUtils.getDelive(HttpUtils.IMEI, 0, id, SaleId, 0));
                }
                break;
            }
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
        mSerialPortManager.startReadThread(id, SaleId);
    }

    private void send04(int id) {
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE_4);
        map.put(Tool.MOTOR_NUMBER, id);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_4);
        mSerialPortManager.startReadThread(id, "");
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
        Map<String, Integer> map = new HashMap<>();
        map.put(Tool.MOTOR, HttpUtils.SERIAL_TYPE);
        mSerialPortManager.sendBytes(map, Tool.SERIAL_TYPE_WHAT_1);
        mSerialPortManager.startReadThread(Tool.SERIAL_TYPE_WHAT_1, "");
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
        mManager.disConnect();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                handler.removeCallbacks(mRunnableSub);
            }
            case MotionEvent.ACTION_UP: {
                handler.postDelayed(mRunnableSub, 120000);
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
                channelStr = "";
                channelLenght = 0;
                channelId = jsonObject.getString("ChannelStatus").split(",");
                send04(Integer.valueOf(channelId[channelLenght]));
                break;
        }
    }


    @Subscribe
    public void onEventMainThread(EventApk event) {
//        SilentInstall.install(event.getPath());
    }
}
