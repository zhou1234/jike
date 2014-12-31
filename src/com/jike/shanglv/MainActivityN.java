package com.jike.shanglv;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;
import com.jike.shanglv.NetAndJson.UserInfo;
import com.jike.shanglv.Update.UpdateManager;
import com.jike.shanglv.supercollection.ActivityQianbao;
import com.jike.shanglv.weixin.PayActivity;
import com.umeng.analytics.MobclickAgent;

@SuppressWarnings({ "deprecation", "unused" })
public class MainActivityN extends ActivityGroup implements
		OnCheckedChangeListener {

	public static MainActivity instance = null;
	private RadioGroup radio_group;
	private Intent mIntent;
	private ViewFlipper container;
	private RadioButton radio_order, radio_home, radio_mine, radio_more;
	private Context context;
	private SharedPreferences sp;
	private String loginReturnJson = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			((MyApplication) getApplication()).addActivity(MainActivityN.this);
			// goB2BHome();
			// MobclickAgent.updateOnlineConfig(context);
			initView();
			initHomePage();
			radio_group.setOnCheckedChangeListener(this);

			// if (!((MyApplication) getApplication()).getHasCheckedUpdate()) {
			MyApp ma = new MyApp(MainActivityN.this);
			UpdateManager manager = new UpdateManager(MainActivityN.this, ma
					.getHm().get(PackageKeys.UPDATE_NOTE.getString())
					.toString());
			manager.checkForUpdates(false);
			((MyApplication) getApplication()).setHasCheckedUpdate(true);
			// }
			queryUserInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 如果为B2B程序，则跳到商旅助手的主菜单界面
	 * 
	 * private void goB2BHome() { if ((new MyApp(MainActivity.this).getHm().get(
	 * PackageKeys.PLATFORM.getString()) == Platform.B2B)) { Intent intent = new
	 * Intent(MainActivity.this, ActivityBMenu.class);
	 * MainActivity.this.startActivity(intent); MainActivity.this.finish(); } }
	 */

	private void queryUserInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MyApp ma = new MyApp(context);
					// int utype = 0;
					// Platform pf = (Platform) ma.getHm().get(
					// PackageKeys.PLATFORM.getString());
					// if (pf == Platform.B2B)
					// utype = 1;
					// else if (pf == Platform.B2C)
					// utype = 2;
					String str = "{\"uname\":\""
							+ sp.getString(SPkeys.lastUsername.getString(), "")
							+ "\",\"upwd\":\""
							+ sp.getString(SPkeys.lastPassword.getString(), "")
							// + "\",\"utype\":\"" + utype
							+ "\"}";
					String param = "action=userlogin&sitekey=&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&str="
							+ str
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "userlogin" + str);
					loginReturnJson = HttpUtils.getJsonContent(
							ma.getServeUrl(), param);
					Log.v("loginReturnJson", loginReturnJson);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {// 在主界面判断用户名密码是否失效
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 获取登录返回的数据

				JSONTokener jsonParser;
				jsonParser = new JSONTokener(loginReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						String content = jsonObject.getString("d");
						sp.edit()
								.putString(SPkeys.UserInfoJson.getString(),
										content).commit();
						// 以下代码将用户信息反序列化到SharedPreferences中
						UserInfo user = JSONHelper.parseObject(content,
								UserInfo.class);
						sp.edit()
								.putString(SPkeys.userid.getString(),
										user.getUserid()).commit();
						sp.edit()
								.putString(SPkeys.username.getString(),
										user.getUsername()).commit();
						sp.edit()
								.putString(SPkeys.amount.getString(),
										user.getAmmount()).commit();
						sp.edit()
								.putString(SPkeys.siteid.getString(),
										user.getSiteid()).commit();
						sp.edit()
								.putString(SPkeys.userphone.getString(),
										user.getMobile()).commit();
						sp.edit()
								.putString(SPkeys.useremail.getString(),
										user.getEmail()).commit();
						sp.edit()
								.putBoolean(SPkeys.loginState.getString(), true)
								.commit();
						sp.edit()
								.putString(SPkeys.utype.getString(),
										user.getUsertype()).commit();
						sp.edit()
								.putString(SPkeys.opensupperpay.getString(),
										user.getOpensupperpay()).commit();
					} else {
						sp.edit().putString(SPkeys.userid.getString(), "")
								.commit();
						sp.edit().putString(SPkeys.username.getString(), "")
								.commit();
						sp.edit()
								.putBoolean(SPkeys.loginState.getString(),
										false).commit();
						sp.edit().remove(SPkeys.showCustomer.toString())
								.commit();
						sp.edit().remove(SPkeys.showDealer.toString()).commit();
						sp.edit().remove(SPkeys.utype.toString()).commit();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private void switchPage(int positoon) {
		switch (positoon) {
		case 0:
			mIntent = new Intent(this, OrderActivity.class);
			break;
		case 1:
			mIntent = new Intent(this, HomeActivityNewN.class);
			break;
		case 2:
//			mIntent = new Intent(this, ActivityMall.class);
//			if (!sp.getBoolean(SPkeys.loginState.getString(), false)) {
//				startActivity(new Intent(context, Activity_Login.class));
//				return;
//			}
			 mIntent = new Intent(this, Activity_Web_Frame.class);
			 mIntent.putExtra("flag", "1");
			 mIntent.putExtra(Activity_Web_Frame.TITLE, "云商城");
			 mIntent.putExtra(Activity_Web_Frame.URL,
			 "http://m.99263.com/");
			break;
		case 3:
			// mIntent = new Intent(this, MoreActivity.class);
			mIntent = new Intent(this, MineActivity.class);
			break;

		default:
			break;
		}
		try {
			container.removeAllViews();
			mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Window subActivity = getLocalActivityManager().startActivity(
					"subActivity", mIntent);
			container.addView(subActivity.getDecorView(),
					new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据checkedId来判断选定的Radio，从而进行页面的换
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_order:
			switchPage(0);
			break;
		case R.id.radio_home:
			switchPage(1);
			break;
		case R.id.radio_mine:
			switchPage(2);
			break;
		case R.id.radio_more:
			switchPage(3);
			break;
		default:
			break;
		}
	}

	private void initHomePage() {
		container.removeAllViews();
		mIntent = new Intent(this, HomeActivityNewN.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window subActivity = this.getLocalActivityManager().startActivity(
				"subActivity", mIntent);
		container.addView(subActivity.getDecorView(),
				new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
	}

	/**
	 * 初始化各种控件
	 */
	private void initView() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		container = (ViewFlipper) findViewById(R.id.container);
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radio_order = (RadioButton) findViewById(R.id.radio_order);
		radio_home = (RadioButton) findViewById(R.id.radio_home);
		radio_mine = (RadioButton) findViewById(R.id.radio_mine);
		radio_more = (RadioButton) findViewById(R.id.radio_more);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(MainActivityN.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				MobclickAgent.onKillProcess(context);
				// finish();
				// SysApplication.getInstance().exit();
				((MyApplication) getApplication()).exit();
				android.os.Process.killProcess(android.os.Process.myPid());
				MainActivityN.this.finish();
				System.exit(0);
				// http://864331652.blog.163.com/blog/static/1168625632013415112635566/

				// overridePendingTransition();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this); // 统计时长
		showNetCannotUse(context);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public static void showNetCannotUse(Context context) {
		final Context cont = context;
		if (!HttpUtils.checkNet(context)) {
			// new AlertDialog.Builder(context).setTitle("网络连接失败")
			// .setMessage("无法连接网路，请检查网络设置！")
			// .setPositiveButton("确定", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("无法连接网路，请检查网络设置！");
			cad.setPositiveButton("知道了", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
		}
	}
}
