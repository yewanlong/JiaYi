package com.huahao.serialport.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.huahao.serialport.InitApplication;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Ywl on 2016/7/12.
 */
public abstract class YBaseActivity extends AppCompatActivity {
    public static InitApplication app = InitApplication.getInstance();
    public ProgressDialog pd;
    public static final int REQUEST_FOR_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        if (isApplyEventBus())
            EventBus.getDefault().register(this);
        pd = new ProgressDialog(this);
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
    public void setProgressDialog(String msg) {
        pd.setMessage(msg);
        pd.show();
    }
    public void dismissProgressDialog() {
        if (!isFinishing() && pd != null && pd.isShowing())
            pd.dismiss();
    }
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(getApplicationContext(), permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    public <T> T $(int viewId) {
        return (T) findViewById(viewId);
    }
    public void back(View view) {
        finish();
    }
}
