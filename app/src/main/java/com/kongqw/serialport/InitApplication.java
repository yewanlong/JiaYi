package com.kongqw.serialport;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kongqw.serialport.utils.ImageLoaderUtil;
import com.kongqw.serialport.volley.BaseRequest;
import com.kongqw.serialportlibrary.SerialPort;
import com.kongqw.serialportlibrary.SerialPortFinder;
import com.squareup.leakcanary.LeakCanary;
import com.xuhao.android.libsocket.sdk.OkSocket;

/**
 * Created by kqw on 2016/10/26.
 * InitApplication
 */

public class InitApplication extends Application {
    public static Context applicationContext;
    private static InitApplication instance;

    @Override
    public void onCreate() {
        applicationContext = this;
        instance = this;
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        OkSocket.initialize(this, true);
        ImageLoaderUtil.initImageLoaderConfig(applicationContext);
    }

    public static InitApplication getInstance() {
        return instance;
    }

    public boolean isDebugMode() {
        ApplicationInfo info = getApplicationInfo();
        return (0 != ((info.flags) & ApplicationInfo.FLAG_DEBUGGABLE));
    }

    private RequestQueue mQueue;
    private DefaultRetryPolicy mPolicy;

    /**
     * 将请求加入队列并执行，默认不进行请求缓存，默认6秒超时，retry一次
     *
     * @param what
     * @param request
     * @param tag
     */
    public void addRequestQueue(int what, BaseRequest<?> request, Object tag) {
        addRequestQueue(what, request, false, tag);
    }

    /**
     * 默认6秒超时，retry一次
     *
     * @param what
     * @param request
     * @param shouldCache
     * @param tag
     */
    public void addRequestQueue(int what, BaseRequest<?> request,
                                boolean shouldCache, Object tag) {
        addRequestQueue(what, request, shouldCache, 8 * 1000, true, tag);
    }

    public void addRequestQueue(int what, BaseRequest<?> request,
                                boolean shouldCache, int initialTimeoutMs, boolean retry, Object tag) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        if (mPolicy == null) {
            mPolicy = new DefaultRetryPolicy(initialTimeoutMs, retry ? 1 : 0,
                    1.0f);
        }
//        request.putHeader(getMap());
        request.setRetryPolicy(mPolicy);
        request.setWhat(what);
        request.setShouldCache(shouldCache);
        if (tag != null)
            request.setTag(tag);
        mQueue.add(request);
    }

}