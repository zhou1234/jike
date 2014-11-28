package com.jike.shanglv.weixin;

import com.jike.shanglv.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Down extends Activity {
	private WebView webview;
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.down);
		webview=(WebView) findViewById(R.id.web);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("http://www.baidu.com/");
	}  
}
