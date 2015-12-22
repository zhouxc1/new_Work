package com.lenovo.lenovorobot_new.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lenovo.lenovorobot_new.R;
import com.lenovo.lenovorobot_new.BaseClass.BaseView;
import com.lenovo.lenovorobot_new.adapter.ViewPager_Adapter;
import com.lenovo.lenovorobot_new.childView.FriendHomeView;
import com.lenovo.lenovorobot_new.childView.SettingView;
import com.lenovo.lenovorobot_new.childView.ShowFuncationView;

/**
 * 欢迎界面的展示,这个界面主要干的事情是,判断一下当前的网络环境,开启主要的几个Service 还有解释初始化界面的展示 一个activity
 * 管理着整个界面的展示
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends Activity implements OnCheckedChangeListener {

	private ViewPager viewPager;
	private RadioGroup radioGroup;
	private ArrayList<BaseView> viewList;
	private BaseView friendHomeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initDate();

	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
	}

	/**
	 * 初始化数据
	 */
	private void initDate() {
		// 初始化ViewPager的显示界面
		viewList = initViewPagerChildView();
		viewPager.setAdapter(new ViewPager_Adapter(viewList, this));

		radioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

	/**
	 * 如果之后,添加界面的时候,其他地方的代码不用动,只需要按照以下方式进行添加即可, 每个程序员开发自己的界面 BaseView
	 * 这个类就是一个可以扩展的类,之后所有显示的View都将继承这个类
	 * 
	 * @return
	 */
	private ArrayList<BaseView> initViewPagerChildView() {
		if (viewList == null)
			viewList = new ArrayList<BaseView>();

		friendHomeView = new FriendHomeView(this);
		viewList.add(friendHomeView);

		friendHomeView = new SettingView(this);
		viewList.add(friendHomeView);

		friendHomeView = new ShowFuncationView(this);
		viewList.add(friendHomeView);

		return viewList;
	}
}
