package com.njcc.trover.weather.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Trover on 2017/2/6 15:30
 * 邮箱：1059245088@qq.com
 */
public class MyPagerAdapter extends PagerAdapter {
	
	
	private List<View> listViews;

	public MyPagerAdapter(ArrayList<View> mListViews) {
		super();
		this.listViews = mListViews;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(listViews.get(position));
		return listViews.get(position);
	}

	@Override
	public int getCount() {
		return listViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listViews.get(position));
	}
}
