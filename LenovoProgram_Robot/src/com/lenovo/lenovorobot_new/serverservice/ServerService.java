package com.lenovo.lenovorobot_new.serverservice;

import android.os.Handler;
import android.os.Message;

import com.lenovo.lenovorobot_new.BaseClass.BaseService;
import com.lenovo.mi.plato.comm.MoTransport;

/**
 * 主要的任务是和服务器保持连接,发送和接收服务器相关信息,这是一个Service
 * 
 * @author Administrator
 * 
 */
public class ServerService extends BaseService {

	private AcceptServerMessage acceptServerMessageUtils;
	private SendServerMessage sendServerMessage;

	// 连接服务器对象
	private MoTransport mtServer;
	// 用来给外界使用的handler
	public static Handler handler;

	@Override
	public void initService() {
		mtServer = new MoTransport();
		acceptServerMessageUtils = new AcceptServerMessage(
				getApplicationContext(), mtServer);
		// 获取传输器对象
		sendServerMessage = new SendServerMessage(mtServer);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				
			}
		};
	}

	@Override
	public void initServiceDate() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (acceptServerMessageUtils != null) {
			acceptServerMessageUtils.stopServer();
		}
	}
}
