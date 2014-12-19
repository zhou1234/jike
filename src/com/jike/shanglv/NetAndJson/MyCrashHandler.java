package com.jike.shanglv.NetAndJson;

import java.lang.Thread.UncaughtExceptionHandler;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;

public class MyCrashHandler implements UncaughtExceptionHandler {
	// 需求是 整个应用程序 只有一个 MyCrash-Handler
	private static MyCrashHandler myCrashHandler;
	private Context context;

	// 1.私有化构造方法
	private MyCrashHandler() {

	}

	public static synchronized MyCrashHandler getInstance() {
		if (myCrashHandler != null) {
			return myCrashHandler;
		} else {
			myCrashHandler = new MyCrashHandler();
			return myCrashHandler;
		}
	}

	public void init(Context context) {
		this.context = context;
	}

	public void uncaughtException(Thread arg0, Throwable arg1) {
		System.out.println("程序挂掉了 ");
		MobclickAgent.reportError(context, arg1.toString());
		// 干掉当前的程序
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}