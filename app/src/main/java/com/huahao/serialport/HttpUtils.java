package com.huahao.serialport;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.huahao.serialport.volley.RequestListener;
import com.huahao.serialport.volley.StringRequest;

import java.util.Date;


/**
 * Created by Lkn on 2018/1/10.
 */

public class HttpUtils {
    protected final static String TCP_URL = "zxy.vpandian.com";
    //    protected final static String TCP_URL = "104.238.184.237";
    public final static String TCP_IP = TCP_URL;
    protected final static int TCP_PRO = 1368;
//    protected final static int TCP_PRO = 8080;

    public final static int TCP_PRO_IP = TCP_PRO;

    public final static int SERIAL_TYPE_5 = 5; //启动电机
    public final static int SERIAL_TYPE_4 = 4; //查看电机是否异常
    public final static int SERIAL_TYPE_3 = 3; //poll

    public final static int HTTP_STATUS = 0;

    public final static String HTTP_BASE = "http://hh.vpandian.com/api";
    public static String IMEI = "868575021770443";

    public static String getCheckIn(long msgId, String imei) {
        return "Action=CheckIn&Imei=" + imei + "&MsgId=" + msgId + "&Timer="
                + new Date().getTime() + "&devicefrom=2";
    }

    public static String getCSQ(long msgId, String imei) {
        return "Action=CSQ&Imei=" + imei + "&MsgId=" + msgId + "&Timer="
                + new Date().getTime();
    }

    public static String getDelive(String imei, int Result, int ChannelIndex, String SaleId, int AlarmCode) {
        return "Action=Deliver&Imei=" + imei + "&MsgId=1" + "&Timer="
                + new Date().getTime() + "&Result=" + Result + "&ChannelIndex=" + ChannelIndex + "&SaleId=" + SaleId + "&AlarmCode=" + AlarmCode;
    }

    public static String getChannelStatus(String imei, String ChannelStatus) {
        return "MsgId=1&Imei=" + imei + "&Action=ChannelStatus&Timer=" + new Date().getTime() + "&Index=0&ChannelStatus=" + ChannelStatus;
    }

    public static StringRequest getAppList(RequestListener<String> listener) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", IMEI);
        StringRequest request = new StringRequest(Request.Method.GET, HttpUtils.HTTP_BASE + "/goodsCatList.do?pjson=" + jsonObject.toString(),
                listener);
        return request;
    }

    public static StringRequest getUpdate(RequestListener<String> listener, int code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", code);
        StringRequest request = new StringRequest(Request.Method.GET, HttpUtils.HTTP_BASE + "/updateApp.do?pjson=" + jsonObject.toString(),
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
