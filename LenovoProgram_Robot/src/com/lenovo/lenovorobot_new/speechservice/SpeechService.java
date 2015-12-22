package com.lenovo.lenovorobot_new.speechservice;

import com.lenovo.lenovorobot_new.BaseClass.BaseService;

/**
 * 语音服务 语音的相关部分全部要经过这个Service
 * 
 * @author Administrator
 * 
 */
public class SpeechService extends BaseService {

	private SpeechWakeUtils speechWakeUtils;

	@Override
	public void initService() {

		speechWakeUtils = new SpeechWakeUtils(getApplicationContext());
	}

	@Override
	public void initServiceDate() {
		speechWakeUtils.startWake();
	}
}
