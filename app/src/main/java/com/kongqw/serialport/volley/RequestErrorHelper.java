package com.kongqw.serialport.volley;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.Map;

/**
 * volley error handler
 * @author YWL
 *
 */
public class RequestErrorHelper {
	public final static int SUCCESS = 1;
	public final static int GENERIC_ERROR = 0;
	public final static int TIMEOUT_ERROR = -1;
	public final static int SERVER_PROBLEM = -2;
	public final static int NETWORK_PROBLEM = -3;
	public final static int CREDENTIALLESS = -4;
	public final static int JSON_ERROR = -5;
	public final static int PARSE_ERROR = -6;
	public final static int IO_ERROR = -7;

	public int code;
	public String content;
	
	private RequestErrorHelper(int code, String content) {
		this.code = code;
		this.content = content;
	}
	
	/**
	 * Returns appropriate message which is to be displayed to the user 
     * against the specified error object.
	 * @param error
	 * @return
	 */
	public static RequestErrorHelper getMessage(Object error) {
		if (error instanceof ParseError) {
			return new RequestErrorHelper(PARSE_ERROR, "parse error");
		}else if (error instanceof TimeoutError) {
			return new RequestErrorHelper(TIMEOUT_ERROR, "timeout error");
		} else if (isServerProblem(error)) {
			return new RequestErrorHelper(SERVER_PROBLEM, handleServerError(error));
		} else if (isNetworkProblem(error)) {
			return new RequestErrorHelper(NETWORK_PROBLEM, "network error");
		}
		return new RequestErrorHelper(GENERIC_ERROR, "unknow error");
	}

	private static boolean isNetworkProblem(Object error) {
		return (error instanceof NetworkError) || (error instanceof NoConnectionError);
	}

	private static boolean isServerProblem(Object error) {
		return (error instanceof ServerError) || (error instanceof AuthFailureError);
	}

	/**
	 * Handles the server error, tries to determine whether to show a stock
	 * message or to show a message retrieved from the server.
	 * 
	 * @param err
	 * @param context
	 * @return
	 */
	private static String handleServerError(Object err) {
		VolleyError error = (VolleyError) err;

		NetworkResponse response = error.networkResponse;

		if (response != null) {
			switch (response.statusCode) {
			case 404:
			case 422:
			case 401:
				try {
					// server might return error like this { "error":
					// "Some error occured" }
					// Use "FastJson" to parse the result
					Map<String, String> result = JSON.parseObject(new String(response.data), new TypeReference<Map<String, String>>() {
					});
					if (result != null && result.containsKey("error")) {
						return result.get("error");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				// invalid request
				return error.getMessage();

			default:
				break;
			}
		}
		return "unknow error";
	}
}
