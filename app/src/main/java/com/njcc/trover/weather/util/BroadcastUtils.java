package com.njcc.trover.weather.util;

import android.content.Context;
import android.content.Intent;

import com.njcc.trover.weather.activity.MainActivity;
import com.njcc.trover.weather.service.DeskWidget41Service;
import com.njcc.trover.weather.service.NotificationService;

/**
 * 作者：Trover on 2017/2/6 15:44
 * 邮箱：1059245088@qq.com
 */
public class BroadcastUtils {
	public static void sendUpdateWidgetWeatherBroadcast(Context context) {
		context.sendBroadcast(new Intent(DeskWidget41Service.ACTION_UPDATE_WIDGET_WEATHER));
	}

	public static void sendUpdateWidgetTextColorBroadcast(Context context) {
		context.sendBroadcast(new Intent(DeskWidget41Service.ACTION_UPDATE_WIDGET_TEXT_COLOR));
	}

	public static void sendNotificationBroadcast(Context context) {
		context.sendBroadcast(new Intent(NotificationService.ACTION_SHOW_NOTICATION));
	}

	public static void sendShowHomeDataBroadcast(Context context) {
		context.sendBroadcast(new Intent(MainActivity.ACTION_SHOWDATA));
	}

	public static void sendSetWidgetClickListenerBroadcast(Context context) {
		context.sendBroadcast(new Intent(DeskWidget41Service.ACTION_SET_WIDGET_CLICK_LISTENER));
	}

}
