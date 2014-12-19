package com.jike.shanglv.supercollection;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.jike.shanglv.Activity_Login;
import com.jike.shanglv.MyApp;
import com.jike.shanglv.MyApplication;
import com.jike.shanglv.R;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;
import com.jike.shanglv.NetAndJson.UserIn;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityMyAccout extends Activity {
	public static final int JILU = 2;
	private ImageButton back_iv;
	private Button logout_button;
	private ImageView frame_ani_iv;
	private TextView username_tv, phone_tv, dangqianyue_tv, bukeyongyue_tv;
	private SharedPreferences sp;
	private Context context;
	private RelativeLayout jiaoyijilu_rl, yue_rl;
	private LinearLayout loading_ll;
	private String loginReturnJson;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myaccounts);
		((MyApplication) getApplication()).addActivity(this);
		context = this;
		frame_ani_iv = (ImageView) findViewById(R.id.frame_ani_iv);
		loading_ll = (LinearLayout) findViewById(R.id.loading_ll);
		yue_rl = (RelativeLayout) findViewById(R.id.yue_rl);
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		back_iv = (ImageButton) findViewById(R.id.back_imgbtn);
		back_iv.setOnClickListener(btnClickListner);
		logout_button = (Button) findViewById(R.id.logout_button);
		logout_button.setOnClickListener(btnClickListner);

		username_tv = (TextView) findViewById(R.id.username_tv);
		dangqianyue_tv = (TextView) findViewById(R.id.dangqianyue_tv);
		bukeyongyue_tv = (TextView) findViewById(R.id.bukeyongyue_tv);
		phone_tv = (TextView) findViewById(R.id.phone_tv);

		username_tv.setText(sp.getString(SPkeys.username.getString(), ""));
		phone_tv.setText(sp.getString(SPkeys.userphone.getString(), ""));
		((RelativeLayout) findViewById(R.id.jiaoyijilu_rl))
				.setOnClickListener(btnClickListner);
		sartQueryYue();
		// if(!sp.getString(SPkeys.unavailableamount.getString(), "").isEmpty())
		// bukeyongyue_tv.setText("￥"+sp.getString(SPkeys.unavailableamount.getString(),
		// ""));
		// bukeyongyue_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		// dangqianyue_tv.setText("￥"+sp.getString(SPkeys.amount.getString(),
		// ""));
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		loading_ll.setVisibility(View.VISIBLE);
		frame_ani_iv.setBackgroundResource(R.anim.frame_rotate_ani);
		AnimationDrawable anim = (AnimationDrawable) frame_ani_iv
				.getBackground();
		anim.setOneShot(false);
		anim.start();
	}

	private void sartQueryYue() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(context);
				String str = "{\"username\":\""
						+ sp.getString(SPkeys.lastUsername.getString(), "")
						+ "\",pwd:\""
						+ sp.getString(SPkeys.lastPassword.getString(), "")
						+ "\"}";
				String param = "action=suppaylogin&sitekey=&userkey="
						+ MyApp.userkey + "&str=" + str + "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "suppaylogin" + str);
				// loginReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
				// param);
				loginReturnJson = HttpUtils.myPost(ma.getServeUrl(), param);
				Log.v("loginReturnJson", loginReturnJson);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 获取登录返回的数据
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(loginReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");
					sp = getSharedPreferences(SPkeys.SPNA.getString(), 0);
					if (state.equals("0000")) {
						String content = jsonObject.getString("d");

						// 以下代码将用户信息反序列化到SharedPreferences中
						UserIn user = JSONHelper.parseObject(content,
								UserIn.class);
						sp.edit()
								.putString(SPkeys.userid.getString(),
										user.getUserid()).commit();
						sp.edit()
								.putString(SPkeys.username.getString(),
										user.getUsername()).commit();
						sp.edit()
								.putString(SPkeys.amount.getString(),
										user.getAmount()).commit();
						sp.edit()
								.putString(SPkeys.siteid.getString(),
										user.getSiteid()).commit();
						sp.edit()
								.putString(SPkeys.userphone.getString(),
										user.getMobile()).commit();
						sp.edit()
								.putString(
										SPkeys.unavailableamount.getString(),
										user.getUnavailableamount()).commit();

						if (user.getUnavailableamount() != null
								&& !user.getUnavailableamount().isEmpty())
							bukeyongyue_tv.setText("￥"
									+ user.getUnavailableamount());
						bukeyongyue_tv.getPaint().setFlags(
								Paint.STRIKE_THRU_TEXT_FLAG);
						dangqianyue_tv
								.setText("￥" + user.getAmount() == null ? "0"
										: user.getAmount());
						loading_ll.setVisibility(View.GONE);
					} else if (state.equals("1003")) {
						new AlertDialog.Builder(context)
								.setTitle("用户名密码已更改，请重新登录")
								.setPositiveButton("确认", null).show();
						sp.edit().putString(SPkeys.userid.getString(), "")
								.commit();
						sp.edit().putString(SPkeys.username.getString(), "")
								.commit();
						sp.edit()
								.putBoolean(SPkeys.loginState.getString(),
										false).commit();
						sp.edit()
								.putString(SPkeys.lastUsername.getString(), "")
								.commit();
						sp.edit()
								.putBoolean(SPkeys.lastPassword.getString(),
										false).commit();
						startActivity(new Intent(context, Activity_Login.class));
						ActivityMyAccout.this.finish();
					} else {
						loading_ll.setVisibility(View.GONE);
						dangqianyue_tv.setText("查询失败");
						dangqianyue_tv.setTextColor(getResources().getColor(
								R.color.red));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	View.OnClickListener btnClickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				ActivityMyAccout.this.finish();
				break;
			case R.id.jiaoyijilu_rl:
				setResult(JILU, getIntent().putExtra("operation", "查看交易记录"));
				ActivityMyAccout.this.finish();
				break;
			case R.id.logout_button:
				new AlertDialog.Builder(context).setTitle("确认注销")
						.setNegativeButton("取消", null).setMessage("确认注销当前用户？")
						.setPositiveButton("注销", new OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								sp.edit()
										.putString(SPkeys.userid.getString(),
												"").commit();
								sp.edit()
										.putString(SPkeys.username.getString(),
												"").commit();
								sp.edit()
										.putBoolean(
												SPkeys.loginState.getString(),
												false).commit();
								startActivity(new Intent(context,
										Activity_Login.class));
								ActivityMyAccout.this.finish();
							}
						}).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ActivityMyAccout"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityMyAccout");
		MobclickAgent.onPause(this);

	}
}
