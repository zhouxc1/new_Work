package com.lenovo.lenovorobot_new.BaseClass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class BaseService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initService();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initServiceDate();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public abstract void initServiceDate();

	public abstract void initService();

}
