package com.lenovo.lenovorobot_new.serverservice;

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
	// 获得传输器对象
	private MoTransport moTransportInfo;
	private SendServerMessage sendServerMessage;

	@Override
	public void initService() {
		acceptServerMessageUtils = new AcceptServerMessage(
				getApplicationContext());
		// 获取传输器对象
		moTransportInfo = acceptServerMessageUtils.getMoTransportInfo();
		sendServerMessage = new SendServerMessage(moTransportInfo);
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
