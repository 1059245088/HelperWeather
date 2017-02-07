package com.njcc.trover.weather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.njcc.trover.weather.R;

/**
 * 作者：Trover on 2017/2/6 13:45
 * 邮箱：1059245088@qq.com
 */
public class CircleTempView extends FrameLayout {

	private ImageView iv_temp;
	private TextView tv_temp;

	/**
	 * 初始化视图(加载布局，实例化控件)
	 *
	 * @param context
	 */
	private void initView(Context context) {
		//加载布局文件
		View.inflate(context, R.layout.custom_circle_temp, CircleTempView.this);
		iv_temp = (ImageView) findViewById(R.id.iv_temp);
		tv_temp = (TextView) findViewById(R.id.tv_temp);
	}

	/**
	 * 设置icon图标
	 *
	 * @param iconResId 图标id
	 */
	public void setTempIcon(int iconResId) {
		iv_temp.setImageResource(iconResId);
	}

	/**
	 * 设置温度文本
	 *
	 * @param tempValue 温度值
	 */
	public void setTempValue(String tempValue) {
		tv_temp.setText(tempValue);
	}

	public CircleTempView(Context context) {
		super(context);
		initView(context);
	}

	public CircleTempView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleTempView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
}
