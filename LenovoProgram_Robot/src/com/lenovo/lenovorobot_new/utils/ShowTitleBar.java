package com.lenovo.lenovorobot_new.utils;

import com.lenovo.lenovorobot_new.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ShowTitleBar {
	private Context context;

	public ShowTitleBar(Context context) {
		this.context = context;
	}

	public void showBar() {

		// Notification notification = new Notification(R.drawable.ic_launcher,
		// "MService", System.currentTimeMillis());
		// Intent intent = new Intent(context, MainActivity.class);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		// intent, 0);
		// notification.setLatestEventInfo(context, "MService", "is running",
		// pendingIntent);
		// ((MyService) context).startForeground(1, notification);
	}
}
