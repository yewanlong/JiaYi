package com.huahao.serialport.utils;

import android.widget.Toast;

import com.huahao.serialport.InitApplication;


public class VToast {
	private static Toast toast;

	private VToast() {

	}

	/**
	 *短时间显示toast,在旧的toast未消失之前，新的不会产生
	 *
	 */
	public static void showShort(CharSequence message) {
		if (toast == null) {
			toast = Toast.makeText(InitApplication.applicationContext, message, Toast.LENGTH_SHORT);
		} else {
			toast.setText(message);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}

	/**
	 * 短时间显示toast,在旧的toast未消失之前，新的不会产生
	 *
	 */
	public static void showShort(int message) {
		if (toast == null) {
			toast = Toast.makeText(InitApplication.applicationContext, message, Toast.LENGTH_SHORT);
		} else {
			toast.setText(InitApplication.applicationContext.getResources().getString(message));
			toast.setDuration(Toast.LENGTH_SHORT);
		}
	 	toast.show();
	}

	/**
	 * 长时间显示toast,在旧的toast未消失之前，新的不会产生
	 *
	 */
	public static void showLong(int message) {
		if (toast == null) {
			toast = Toast.makeText(InitApplication.applicationContext, message, Toast.LENGTH_LONG);
		} else {
			toast.setText(InitApplication.applicationContext.getResources().getString(message));
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.show();
	}

	/**
	 * 长时间显示toast,在旧的toast未消失之前，新的不会产生
	 *
	 */
	public static void showLong(CharSequence message) {
		if (toast == null) {
			toast = Toast.makeText(InitApplication.applicationContext, message, Toast.LENGTH_LONG);
		} else {
			toast.setText(message);
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.show();
	}
}
