package com.lenovo.lenovorobot_new.utils;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断手机的当前的网络状态
 * 
 * @author Administrator
 * 
 */
public class NetUtils {
	/**
	 * 判断用户的手机网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetwork(Context context) {
		// 区分使用的联网设备：WIFI、基站
		// 如果利用基站上网，wap还是net
		// 区分办法：判断代理的ip内容，如果非空使用wap
		// 如果两种方式都不能联网，提示用户无网络

		boolean isWifi = isWifiConnection(context);
		boolean isBaseState = isBaseStateConnection(context);

		if (!isWifi && !isBaseState) {
			return false;
		}

		if (isBaseState) {
			String ip = android.net.Proxy.getDefaultHost();
			if (StringUtils.isNotBlank(ip)) {
			}
		}
		return true;
	}

	/**
	 * 判断基站是否处理可连接状态
	 * 
	 * @param context
	 * @return
	 */

	private static boolean isBaseStateConnection(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * 判断WIFI是否处理可连接状态
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isWifiConnection(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}
}
