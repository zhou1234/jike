package com.jike.shanglv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;
import com.jike.shanglv.Enums.SPkeys;


/**
 *     class desc: �������� (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ���
 *     (2)�ǣ������GuideActivity���������MainActivity (3)3s��ִ��(2)����
 */
public class WelcomeActivity  extends Activity {
	boolean isFirstIn = false;
	SharedPreferences preferences;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// �ӳ�3��
	private static final long SPLASH_DELAY_MILLIS = 2000;
	Context mContext;
	/**
	 * Handler:��ת����ͬ����
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		mContext=this;
		init();
		((MyApplication)getApplication()).addActivity(this);
	}

	private void init() {
		MyApp mApp=new MyApp(getApplicationContext());	
		((ImageView)findViewById(R.id.welcome_iv)).setBackgroundResource((Integer) mApp.getHm().get(PackageKeys.WELCOME_DRAWABLE.getString()));
		// ��ȡSharedPreferences����Ҫ������
		// ʹ��SharedPreferences����¼�����ʹ�ô���
		preferences = getSharedPreferences(
				SPkeys.SPNAME.getString(), MODE_PRIVATE);
		
		try {
			isFirstIn = preferences.getBoolean(SPkeys.isFirstIn.getString()+mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode, false);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
		if (isFirstIn) {
			// ʹ��Handler��postDelayed������3���ִ����ת��MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			//			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			Message msg = new Message();
			msg.what=GO_GUIDE;
			mHandler.sendMessage(msg);
//			preferences.edit().putBoolean(SPkeys.isFirstIn.getString(), true).commit();
		}
	}

	private void goHome() {
		Intent intent =null;
//		if((new MyApp(WelcomeActivity.this).getHm().get(PackageKeys.PLATFORM.getString())==Platform.B2C)){
//			intent = new Intent(WelcomeActivity.this, MainActivity.class);
//		}else if((new MyApp(WelcomeActivity.this).getHm().get(PackageKeys.PLATFORM.getString())==Platform.B2B)){
//			intent = new Intent(WelcomeActivity.this, ActivityBMenuNew.class);
//		}
		intent = new Intent(WelcomeActivity.this, MainActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}
}
