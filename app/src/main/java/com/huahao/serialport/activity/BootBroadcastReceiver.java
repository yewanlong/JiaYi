package com.huahao.serialport.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Lkn on 2018/2/9.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    static final String ACTION2 = "android.intent.action.PACKAGE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ywl","BootBroadcastReceiver:");
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
            Log.i("ywl","BootBroadcastReceiver:"+ACTION);
        } else if (intent.getAction().equals(ACTION2)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
            Log.i("ywl","BootBroadcastReceiver:"+ACTION2);
        }
    }
}
