package com.kongqw.serialport.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kongqw.serialport.R;
import com.kongqw.serialport.utils.ScreenUtil;


public class HomeOrderDialog extends YBaseActivity implements OnClickListener {

    private void initManager() {
        WindowManager.LayoutParams params = getWindow().getAttributes(); // 获取对话框当前的参数值
        int height = (int) (ScreenUtil.getInstance(this).getHeight() * 0.92);
        params.height = height;
        params.width = ScreenUtil.getInstance(this).getWidth();
        params.gravity = Gravity.TOP;
        onWindowAttributesChanged(params);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initView() {
        initManager();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }
}
