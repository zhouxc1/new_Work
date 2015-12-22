package com.lenovo.lenovorobot_new.application;

import android.app.Application;
import android.content.Intent;

import com.lenovo.lenovorobot_new.miervice.MIService;
import com.lenovo.lenovorobot_new.serverservice.ServerService;
import com.lenovo.lenovorobot_new.speechservice.SpeechService;

public class MyApplication extends Application {
	private Intent service;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initService();
	}

	/**
	 * 开启Service的地方,如果网络正常的话开启服务器Service,如果不正常将不会开启
	 */
	private void initService() {
		// 语音服务
		service = new Intent(this, SpeechService.class);
		startService(service);
		// 服务器服务
		service = new Intent(this, ServerService.class);
		startService(service);
		// 底层服务
		service = new Intent(this, MIService.class);
		startService(service);
	}
}
