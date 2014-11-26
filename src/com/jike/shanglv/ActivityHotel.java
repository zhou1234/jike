package com.jike.shanglv;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.NetAndJson.HttpUtils;


public class ActivityHotel extends Activity {

	private final int ruzhudate = 0, lidiandate = 1, ruzhucity = 2;
	private ImageButton back_imgbtn, home_imgbtn;
	private TextView city_tv, ruzhu_date_tv, lidian_date_tv, xingji_tv,
			jiage_tv;
	private com.jike.shanglv.Common.ClearEditText keywords_et;
	private LinearLayout city_ll, my_position_ll, ruzhu_date_ll,
			lidian_date_ll, xingji_ll, jiage_ll;
	private Button search_button;
	private Context context;
	InputMethodManager imm;
	private SharedPreferences sp;
	private Boolean isNearby = false;
	private double latitude, longtitude;
	private String myaddress = "";
	private int errorCode;// ��λ���
	private LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_hotel);
			initView();
			myNear();
			((MyApplication) getApplication()).addActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void myNear() {
		mLocationClient.start();
		mLocationClient.requestLocation();
		city_tv.setText("�Ҹ����ľƵ�");
		isNearby = true;
	}

	private void initView() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListener());
		InitLocation();

		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		city_tv = (TextView) findViewById(R.id.city_tv);
		ruzhu_date_tv = (TextView) findViewById(R.id.ruzhu_date_tv);
		lidian_date_tv = (TextView) findViewById(R.id.lidian_date_tv);
		xingji_tv = (TextView) findViewById(R.id.xingji_tv);
		jiage_tv = (TextView) findViewById(R.id.jiage_tv);
		keywords_et = (ClearEditText) findViewById(R.id.keywords_et);
		my_position_ll = (LinearLayout) findViewById(R.id.my_position_ll);
		ruzhu_date_ll = (LinearLayout) findViewById(R.id.ruzhu_date_ll);
		lidian_date_ll = (LinearLayout) findViewById(R.id.lidian_date_ll);
		xingji_ll = (LinearLayout) findViewById(R.id.xingji_ll);
		jiage_ll = (LinearLayout) findViewById(R.id.jiage_ll);
		city_ll = (LinearLayout) findViewById(R.id.city_ll);
		city_ll.setOnClickListener(clickListener);
		back_imgbtn.setOnClickListener(clickListener);
		home_imgbtn.setOnClickListener(clickListener);
		my_position_ll.setOnClickListener(clickListener);
		ruzhu_date_ll.setOnClickListener(clickListener);
		lidian_date_ll.setOnClickListener(clickListener);
		xingji_ll.setOnClickListener(clickListener);
		jiage_ll.setOnClickListener(clickListener);

		ruzhu_date_tv.setText(DateUtil.GetDateAfterToday(1));
		lidian_date_tv.setText(DateUtil.GetDateAfterToday(2));

		search_button = (Button) findViewById(R.id.chongzhi_button);
		search_button.setOnClickListener(clickListener);
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ:�߾���
		option.setCoorType("gcj02");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		int span = 1000;
		option.setScanSpan(span);// ���÷���λ����ļ��ʱ��Ϊ1000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {// Receive Location
			try {
				latitude = location.getLatitude();
				longtitude = location.getLongitude();
				myaddress = location.getAddrStr();
				errorCode = location.getLocType();
				// ((TextView)findViewById(R.id.test_tv)).setText(myaddress);
				if (errorCode == 63 || errorCode == 63 || errorCode == 67
						|| (errorCode > 500 && errorCode < 701)) {
					// Toast.makeText(context, "�����쳣���Զ���λʧ��", 0).show();
					city_tv.setText("�Ϻ�");
					isNearby = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				case R.id.back_imgbtn:
					finish();
					break;
				case R.id.home_imgbtn:
					startActivity(new Intent(context, MainActivity.class));
					break;
				case R.id.my_position_ll:
					myNear();
					break;
				case R.id.city_ll:
					Intent cityIntent = new Intent();
					cityIntent
							.setClass(
									context,
									com.jike.shanglv.SeclectCity.HotelCityActivity.class);
					startActivityForResult(cityIntent, ruzhucity);
					break;
				case R.id.ruzhu_date_ll:
					Intent dateIntent = new Intent();
					dateIntent.setClass(context,
							com.jike.shanglv.ShipCalendar.MainActivity.class);
					dateIntent.putExtra(
							com.jike.shanglv.ShipCalendar.MainActivity.TITLE,
							"��ס����");
					startActivityForResult(dateIntent, ruzhudate);
					break;
				case R.id.lidian_date_ll:
					Intent dateIntent1 = new Intent();
					dateIntent1.setClass(context,
							com.jike.shanglv.ShipCalendar.MainActivity.class);
					dateIntent1.putExtra(
							com.jike.shanglv.ShipCalendar.MainActivity.TITLE,
							"�������");
					startActivityForResult(dateIntent1, lidiandate);
					break;
				case R.id.xingji_ll:
					imm.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					// popupWindow_xingji.showAtLocation(buxian_xingji_btn,Gravity.BOTTOM,
					// 0, 0);

					iniPopupWindow(0, initXingjiData());
					pwMyPopWindow.showAtLocation(search_button, Gravity.BOTTOM,
							0, 0);
					break;
				case R.id.jiage_ll:
					imm.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					// popupWindow_jiage.showAtLocation(buxian_price_btn,Gravity.BOTTOM,
					// 0, 0);
					iniPopupWindow(1, initJiageData());
					pwMyPopWindow.showAtLocation(search_button, Gravity.BOTTOM,
							0, 0);
					break;
				case R.id.chongzhi_button:
					if (!sp.getBoolean(SPkeys.loginState.getString(), false)) {
						startActivity(new Intent(context, Activity_Login.class));
						break;
					}
					if (DateUtil.compareDateIsBefore(ruzhu_date_tv.getText()
							.toString(), lidian_date_tv.getText().toString())) {
						// new
						// AlertDialog.Builder(context).setTitle("��ס���ڲ��ܴ����������")
						// .setPositiveButton("֪����", null).show();
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, true);
						cad.setTitle("��ס���ڲ��ܴ����������");
						cad.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}
						});
						break;
					}
					if (HttpUtils.showNetCannotUse(context)) {
						break;
					}
					if (city_tv.getText().toString().equals("�Ҹ����ľƵ�")) {
						isNearby = true;
					}
					if (city_tv.getText().toString().equals("�Ҹ����ľƵ�")
							&& (myaddress == null || myaddress.equals(""))) {// ��λʧ��
						city_tv.setText("�Ϻ�");
						isNearby = false;
						Toast.makeText(context, "��λʧ�ܣ���ѡ����н��в�ѯ", 0).show();
						break;
					}
					Intent intents = new Intent(context,
							ActivityHotelSearchlist.class);
					intents.putExtra("nearby", isNearby);
					intents.putExtra("latitude", latitude);
					intents.putExtra("longtitude", longtitude);
					intents.putExtra("myaddress", myaddress);
					intents.putExtra("city", city_tv.getText());
					intents.putExtra("ruzhu_date", ruzhu_date_tv.getText()
							.toString());
					intents.putExtra("lidian_date", lidian_date_tv.getText()
							.toString());
					intents.putExtra("starlevel", xingji_tv.getText()
							.toString());
					intents.putExtra("price", jiage_tv.getText().toString());
					intents.putExtra("keywords", keywords_et.getText()
							.toString());
					startActivity(intents);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/*
	 * ѡ����л����ں������Ե�����
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (data == null)
				return;
			Bundle b = data.getExtras();
			String myDate = DateUtil.GetTodayDate();// ��ȡ��com.jike.jikepart.ShipCalendar.MainActivity�лش���ֵ
			switch (requestCode) {
			case ruzhudate:
				if (b != null && b.containsKey("pickedDate")) {
					myDate = b.getString("pickedDate");
					ruzhu_date_tv.setText(myDate);
					if (DateUtil.compareDateIsBefore(myDate, lidian_date_tv
							.getText().toString().trim())) {
						try {
							lidian_date_tv.setText(DateUtil
									.getSpecifiedDayAfter(myDate));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case lidiandate:
				if (b != null && b.containsKey("pickedDate")) {
					myDate = b.getString("pickedDate");
					lidian_date_tv.setText(myDate);
					if (DateUtil.compareDateIsBefore(ruzhu_date_tv.getText()
							.toString().trim(), myDate)) {
						try {
							if (DateUtil.IsMoreThanToday(myDate)) {
								ruzhu_date_tv.setText(DateUtil
										.getSpecifiedDayBefore(myDate));
							} else {
								ruzhu_date_tv.setText(DateUtil.GetTodayDate());
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case ruzhucity:
				if (b != null && b.containsKey("pickedCity")) {
					String myCity = b.getString("pickedCity");
					city_tv.setText(myCity);
					isNearby = false;
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private PopupWindow pwMyPopWindow;// popupwindow
	private ListView lvPopupList;
	private int currentID_XJ = 0;
	private int currentID_JG = 0;

	/*
	 * xjOrJg 0:�Ǽ���1���۸�
	 */
	private void iniPopupWindow(final int xjOrJg,
			final List<Map<String, Object>> list1) {
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.popupwindow_list_select, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout);
		pwMyPopWindow.setFocusable(true);// �������popupwindow�е�ListView�ſ��Խ��յ���¼�

		MyListAdapter adapter = new MyListAdapter(context, list1);
		adapter.setCurrentID(xjOrJg == 0 ? currentID_XJ : currentID_JG);
		lvPopupList.setAdapter(adapter);
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					if (xjOrJg == 0) {// 0:�Ǽ�
						xingji_tv.setText(list1.get(position).get("title")
								.toString());
						currentID_XJ = position;
						pwMyPopWindow.dismiss();
					} else if (xjOrJg == 1) {// 1���۸�
						jiage_tv.setText(list1.get(position).get("title")
								.toString());
						currentID_JG = position;
						pwMyPopWindow.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// ����popupwindow�Ŀ�Ⱥ͸߶�����Ӧ
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		pwMyPopWindow.setWidth(LayoutParams.FILL_PARENT);// lvPopupList.getMeasuredWidth()
		pwMyPopWindow.setHeight(LayoutParams.FILL_PARENT);// ((lvPopupList.getMeasuredHeight())*
															// list1.size());
		pwMyPopWindow.setAnimationStyle(R.style.AnimBottomPopup);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// ����popupwindow�����Ļ�����ط���ʧ
		pwMyPopWindow.setBackgroundDrawable(dw);// (new BitmapDrawable());//
												// ���ñ���ͼƬ�������ڲ��������ã�Ҫͨ������������
		pwMyPopWindow.setOutsideTouchable(true);// ����popupwindow�ⲿ��popupwindow��ʧ�����Ҫ�����popupwindowҪ�б���ͼƬ�ſ��Գɹ�������

		// �Ե�����ȫ��ѡ������OnTouchListener�����жϻ�ȡ����λ�ã������listview���������ٵ�����
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					View layout = inflater.inflate(
							R.layout.popupwindow_list_select, null);
					int height = lvPopupList.getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							pwMyPopWindow.dismiss();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
	}

	private ArrayList<Map<String, Object>> initXingjiData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "����");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "���Ǽ�/����");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "���Ǽ�/�ߵ�");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "���Ǽ�/����");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "���Ǽ�������");
		list.add(map);
		return list;
	}

	private ArrayList<Map<String, Object>> initJiageData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "����");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "��150����");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "��150-��300");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "��301-��450");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "��451-��600");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "��601-��1000");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "��1000����");
		list.add(map);
		return list;
	}

	private class MyListAdapter extends BaseAdapter {

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		private LayoutInflater inflater;
		List<Map<String, Object>> list;
		Context c;
		int currentID = 0;

		public MyListAdapter(Context context, List<Map<String, Object>> list2) {
			inflater = LayoutInflater.from(context);
			this.c = context;
			this.list = list2;
		}

		public void setList(ArrayList<Map<String, Object>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				Holder myHolder;
				if (convertView == null) {
					myHolder = new Holder();
					convertView = inflater.inflate(
							R.layout.item_train_baoxian_list_single, null);
					myHolder.title = (TextView) convertView
							.findViewById(R.id.title);
					myHolder.iv = (ImageView) convertView
							.findViewById(R.id.img);
					convertView.setTag(myHolder);
				} else {
					myHolder = (Holder) convertView.getTag();
				}
				if (position == this.currentID)
					myHolder.iv.setBackgroundDrawable(c.getResources()
							.getDrawable(R.drawable.radio_clk));
				else
					myHolder.iv.setBackgroundDrawable(c.getResources()
							.getDrawable(R.drawable.radio));
				myHolder.title.setText(list.get(position).get("title")
						.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

		class Holder {
			ImageView iv;
			TextView title;
		}

		public void setCurrentID(int currentID) {
			this.currentID = currentID;
		}
	}
}
