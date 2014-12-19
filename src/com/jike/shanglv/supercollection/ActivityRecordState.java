package com.jike.shanglv.supercollection;


import com.jike.shanglv.MyApplication;
import com.jike.shanglv.R;
import com.jike.shanglv.NetAndJson.JSONHelper;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityRecordState extends Activity {

	protected static final String RECORDINFO = "RECORDINFO";
	private Record record;
	private ImageView state_iv;
	private TextView time1_tv, time1_name_tv, time2_tv, time2_name_tv,
			time3_tv, time3_name_tv;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_state_detail);
		((MyApplication) getApplication()).addActivity(this);
		((ImageButton) findViewById(R.id.back_imgbtn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		state_iv = (ImageView) findViewById(R.id.state_iv);
		time1_tv = (TextView) findViewById(R.id.time1_tv);
		time1_name_tv = (TextView) findViewById(R.id.time1_name_tv);
		time2_tv = (TextView) findViewById(R.id.time2_tv);
		time2_name_tv = (TextView) findViewById(R.id.time2_name_tv);
		time3_tv = (TextView) findViewById(R.id.time3_tv);
		time3_name_tv = (TextView) findViewById(R.id.time3_name_tv);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(RECORDINFO)) {
				try {
					record = JSONHelper.parseObject(
							bundle.getString(RECORDINFO), Record.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String state = record.getS();
		if (state.equals(StateEnum.neworder.getString())) {
			time1_tv.setText("");
			time1_name_tv.setText("");
			time2_tv.setText("");
			time2_name_tv.setText("");
			time3_tv.setText(record.getCt().replace("null", ""));
			time3_name_tv.setText("创单");
		} else if (state.equals(StateEnum.yishoukuan.getString())) {
			time1_tv.setText("预计 " + record.getSt().replace("null", ""));
			time1_name_tv.setText("到账");
			time2_tv.setText(record.getPt().replace("null", ""));
			time2_name_tv.setText("已收款");
			time3_tv.setText(record.getCt());
			time3_name_tv.setText("创单");
		} else if (state.equals(StateEnum.ruzhangzhong.getString())) {
			time1_tv.setText("预计 " + record.getSt().replace("null", ""));
			time1_name_tv.setText("到账");
			time2_tv.setText(record.getSt());
			time2_name_tv.setText("入账中");
			time3_tv.setText(record.getRst());
			time3_name_tv.setText("已收款");
			time1_tv.setTextColor(getResources().getColor(R.color.price_yellow));
			time1_name_tv.setTextColor(getResources().getColor(
					R.color.price_yellow));
			time2_tv.setTextColor(getResources().getColor(
					R.color.login_text_color));
			time2_name_tv.setTextColor(getResources().getColor(
					R.color.login_text_color));
		} else if (state.equals(StateEnum.yiwancheng.getString())) {
			time3_tv.setText(record.getCt().replace("null", ""));
			time3_name_tv.setText("创单");
			time2_tv.setText(record.getPt());
			time2_name_tv.setText("已收款");
			time1_tv.setText(record.getRst());
			time1_name_tv.setText("已到账");
			state_iv.setBackground(getResources().getDrawable(
					R.drawable.slqb_daozhangzhuangtai_bg1));
		} else if (state.equals(StateEnum.yiquxiao.getString())) {
			time1_tv.setText("");
			time1_name_tv.setText("");
			time2_tv.setText(record.getQt().replace("null", ""));
			time2_name_tv.setText("取消");
			time3_tv.setText(record.getCt().replace("null", ""));
			time3_name_tv.setText("创单");
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ActivityRecordState"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityRecordState");
		MobclickAgent.onPause(this);

	}
}
