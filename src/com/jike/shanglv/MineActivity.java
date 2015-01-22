package com.jike.shanglv;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;
import com.jike.shanglv.NetAndJson.UserInfo;
import com.jike.shanglv.supercollection.ActivityQianbao;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MineActivity extends Activity {
	private ImageButton back_iv, user_login_imgbtn;
	private RelativeLayout my_account_rl, all_order_rl, noLogin_rl,
			hasLogin_rl, geng_duo;
	private TextView welcome_tv, username_tv, zhanghuyue_tv, chongzhi_tv;
	private SharedPreferences sp;
	private Boolean loginState = false;
	Context context;
	String loginReturnJson = "";
	private Button logout_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_mine);
			((MyApplication) getApplication()).addActivity(this);

			context = MineActivity.this;
			sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
			welcome_tv = (TextView) findViewById(R.id.welcome_tv);
			username_tv = (TextView) findViewById(R.id.username_tv);
			zhanghuyue_tv = (TextView) findViewById(R.id.zhanghuyue_tv);
			chongzhi_tv = (TextView) findViewById(R.id.chongzhi_tv);
			back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
			user_login_imgbtn = (ImageButton) findViewById(R.id.user_login_imgbtn);
			user_login_imgbtn.setOnClickListener(btnClickListner);
			back_iv.setOnClickListener(btnClickListner);
			my_account_rl = (RelativeLayout) findViewById(R.id.my_account_rl);
			all_order_rl = (RelativeLayout) findViewById(R.id.all_order_rl);
			geng_duo = (RelativeLayout) findViewById(R.id.geng_duo);
			geng_duo.setOnClickListener(btnClickListner);
			all_order_rl.setOnClickListener(btnClickListner);
			my_account_rl.setOnClickListener(btnClickListner);
			noLogin_rl = (RelativeLayout) findViewById(R.id.noLogin_rl);
			hasLogin_rl = (RelativeLayout) findViewById(R.id.hasLogin_rl);

			logout_button = (Button) findViewById(R.id.out_button);

			loginState = sp.getBoolean(SPkeys.loginState.getString(), false);
			if (!loginState) {
				logout_button.setVisibility(View.GONE);
				hasLogin_rl.setVisibility(View.GONE);
				noLogin_rl.setVisibility(View.VISIBLE);
			} else {
				logout_button.setVisibility(View.VISIBLE);
				hasLogin_rl.setVisibility(View.VISIBLE);
				noLogin_rl.setVisibility(View.GONE);
				username_tv.setText(sp.getString(SPkeys.username.getString(),
						""));
				zhanghuyue_tv.setText(sp.getString(SPkeys.amount.getString(),
						""));
				chongzhi_tv.setOnClickListener(btnClickListner);
			}
			welcome_tv.setText("欢迎您，来到"
					+ getApplication().getResources().getString(
							(Integer) (new MyApp(getApplicationContext())
									.getHm().get(PackageKeys.APP_NAME
									.getString()))));
			queryUserInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logout_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, false);
				cad.setTitle("确认注销当前用户？");
				cad.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						sp.edit().putString(SPkeys.userid.getString(), "")
								.commit();
						sp.edit().putString(SPkeys.username.getString(), "")
								.commit();
						sp.edit()
								.putString(SPkeys.lastPassword.getString(), "")
								.commit();
						sp.edit()
								.putBoolean(SPkeys.loginState.getString(),
										false).commit();
						sp.edit().remove(SPkeys.showCustomer.toString())
								.commit();
						sp.edit().remove(SPkeys.showDealer.toString()).commit();
						sp.edit().remove(SPkeys.utype.toString()).commit();
						startActivity(new Intent(context, MainActivityN.class));
						cad.dismiss();
					}
				});
				cad.setNegativeButton1("取消", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});

			}
		});
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				case R.id.back_imgbtn:
					startActivity(new Intent(MineActivity.this,
							MainActivityN.class));
					break;

				case R.id.geng_duo:
					if (!loginState) {
						Toast.makeText(getApplicationContext(), "请先登录！", 0)
								.show();
						break;
					}
					startActivity(new Intent(MineActivity.this,
							MoreActivity.class));

					break;
				case R.id.all_order_rl:
					// startActivity(new Intent(MineActivity.this,
					// OrderActivity.class));
					if (!loginState) {
						Toast.makeText(getApplicationContext(), "请先登录！", 0)
								.show();
						break;
					}
					startActivity(new Intent(MineActivity.this,
							ActivityTransactionDetails.class));
					break;
				case R.id.user_login_imgbtn:
					startActivity(new Intent(MineActivity.this,
							Activity_Login.class));
					break;
				case R.id.my_account_rl:
					if (!loginState) {
						Toast.makeText(getApplicationContext(), "请先登录！", 0)
								.show();
						break;
					}
					startActivity(new Intent(MineActivity.this,
							ActivityMyAccout.class));
					break;
				case R.id.chongzhi_tv:
					startActivity(new Intent(MineActivity.this,
							ActivityZhanghuchongzhi.class));
					break;
				default:
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
		loginState = sp.getBoolean(SPkeys.loginState.getString(), false);
		if (!loginState) {
			hasLogin_rl.setVisibility(View.GONE);
			noLogin_rl.setVisibility(View.VISIBLE);
		} else {
			hasLogin_rl.setVisibility(View.VISIBLE);
			noLogin_rl.setVisibility(View.GONE);

			username_tv.setText(sp.getString(SPkeys.username.getString(), ""));
			zhanghuyue_tv.setText(sp.getString(SPkeys.amount.getString(), ""));
		}
		queryUserInfo();
		MobclickAgent.onPageStart("MineActivity"); // 统计页面
	}

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
						zhanghuyue_tv.setText(user.getAmmount());
						username_tv.setText(user.getUsername());
					} else if (state.equals("1003")) {
						sp.edit().putString(SPkeys.userid.getString(), "")
								.commit();
						sp.edit().putString(SPkeys.username.getString(), "")
								.commit();
						sp.edit()
								.putBoolean(SPkeys.loginState.getString(),
										false).commit();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MineActivity");

	}
}
