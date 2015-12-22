package com.lenovo.lenovorobot_new.application;

import android.app.Application;
import android.content.Intent;

import com.iflytek.cloud.SpeechUtility;
import com.lenovo.lenovorobot_new.miervice.MIService;
import com.lenovo.lenovorobot_new.serverservice.ServerService;
import com.lenovo.lenovorobot_new.speechservice.SpeechService;

public class MyApplication extends Application {
	private Intent service;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		initService();

		initSpeech();

		super.onCreate();
	}

	private void initSpeech() {
		// 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用半角“,”分隔。
		// 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

		// 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

		SpeechUtility.createUtility(MyApplication.this, "appid=567904be");

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
