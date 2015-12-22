package com.lenovo.lenovorobot_new.BaseClass;

import android.content.Context;
import android.view.View;

public abstract class BaseView {
	private Context context;
	private View rootView;

	public BaseView(Context context) {
		this.context = context;
		rootView = getRootView();
		getData();
	}

	public abstract void getData();

	public abstract View getRootView();
}
