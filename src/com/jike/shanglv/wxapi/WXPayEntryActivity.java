package com.jike.shanglv.wxapi;

import com.jike.shanglv.ActivityInlandAirlineticketOrderDetail;
import com.jike.shanglv.ActivityZhanghuchongzhi;
import com.jike.shanglv.MyApplication;
import com.jike.shanglv.R;
import com.jike.shanglv.weixin.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			int code = resp.errCode;
			String str = null;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			if (code == 0) {
				str = "支付成功";
			}
			if (code == -1) {
				str = "支付失败";
			}
			if (code == -2) {
				str = "支付取消";
			}
			builder.setMessage(str);
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							MyApplication application = (MyApplication) getApplication();
							int code = application.getCode();
							if (code == 1) {
								startActivity(new Intent(
										WXPayEntryActivity.this,
										ActivityInlandAirlineticketOrderDetail.class));
								finish();
							}
							if (code == 15) {
								startActivity(new Intent(
										WXPayEntryActivity.this,
										ActivityZhanghuchongzhi.class));
								finish();
							}
							
						}
					});
			builder.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WXPayEntryActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WXPayEntryActivity");
		MobclickAgent.onPause(this);

	}
}