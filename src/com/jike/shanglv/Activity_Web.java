package com.jike.shanglv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.umeng.analytics.MobclickAgent;

public class Activity_Web extends Activity {
	private WebView webView;
	private String url;
	private LinearLayout loading_ll;
	private ImageView frame_ani_iv;
	private SharedPreferences sp;
	private Context context;
	private TextView title_tv;
	private ImageView out;
	private LinearLayout notNetWork_ll;
	private Button jiazai_bt;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_web);
			context = Activity_Web.this;
			((MyApplication) getApplication()).addActivity(this);
			init();
			loading_ll.setVisibility(View.VISIBLE);
			frame_ani_iv.setBackgroundResource(R.anim.frame_rotate_ani);
			AnimationDrawable anim = (AnimationDrawable) frame_ani_iv
					.getBackground();
			anim.setOneShot(false);
			anim.start();
			((ImageButton) findViewById(R.id.back))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (webView.canGoBack()) {
								webView.goBack();
							} else {
								out();
							}

						}
					});
			out.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(Activity_Web.this,
							MainActivityN.class));
				}
			});

			jiazai_bt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (HttpUtils.checkNet(context)) {
						webView.loadUrl(url);
					}
				}
			});
			webView.setWebChromeClient(new WebChromeClient() {

				@Override
				public void onReceivedTitle(WebView view, String title) {
					super.onReceivedTitle(view, title);
					title_tv.setText(title);
				}
				
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					super.onProgressChanged(view, newProgress);
					if (HttpUtils.checkNet(context)) {
						notNetWork_ll.setVisibility(View.GONE);
					} else {
						notNetWork_ll.setVisibility(View.VISIBLE);
					}

					if (newProgress == 100) {
						loading_ll.setVisibility(View.GONE);
						webView.setVisibility(View.VISIBLE);

					}

				}
			});

			webView.setWebViewClient(new WebViewClient() {// / 不重写的话，会跳到手机浏览器中
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					view.goBack();

				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					Activity_Web.this.url = url;
					if (HttpUtils.checkNet(context)) {
						notNetWork_ll.setVisibility(View.GONE);
						view.loadUrl(url);
					} else {
						notNetWork_ll.setVisibility(View.VISIBLE);
					}

					return true;
				}

				@Override
				public WebResourceResponse shouldInterceptRequest(WebView view,
						String url) {
					if (url.startsWith("http") || url.startsWith("https")) {

						return super.shouldInterceptRequest(view, url);

					} else {

						Intent in = new Intent(Intent.ACTION_VIEW, Uri
								.parse(url));

						startActivity(in);

						return null;
					}
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
				}

			});
			webView.loadUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		out = (ImageView) findViewById(R.id.out);
		title_tv = (TextView) findViewById(R.id.title);
		notNetWork_ll = (LinearLayout) findViewById(R.id.notNetWork_ll);
		jiazai_bt = (Button) findViewById(R.id.jiazai_bt);

		String OrderList = "";
		Intent intent = getIntent();
		if (intent != null) {
			String action = intent.getStringExtra("action");
			title_tv.setText(intent.getStringExtra("title"));
			if (action.equals("list")) {
				OrderList = intent.getStringExtra("OrderList");
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
				+ pwd + "&sign=" + CommonFunc.MD5(MyApp.Pkey + name + pwd)
				+ OrderList;
		// &returnUrl=Insurance/Index
		// &returnUrl=Union/Index

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				out();
			}

		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Activity_Web"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Activity_Web");
		MobclickAgent.onPause(this);
	}

	private void out() {
		final CustomerAlertDialog cad = new CustomerAlertDialog(context, false);
		cad.setTitle("是否确认退出?");
		cad.setPositiveButton("确认退出", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Activity_Web.this, MainActivityN.class));
				cad.dismiss();
			}
		});
		cad.setNegativeButton1("再看看", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cad.dismiss();
			}
		});

	}

}
