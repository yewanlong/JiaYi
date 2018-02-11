package com.huahao.serialport.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenUtil {
	private static ScreenUtil screenUtil;
	private DisplayMetrics displayMetrics;
	private Display display;
	private Context context;

	public ScreenUtil(Context context) {
		displayMetrics = new DisplayMetrics();
		display = ((Activity) context).getWindowManager().getDefaultDisplay();
		display.getMetrics(displayMetrics);
		this.context = context.getApplicationContext();
	}
	public DisplayMetrics displayMetrics(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}
	public static ScreenUtil getInstance(Context context) {
		if (screenUtil == null) {
			screenUtil = new ScreenUtil(context);
		}
		return screenUtil;
	}

	public int getWidth() {
		return display.getWidth();
	}

	public int getDipWidth() {
		return (int) (display.getWidth() / displayMetrics.density);
	}

	public int getHeight() {
		return display.getHeight();
	}

	public int getDipHeight() {
		return (int) (display.getHeight() / displayMetrics.density);
	}

	public int px2dip(int px) {
		return (int) (px / displayMetrics.density);
	}

	public int dip2px(int dip) {
		return (int) (dip * displayMetrics.density);
	}

	public int sp2px(int spValue) {
		return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
	}
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
