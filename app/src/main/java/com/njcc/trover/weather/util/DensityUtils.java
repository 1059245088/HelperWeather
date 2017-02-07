package com.njcc.trover.weather.util;

import android.content.Context;

/**
 * 作者：Trover on 2017/2/6 17:22
 * 邮箱：1059245088@qq.com
 */
public class DensityUtils {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
