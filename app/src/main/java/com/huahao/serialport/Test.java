package com.huahao.serialport;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.huahao.serialport.activity.YBaseActivity;
import com.kongqw.serialportlibrary.Tool;

/**
 * Created by Lkn on 2018/3/13.
 */

public class Test extends YBaseActivity {
    String str[] = new String[]{"7F0001F0200A7F", "8001F02380", "7F0002F0E87022", "7F8001F02380", "7F001DF00030333638434E590000010601050A1432",
            "6402020202020200006404F6F3", "7F8001F02380", "7F0002F0E87022", "7F8001F02380",
            "7F0001F0200A", "7F8001F02380", "7F0001F0200A", "7F8001F02380", "7F0001F0200A",
            "7F8001F02380", "7F0001F0200A", "7F8001F02380", "7F0001F0200A", "7F8001F02380",
            "7F8001F023807F0001F0200A", "7F8001F02380", "7F0001F0200A", "7F8001F02380",
            "7F0001F0200A7F", "8001F02380", "7F0001F0200A7F8001F02380", "7F0001F0200A",
            "7F8001F023807F0001F0200A", "7F8001F023807F0001F0200A", "7F8001F02380",
            "7F0001F0200A7F8001F02380", "7F0001F0200A", "7F8001F02380", "7F8001F02380",
            "7F8001F023807F0001F0200A", "7F8001F023807F0001F0200A", "7F8001F023807F0001F0200A", "7F8001F02380",
            "7F0001F0200A7F8001F02380", "7F0001F020", "0A7F8001F02380", "7F0001F0200A7F8001F02380",
            "7F0001F0200A7F8001F02380", "7F0001F0200A7F8001F02380", "7F0001F0200A7F8001F02380", "7F0001F0200A",
            "7F8001F023807F0001F0200A", "7F8001F023807F0001F0200A", "7F8001F02380", "7F0001F0200A",
            "7F0001F0200A7F0001F0200A", "7F8001F02380", "7F0001F0200A", "7F8001F02380", "7F0001F0200A",
            "7F0001F0200A7F8001F02380", "7F0001F0200A", "7F0001F0200A7F0001F0200A", "7F8001F02380", "7F0001F0200A",
            "7F8001F023807F0001F0200A", "7F8001F023807F0001F0200A", "7F8001F023807F0001F0200A"};

    private int position;
    private EditText edit_query, edit_query2;
    private String statusStr = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        edit_query = $(R.id.edit_query);
        edit_query2 = $(R.id.edit_query2);
    }

    @Override
    protected void initData() {
    }

    private Handler handler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (position >= str.length) {
                if (statusStr.length() != 0) {
                    getSend(statusStr, false);
                } else {
                    handler.removeCallbacks(mRunnable);
                }
                return;
            }
            edit_query2.setText(edit_query2.getText().toString() + str[position] + " ");
            getSend(str[position], true);
            position++;
        }
    };

    private void getSend(String bytes, boolean isStatus) {
        if (isStatus) {
            statusStr = statusStr + bytes;
        }
        if (statusStr.length() < 6) {
            sendSwitch(true);
            return;
        }
        int length = (Tool.toInt(statusStr.substring(4, 6))) * 2 + 10;
        if (statusStr.length() < length) {
            sendSwitch(true);
            return;
        }
        String newStr = statusStr;
        if (statusStr.length() > length) {
            newStr = statusStr.substring(0, length);
            statusStr = statusStr.substring(length, statusStr.length());
            sendSwitch(false);
        } else {
            statusStr = "";
            sendSwitch(true);
        }
        edit_query.setText(edit_query.getText().toString() + newStr + " ");
    }

    private void sendSwitch(boolean isStatus) {
        if (statusStr.length() != 0 && !isStatus) {
            getSend(statusStr, false);
        } else {
            handler.postDelayed(mRunnable, 1000);
        }
    }

    @Override
    protected void initListener() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(mRunnable, 1000);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(mRunnable);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_query.setText("");
                edit_query2.setText("");
            }
        });
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }
}
