package com.lenovo.lenovorobot_new.utils;

import android.content.Context;
import android.util.Log;

/**
 * 自定义的Log和Toast
 * 
 * @author ZXC
 * 
 */
public class Log_Toast {
	private boolean isLog = true;
	private boolean isToast = true;
	private Context context;

	public Log_Toast(Context context) {
		this.context = context;
	}

	public void i(String TAG, String content) {
		if (isLog)
			Log.i(TAG, content);
	}

	public void Toast(String showContent, int time) {
		if (isToast)
			android.widget.Toast.makeText(context, showContent, time).show();
	}
}
