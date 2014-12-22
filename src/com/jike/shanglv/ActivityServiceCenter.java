package com.jike.shanglv;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityServiceCenter extends Activity {
	private String url = "http://m.51jp.cn/About/Service.html";
	private WebView webView;
	private LinearLayout loading_ll;
	private ImageView frame_ani_iv;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_frame);
		((MyApplication) getApplication()).addActivity(this);

		((ImageButton) findViewById(R.id.back))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		((ImageButton) findViewById(R.id.home))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(ActivityServiceCenter.this,
								MainActivityN.class));
					}
				});

		// webView = new WebView(this);
		loading_ll = (LinearLayout) findViewById(R.id.loading_ll);
		frame_ani_iv = (ImageView) findViewById(R.id.frame_ani_iv);
		webView = (WebView) findViewById(R.id.webView);
		 WebSettings webSettings = webView.getSettings();
		 webSettings.setJavaScriptEnabled(true);//
		// 在WebView中使用JavaScript，若页面中用了JavaScript，必须为WebView使能JavaScript
		((TextView) findViewById(R.id.title)).setText("客服中心");

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				loading_ll.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
			}
		});

		// webView.setWebViewClient(new WebViewClient() {// / 不重写的话，会跳到手机浏览器中
		// @Override
		// public void onReceivedError(WebView view, int errorCode,
		// String description, String failingUrl) { // Handle the
		// // error
		// }
		//
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// return true;
		// }
		//
		// @Override
		// public void onPageFinished(WebView view, String url) {
		// super.onPageFinished(view, url);
		// loading_ll.setVisibility(View.GONE);
		// webView.setVisibility(View.VISIBLE);
		// }
		// });
		// webView.loadUrl(url);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		try {
//			loading_ll.setVisibility(View.VISIBLE);
//			frame_ani_iv.setBackgroundResource(R.anim.frame_rotate_ani);
//			AnimationDrawable anim = (AnimationDrawable) frame_ani_iv
//					.getBackground();
//			anim.setOneShot(false);
//			anim.start();
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
		webView.loadUrl(url);
		MobclickAgent.onPageStart("ActivityServiceCenter"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityServiceCenter");
		MobclickAgent.onPause(this);
	}
}
