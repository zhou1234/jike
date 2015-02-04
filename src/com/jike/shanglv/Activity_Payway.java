package com.jike.shanglv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.supercollection.ActivityQianbao;
import com.jike.shanglv.weixin.Constants;
import com.jike.shanglv.weixin.PayActivity;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class Activity_Payway extends Activity {
	private IWXAPI api;
	public static final String CHONGZHI_AMOUNT = "CHONGZHI_AMOUNT";
	private Context context;
	private SharedPreferences sp;
	private String amount = "0";
	private TextView tv_counterFee;
	private int paysystype;
	private String body;
	private String orderID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payway);
		context = this;
		api = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		findViewById(R.id.wx_pay_rl).setOnClickListener(myClickListener);
		findViewById(R.id.online_pay_rl).setOnClickListener(myClickListener);
		// if (!sp.getString(SPkeys.opensupperpay.getString(), "false").equals(
		// "true")) {
		if (!sp.getString(SPkeys.utype.getString(), "0").equals("1")) {
			findViewById(R.id.sc_pay_rl).setVisibility(View.GONE);
			findViewById(R.id.bottom_line).setVisibility(View.GONE);
		}
		tv_counterFee = (TextView) findViewById(R.id.tv_counterFee);
		findViewById(R.id.sc_pay_rl).setOnClickListener(myClickListener);
		findViewById(R.id.back_imgbtn).setOnClickListener(myClickListener);
		try {
			amount = getIntent().getExtras().getString(CHONGZHI_AMOUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = getIntent();
		if (intent != null) {
			paysystype = intent.getIntExtra("paysystype", -1);
			body = intent.getStringExtra("body");
		}
		if (paysystype == 15) {
			tv_counterFee.setVisibility(View.GONE);
		} else {
			tv_counterFee.setVisibility(View.VISIBLE);
		}
		if (paysystype == 1 || paysystype == 2 || paysystype == 14) {
			orderID = intent.getStringExtra("orderID");
			findViewById(R.id.sc_pay_rl).setVisibility(View.GONE);
			findViewById(R.id.bottom_line).setVisibility(View.GONE);
		}
	}

	OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			try {
				switch (arg0.getId()) {
				case R.id.wx_pay_rl:
					Intent intent1 = new Intent(context, PayActivity.class);
					intent1.putExtra("amount", amount);
					intent1.putExtra("body", body);
					intent1.putExtra("paysystype", paysystype);
					intent1.putExtra("orderID", orderID);
					boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
					if (api.isWXAppInstalled()) {
						if (isPaySupported) {
							startActivity(intent1);
							finish();
						} else {
							Toast.makeText(context, "当前微信版本不支持",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(context, "微信未安装", Toast.LENGTH_SHORT)
								.show();
					}

					break;
				case R.id.online_pay_rl:
					String userid = sp.getString(SPkeys.userid.getString(), "");
					// int paysystype = 15;
					String siteid = sp.getString(SPkeys.siteid.getString(), "");
					String sign = CommonFunc.MD5(orderID + amount + userid
							+ paysystype + siteid);
					MyApp ma = new MyApp(context);
					// <string
					// name="test_pay_server_url">http://gatewayceshi.51jp.cn/PayMent/BeginPay.aspx?orderID=%1$s&amp;amount=%2$s&amp;userid=%3$s&amp;paysystype=%4$s&amp;siteid=%5$s&amp;sign=%6$s</string>
					String url = String.format(ma.getPayServeUrl(), orderID,
							amount, userid, paysystype, siteid, sign);
					Intent intent = new Intent(context, Activity_Web_Pay.class);
					intent.putExtra(Activity_Web_Pay.URL, url);
					intent.putExtra(Activity_Web_Pay.TITLE, body);
					startActivity(intent);
					finish();
					break;
				case R.id.sc_pay_rl:
					// Intent intent5 = new Intent(
					// context,
					// com.jike.shanglv.pos.jike.mpos.newversion.MopsWelcomeActivity.class);
					// sp.edit()
					// .putString(SPkeys.chongZhiJinE.getString(), amount)
					// .commit();
					// intent5.setClass(getApplication(),
					// com.jike.shanglv.pos.jike.mpos.newversion.MopsWelcomeActivity.class);

					Intent intent5 = new Intent(context, ActivityQianbao.class);
					intent5.putExtra("amt", amount);
					startActivity(intent5);
					break;
				case R.id.back_imgbtn:
					finish();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
