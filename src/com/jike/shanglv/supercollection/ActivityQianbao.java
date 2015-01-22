package com.jike.shanglv.supercollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jike.shanglv.Activity_Login;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.shanglv.Activity_Web_Pay;
import com.jike.shanglv.MyApp;
import com.jike.shanglv.MyApplication;
import com.jike.shanglv.R;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;
import com.jike.shanglv.NetAndJson.UserIn;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class ActivityQianbao extends Activity implements
		RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener {

	private LinearLayout user_ll, shoukuan_ll, jiaoyijilu_ll,
			jiaoyijilu_block_ll, shoukuan_block_ll;
	private TextView dangqianyue_tv, shijidaozhangjine_tv, shouxufei_tv;
	private EditText shoukuanjine_et;
	private SharedPreferences sp;
	private Button ok_button;
	private RefreshListView listview;
	// private ListView listview1;
	private RelativeLayout T0_rl, T3_rl, T6_rl;
	private ImageView user_imgbtn, T0_select_imgbtn, T3_select_imgbtn,
			T6_select_imgbtn, sk_iv, jyjl_iv;
	private double rate = 0f, realamount;
	private int time = 6, records = 0, curentPage = 1;
	private Drawable selectedDrawable, unselectedDrawable;
	Context context;
	private CustomProgressDialog progressdialog;
	private String recordReturnJson = "", loginReturnJson = "";
	private ArrayList<Record> records_list;
	ListAdapter adapter;
	Boolean isTimeout = true;
	private String amt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qianbao);
		initView();
		Intent intent = getIntent();
		if (intent != null) {
			amt = intent.getStringExtra("amt");
			shoukuanjine_et.setText(amt);
		}
		((MyApplication) getApplication()).addActivity(this);
		sartQueryUserInfo();
		findViewById(R.id.back_imgbtn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();

					}
				});
	}

	@Override
	protected void onResume() {
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		sartQueryUserInfo();
		MobclickAgent.onPageStart("ActivityQianbao"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		super.onResume();
	}

	private void initView() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		records_list = new ArrayList<Record>();
		selectedDrawable = context.getResources().getDrawable(
				R.drawable.slqb_time_cur_select);
		unselectedDrawable = context.getResources().getDrawable(
				R.drawable.slqb_time_select);
		shoukuan_ll = (LinearLayout) findViewById(R.id.shoukuan_ll);
		jiaoyijilu_ll = (LinearLayout) findViewById(R.id.jiaoyijilu_ll);
		dangqianyue_tv = (TextView) findViewById(R.id.dangqianyue_tv);
		shijidaozhangjine_tv = (TextView) findViewById(R.id.shijidaozhangjine_tv);
		shouxufei_tv = (TextView) findViewById(R.id.shouxufei_tv);
		shoukuanjine_et = (EditText) findViewById(R.id.shoukuanjine_et);
		ok_button = (Button) findViewById(R.id.ok_button);
		user_imgbtn = (ImageView) findViewById(R.id.user_imgbtn);
		user_ll = (LinearLayout) findViewById(R.id.user_ll);
		user_imgbtn.setOnClickListener(clickListener);
		user_ll.setOnClickListener(clickListener);
		T0_select_imgbtn = (ImageView) findViewById(R.id.T0_select_imgbtn);
		T3_select_imgbtn = (ImageView) findViewById(R.id.T3_select_imgbtn);
		T6_select_imgbtn = (ImageView) findViewById(R.id.T6_select_imgbtn);
		sk_iv = (ImageView) findViewById(R.id.sk_iv);
		jyjl_iv = (ImageView) findViewById(R.id.jyjl_iv);
		listview = (RefreshListView) findViewById(R.id.listview);
		// listview1=(ListView) findViewById(R.id.listview1);
		listview.setOnRefreshListener(this);
		listview.setOnLoadMoreListener(this);
		jiaoyijilu_block_ll = (LinearLayout) findViewById(R.id.jiaoyijilu_block_ll);
		shoukuan_block_ll = (LinearLayout) findViewById(R.id.shoukuan_block_ll);
		T0_rl = (RelativeLayout) findViewById(R.id.T0_rl);
		T3_rl = (RelativeLayout) findViewById(R.id.T3_rl);
		T6_rl = (RelativeLayout) findViewById(R.id.T6_rl);
		jiaoyijilu_ll.setOnClickListener(clickListener);
		shoukuan_ll.setOnClickListener(clickListener);
		T0_select_imgbtn.setOnClickListener(clickListener);
		T3_select_imgbtn.setOnClickListener(clickListener);
		T6_select_imgbtn.setOnClickListener(clickListener);
		T0_rl.setOnClickListener(clickListener);
		T3_rl.setOnClickListener(clickListener);
		T6_rl.setOnClickListener(clickListener);
		ok_button.setOnClickListener(clickListener);

		dangqianyue_tv.setText(sp.getString(SPkeys.amount.getString(), "0"));
		shoukuanjine_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isValidAmout(shoukuanjine_et.getText().toString().trim()))
					setShouxufei();
			}
		});

		// UpdateManager manager=new
		// UpdateManager(ActivityQianbao.this,"shanglvqianbao");
		// manager.checkForUpdates();
	}

	OnClickListener clickListener = new OnClickListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.user_ll:
			case R.id.user_imgbtn:
				startActivityForResult(new Intent(context,
						ActivityMyAccout.class), 0);
				break;
			case R.id.shoukuan_ll:
				jiaoyijilu_block_ll.setVisibility(View.GONE);
				shoukuan_block_ll.setVisibility(View.VISIBLE);
				sk_iv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.slqb_shoukuan_blue_btn));
				jyjl_iv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.slqb_jilu_gray_btn));
				break;
			case R.id.jiaoyijilu_ll:
				startQueryRecord();
				jiaoyijilu_block_ll.setVisibility(View.VISIBLE);
				shoukuan_block_ll.setVisibility(View.GONE);
				sk_iv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.slqb_shoukuan_gray_btn));
				jyjl_iv.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.slqb_jilu_blue_btn));
				break;
			case R.id.T0_rl:
			case R.id.T0_select_imgbtn:
				rate = 0.006f;
				time = 0;
				T0_select_imgbtn.setBackgroundDrawable(selectedDrawable);
				T3_select_imgbtn.setBackgroundDrawable(unselectedDrawable);
				T6_select_imgbtn.setBackgroundDrawable(unselectedDrawable);
				setShouxufei();
				break;
			case R.id.T3_rl:
			case R.id.T3_select_imgbtn:
				rate = 0.003f;
				time = 3;
				T3_select_imgbtn.setBackgroundDrawable(selectedDrawable);
				T0_select_imgbtn.setBackgroundDrawable(unselectedDrawable);
				T6_select_imgbtn.setBackgroundDrawable(unselectedDrawable);
				setShouxufei();
				break;
			case R.id.T6_rl:
			case R.id.T6_select_imgbtn:
				rate = 0f;
				time = 6;
				T6_select_imgbtn.setBackgroundDrawable(selectedDrawable);
				T3_select_imgbtn.setBackgroundDrawable(unselectedDrawable);
				T0_select_imgbtn.setBackgroundDrawable(unselectedDrawable);
				setShouxufei();
				break;
			case R.id.ok_button:
				if (shoukuanjine_et.getText().toString().trim().length() == 0) {
					new AlertDialog.Builder(context).setTitle("请输入收款金额！")
							.setPositiveButton("确认", null).show();
					break;
				}
				if (!isValidAmout(shoukuanjine_et.getText().toString().trim())) {
					new AlertDialog.Builder(context)
							.setTitle("请输入正确的收款金额，单笔限额两万元！")
							.setPositiveButton("确认", null).show();
					break;
				}
				String userid = sp.getString(SPkeys.userid.getString(), "");
				String siteid = sp.getString(SPkeys.siteid.getString(), "");
				// amount + time + realamount + userid + "_superpay";
				String sign = CommonFunc.MD5(shoukuanjine_et.getText()
						.toString().trim()
						+ time
						+ String.format("%.2f", realamount)
						+ userid
						+ "_superpay");
				MyApp ma = new MyApp(context);//
				String ur = String
						.format(getResources().getString(
								R.string.qb_pay_server_url), shoukuanjine_et
								.getText().toString().trim(), time,
								String.format("%.2f", realamount), userid,
								siteid, sign);
				// http: //
				// gatewayv3.51jp.cn/PayMent/BeginPay.aspx?orderID=%1$s&amp;amount=%2$s&amp;userid=%3$s&amp;paysystype=%4$s&amp;siteid=%5$s&amp;sign=%6$s
				ur += "&r=" + (int) (Math.random() * 1000);
				Intent intent = new Intent(context, Activity_Web_Pay.class);
				intent.putExtra(Activity_Web_Pay.URL, ur);
				intent.putExtra(Activity_Web_Pay.TITLE, "无卡收款");
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	Comparator<Record> comparator_time = new Comparator<Record>() {
		@Override
		public int compare(Record s1, Record s2) {
			if (s1.getPt() != null && s2.getPt() != null
					&& !s1.getPt().equals(s2.getPt())) {
				return s2.getPt().compareTo(s1.getPt());
			} else if (s1.getCt() != null && s2.getCt() != null
					&& !s1.getCt().equals(s2.getCt())) {
				return s2.getCt().compareTo(s1.getCt());
			} else
				return 0;
		}
	};

	private void setShouxufei() {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		String amoutString = shoukuanjine_et.getText().toString().trim();
		if (!amoutString.isEmpty()) {
			double shouxufei = (rate * Double.valueOf(amoutString));
			if (0 < shouxufei && shouxufei < 0.01) {
				shouxufei = 0.01;
			}
			realamount = ((Double.valueOf(amoutString)) - shouxufei);
			shouxufei_tv.setText("(手续费￥" + String.format("%.2f", shouxufei)
					+ ")");
			shijidaozhangjine_tv.setText("￥"
					+ String.format("%.2f", realamount));
		} else {
			shijidaozhangjine_tv.setText("");
			shouxufei_tv.setText("");
		}
	}

	private void startQueryRecord() {
		if (HttpUtils.showNetCannotUse(context)) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// action=flist&str={'s':'sha','e':hfe,'sd':'2014-01-28','userid':'649','siteid':'65'}
				MyApp ma = new MyApp(context);
				String str = "{\"currpage\":\"" + curentPage
						+ "\",\"pagesize\":\"" + 20 + "\",\"userid\":\""
						+ sp.getString(SPkeys.userid.getString(), "")
						+ "\",\"siteid\":\""
						+ sp.getString(SPkeys.siteid.getString(), "") + "\"}";
				String param = "action=suppayrecords&str=" + str + "&userkey="
						+ MyApp.userkey + "&sitekey=" + MyApp.sitekey
						+ "&sign="
						+ CommonFunc.MD5(MyApp.userkey + "suppayrecords" + str);
				// recordReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
				// param);
				recordReturnJson = HttpUtils.myPost(ma.getServeUrl(), param);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
				handler.sendEmptyMessageDelayed(2, 5000);
			}
		}).start();

		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("正在查询交易信息，请稍候...");
		progressdialog.setCancelable(true);
		progressdialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		progressdialog.show();
	}

	private void sartQueryUserInfo() {
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

				loginReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Log.v("loginReturnJson", loginReturnJson);
				Message msg = new Message();
				msg.what = 3;
				handler.sendMessage(msg);
			}
		}).start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				if (isTimeout) {
					new AlertDialog.Builder(context).setTitle("请求超时，请稍后再试！")
							.setPositiveButton("确认", null).show();
					progressdialog.dismiss();
				}
				break;
			case 1:
				isTimeout = false;
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(recordReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						createList(jsonObject.getJSONObject("d").getJSONArray(
								"o"));
						records = jsonObject.getJSONObject("d").getInt(
								"records");
						adapter = new ListAdapter(context, records_list);
						listview.setAdapter(adapter);
						// listview1.setAdapter(adapter);
						// setListViewHeightBasedOnChildren(listview1);
						if (records_list.size() == 0)
							new AlertDialog.Builder(context)
									.setTitle("未查到交易记录信息")
									.setPositiveButton("确认", null).show();
						listview.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Record ql = records_list.get(position - 1);
								Intent intents = new Intent(context,
										ActivityOrderDetail.class);
								intents.putExtra(
										ActivityRecordState.RECORDINFO,
										JSONHelper.toJSON(ql));
								startActivity(intents);
							}
						});

					} else {
						String message = jsonObject.getJSONObject("d")
								.getString("msg");
						new AlertDialog.Builder(context).setTitle("查询失败")
								.setMessage(message)
								.setPositiveButton("确认", null).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
				break;
			case 3:// 获取登录返回的数据
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

						dangqianyue_tv
								.setText("￥" + user.getAmount() != null ? user
										.getAmount() : "0");
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
						ActivityQianbao.this.finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	/**
	 * 构建list对象
	 * 
	 * @param flist_list
	 */
	private void createList(JSONArray flist_list) {
		for (int i = 0; i < flist_list.length(); i++) {
			try {
				Record hb = JSONHelper.parseObject(flist_list.getJSONObject(i),
						Record.class);
				records_list.add(hb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		records_list = removeDuplicteRecord(records_list);
	}

	private class ListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Record> recordList;

		public ListAdapter(Context context, List<Record> list1) {
			this.inflater = LayoutInflater.from(context);
			this.recordList = list1;
			Collections.sort(recordList, comparator_time);
		}

		@Override
		public int getCount() {
			return recordList.size();
		}

		@Override
		public Object getItem(int position) {
			return recordList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void refreshData(List<Record> data) {
			this.recordList = data;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_record_list, null);
			}
			TextView orderid_tv = (TextView) convertView
					.findViewById(R.id.orderid_tv);
			TextView bank_tv = (TextView) convertView
					.findViewById(R.id.bank_tv);
			TextView bankcard_tv = (TextView) convertView
					.findViewById(R.id.bankcard_tv);
			TextView receiveTime_tv = (TextView) convertView
					.findViewById(R.id.receiveTime_tv);
			TextView daozhangTime_tv = (TextView) convertView
					.findViewById(R.id.daozhangTime_tv);
			TextView state_tv = (TextView) convertView
					.findViewById(R.id.state_tv);
			TextView money_tv = (TextView) convertView
					.findViewById(R.id.money_tv);
			TextView time1_name_tv = (TextView) convertView
					.findViewById(R.id.time1_name_tv);
			TextView time2_name_tv = (TextView) convertView
					.findViewById(R.id.time2_name_tv);
			String state = recordList.get(position).getS();
			String bank = recordList.get(position).getBn();
			String bankCard = recordList.get(position).getBc();
			state_tv.setText(state);
			orderid_tv.setText(recordList.get(position).getNo());
			bank_tv.setText(bank.length() > 3 ? (bank.substring(0, 4) + "...")
					: "");
			bankcard_tv.setText(bankCard.length() > 4 ? (bankCard.substring(
					bankCard.length() - 4, bankCard.length())) : "");
			money_tv.setText(recordList.get(position).getA());
			receiveTime_tv.setText(recordList.get(position).getPt());
			if (state.equals(StateEnum.neworder.getString())
					|| state.equals(StateEnum.yishoukuan.getString())
					|| state.equals(StateEnum.ruzhangzhong.getString())) {
				state_tv.setTextColor(getResources().getColor(R.color.blue));
				daozhangTime_tv.setText(recordList.get(position).getSt());
				time1_name_tv.setText("入账时间:");
				time2_name_tv.setText("预计到账:");
				if (state.equals(StateEnum.ruzhangzhong.getString())) {
					state_tv.setTextColor(getResources().getColor(R.color.red));
				}
			} else if (state.equals(StateEnum.yiwancheng.getString())) {
				state_tv.setTextColor(getResources().getColor(R.color.red));
				daozhangTime_tv.setText(recordList.get(position).getRst());
				time1_name_tv.setText("入账时间:");
				time2_name_tv.setText("到账时间:");
			} else if (state.equals(StateEnum.yiquxiao.getString())) {
				state_tv.setTextColor(getResources().getColor(R.color.red));
				daozhangTime_tv.setText(recordList.get(position).getQt());
				time1_name_tv.setText("入账时间:");
				time2_name_tv.setText("取消时间:");
			}
			return convertView;
		}
	}

	@Override
	public void OnLoadMore() {
		LoadMoreDataAsynTask mLoadMoreAsynTask = new LoadMoreDataAsynTask();
		mLoadMoreAsynTask.execute();
	}

	@Override
	public void OnRefresh() {
		RefreshDataAsynTask mRefreshAsynTask = new RefreshDataAsynTask();
		mRefreshAsynTask.execute();
	}

	class RefreshDataAsynTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(3000);
				curentPage++;
				startQueryRecord();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.refreshData(records_list);
			listview.onRefreshComplete();
		}
	}

	class LoadMoreDataAsynTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(3000);
				curentPage++;
				startQueryRecord();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.refreshData(records_list);
			if (records_list.size() == Integer.valueOf(records)) {
				listview.onLoadMoreComplete(true);
			} else {
				listview.onLoadMoreComplete(false);
			}
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView2) {
		ListAdapter listAdapter = (ListAdapter) listView2.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView2);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView2.getLayoutParams();
		params.height = totalHeight
				+ (listView2.getDividerHeight() * (listAdapter.getCount() - 1));
		listView2.setLayoutParams(params);
	}

	// 去除重复的订单
	public static ArrayList<Record> removeDuplicteRecord(
			ArrayList<Record> userList) {
		Set<Record> s = new TreeSet<Record>(new Comparator<Record>() {
			@Override
			public int compare(Record o1, Record o2) {
				return o1.getNo().compareTo(o2.getNo());
			}
		});
		s.addAll(userList);
		return new ArrayList<Record>(s);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ActivityMyAccout.JILU) {
			jiaoyijilu_ll.performClick();
		}
	}

	public static boolean isValidAmout(String amout) {
		if (!amout.isEmpty() && Float.valueOf(amout) > 20000) {
			return false;
		}
		if (amout.startsWith("0")) {
			return false;
		}
		String str = "^[1-9]\\d*(\\.\\d+)?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(amout);
		return m.matches();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityQianbao");
		MobclickAgent.onPause(this);

	}
}
