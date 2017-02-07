package com.njcc.trover.weather.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 作者：Trover on 2017/2/6 15:27
 * 邮箱：1059245088@qq.com
 */
public class VersionUtils {
	/**
	 * 获取当前App版本号:versionName
	 *
	 * @return 返回当前App的版本号
	 */
	public static String getVersionName(Context context) {
		// 得到包管理器
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
}
