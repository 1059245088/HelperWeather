package com.njcc.trover.weather.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;


import com.njcc.trover.weather.R;
import com.njcc.trover.weather.activity.MainActivity;
import com.njcc.trover.weather.activity.SettingsActivity;
import com.njcc.trover.weather.bean.WeatherRealtime;
import com.njcc.trover.weather.db.dao.CityQueryDao;
import com.njcc.trover.weather.db.dao.WeatherDao;
import com.njcc.trover.weather.receiver.WeatherWidget;
import com.njcc.trover.weather.util.DateUtils;
import com.njcc.trover.weather.util.WeatherUtils;

/**
 * 作者：Trover on 2017/2/6 15:45
 * 邮箱：1059245088@qq.com
 */
public class DeskWidget41Service extends Service {

	public static final String ACTION_UPDATE_WIDGET_WEATHER = "com.njcc.trover.weather.action_update_widget_weather";
	public static final String ACTION_UPDATE_WIDGET_TEXT_COLOR = "com.njcc.trover.weather.action_update_widget_text_color";
	public static final String ACTION_SET_WIDGET_CLICK_LISTENER = "com.njcc.trover.weather.action_set_widget_click_listener";

	private AppWidgetManager widgetManager;
	private WeatherDao dao;
	private WidgetUpdateReceiver widgetUpdateReceiver;
	private SharedPreferences mSP;
	private RemoteViews remoteViews;

