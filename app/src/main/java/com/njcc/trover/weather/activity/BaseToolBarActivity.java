package com.njcc.trover.weather.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.njcc.trover.weather.R;

/**
 * 作者：Trover on 2017/2/6 11:28
 * 邮箱：1059245088@qq.com
 */
public abstract class BaseToolBarActivity extends AppCompatActivity {

	public Toolbar toolbar;
	/**
	 * 开启状态栏后的颜色(默认为colorPrimary)
	 */
	private int mTranslucentBarColor = R.color.colorPrimary;
	/**
	 * 是否开启透明状态栏(默认关闭)
	 */
	private boolean mEnableTranslucentBar = false;

	public void setTranslucentBarColor(int mTranslucentBarColor) {
		this.mTranslucentBarColor = mTranslucentBarColor;
	}

	public void setEnableTranslucentBar(boolean mEnableTranslucentBar) {
		this.mEnableTranslucentBar = mEnableTranslucentBar;
	}

	/**
	 * 布局文件接口
	 *
	 * @return 布局文件资源id
	 */
	public abstract int createContentView();

	/**
	 * 初始化相关配置，如透明状态栏开关以及颜色控制、手势开关......
	 */
	public abstract void initConfiguration();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initConfiguration();
		super.onCreate(savedInstanceState);
		setContentView(createContentView());
		translucentBar(mEnableTranslucentBar, mTranslucentBarColor);
		initToolbar();
	}

	private void initToolbar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				onBackPressed();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 状态栏变色处理 4.4以上
	 *
	 * @param color 状态栏颜色
	 *              4.4状态栏显示为改颜色
	 *              5.0自动会加上半透明效果
	 */
	public void translucentBar(boolean enableTranslucentBar, int color) {
		if (!enableTranslucentBar) {
			return;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 获取状态栏高度
			int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
			int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
			// 绘制一个和状态栏一样高的矩形，并添加到视图中
			View rectView = new View(this);
			LinearLayout.LayoutParams params
					= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
			rectView.setLayoutParams(params);
			rectView.setBackgroundColor(getResources().getColor(color));
			// 添加矩形View到布局中
			ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
			decorView.addView(rectView);
			// 设置根布局的参数
			ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setFitsSystemWindows(true);
			rootView.setClipToPadding(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
