package com.lenovo.lenovorobot_new.miervice;

import android.os.Handler;
import android.os.Message;

import com.lenovo.lenovorobot_new.BaseClass.BaseService;

/**
 * 和底层交互的服务,只要是和底层相关的操作都要从这个service中经过
 * 
 * @author Administrator
 * 
 */
public class MIService extends BaseService {
	public static Handler handler;

	@Override
	public void initService() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

			}
		};
	}

	@Override
	public void initServiceDate() {

	}
}
