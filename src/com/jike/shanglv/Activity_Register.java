//用户注册
package com.jike.shanglv;

import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;


public class Activity_Register extends Activity {

	private ImageView back_imgbtn;
	private EditText uername_input_et, password_input_et, confirm_input_et,
			mobile_input_et, email_input_et, recommend_input_et,
			realname_input_et;
	private Button register_btn;
	private Context context;

	private CustomProgressDialog progressdialog;
	private SharedPreferences sp;
	private String registerReturnJson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_register);
			try {
				init();
			} catch (Exception e) {
				// TODO: handle exception
			}
			((MyApplication) getApplication()).addActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);

		uername_input_et = (EditText) findViewById(R.id.uername_input_et);
		password_input_et = (EditText) findViewById(R.id.password_input_et);
		confirm_input_et = (EditText) findViewById(R.id.confirm_input_et);
		mobile_input_et = (EditText) findViewById(R.id.mobile_input_et);
		email_input_et = (EditText) findViewById(R.id.email_input_et);
		recommend_input_et = (EditText) findViewById(R.id.recommend_input_et);
		realname_input_et = (EditText) findViewById(R.id.realname_input_et);

		register_btn = (Button) findViewById(R.id.register_btn);
		back_imgbtn = (ImageView) findViewById(R.id.back_imgbtn);

		register_btn.setOnClickListener(myListener);
		back_imgbtn.setOnClickListener(myListener);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(registerReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");
					JSONObject data = jsonObject.getJSONObject("d");

					if (state.equals("0000")) {
						// new AlertDialog.Builder(context).setTitle("注册成功")
						// .setMessage("注册成功,请登录！")
						// .setPositiveButton("确认",
						// new DialogInterface.OnClickListener() {
						// public void onClick(
						// DialogInterface dialog,
						// int id) {
						// startActivity(new Intent(
						// context,
						// Activity_Login.class));
						// }
						// }).show();
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, true);
						cad.setTitle("注册成功,请登录！");
						cad.setPositiveButton("立即登录", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(context,
										Activity_Login.class));
								cad.dismiss();
							}
						});
					} else {
						// new AlertDialog.Builder(context).setTitle("注册失败")
						// .setMessage(message)
						// .setPositiveButton("确认", null).show();
						String message = "";
						try {
							message = data.getJSONObject("d").getString("msg");
						} catch (Exception e) {
							message = data.getString("msg");
						}
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, true);
						cad.setTitle(message);
						cad.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
				break;
			}
		}
	};

	OnClickListener myListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_imgbtn:
				finish();
				break;
			case R.id.register_btn:
				if (checkValid())
					startRegister();
				break;
			default:
				break;
			}
		}
	};

	private void startRegister() {
		if (HttpUtils.showNetCannotUse(context)) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MyApp ma = new MyApp(context);
					String str = "{\"regCode\":\""
							+ recommend_input_et.getText().toString().trim()
							+ "\",\"loginName\":\""
							+ uername_input_et.getText().toString().trim()
							+ "\",\"phone\":\""
							+ mobile_input_et.getText().toString().trim()
							+ "\",\"loginPass\":\""
							+ password_input_et.getText().toString().trim()
							+ "\",\"realName\":\""
							+ realname_input_et.getText().toString().trim()
							+ "\",\"email\":\""
							+ email_input_et.getText().toString().trim()
							+ "\"}";

					String param = "action=userreg&str="
							+ str
							+ "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "userreg" + str);
					;
					registerReturnJson = HttpUtils.getJsonContent(
							ma.getServeUrl(), param);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("注册中，请稍候...");
		progressdialog.setCancelable(true);
		progressdialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		progressdialog.show();
	}

	/**
	 * 验证输入的合法性
	 * */
	private Boolean checkValid() {
		if (uername_input_et.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("用户名不能为空")
			// .setMessage("请输入用户名！").setPositiveButton("确定", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("用户名不能为空");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (!CommonFunc.isValidUserName(uername_input_et.getText().toString()
				.trim())) {
			// new AlertDialog.Builder(context).setTitle("用户名格式不正确")
			// .setMessage("为保证密码的安全性，请输入6-12位的数字、字母或下划线的组合！")
			// .setPositiveButton("确定", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("请输入用户名，由6-12位的数字、字母或下划线的组合！");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (password_input_et.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("密码不能为空")
			// .setMessage("请输入密码！").setPositiveButton("确定", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("请输入密码！");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		} else if (!CommonFunc.isValidPassword(password_input_et.getText()
				.toString().trim())) {
			// new AlertDialog.Builder(context).setTitle("密码格式不正确")
			// .setMessage("为保证密码的安全性，请输入6-20位的数字或字母的组合！")
			// .setPositiveButton("确定", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("为保证密码的安全性，请输入6-20位的数字或字母的组合！");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (!password_input_et.getText().toString().trim()
				.equals(confirm_input_et.getText().toString().trim())) {
			// new AlertDialog.Builder(context).setTitle("密码不一致")
			// .setMessage("请确保两次输入的密码相同！").setPositiveButton("确定", null)
			// .show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("两次输入密码不一致");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (!CommonFunc.isMobileNO(mobile_input_et.getText().toString().trim())) {
			// new AlertDialog.Builder(context).setTitle("手机号码格式不正确")
			// .setMessage("请输入合法的手机号码！").setPositiveButton("确定", null)
			// .show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("手机号码格式不正确");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (realname_input_et.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("姓名不能为空")
			// .setMessage("请输入真实姓名！").setPositiveButton("确定", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("姓名不能为空");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		// 邮箱地址不做必填项，如果有内容，则校验格式
		if (email_input_et.getText().toString().trim().length() != 0
				&& !CommonFunc.isEmail(email_input_et.getText().toString()
						.trim())) {
			// new AlertDialog.Builder(context).setTitle("邮箱格式不正确")
			// .setMessage("请输入合法的电子邮件名！").setPositiveButton("确定", null)
			// .show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("邮箱格式不正确");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (recommend_input_et.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("邀请码不能为空")
			// .setMessage("请输入邀请码！").setPositiveButton("确定", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("请输入邀请码！");
			cad.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
