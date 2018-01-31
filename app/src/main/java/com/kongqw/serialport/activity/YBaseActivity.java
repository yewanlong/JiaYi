package com.kongqw.serialport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kongqw.serialport.InitApplication;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Ywl on 2016/7/12.
 */
public abstract class YBaseActivity extends AppCompatActivity {
    public static InitApplication app = InitApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        if (isApplyEventBus())
            EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract boolean isApplyEventBus();


    @Override
    protected void onDestroy() {
        if (isApplyEventBus()) EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public <T> T $(int viewId) {
        return (T) findViewById(viewId);
    }
    public void back(View view) {
        finish();
    }
}
