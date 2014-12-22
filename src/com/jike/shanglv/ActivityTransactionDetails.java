package com.jike.shanglv;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Common.RefreshListView;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.Models.Details;
import com.jike.shanglv.NetAndJson.HttpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityTransactionDetails extends Activity implements
		View.OnClickListener, RefreshListView.IOnRefreshListener,
		RefreshListView.IOnLoadMoreListener {
	private RefreshListView listView;
	private ImageButton back_imgbtn;
	private ImageButton home_imgbtn;
	private Context context;
	private String trainsReturnJson;
	private SharedPreferences sp;
	private String pgindex = "1";
	private String pgsize = "20000";
	private String starttime;
	private String endtime;
	private CustomProgressDialog progressdialog;
	private List<Details> list_details;
	private int i = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_details);
		init();
		startQuery();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				try {
					if (progressdialog != null) {
						progressdialog.dismiss();
					}
					if (trainsReturnJson.length() == 0) {
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, true);
						cad.setTitle("查询失败");
						cad.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}
						});
						break;
					}

					JSONObject jsonObject = new JSONObject(trainsReturnJson);
					String state = jsonObject.getString("c");
					if (state.equals("0000")) {
						Details details = null;
						JSONArray array = jsonObject.getJSONArray("d");
						for (int i = 0; i < array.length(); i++) {
							details = new Details();
							JSONObject object = array.getJSONObject(i);
							details.setOrderno(object.getString("orderno"));
							details.setCashtradeapplication(object
									.getString("cashtradeapplication"));
							details.setInamount(object.getString("amount"));
							details.setCurrentsettlementbalance(object
									.getString("currentsettlementbalance"));
							details.setCreatetime(object
									.getString("createtime"));
							details.setOutorderno(object
									.getString("outorderno"));
							details.setRemark(object.getString("remark"));
							list_details.add(details);
						}
					} else {
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, true);
						cad.setTitle("查询失败");
						cad.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (list_details.size() < 10) {
					listView.onLoadMoreComplete(true);
				} else {
					listView.onLoadMoreComplete(false);
				}
				listView.setAdapter(new MyAdapter());
				break;

			}
		}

	};

	private void startQuery() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				MyApp ma = new MyApp(context);
				JSONObject str = new JSONObject();
				try {
					str.put("pgindex", pgindex);
					str.put("pgsize", pgsize);
					str.put("starttime", starttime);
					str.put("endtime", endtime);
					str.put("userid",
							sp.getString(SPkeys.userid.getString(), ""));

					String param = "action=cashtraderecord&str="
							+ str.toString()
							+ "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "cashtraderecord" + str) + "&sitekey="
							+ MyApp.sitekey;
					trainsReturnJson = HttpUtils.getJsonContent(
							ma.getServeUrl(), param);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();
		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("正在查询，请稍候...");
		progressdialog.setCancelable(true);
		progressdialog.show();

	}

	private void init() {
		list_details = new ArrayList<Details>();
		context = this;
		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(this);
		home_imgbtn.setOnClickListener(this);
		listView = (RefreshListView) findViewById(R.id.refreshListView1);
		listView.setOnRefreshListener(this);
		listView.setOnLoadMoreListener(this);

		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);

		starttime = DateUtil.GetDateAfterToday(-30);
		endtime = DateUtil.GetDateAfterToday(1);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.home_imgbtn:
			startActivity(new Intent(context, MainActivityN.class));
			break;

		case R.id.back_imgbtn:
			finish();
			break;
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list_details.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_details.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			Holder holder = null;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.listview_item,
						arg2, false);
				holder = new Holder(view);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}
			TextView tv_orderId = holder.getTv_orderId();
			TextView tv_source = holder.getTv_source();
			TextView tv_chuZhang = holder.getTv_chuZhang();
			TextView tv_jieYu = holder.getTv_jieYu();
			TextView tv_time = holder.getTv_time();
			TextView tv_pingTaiDingDan = holder.getTv_pingTaiDingDan();
			TextView tv_beiZhu = holder.getTv_beiZhu();
			TextView tv_chuRu = holder.getTv_chuRu();

			tv_orderId.setText(list_details.get(arg0).getOrderno());
			tv_source.setText(list_details.get(arg0).getCashtradeapplication());
			tv_jieYu.setText(list_details.get(arg0)
					.getCurrentsettlementbalance());
			tv_time.setText(list_details.get(arg0).getCreatetime());
			tv_pingTaiDingDan.setText(list_details.get(arg0).getOutorderno());
			tv_beiZhu.setText(list_details.get(arg0).getRemark());
			String amt = list_details.get(arg0).getInamount().trim();
			if (!amt.equals("")) {
				if (amt.charAt(0) == '-') {
					tv_chuZhang.setTextColor(Color.RED);
					tv_chuRu.setText("出账:");
				} else {
					tv_chuZhang.setTextColor(Color.BLUE);
					tv_chuRu.setText("入账:");
				}
			}else{
				amt="0.00";
			}
			tv_chuZhang.setText(amt);
			return view;
		}
	}

	class Holder {
		View view;
		TextView tv_chuRu;
		TextView tv_orderId;
		TextView tv_source;
		TextView tv_chuZhang;
		TextView tv_jieYu;
		TextView tv_time;
		TextView tv_pingTaiDingDan;
		TextView tv_beiZhu;

		public Holder(View view) {
			this.view = view;
		}

		public TextView getTv_orderId() {
			if (tv_orderId == null) {
				tv_orderId = (TextView) view.findViewById(R.id.tv_orderId);
			}
			return tv_orderId;
		}

		public TextView getTv_source() {
			if (tv_source == null) {
				tv_source = (TextView) view.findViewById(R.id.tv_source);
			}
			return tv_source;
		}

		public TextView getTv_chuZhang() {
			if (tv_chuZhang == null) {
				tv_chuZhang = (TextView) view.findViewById(R.id.tv_chuZhang);
			}
			return tv_chuZhang;
		}

		public TextView getTv_jieYu() {
			if (tv_jieYu == null) {
				tv_jieYu = (TextView) view.findViewById(R.id.tv_jieYu);
			}
			return tv_jieYu;
		}

		public TextView getTv_time() {
			if (tv_time == null) {
				tv_time = (TextView) view.findViewById(R.id.tv_time);
			}
			return tv_time;
		}

		public TextView getTv_pingTaiDingDan() {
			if (tv_pingTaiDingDan == null) {
				tv_pingTaiDingDan = (TextView) view
						.findViewById(R.id.tv_pingTaiDingDan);
			}
			return tv_pingTaiDingDan;
		}

		public TextView getTv_beiZhu() {
			if (tv_beiZhu == null) {
				tv_beiZhu = (TextView) view.findViewById(R.id.tv_beiZhu);
			}
			return tv_beiZhu;
		}

		public TextView getTv_chuRu() {
			if (tv_chuRu == null) {
				tv_chuRu = (TextView) view.findViewById(R.id.tv_chuRu);
			}
			return tv_chuRu;
		}

	}

	@Override
	public void OnLoadMore() {
		try {
			LoadMoreDataAsynTask mLoadMoreAsynTask = new LoadMoreDataAsynTask();
			mLoadMoreAsynTask.execute();
		} catch (Exception e) {
		}
	}

	@Override
	public void OnRefresh() {
		try {
			RefreshDataAsynTask mRefreshAsynTask = new RefreshDataAsynTask();
			mRefreshAsynTask.execute();
		} catch (Exception e) {
		}
	}

	class RefreshDataAsynTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				starttime = DateUtil.GetDateAfterToday(-30 - 30 * i);
				endtime = DateUtil.GetDateAfterToday(1 - 30 * i);
				i++;
				Thread.sleep(3000);
				startQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				listView.onRefreshComplete();
			} catch (Exception e) {
			}
		}
	}

	class LoadMoreDataAsynTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(6000);
				starttime = DateUtil.GetDateAfterToday(-30 - 30 * i);
				endtime = DateUtil.GetDateAfterToday(1 - 30 * i);
				i++;
				startQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			listView.onLoadMoreComplete(false);

		}
	}

}
