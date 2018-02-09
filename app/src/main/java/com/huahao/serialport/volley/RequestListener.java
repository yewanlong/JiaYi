package com.huahao.serialport.volley;


import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

/**
 * 请求监听类
 * @author YWL
 *
 * @param <T>
 */
public abstract class RequestListener<T> implements Listener<T>,ErrorListener {

    public RequestListener() {
    }
    
    @Override
	public void onResponse(T response) {
		VolleyLog.e("%s", "");
	}

	@Override
    public final void onErrorResponse(VolleyError error) {
		VolleyLog.e("%s", error.getMessage());
    }

    /**
     * 成功
     * @param response
     */
    protected abstract void onSuccess(int what, T response);

    /**
     * 错误
     * @param code TODO
     * @param message TODO
     * @param error
     */
    protected abstract void onError(int what, int code, String message);
}
