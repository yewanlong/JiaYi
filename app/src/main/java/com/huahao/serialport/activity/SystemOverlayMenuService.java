package com.huahao.serialport.activity;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Lkn on 2018/5/3.
 */

public class SystemOverlayMenuService extends Service {
    private final String TAG = "ywl";
    private WindowManager windowManager;
    private Button button;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        showFlowWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        windowManager.removeView(button);
    }

    public void showFlowWindow() {
        Log.v(TAG, "showFlowWindow");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        button = new Button(getApplicationContext());
        button.setText("打开");
        button.setBackgroundColor(Color.RED);
        button.setWidth(100);
        button.setHeight(100);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = 100;
        params.height = 100;
        params.x = 300;
        params.y = 300;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(getBaseContext(), MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(dialogIntent);
                Intent intent = new Intent(getBaseContext(), SystemOverlayMenuService.class);
                stopService(intent);
            }
        });
        windowManager.addView(button, params);
    }
}
