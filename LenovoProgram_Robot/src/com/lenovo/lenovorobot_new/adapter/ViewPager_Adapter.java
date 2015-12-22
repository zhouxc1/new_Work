package com.lenovo.lenovorobot_new.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.lenovo.lenovorobot_new.BaseClass.BaseView;

/**
 * ViewPager 的 adapter
 * 
 * @author Administrator
 * 
 */
public class ViewPager_Adapter extends PagerAdapter {
	private Context context;
	private ArrayList<BaseView> viewList;

	public ViewPager_Adapter(ArrayList<BaseView> viewList, Context context) {
		this.viewList = viewList;
		this.context = context;
		// 刷新当前的试图
		notifyDataSetChanged();
	}

	/**
	 * View 显示的个数
	 */
	@Override
	public int getCount() {
		if (viewList != null)
			viewList.size();
		return 0;
	}

	/**
	 * 返回当前显示的View
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		return super.instantiateItem(container, position);
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == obj;
	}

	/**
	 * 销毁当前的 View
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

}
