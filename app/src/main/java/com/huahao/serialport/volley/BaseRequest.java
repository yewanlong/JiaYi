package com.huahao.serialport.volley;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.huahao.serialport.BuildConfig;
import com.huahao.serialport.InitApplication;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



/**
 * 请求基类
 *
 * @param <T>
 * @author YWL
 */
public abstract class BaseRequest<T> extends Request<T> {
    protected final RequestListener<T> listener;

    private Map<String, String> headers;

    private Map<String, String> params;

    private int what = 0;

    private String mRequestBody;
    protected static final String PROTOCOL_CHARSET = "utf-8";

    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=utf-8", PROTOCOL_CHARSET);

    public BaseRequest(int method, String url, RequestListener<T> listener) {
        super(method, url, listener);
        if (BuildConfig.DEBUG && InitApplication.getInstance().isDebugMode()) {
            VolleyLog.d("%s", url);
        }
        this.listener = listener;
    }

    /**
     * 此方法只用JsonOb
     *
     * @param method
     * @param url
     * @param jsonRequest
     * @param listener
     */
    public BaseRequest(int method, String url, JSONObject jsonRequest, RequestListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        mRequestBody = (jsonRequest == null) ? null : jsonRequest.toString();
        if (BuildConfig.DEBUG && InitApplication.getInstance().isDebugMode()) {
            VolleyLog.d("%s", url);
            VolleyLog.d("%s", jsonRequest);
        }
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public void putHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public void putHeader(String key, String value) {
        if (headers == null)
            headers = new HashMap<String, String>();
        headers.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers == null ? super.getHeaders() : headers;
    }

    public void putObjectParams(Map<String, Object> objectParams) {
        if (params == null)
            params = new HashMap<String, String>();
        Set<String> paramSet = objectParams.keySet();
        for (String key : paramSet) {
            String value = String.valueOf(objectParams.get(key));
            if (value == null)
                continue;
            params.put(key, String.valueOf(value));
        }
    }

    public void putParams(Map<String, String> params) {
        this.params = params;
    }

    public void putParams(String key, Object value) {
        if (params == null)
            params = new HashMap<String, String>();
        params.put(key, String.valueOf(value));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (BuildConfig.DEBUG && InitApplication.getInstance().isDebugMode()) {
            VolleyLog.d("%s", params == null ? "" : params.toString());
        }
        return params == null ? getParams() : params;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onSuccess(what, response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        RequestErrorHelper helper = RequestErrorHelper.getMessage(error);
        if (BuildConfig.DEBUG && InitApplication.getInstance().isDebugMode()) {
            VolleyLog.e("%s", helper.code + ": " + helper.content);
        }
        listener.onError(what, helper.code, helper.content);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (params != null) {
            mRequestBody = getParams().toString();
        }
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    public byte[] getPostBody() throws AuthFailureError {

        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }
}
