package com.jike.shanglv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
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
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class Activity_Web extends Activity {
	private WebView webView;
	private String url;
	private LinearLayout loading_ll;
	private ImageView frame_ani_iv;
	private SharedPreferences sp;
	private Context context;
	private TextView title_tv;
	private ImageView out_iv;
	private ImageView share_iv;
	private TextView fenxiang_tv;
	private LinearLayout notNetWork_ll;
	private Button jiazai_bt;
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(MyApp.DESCRIPTOR);
	private String shareURL = "";
	private String title;
	private String imageURL = "";

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
								// out();
								startActivity(new Intent(Activity_Web.this,
										MainActivityN.class));
							}

						}
					});
			out_iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(Activity_Web.this,
							MainActivityN.class));
				}
			});
			share_iv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					addCustomPlatforms();

				}
			});
			fenxiang_tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					addCustomPlatforms();

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
					Activity_Web.this.title = title;
					handler.sendEmptyMessage(0);
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

			webView.setWebViewClient(new WebViewClient() {// / ����д�Ļ����������ֻ��������
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
					String str = "javascript:window.handler.show(document.getElementById("
							+ "\"shareUrl\"" + ").innerHTML);";
					view.loadUrl(str.trim());
					String str1 = "javascript:window.handler.showImg(document.getElementById("
							+ "\"shareProductPicture\"" + ").innerHTML);";
					view.loadUrl(str1.trim());
				}

			});
			webView.loadUrl(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		out_iv = (ImageView) findViewById(R.id.out_iv);
		share_iv = (ImageView) findViewById(R.id.share_iv);
		fenxiang_tv = (TextView) findViewById(R.id.fenxiang_tv);
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
		loading_ll = (LinearLayout) findViewById(R.id.loading_ll);
		frame_ani_iv = (ImageView) findViewById(R.id.frame_ani_iv);
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);// ��WebView��ʹ��JavaScript����ҳ��������JavaScript������ΪWebViewʹ��JavaScript
		webView.addJavascriptInterface(new Object() {

			@JavascriptInterface
			public void show(String data) {
				String da = data.replace("\n", "").trim();
				shareURL = da;
			}

			@JavascriptInterface
			public void showImg(String data) {
				imageURL = data;
			}

		}, "handler");
		// /AutoLogin?user=aaaa&pwd=33232&sign=34343434erkwlewlewlew

		// sing�Ĳ�������MD5(��Կ+�û���+����)���ٱ���
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
				// out();
				startActivity(new Intent(Activity_Web.this, MainActivityN.class));
			}

		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Activity_Web"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Activity_Web");
		MobclickAgent.onPause(this);
	}

	private void out() {
		final CustomerAlertDialog cad = new CustomerAlertDialog(context, false);
		cad.setTitle("�Ƿ��˳�?");
		cad.setPositiveButton("�˳�", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Activity_Web.this, MainActivityN.class));
				cad.dismiss();
			}
		});
		cad.setNegativeButton1("�ٿ���", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cad.dismiss();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * ������е�ƽ̨</br>
	 */
	private void addCustomPlatforms() {

		// �������SSO��Ȩ
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// �����Ѷ΢��SSO��Ȩ
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// ���΢��ƽ̨
		addWXPlatform();
		// ���QQƽ̨
		addQQQZonePlatform();

		// ��Ӷ���ƽ̨
		addSMS();

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SMS);
		mController.setShareContent(title + " " + shareURL);
		mController.setShareMedia(new UMImage(context, imageURL));
		mController.openShare(this, false);
	}

	/**
	 * @�������� : ���΢��ƽ̨����
	 * @return
	 */
	private void addWXPlatform() {
		// ע�⣺��΢����Ȩ��ʱ�򣬱��봫��appSecret
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appId = context.getResources().getString(R.string.weixin_appId);
		String appSecret = context.getResources().getString(
				R.string.weixin_appSecret);
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(Activity_Web.this, appId,
				appSecret);
		wxHandler.setTitle("���ùܼ�-���̳�");
		wxHandler.setTargetUrl(shareURL);
		wxHandler.addToSocialSDK();

		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.setTitle("���ùܼ�-���̳�");
		wxCircleHandler.setTargetUrl(shareURL);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @�������� : ���QQƽ̨֧�� QQ��������ݣ� �����������ͣ� �����������֡�ͼƬ�����֡���Ƶ. ����˵�� : title, summary,
	 *       image url�б�����������һ��, targetUrl��������,��ҳ��ַ������"http://"��ͷ . title :
	 *       Ҫ������� summary : Ҫ��������ָ��� image url : ͼƬ��ַ [������������������дһ��] targetUrl
	 *       : �û�����÷���ʱ��ת����Ŀ���ַ [����] ( ������д��Ĭ������Ϊ������ҳ )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = context.getResources().getString(R.string.qq_appId);
		String appKey = context.getResources().getString(R.string.qq_appKey);
		// ���QQ֧��, ��������QQ�������ݵ�target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.setTitle("���ùܼ�-���̳�");
		qqSsoHandler.setTargetUrl("http://" + shareURL);
		qqSsoHandler.addToSocialSDK();

		// ���QZoneƽ̨
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
				appKey);
		qZoneSsoHandler.setTargetUrl("http://" + shareURL);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * ��Ӷ���ƽ̨</br>
	 */
	private void addSMS() {
		// ��Ӷ���
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.setTargetUrl(shareURL);
		smsHandler.addToSocialSDK();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (title.contains("��Ʒ����")) {
					out_iv.setVisibility(View.GONE);
					share_iv.setVisibility(View.VISIBLE);
					fenxiang_tv.setVisibility(View.VISIBLE);
				} else {
					out_iv.setVisibility(View.VISIBLE);
					share_iv.setVisibility(View.GONE);
					fenxiang_tv.setVisibility(View.GONE);
				}
				break;

			}

		}
	};

}
