package com.jike.shanglv.NetAndJson;

import java.lang.Thread.UncaughtExceptionHandler;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;

public class MyCrashHandler implements UncaughtExceptionHandler {
	// ������ ����Ӧ�ó��� ֻ��һ�� MyCrash-Handler
	private static MyCrashHandler myCrashHandler;
	private Context context;

	// 1.˽�л����췽��
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
		System.out.println("����ҵ��� ");
		MobclickAgent.reportError(context, arg1.toString());
		// �ɵ���ǰ�ĳ���
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}