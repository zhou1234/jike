package com.jike.shanglv;

import com.jike.shanglv.Enums.SPkeys;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressWarnings("unused")
public class OrderActivity extends Activity {

	private ImageButton back_iv;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_order);
			sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
			((MyApplication) getApplication()).addActivity(this);

			back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
			back_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(OrderActivity.this,
							MainActivityN.class));
				}
			});
			((RelativeLayout) findViewById(R.id.gnjp_rl))
					.setOnClickListener(btnClickListner);
			((RelativeLayout) findViewById(R.id.gjjp_rl))
					.setOnClickListener(btnClickListner);
			((RelativeLayout) findViewById(R.id.jd_rl))
					.setOnClickListener(btnClickListner);
			((RelativeLayout) findViewById(R.id.hcp_rl))
					.setOnClickListener(btnClickListner);
			((RelativeLayout) findViewById(R.id.hfcz_rl))
					.setOnClickListener(btnClickListner);
			((RelativeLayout) findViewById(R.id.gjjp_xqd_rl))
					.setOnClickListener(btnClickListner);
			((RelativeLayout) findViewById(R.id.mall_rl))
					.setOnClickListener(btnClickListner);
			((RelativeLayout) findViewById(R.id.visa_rl))
					.setOnClickListener(btnClickListner);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent(OrderActivity.this,
						ActivityOrderList.class);
				if (!sp.getBoolean(SPkeys.loginState.getString(), false)) {
					startActivity(new Intent(OrderActivity.this,
							Activity_Login.class));
					return;
				}
				switch (v.getId()) {
				case R.id.gnjp_rl:
					intent.putExtra(ActivityOrderList.ACTION_TOKENNAME,
							ActivityOrderList.FLIGHT_ORDERLIST);
					intent.putExtra(ActivityOrderList.TITLE_TOKENNAME, "国内机票订单");
					startActivity(intent);
					break;
				case R.id.gjjp_xqd_rl:
					intent.putExtra(ActivityOrderList.ACTION_TOKENNAME,
							ActivityOrderList.DEMAND_ORDERLIST);
					intent.putExtra(ActivityOrderList.TITLE_TOKENNAME, "国际需求订单");
					startActivity(intent);
					break;
				case R.id.gjjp_rl:
					intent.putExtra(ActivityOrderList.ACTION_TOKENNAME,
							ActivityOrderList.INTFLIGHT_ORDERLIST);
					intent.putExtra(ActivityOrderList.TITLE_TOKENNAME, "国际机票订单");
					startActivity(intent);
					break;
				case R.id.jd_rl:
					intent.putExtra(ActivityOrderList.ACTION_TOKENNAME,
							ActivityOrderList.HOTEL_ORDERLIST);
					intent.putExtra(ActivityOrderList.TITLE_TOKENNAME, "酒店订单");
					startActivity(intent);
					break;
				case R.id.hcp_rl:
					intent.putExtra(ActivityOrderList.ACTION_TOKENNAME,
							ActivityOrderList.TRAIN_ORDERLIST);
					intent.putExtra(ActivityOrderList.TITLE_TOKENNAME, "火车票订单");
					startActivity(intent);
					break;
				case R.id.hfcz_rl:
					intent.putExtra(ActivityOrderList.ACTION_TOKENNAME,
							ActivityOrderList.PHONE_ORDERLIST);
					intent.putExtra(ActivityOrderList.TITLE_TOKENNAME, "话费充值订单");
					startActivity(intent);
					break;
				case R.id.mall_rl:
					Intent intent1 = new Intent(OrderActivity.this,
							Activity_Web.class);
					intent1.putExtra("action", "list");
					intent1.putExtra("title", "云商城列表");
					intent1.putExtra("OrderList", "&returnUrl=/Mall/OrderList");
					startActivity(intent1);
					break;
				case R.id.visa_rl:
					Intent intent2 = new Intent(OrderActivity.this,
							Activity_Web.class);
					intent2.putExtra("action", "list");
					intent2.putExtra("title", "签证列表");
					intent2.putExtra("OrderList", "&returnUrl=/Visa/OrderList");
					startActivity(intent2);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderActivity"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderActivity");

	}
}
