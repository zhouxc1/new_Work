package com.lenovo.lenovorobot_new.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用来存储机器人的一些基本信息,目的是获取的时候方便,不会存在获取不到值得情况
 * 
 * @author Administrator
 * 
 */
public class SaveRobotInfoUtils {

	private Editor edit;
	private Log_Toast log_Toast;
	private SharedPreferences sharedPreferences;

	public SaveRobotInfoUtils(Context context) {
		sharedPreferences = context.getSharedPreferences("robotinfo",
				Context.MODE_PRIVATE);
		edit = sharedPreferences.edit();

		log_Toast = new Log_Toast(context);
	}

	public void saveInt(String key, int saveValue) {
		boolean commit = edit.putInt(key, saveValue).commit();
		if (commit) {
			log_Toast.Toast("保存成功", 0);
		} else {
			log_Toast.Toast("保存失败", 0);
		}
	}

	public void saveString(String key, String saveValue) {
		boolean commit = edit.putString(key, saveValue).commit();
		if (commit) {
			log_Toast.Toast("保存成功", 0);
		} else {
			log_Toast.Toast("保存失败", 0);
		}
	}

	public void saveBoolean(String key, boolean saveValue) {
		boolean commit = edit.putBoolean(key, saveValue).commit();
		if (commit) {
			log_Toast.Toast("保存成功", 0);
		} else {
			log_Toast.Toast("保存失败", 0);
		}
	}

	public int getInt(String key) {
		return sharedPreferences.getInt(key, -1);
	}

	public String getString(String key) {
		return sharedPreferences.getString(key, "");
	}

	public boolean getBoolean(String key) {
		return sharedPreferences.getBoolean(key, false);
	}
}
