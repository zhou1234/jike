package com.jike.shanglv;



import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Activity_Scenery extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scenery);
		((ImageButton)findViewById(R.id.back_imgbtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		((ImageButton)findViewById(R.id.home_imgbtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Activity_Scenery");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Activity_Scenery"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

}
