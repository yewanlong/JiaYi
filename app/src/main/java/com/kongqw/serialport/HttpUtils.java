package com.kongqw.serialport;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.kongqw.serialport.volley.RequestListener;
import com.kongqw.serialport.volley.StringRequest;


/**
 * Created by Lkn on 2018/1/10.
 */

public class HttpUtils {
    protected final static String TCP_URL = "zxy.vpandian.com";
    public final static String TCP_IP = TCP_URL;
    protected final static int TCP_PRO = 1368;
    public final static int TCP_PRO_IP = TCP_PRO;
    //命令
    public final static int SERIAL_TYPE = 1; //获取序列号
    public final static int SERIAL_TYPE_5 = 5; //启动电机

    public final static int HTTP_STATUS = 0;

    public final static String HTTP_BASE = "http://hh.vpandian.com/api";
    public static String IMEI = "868575021770443";

    public static StringRequest getAppList(RequestListener<String> listener) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", IMEI);
        StringRequest request = new StringRequest(Request.Method.GET, HttpUtils.HTTP_BASE + "/goodscat.do?pjson=" + jsonObject.toString(),
                listener);
        return request;
    }

    public static StringRequest getLunbo(RequestListener<String> listener) {
        StringRequest request = new StringRequest(Request.Method.GET, HttpUtils.HTTP_BASE + "/lunbo.do",
                listener);
        return request;
    }

    public static StringRequest getOrder(RequestListener<String> listener, String pids) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", IMEI);
        jsonObject.put("pids", pids);
        jsonObject.put("shuoming", "");
        StringRequest request = new StringRequest(Request.Method.GET, HttpUtils.HTTP_BASE + "/orderadd.do?pjson=" + jsonObject.toString(),
                listener);
        return request;
    }
    public static StringRequest getOrder2(RequestListener<String> listener, String pids) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("oid", pids);
        StringRequest request = new StringRequest(Request.Method.GET, HttpUtils.HTTP_BASE + "/wxcodepay.do?pjson=" + jsonObject.toString(),
                listener);
        return request;
    }
}
