package com.jike.shanglv.weixin;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jike.shanglv.MyApp;
import com.jike.shanglv.MyApplication;
import com.jike.shanglv.R;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class PayActivity extends Activity {

	private static final String TAG = "PayActivity";
	private SharedPreferences sp;
	private IWXAPI api;
	private String body;
	private String amount;
	private String amountWX;
	private int paysystype;
	private String result;
	private Context context;
	private ProgressDialog progressDialog;
	private float amo;
	private String orderID;
	private MyApplication maApp;
	private String orderno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		context = this;
		Intent intent = getIntent();
		if (intent != null) {
			body = intent.getStringExtra("body");
			orderID = intent.getStringExtra("orderID");
			amount = intent.getStringExtra("amount");
			amo = Float.parseFloat(amount);
			amountWX = Integer.toString(Math.round(amo * 100));
			paysystype = intent.getIntExtra("paysystype", -1);
		}
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		startRequest();
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
		maApp = (MyApplication) getApplication();
		maApp.setCode(paysystype);
	}

	private void startRequest() {
		progressDialog = ProgressDialog.show(PayActivity.this, "", "加载中。 。 。");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String userid = sp.getString(SPkeys.userid.getString(), "");
				String siteid = sp.getString(SPkeys.siteid.getString(), "");
				MyApp ma = new MyApp(context);
				if (paysystype == 15) {
					String action = "action=paylog";
					String str = "{\"orderID\":\"" + "" + "\",\"amount\":\""
							+ amount + "\",\"userid\":\"" + userid
							+ "\",\"paysystype\":\"" + paysystype
							+ "\",\"siteid\":\"" + siteid + "\"}";
					String url = action
							+ "&str="
							+ str
							+ "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sitekey="
							+ MyApp.sitekey
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "paylog" + str);
					result = HttpUtils.getJsonContent(ma.getServeUrl(), url);
				}
				if (paysystype == 1 || paysystype == 2 || paysystype == 14) {
					float amount1 = (float) (amo + amo * 0.006);
					amountWX = Integer.toString(Math.round(amount1 * 100));
					String action = "action=paylog";
					String str = "{\"orderID\":\"" + orderID
							+ "\",\"amount\":\"" + amo + "\",\"userid\":\""
							+ userid + "\",\"paysystype\":\"" + paysystype
							+ "\",\"siteid\":\"" + siteid + "\"}";
					String url = action
							+ "&str="
							+ str
							+ "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sitekey="
							+ MyApp.sitekey
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "paylog" + str);
					result = HttpUtils.getJsonContent(ma.getServeUrl(), url);
					maApp.setOrderID(orderID);
				}
				handler.sendEmptyMessage(0);
			}
		}).start();

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			progressDialog.dismiss();
			try {
				JSONObject jsonObject = new JSONObject(result);//{"c":"0000","d":"添加支付日志成功","orderno":"201412230907471110"}
				String str = jsonObject.getString("c");
				if (str.equals("0000")) {
					orderno=jsonObject.getString("orderno");
					new GetAccessTokenTask().execute();
				} else {
					String data = jsonObject.getString("d");
					Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 微信公众平台商户模块和商户约定的密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genPackage这个过程由服务器端完成
	 */
	private static final String PARTNER_KEY = "5ff70375192c7e6acd8d399edb3fa033";

	// 8934e7d15453e97507ef794cf7b0519d";

	private String genPackage(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(PARTNER_KEY); // 注意：不能hardcode在客户端，建议genPackage这个过程都由服务器端完成

		// 进行md5摘要前，params内容为原始内容，未经过url encode处理
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();

		return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
	}

	/**
	 * 微信开放平台和商户约定的密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	 */
	private static final String APP_SECRET = "fd124f9b842220977273f2575f960abb";// db426a9829e4b49a0dcac7b4162da6b6";
																				// //
																				// wxd930ea5d5a258f4f
																				// 对应的密钥

	/**
	 * 微信开放平台和商户约定的支付密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	 */
	private static final String APP_KEY = "1MMmhqKN3nbi2QWDRCb3fXnxrzrjGaDztXkzvvizATijOmOL3XdeXx178n5kRV4kYt9HYZQf2e7K71qjzTIGt9r2zednCm7fFXi5SQ5mCmhZFZzZfqvTcauCHbjE3WEQ";

	// "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K";
	// //
	// wxd930ea5d5a258f4f
	// 对应的支付密钥

	private class GetAccessTokenTask extends
			AsyncTask<Void, Void, GetAccessTokenResult> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// dialog = ProgressDialog.show(PayActivity.this,
			// getString(R.string.app_tip),
			// getString(R.string.getting_access_token));
			dialog = ProgressDialog.show(PayActivity.this, "", "加载中。 。 。");
		}

		@Override
		protected void onPostExecute(GetAccessTokenResult result) {
			if (dialog != null) {
				dialog.dismiss();
			}

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				// Toast.makeText(PayActivity.this,
				// R.string.get_access_token_succ, Toast.LENGTH_LONG)
				// .show();
				Log.d(TAG, "onPostExecute, accessToken = " + result.accessToken);

				GetPrepayIdTask getPrepayId = new GetPrepayIdTask(
						result.accessToken);
				getPrepayId.execute();
			} else {
				Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_LONG)
						.show();
				finish();
			}
		}

		@Override
		protected GetAccessTokenResult doInBackground(Void... params) {
			GetAccessTokenResult result = new GetAccessTokenResult();

			String url = String
					.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
							Constants.APP_ID, APP_SECRET);
			Log.d(TAG, "get access token, url = " + url);

			byte[] buf = Util.httpGet(url);
			if (buf == null) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}

			String content = new String(buf);
			result.parseFrom(content);
			return result;
		}
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, GetPrepayIdResult> {

		private ProgressDialog dialog;
		private String accessToken;

		public GetPrepayIdTask(String accessToken) {
			this.accessToken = accessToken;
		}

		@Override
		protected void onPreExecute() {
			// dialog = ProgressDialog.show(PayActivity.this,
			// getString(R.string.app_tip),
			// getString(R.string.getting_prepayid));
			dialog = ProgressDialog.show(PayActivity.this, "", "加载中。 。 。");
		}

		@Override
		protected void onPostExecute(GetPrepayIdResult result) {
			if (dialog != null) {
				dialog.dismiss();
			}

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				// Toast.makeText(PayActivity.this, R.string.get_prepayid_succ,
				// Toast.LENGTH_LONG).show();
				sendPayReq(result);
			} else {
				Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_LONG)
						.show();
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected GetPrepayIdResult doInBackground(Void... params) {

			String url = String.format(
					"https://api.weixin.qq.com/pay/genprepay?access_token=%s",
					accessToken);
			String entity = genProductArgs();

			Log.d(TAG, "doInBackground, url = " + url);
			Log.d(TAG, "doInBackground, entity = " + entity);

			GetPrepayIdResult result = new GetPrepayIdResult();

			byte[] buf = Util.httpPost(url, entity);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}

			String content = new String(buf);
			Log.d(TAG, "doInBackground, content = " + content);
			result.parseFrom(content);
			return result;
		}
	}

	private static enum LocalRetCode {
		ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
	}

	private static class GetAccessTokenResult {

		private static final String TAG = "GetAccessTokenResult";

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String accessToken;
		public int expiresIn;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("access_token")) { // success case
					accessToken = json.getString("access_token");
					expiresIn = json.getInt("expires_in");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					errCode = json.getInt("errcode");
					errMsg = json.getString("errmsg");
					localRetCode = LocalRetCode.ERR_JSON;
				}

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	private static class GetPrepayIdResult {

		private static final String TAG = "GetPrepayIdResult";

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String prepayId;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("prepayid")) { // success case
					prepayId = json.getString("prepayid");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					localRetCode = LocalRetCode.ERR_JSON;
				}

				errCode = json.getInt("errcode");
				errMsg = json.getString("errmsg");

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 建议 traceid 字段包含用户信息及订单信息，方便后续对订单状态的查询和跟踪
	 */
	private String getTraceId() {
		return "crestxu_" + genTimeStamp();
	}

	private long timeStamp;
	private String nonceStr, packageValue;

	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());

		String sha1 = Util.sha1(sb.toString());
		Log.d(TAG, "genSign, sha1 = " + sha1);
		return sha1;
	}

	private String genProductArgs() {
		JSONObject json = new JSONObject();

		try {
			json.put("appid", Constants.APP_ID);
			String traceId = getTraceId(); // traceId
											// 由开发者自定义，可用于订单的查询与跟踪，建议根据支付用户信息生成此id
			json.put("traceid", traceId);
			nonceStr = genNonceStr();
			json.put("noncestr", nonceStr);

			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("bank_type", "WX"));
			packageParams.add(new BasicNameValuePair("body", body));
			packageParams.add(new BasicNameValuePair("fee_type", "1"));
			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://servers.51jp.cn/PayNotify-Weixin"));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					orderno));
			packageParams.add(new BasicNameValuePair("partner",
					Constants.PARTNER_ID));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"196.168.1.1"));
			packageParams.add(new BasicNameValuePair("total_fee", amountWX));
			packageValue = genPackage(packageParams);

			json.put("package", packageValue);
			timeStamp = genTimeStamp();
			json.put("timestamp", timeStamp);

			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			signParams.add(new BasicNameValuePair("appkey", APP_KEY));
			signParams.add(new BasicNameValuePair("noncestr", nonceStr));
			signParams.add(new BasicNameValuePair("package", packageValue));
			signParams.add(new BasicNameValuePair("timestamp", String
					.valueOf(timeStamp)));
			signParams.add(new BasicNameValuePair("traceid", traceId));
			json.put("app_signature", genSign(signParams));

			json.put("sign_method", "sha1");
		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}

		return json.toString();
	}

	private void sendPayReq(GetPrepayIdResult result) {

		PayReq req = new PayReq();
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.PARTNER_ID;
		req.prepayId = result.prepayId;
		req.nonceStr = nonceStr;
		req.timeStamp = String.valueOf(timeStamp);
		req.packageValue = "Sign=" + packageValue;

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("appkey", APP_KEY));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genSign(signParams);

		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.sendReq(req);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("PayActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("PayActivity");
		MobclickAgent.onPause(this);
	}
}
