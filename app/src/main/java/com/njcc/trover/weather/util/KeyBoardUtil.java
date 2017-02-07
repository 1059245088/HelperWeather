package com.njcc.trover.weather.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * 作者：Trover on 2017/2/6 16:23
 * 邮箱：1059245088@qq.com
 */
public class KeyBoardUtil {

	/**
	 * 如果软键盘显示则收起软键盘
	 *
	 * @param activity Activity实例
	 */
	public static void hintKeyBoard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
		if (imm.isActive() && activity.getCurrentFocus() != null) {
			if (activity.getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
}
