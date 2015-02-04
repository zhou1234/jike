package com.jike.shanglv;

import com.jike.shanglv.Models.Details;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityTransactionDetails extends Activity implements
		View.OnClickListener {
	private TextView tv_chuRu;
	private TextView tv_orderId;
	private TextView tv_source;
	private TextView tv_chuZhang;
	private TextView tv_jieYu;
	private TextView tv_time;
	private TextView tv_pingTaiDingDan;
	private TextView tv_beiZhu;
	private ImageButton back_imgbtn;
	private ImageButton home_imgbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_details);
		init();
		Intent intent = getIntent();
		if (intent != null) {
			Details details = (Details) intent.getSerializableExtra("Details");
			tv_orderId.setText(details.getOrderno());
			tv_source.setText(details.getCashtradeapplication());
			tv_jieYu.setText(details.getCurrentsettlementbalance());
			tv_time.setText(details.getCreatetime());
			tv_pingTaiDingDan.setText(details.getOutorderno());
			tv_beiZhu.setText(details.getRemark());
			String amt = details.getInamount().trim();
			if (!amt.equals("")) {
				if (amt.charAt(0) == '-') {
					tv_chuZhang.setTextColor(Color.RED);
					tv_chuRu.setText("≥ˆ’À:");
				} else {
					tv_chuZhang.setTextColor(Color.BLUE);
					tv_chuRu.setText("»Î’À:");
				}
			} else {
				amt = "0.00";
			}
			tv_chuZhang.setText(amt);
		}
	}

	private void init() {
		tv_chuRu = (TextView) findViewById(R.id.tv_chuRu);
		tv_orderId = (TextView) findViewById(R.id.tv_orderId);
		tv_source = (TextView) findViewById(R.id.tv_source);
		tv_chuZhang = (TextView) findViewById(R.id.tv_chuZhang);
		tv_jieYu = (TextView) findViewById(R.id.tv_jieYu);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_pingTaiDingDan = (TextView) findViewById(R.id.tv_pingTaiDingDan);
		tv_beiZhu = (TextView) findViewById(R.id.tv_beiZhu);
		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(this);
		home_imgbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.home_imgbtn:
			startActivity(new Intent(this, MainActivityN.class));
			break;

		case R.id.back_imgbtn:
			finish();
			break;
		}

	}

}
