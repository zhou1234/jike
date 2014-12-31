package com.jike.shanglv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.umeng.analytics.MobclickAgent;

public class ActivityMall extends Activity {
	private WebView webView;
	private String url;
	private LinearLayout loading_ll;
	private ImageView frame_ani_iv;
	private SharedPreferences sp;
	private Context context;
	private TextView title;
	private TextView out;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_mall);
			context = this;
			((MyApplication) getApplication()).addActivity(this);
			init();
			((ImageButton) findViewById(R.id.back))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							webView.goBack();
						}
					});
			out.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(context, MainActivityN.class));
				}
			});

			webView.setWebViewClient(new WebViewClient() {// / 不重写的话，会跳到手机浏览器中
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) { // Handle the
																	// error
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (HttpUtils.showNetCannotUse(context)) {
						return false;
					}
					view.loadUrl(url);
					return true;
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					loading_ll.setVisibility(View.GONE);
					webView.setVisibility(View.VISIBLE);
				}
			});
			if (HttpUtils.showNetCannotUse(context)) {
				return;
			}
			webView.loadUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		out = (TextView) findViewById(R.id.out);
		title = (TextView) findViewById(R.id.title);
		Intent intent = getIntent();
		if (intent != null) {
			if (intent.getStringExtra("flag").equals("1")) {
				out.setVisibility(View.GONE);
			}

		}
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		webView = new WebView(this);
		loading_ll = (LinearLayout) findViewById(R.id.loading_ll);
		frame_ani_iv = (ImageView) findViewById(R.id.frame_ani_iv);
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);// 在WebView中使用JavaScript，若页面中用了JavaScript，必须为WebView使能JavaScript
		// /AutoLogin?user=aaaa&pwd=33232&sign=34343434erkwlewlewlew
		// sing的参数生成MD5(公钥+用户名+密码)后再编码
		MyApp myApp = new MyApp(context);
		String name = sp.getString(SPkeys.username.getString(), "");
		String pwd = sp.getString(SPkeys.lastPassword.getString(), "");
		url = myApp.getMallServeUrl() + "AutoLogin?user=" + name + "&pwd="
				+ pwd + "&sign=" + CommonFunc.MD5(MyApp.Pkey + name + pwd);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		try {
			// loading_ll.setVisibility(View.VISIBLE);
			// frame_ani_iv.setBackgroundResource(R.anim.frame_rotate_ani);
			// AnimationDrawable anim = (AnimationDrawable) frame_ani_iv
			// .getBackground();
			// anim.setOneShot(false);
			// anim.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loading_ll.setVisibility(View.VISIBLE);
		frame_ani_iv.setBackgroundResource(R.anim.frame_rotate_ani);
		AnimationDrawable anim = (AnimationDrawable) frame_ani_iv
				.getBackground();
		anim.setOneShot(false);
		anim.start();
		if (HttpUtils.showNetCannotUse(context)) {
			return;
		}
		webView.loadUrl(url);

		MobclickAgent.onPageStart("ActivityMall"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityMall");
		MobclickAgent.onPause(this);
	}
}
