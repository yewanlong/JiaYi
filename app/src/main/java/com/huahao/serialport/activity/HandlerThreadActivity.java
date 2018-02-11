package com.huahao.serialport.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Lkn on 2018/2/8.
 */

public class HandlerThreadActivity extends AppCompatActivity {
    private static final String TAG = "ywl";
    private HandlerThread mHandlerThread;
    private MyHandler mMyHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generatedmethod stub
        super.onCreate(savedInstanceState);
        TextView text = new TextView(this);
        text.setText("HandlerThreadActivity");
        setContentView(text);

        //这个类由Android应用程序框架提供
        mHandlerThread = new HandlerThread("handler_thread");

        //在使用HandlerThread的getLooper()方法之前，必须先调用该类的start();
        mHandlerThread.start();
        //即这个Handler是运行在mHandlerThread这个线程中
        mMyHandler = new MyHandler(mHandlerThread.getLooper());
        mMyHandler.sendEmptyMessage(1);
        mMyHandler.sendEmptyMessage(2);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Log.i("ywl", "mRunnableSub");
            }
        };
        handler.postDelayed(mRunnable, 10);
    }
    private Handler handler = new Handler();

    private class MyHandler extends Handler {

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.i("ywl", "1111");
                    break;
                case 2:
                    Log.i("ywl", "222");
                    break;
            }
            super.handleMessage(msg);
        }
    }

}