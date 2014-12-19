package com.jike.shanglv;

import java.util.LinkedList;
import java.util.List;

import com.jike.shanglv.NetAndJson.MyCrashHandler;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

//		MyCrashHandler handler = MyCrashHandler.getInstance();
//		handler.init(getApplicationContext());
//		Thread.setDefaultUncaughtExceptionHandler(handler);

	}

	private String orderID;
	private int code;
	private Boolean hasCheckedUpdate = false;

	public Boolean getHasCheckedUpdate() {
		return hasCheckedUpdate;
	}

	public void setHasCheckedUpdate(Boolean hasCheckedUpdate) {
		this.hasCheckedUpdate = hasCheckedUpdate;
	}

	// ��¼Activity�б������˳�
	private List<Activity> activityList = new LinkedList<Activity>();

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		activityList.clear();
	}

	// ���ڻ�Ʊ��ѯ�������������������
	private String startcity_code = "", arrivecity_code = "";

	public String getStartcity_code() {
		return startcity_code;
	}

	public void setStartcity_code(String startcity_code) {
		this.startcity_code = startcity_code;
	}

	public String getArrivecity_code() {
		return arrivecity_code;
	}

	public void setArrivecity_code(String arrivecity_code) {
		this.arrivecity_code = arrivecity_code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