	private class WidgetUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 接收到更新Widget的广播就更新WidgetUI
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_TIME_TICK) || action.equals(Intent.ACTION_TIME_CHANGED)) {
				// 更新时间
				updateWidgetTime();
			} else if (action.equals(ACTION_UPDATE_WIDGET_WEATHER)) {
				// 更新插件的天气信息
				updateWidgetWeather();
				updateWidgetTime();
			} else if (action.equals(ACTION_UPDATE_WIDGET_TEXT_COLOR)) {
				// 更新插件的颜色
				updateWidgetTextColor();
			} else if (action.equals(ACTION_SET_WIDGET_CLICK_LISTENER)) {
				setClickListener();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mSP = getSharedPreferences(getString(R.string.config), MODE_PRIVATE);
		dao = new WeatherDao(DeskWidget41Service.this);
		widgetManager = AppWidgetManager.getInstance(DeskWidget41Service.this);
		setClickListener();
		widgetUpdateReceiver = new WidgetUpdateReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK); // 时间的流逝
		intentFilter.addAction(Intent.ACTION_TIME_CHANGED); // 时间被改变，人为
		intentFilter.addAction(ACTION_UPDATE_WIDGET_WEATHER); // 监听更新桌面天气信息的广播
		intentFilter.addAction(ACTION_UPDATE_WIDGET_TEXT_COLOR); // 监听更新桌面插件颜色的广播
		intentFilter.addAction(ACTION_SET_WIDGET_CLICK_LISTENER); // 监听设置桌面插件点击监听的广播
		registerReceiver(widgetUpdateReceiver, intentFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		updateWidgetWeather();
		updateWidgetTime();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 设置插件点击事件
	 */
	private void setClickListener() {
		ComponentName provider = new ComponentName(DeskWidget41Service.this, WeatherWidget.class);
		RemoteViews widgetView = getRemoteViews();
		// 设置widget的点击监听
		PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) SystemClock.uptimeMillis(),
				new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		widgetView.setOnClickPendingIntent(R.id.ll_realTimeArea, pendingIntent);

		pendingIntent = PendingIntent.getActivity(this, (int) SystemClock.uptimeMillis(),
				new Intent(AlarmClock.ACTION_SHOW_ALARMS), PendingIntent.FLAG_UPDATE_CURRENT);
		widgetView.setOnClickPendingIntent(R.id.ll_timeArea, pendingIntent);
		widgetManager.updateAppWidget(provider, widgetView);
	}

	/**
	 * 更新桌面插件UI
	 */
	private void updateWidgetWeather() {
		ComponentName provider = new ComponentName(DeskWidget41Service.this, WeatherWidget.class);
		RemoteViews widgetView = getRemoteViews();
		// 获取设置的部件Text颜色值
		int widgetTextColor = mSP.getInt(getString(R.string.widget_text_color), SettingsActivity.WIDGET_TEXT_COLOR_DEFAULT);
		String weatherId = dao.getMainAreaId();
		widgetView.setViewVisibility(R.id.rl_init, View.INVISIBLE);
		if (!TextUtils.isEmpty(weatherId)) {
			// 有城市
			widgetView.setViewVisibility(R.id.rl_noCity, View.INVISIBLE);
			if (dao.haveCache(weatherId)) {
				// 城市有缓存
				widgetView.setViewVisibility(R.id.rl_noData, View.INVISIBLE);
				widgetView.setViewVisibility(R.id.rl_dataArea, View.VISIBLE);
				WeatherRealtime realtime = dao.getDataFromRealtime(weatherId);
				String type = realtime.getWeather();
				String temp = realtime.getWendu();
				if (widgetTextColor == 1) {
					widgetView.setImageViewResource(R.id.iv_typeIcon, WeatherUtils.getBlackIconIdByTypeName(type));
				} else {
					widgetView.setImageViewResource(R.id.iv_typeIcon, WeatherUtils.getWhiteIconIdByTypeName(type));
				}
				StringBuilder sb = new StringBuilder();
				sb.append(CityQueryDao.getAreaNameByWeatherId(weatherId));
				sb.append("  |  ");
				sb.append(type + "  " + temp + "°C");
				widgetView.setTextViewText(R.id.tv_areaAndTempAndTemp, sb.toString());
				widgetView.setInt(R.id.tv_areaAndTempAndTemp, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
			} else {
				// 城市无缓存
				widgetView.setViewVisibility(R.id.rl_noData, View.VISIBLE);
				widgetView.setViewVisibility(R.id.rl_dataArea, View.INVISIBLE);
				widgetView.setInt(R.id.tv_noData, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
			}
		} else {
			// 无城市
			widgetView.setViewVisibility(R.id.rl_noCity, View.VISIBLE);
			widgetView.setViewVisibility(R.id.rl_noData, View.INVISIBLE);
			widgetView.setViewVisibility(R.id.rl_dataArea, View.INVISIBLE);
			widgetView.setInt(R.id.tv_noCity, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
		}
		// 更新
		widgetManager.updateAppWidget(provider, widgetView);
	}

	/**
	 * 更新桌面插件时间
	 */
	private void updateWidgetTime() {
		ComponentName provider = new ComponentName(DeskWidget41Service.this, WeatherWidget.class);
		RemoteViews widgetView = getRemoteViews();
		// 获取设置的部件Text颜色值
		int widgetTextColor = mSP.getInt(getString(R.string.widget_text_color), SettingsActivity.WIDGET_TEXT_COLOR_DEFAULT);
		widgetView.setTextViewText(R.id.tv_systemTime, DateUtils.getSimpleSystemTime());
		widgetView.setInt(R.id.tv_systemTime, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.getSimpleSystemDate());
		sb.append("  |  ");
		sb.append(DateUtils.getSimpleLunarToday());
		widgetView.setTextViewText(R.id.tv_systemDate, sb.toString());
		widgetView.setInt(R.id.tv_systemDate, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
		// 更新
		widgetManager.updateAppWidget(provider, widgetView);
	}

	/**
	 * 更新桌面插件颜色
	 */
	private void updateWidgetTextColor() {
		ComponentName provider = new ComponentName(DeskWidget41Service.this, WeatherWidget.class);
		RemoteViews widgetView = getRemoteViews();
		// 获取设置的部件Text颜色值
		int widgetTextColor = mSP.getInt(getString(R.string.widget_text_color), SettingsActivity.WIDGET_TEXT_COLOR_DEFAULT);
		String weatherId = dao.getMainAreaId();
		if (!TextUtils.isEmpty(weatherId)) {
			// 有城市
			widgetView.setViewVisibility(R.id.rl_noCity, View.INVISIBLE);
			if (dao.haveCache(weatherId)) {
				// 城市有缓存
				widgetView.setViewVisibility(R.id.rl_noData, View.INVISIBLE);
				widgetView.setViewVisibility(R.id.rl_dataArea, View.VISIBLE);
				WeatherRealtime realtime = dao.getDataFromRealtime(weatherId);
				String type = realtime.getWeather();
				if (widgetTextColor == 1) {
					widgetView.setImageViewResource(R.id.iv_typeIcon, WeatherUtils.getBlackIconIdByTypeName(type));
				} else {
					widgetView.setImageViewResource(R.id.iv_typeIcon, WeatherUtils.getWhiteIconIdByTypeName(type));
				}
				widgetView.setInt(R.id.tv_areaAndTempAndTemp, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
				// 更新时间和日期的颜色
				widgetView.setInt(R.id.tv_systemTime, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
				widgetView.setInt(R.id.tv_systemDate, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
			} else {
				// 城市无缓存
				widgetView.setViewVisibility(R.id.rl_noData, View.VISIBLE);
				widgetView.setViewVisibility(R.id.rl_dataArea, View.INVISIBLE);
				widgetView.setInt(R.id.tv_noData, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
			}
		} else {
			// 无城市
			widgetView.setViewVisibility(R.id.rl_noCity, View.VISIBLE);
			widgetView.setViewVisibility(R.id.rl_noData, View.INVISIBLE);
			widgetView.setViewVisibility(R.id.rl_dataArea, View.INVISIBLE);
			widgetView.setInt(R.id.tv_noCity, "setTextColor", getResources().getColor(SettingsActivity.mWidgetTextColorIds[widgetTextColor]));
		}
		// 更新
		widgetManager.updateAppWidget(provider, widgetView);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (widgetUpdateReceiver != null) {
			unregisterReceiver(widgetUpdateReceiver);
		}
	}

	public RemoteViews getRemoteViews() {
		if (remoteViews == null) {
			remoteViews = new RemoteViews(getPackageName(), R.layout.widget_desk_weather_41);
		}
		return remoteViews;
	}
}
