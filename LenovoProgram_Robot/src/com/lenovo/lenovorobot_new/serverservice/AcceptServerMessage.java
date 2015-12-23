package com.lenovo.lenovorobot_new.serverservice;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.lenovo.lenovorobot_new.bin.ServerAddressBin;
import com.lenovo.lenovorobot_new.utils.AcceptOpenServerPackageUtils;
import com.lenovo.lenovorobot_new.utils.FileLoadUtils;
import com.lenovo.lenovorobot_new.utils.FileLoadUtils.FileLoadListener;
import com.lenovo.lenovorobot_new.utils.Log_Toast;
import com.lenovo.lenovorobot_new.utils.NetUtils;
import com.lenovo.mi.plato.comm.ITransportCallback;
import com.lenovo.mi.plato.comm.MoPacket;
import com.lenovo.mi.plato.comm.MoTransport;
import com.lenovo.mi.plato.comm.TransportStatus;

/**
 * 用来接收服务器发送过来的消息的线程
 * 
 * @author Administrator
 * 
 */
public class AcceptServerMessage implements FileLoadListener,
		ITransportCallback {
	private static final String TAG = "AcceptServerMessageUtils";
	private boolean isCheckNetwork;
	private ServerAddressBin fromJson;
	private Log_Toast log_Toast;

	// 用来控制连接线程的标记
	private boolean isStartConnect = true;
	private MoPacket mp;
	private Timer timer;
	private TimerTask task;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int key = msg.what;
			switch (key) {
			case 1:
				log_Toast.Toast("服务器连接成功", 0);
				// 服务器一旦是连接成功的话,就必须向服务器发送心跳,告诉服务器我当前是在线状态的
				sendHeghtFrame();
				break;
			case 2:
				MoPacket pack = (MoPacket) msg.obj;
				if (acceptServerPackageUtils != null) {
					acceptServerPackageUtils.openPackage(pack);
				}
				break;
			case 3:
				log_Toast.Toast("服务器连接超时,正在尝试重新连接", 0);
				break;
			case 4:
				log_Toast.Toast("handler :服务器被强制中断", 0);
				break;
			}
		};
	};
	private Gson gson;
	private AcceptOpenServerPackageUtils acceptServerPackageUtils;
	private MoTransport mtServer;

	public AcceptServerMessage(Context context, MoTransport mtServer) {
		/*
		 * 判断一下当前的网络状态是不是可以使用的,如果是可以使用的话在连接服务器,如果是不可以使用的话 就没有必要在去连接服务器了
		 */
		this.mtServer = mtServer;
		isCheckNetwork = NetUtils.checkNetwork(context);
		FileLoadUtils fileLoadUtilsInfo = FileLoadUtils.getFileLoadUtilsInfo();
		// 加载服务器配置信息,写在文件当中,更换很方便
		fileLoadUtilsInfo.loadFile(context, "server");
		fileLoadUtilsInfo.setOnFileLoadListenerInfo(this);

		log_Toast = new Log_Toast(context);

		gson = new Gson();

		log_Toast.Toast("AcceptServerMessageUtils", 0);

		acceptServerPackageUtils = new AcceptOpenServerPackageUtils(context);
	}

	// public MoTransport getMoTransportInfo() {
	// return mtServer;
	// }

	public void stopServer() {
		if (mtServer != null) {
			mtServer.close();
		}
		isStartConnect = false;
		log_Toast.Toast("关闭和服务器的连接", 0);
	}

	/**
	 * 服务器配置信息加载成功以后,在开启连接服务器的程序,和服务器相连接
	 */
	@Override
	public void setFileResult(String result) {
		fromJson = gson.fromJson(result, ServerAddressBin.class);
		// 开始连接
		if (isCheckNetwork) {
			// 如果是,资源已经加载好了就开始连接服务器
			new ServerThread(fromJson.serverIP.trim(), fromJson.serverPort)
					.start();
		} else {
			log_Toast.Toast("当前无网络状态,请检查网络", 0);
		}
	}

	@Override
	public void setError(int code) {
		log_Toast.Toast("数据加载错误", 0);
	}

	/**
	 * 连接服务器的线程
	 * 
	 * @author Administrator
	 * 
	 */
	class ServerThread extends Thread {

		private String ip;
		private int port;

		public ServerThread(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}

		@Override
		public void run() {
			while (isStartConnect) {
				boolean isConnect = false;

				long time_1 = System.currentTimeMillis();
				while (true) {
					log_Toast.i(TAG, "ip " + this.ip + "   port " + this.port
							+ "   isConnect  " + isConnect);
					// 连接服务器
					isConnect = mtServer.connect(this.ip, this.port);
					SystemClock.sleep(2000);

					long time_2 = System.currentTimeMillis();
					if (isConnect) {
						handler.sendEmptyMessage(1);
						break;
					}
					if ((time_2 - time_1) > 10000) {
						handler.sendEmptyMessage(3);
						time_1 = System.currentTimeMillis();
					}
				}
				if (isConnect) {
					// 设置回调
					mtServer.setCallBack(AcceptServerMessage.this);

					while (isConnect && mtServer != null) {
						mtServer.recvPacket();
						SystemClock.sleep(50);
						if (mtServer.getStatus() == TransportStatus.DISCONNECTED) {
							isConnect = false;
							handler.sendEmptyMessage(4);
							break;
						}
					}
				}
				SystemClock.sleep(1000);
			}
		}
	}

	/**
	 * 机器人端给服务器发送心跳帧
	 */
	private void sendHeghtFrame() {
		// 开始发送心跳帧
		if (timer == null) {
			timer = new Timer();
		}
		task = new TimerTask() {
			@Override
			public void run() {
				if (mtServer.getStatus() == TransportStatus.CONNECTED) {
					if (mp == null) {
						mp = new MoPacket();
					}
					mp.pushInt32(1);

					mp.pushInt32(0);

					mp.pushInt32(7);

					mp.pushInt32(0);

					mtServer.sendPacket(mp);

					mp = null;
				}
			}
		};
		timer.schedule(task, 0, 1000);
	}

	@Override
	public void onConnected(MoTransport pack) {

	}

	@Override
	public void onDestroy(MoTransport arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnecte(MoTransport arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetError(MoTransport arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPacket(MoTransport arg0, MoPacket pack) {
		log_Toast.i(TAG, "onConnected");
		// 这个是主要的方法
		Message msg = new Message();
		msg.what = 2;
		msg.obj = pack;
		handler.sendMessage(msg);
	}
}
