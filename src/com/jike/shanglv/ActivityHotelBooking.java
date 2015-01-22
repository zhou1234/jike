package com.jike.shanglv;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Common.IDCard;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.Models.HotelRoom;
import com.jike.shanglv.Models.HotelRoomComfirm;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.umeng.analytics.MobclickAgent;

public class ActivityHotelBooking extends Activity {

	protected static final int ROOMSRI_MSG = 1;
	protected static final int ORDER_MSG = 2;
	private final int ruzhudate = 0, lidiandate = 1;// ѡ�������������
	private ImageButton back_imgbtn, home_imgbtn;
	private ImageView contact_person_phone_iv;
	private TextView commit_order_tv, hotel_name_tv, xingji_tv, ruzhu_date_tv,
			lidian_date_tv, room_count_tv, will_arrive_time_tv, roomType_tv,
			total_price_tv, garantee_desc_tv, creadit_card_validity_tv,
			identificationType_tv;
	private com.jike.shanglv.Common.ClearEditText name_cet1, name_cet2,
			name_cet3, name_cet4, name_cet5, identificationNum_et,
			creditCard_num_cet, cvv_num_cet, chikaren_name_cet,
			creditCard_identificationNum_cet, contact_person_phone_cet;
	private RelativeLayout ruzhuren_rl2, ruzhuren_rl3, ruzhuren_rl4,
			ruzhuren_rl5, ruzhurenID_rl, creadit_card_validity_rl,
			identificationType_rl;
	private LinearLayout ruzhu_date_ll, lidian_date_ll, Garantee_LL;
	private Button shenfenzheng_btn, huzhao_btn, qita_btn;
	private Context context;

	private PopupWindow popupWindow_order_room_count,
			popupWindow_order_will_arrive_time,
			popupWindow_hotel_guarantee_identification_type;
	private View popupWindowView_order_room_count,
			popupWindowView_order_will_arrive_time,
			popupWindowView_hotel_guarantee_identification_type;
	InputMethodManager imm;
	private SharedPreferences sp;
	private int room_count = 1, guarantee_room_count = 6,
			guarantee_identification_type_isID = 0;// ���û�ҪԤ���ķ�����room_count����guarantee_room_countʱ��Ҫ���������ÿ�����֤��:0���֤1����2����
	private Boolean need_guarantee = false, time_need_guarantee = false,
			roomcount_need_guarantee = false, isSevenDayHotel = false;// �Ƿ���Ҫ�������Ƿ�ʱ��ε������Ƿ񳬹��������������Ƿ�����Ƶ�
	private String ruzhu_date, lidian_date, hotelId, hotelName, starLevel,
			creditYear, creditMonth,// ���ÿ�����Ч����
			orderReturnJson, roomsriReturnJson, successOrderId;// �ύ�����󷵻صĶ�����
	private HotelRoom roomInfoObject;
	private HotelRoomComfirm hotelRoomComfirm;
	private CustomProgressDialog progressdialog;
	private ArrayList<Map<String, Object>> arriveTimeList = new ArrayList<Map<String, Object>>();
	int totalPrice = 0;// һ�䷿N��ķ��� ��ʾʱ����Է�����

	private int month, hour, day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_hotel_booking);
			initView();
			getDate();
			getIntentString();
			startQueryRoomsRI();
			((MyApplication) getApplication()).addActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getIntentString() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("hotelId"))
				hotelId = bundle.getString("hotelId");
			if (bundle.containsKey("hotelName"))
				hotelName = bundle.getString("hotelName");
			if (bundle.containsKey("starLevel"))
				starLevel = bundle.getString("starLevel");
			if (bundle.containsKey("ruzhu_date"))
				ruzhu_date = bundle.getString("ruzhu_date");// ��ס����
			if (bundle.containsKey("lidian_date"))
				lidian_date = bundle.getString("lidian_date");
			if (bundle.containsKey("roomInfo")) {
				String roomInfo = bundle.getString("roomInfo");
				try {
					roomInfoObject = new HotelRoom(new JSONObject(roomInfo),
							true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		hotel_name_tv.setText(hotelName);
		xingji_tv.setText(starLevel);
		try {
			ruzhu_date_tv.setText(DateUtil.getMonthDayDate(ruzhu_date));
			lidian_date_tv.setText(DateUtil.getMonthDayDate(lidian_date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		roomType_tv.setText(roomInfoObject.getTitle());
	}

	private void initView() {
		context = this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(clickListener);
		home_imgbtn.setOnClickListener(clickListener);

		hotel_name_tv = (TextView) findViewById(R.id.hotel_name_tv);
		xingji_tv = (TextView) findViewById(R.id.xingji_tv);
		ruzhu_date_tv = (TextView) findViewById(R.id.ruzhu_date_tv);
		lidian_date_tv = (TextView) findViewById(R.id.lidian_date_tv);
		room_count_tv = (TextView) findViewById(R.id.room_count_tv);
		will_arrive_time_tv = (TextView) findViewById(R.id.will_arrive_time_tv);
		will_arrive_time_tv.setOnClickListener(clickListener);
		commit_order_tv = (TextView) findViewById(R.id.commit_order_tv);
		roomType_tv = (TextView) findViewById(R.id.roomType_tv);
		total_price_tv = (TextView) findViewById(R.id.total_price_tv);
		garantee_desc_tv = (TextView) findViewById(R.id.garantee_desc_tv);
		creadit_card_validity_tv = (TextView) findViewById(R.id.creadit_card_validity_tv);
		identificationType_tv = (TextView) findViewById(R.id.identificationType_tv);
		identificationType_tv.setOnClickListener(clickListener);

		name_cet1 = (ClearEditText) findViewById(R.id.name_cet1);
		name_cet2 = (ClearEditText) findViewById(R.id.name_cet2);
		name_cet3 = (ClearEditText) findViewById(R.id.name_cet3);
		name_cet4 = (ClearEditText) findViewById(R.id.name_cet4);
		name_cet5 = (ClearEditText) findViewById(R.id.name_cet5);
		contact_person_phone_cet = (ClearEditText) findViewById(R.id.contact_person_phone_cet);
		identificationNum_et = (ClearEditText) findViewById(R.id.identificationNum_et);
		creditCard_num_cet = (ClearEditText) findViewById(R.id.creditCard_num_cet);
		cvv_num_cet = (ClearEditText) findViewById(R.id.cvv_num_cet);
		chikaren_name_cet = (ClearEditText) findViewById(R.id.chikaren_name_cet);
		creditCard_identificationNum_cet = (ClearEditText) findViewById(R.id.creditCard_identificationNum_cet);
		ruzhuren_rl2 = (RelativeLayout) findViewById(R.id.ruzhuren_rl2);
		ruzhuren_rl3 = (RelativeLayout) findViewById(R.id.ruzhuren_rl3);
		ruzhuren_rl4 = (RelativeLayout) findViewById(R.id.ruzhuren_rl4);
		ruzhuren_rl5 = (RelativeLayout) findViewById(R.id.ruzhuren_rl5);
		ruzhurenID_rl = (RelativeLayout) findViewById(R.id.ruzhurenID_rl);
		contact_person_phone_iv = (ImageView) findViewById(R.id.contact_person_phone_iv);
		contact_person_phone_iv.setOnClickListener(clickListener);
		commit_order_tv.setOnClickListener(clickListener);
		room_count_tv.setOnClickListener(clickListener);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		popupWindowView_hotel_guarantee_identification_type = inflater.inflate(
				R.layout.popupwindow_hotel_guarantee_identification_type, null);
		popupWindow_hotel_guarantee_identification_type = new PopupWindow(
				popupWindowView_hotel_guarantee_identification_type,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow_hotel_guarantee_identification_type
				.setBackgroundDrawable(new BitmapDrawable());
		// ����PopupWindow�ĵ�������ʧЧ��
		popupWindow_hotel_guarantee_identification_type
				.setAnimationStyle(R.style.popupAnimation);
		shenfenzheng_btn = (Button) popupWindowView_hotel_guarantee_identification_type
				.findViewById(R.id.shenfenzheng_btn);
		huzhao_btn = (Button) popupWindowView_hotel_guarantee_identification_type
				.findViewById(R.id.huzhao_btn);
		qita_btn = (Button) popupWindowView_hotel_guarantee_identification_type
				.findViewById(R.id.qita_btn);
		shenfenzheng_btn.setTag("1");
		huzhao_btn.setTag("2");
		qita_btn.setTag("3");
		shenfenzheng_btn
				.setOnClickListener(hotel_guarantee_identification_type_popupClickListener);
		huzhao_btn
				.setOnClickListener(hotel_guarantee_identification_type_popupClickListener);
		qita_btn.setOnClickListener(hotel_guarantee_identification_type_popupClickListener);

		ruzhu_date_ll = (LinearLayout) findViewById(R.id.ruzhu_date_ll);
		lidian_date_ll = (LinearLayout) findViewById(R.id.lidian_date_ll);
		ruzhu_date_ll.setOnClickListener(clickListener);
		lidian_date_ll.setOnClickListener(clickListener);

		Garantee_LL = (LinearLayout) findViewById(R.id.Garantee_LL);
		creadit_card_validity_rl = (RelativeLayout) findViewById(R.id.creadit_card_validity_rl);
		identificationType_rl = (RelativeLayout) findViewById(R.id.identificationType_rl);
		creadit_card_validity_rl.setOnClickListener(clickListener);
		identificationType_rl.setOnClickListener(clickListener);

		arriveTimeList = initArriveTimeData();

		// ���ڳ�����ϵ�ˣ�ֱ�ӷ����ϴζ�Ʊʱ����ϵ���ֻ��ţ����������򷵻ر����ֻ�����
		if (sp.getString(SPkeys.hoteContactPhone.getString(), "").equals("")) {
			if (CommonFunc.getPhoneNumber(context).equals("")) {
				contact_person_phone_cet.setText(sp.getString(
						SPkeys.userphone.getString(), ""));
			} else {
				contact_person_phone_cet.setText(CommonFunc
						.getPhoneNumber(context));
			}
		} else {
			contact_person_phone_cet.setText(sp.getString(
					SPkeys.hoteContactPhone.getString(), ""));
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
					startActivity(new Intent(context, MainActivityN.class));
					break;
				case R.id.contact_person_phone_iv:
					startActivityForResult(
							new Intent(
									context,
									com.jike.shanglv.SeclectCity.ContactListActivity.class),
							13);
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
				case R.id.room_count_tv:
					imm.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					// popupWindow_order_room_count.showAtLocation(one_room_btn,Gravity.BOTTOM,
					// 0, 0);
					iniPopupWindow(0, initRoomCountData());
					pwMyPopWindow.showAtLocation(back_imgbtn, Gravity.BOTTOM,
							0, 0);
					break;
				case R.id.will_arrive_time_tv:
					imm.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					// popupWindow_order_will_arrive_time.showAtLocation(time1_btn,Gravity.BOTTOM,
					// 0, 0);

					iniPopupWindow(1, arriveTimeList);// ����ʱ������ݶ�̬�仯
					pwMyPopWindow.showAtLocation(back_imgbtn, Gravity.BOTTOM,
							0, 0);
					break;
				case R.id.identificationType_rl:
				case R.id.identificationType_tv:
					imm.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					popupWindow_hotel_guarantee_identification_type
							.showAtLocation(shenfenzheng_btn, Gravity.BOTTOM,
									0, 0);
					break;
				case R.id.creadit_card_validity_rl:
					Calendar c = Calendar.getInstance();
					new DatePickerDialog(context,
							new DatePickerDialog.OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									String month = String
											.valueOf((monthOfYear + 1));
									creditYear = year + "";
									creditMonth = month + "";
									if (month.length() == 1)
										month = "0" + month;
									creadit_card_validity_tv.setText(month
											+ "/"
											+ String.valueOf(year).substring(2,
													4));
								}
							}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 0)
							.show();

					break;
				case R.id.commit_order_tv:
					if (!validInput())
						break;
					startCommitHotelOrder();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void startQueryRoomsRI() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// url?action=roomsrid&sign=7745955d2500de4d473e7badbe5c904d&userkey=2bfc0c48923cf89de19f6113c127ce81&sitekey=defage
					// &str={'hid':'31725',"tm1":"2014-05-12","tm2":"2014-05-13","sid":"65","uid":"3299",
					// "rid":"6246","pid":"56162461294081","suppid":"4081","rooms":"1"}
					MyApp ma = new MyApp(context);
					String str = "";
					try {
						str = "{\"hid\":\"" + hotelId + "\",\"tm1\":\""
								+ ruzhu_date + "\",\"tm2\":\"" + lidian_date
								+ "\",\"rid\":\"" + roomInfoObject.getRid()
								+ "\",\"pid\":\"" + roomInfoObject.getPlanid()
								+ "\",\"suppid\":\""
								+ roomInfoObject.getHotelsupplier()
								+ "\",\"rooms\":\"" + 1 + "\",\"uid\":\""
								+ sp.getString(SPkeys.userid.getString(), "")
								+ "\",\"sid\":\"65\"}";
					} catch (Exception e) {
						e.printStackTrace();
					}
					String param = "action=roomsrid&str="
							+ str
							+ "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sitekey="
							+ MyApp.sitekey
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "roomsrid" + str);
					roomsriReturnJson = HttpUtils.getJsonContent(
							ma.getServeUrl(), param);
					Message msg = new Message();
					msg.what = ROOMSRI_MSG;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void startCommitHotelOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// url?action=hotelorder&sign=7745955d2500de4d473e7badbe5c904d&userkey=2bfc0c48923cf89de19f6113c127ce81&sitekey=defage&str=����
					// &str=
					// tm1����ס��tm2����꣬uid���û�ID��sid����վID��pid���۸�ƻ�ID��rid������ID��hid���Ƶ�ID��suppid:���͹�Ӧ��,roomtype��0�ָ�1Ԥ����hname���Ƶ�����rname����������lasttime������ʱ�䣬cityname��������,amount����contacts����ϵ�ˣ�phone����ϵ�绰,email�����䣬��iscard���Ƿ񵣱�1��0��
					// cardno:���ÿ���,year:��Ч������,month:��Ч������,code:CVV2��,name:�ֿ�������,idtype:֤������,idno:֤������,isInvoice:�Ƿ���Ҫ��Ʊ1��0��invoiceType����Ʊ���ͣ�invoiceTitle����Ʊ̧ͷ��recipient���ռ��ˣ�invoicePhone���ֻ��ţ�postCode����
					// �࣬postAddress����ϸ��ַ,allotmenttype:allotmenttype,ratetype:ratetype,facepaytype:facepaytype,faceprice:faceprice,includebreakfastqty2:includebreakfastqty2,
					// supplierid:supplierid
					// cost���ɱ���rooms����������passenger���ÿ�������sumrate���ܷ��㣬

					MyApp ma = new MyApp(context);
					String str = "";
					String idno = "";// ����Ƶ����ÿ�����ʱ�����֤���벻��ʾ�ˣ��������û���Ϣ��֤����.�ύʱֻ��һ���ֶ�
					if (isSevenDayHotel)
						idno = identificationNum_et.getText().toString().trim();
					else
						idno = creditCard_identificationNum_cet.getText()
								.toString().trim();
					try {
						str = "{\"hid\":\""
								+ hotelId
								+ "\",\"tm1\":\""
								+ ruzhu_date
								+ "\",\"tm2\":\""
								+ lidian_date
								+ "\",\"rid\":\""
								+ roomInfoObject.getRid()
								+ "\",\"pid\":\""
								+ roomInfoObject.getPlanid()
								+ "\",\"suppid\":\""
								+ roomInfoObject.getHotelsupplier()
								+ "\",\"hname\":\""
								+ hotelRoomComfirm.getHotelName()
								+ "\",\"rname\":\""
								+ hotelRoomComfirm.getRoomName()
								+ "\",\"lasttime\":\""
								+ will_arrive_time_tv.getText().toString()
										.replace("֮ǰ", "").replace("֮��", "")
								+ "\",\"amount\":\""
								+ total_price_tv.getText().toString()
										.replace("��", "")
								+ "\",\"phone\":\""
								+ contact_person_phone_cet.getText()
								+ "\",\"contacts\":\""
								+ sp.getString(SPkeys.username.getString(), "")
								+ "\",\"iscard\":\""
								+ (need_guarantee ? "1" : "0")
								+ "\",\"cardno\":\""
								+ creditCard_identificationNum_cet.getText()
								+ "\",\"year\":\""
								+ creditYear
								+ "\",\"month\":\""
								+ creditMonth
								+ "\",\"code\":\""
								+ cvv_num_cet.getText()
								+ "\",\"name\":\""
								+ chikaren_name_cet.getText()
								+ "\",\"idtype\":\""
								+ guarantee_identification_type_isID
								+ "\",\"idno\":\""
								+ idno
								+ "\",\"isInvoice\":\"0\",\"invoiceType\":\"\",\"invoiceTitle\":\"\",\"recipient\":\"\",\"invoicePhone\":\"\",\"postCode\":\"\",\"postAddress\":\"\",\""
								+ "allotmenttype\":\""
								+ hotelRoomComfirm.getAllotmenttype()
								+ "\",\"ratetype\":\""
								+ hotelRoomComfirm.getRatetype()
								+ "\",\"facepaytype\":\""
								+ hotelRoomComfirm.getFacepaytype()
								+ "\",\"faceprice\":\""
								+ hotelRoomComfirm.getFaceprice()
								+ "\",\"includebreakfastqty2\":\""
								+ hotelRoomComfirm.getIncludebreakfastqty2()
								+ "\",\"supplierid\":\""
								+ hotelRoomComfirm.getSupplierid()
								+ "\",\"rooms\":\""
								+ room_count
								+ "\",\"passenger\":\""
								+ getPassengers()
								+ "\",\"cost\":\"\",\"sumrate\":\""
								+ hotelRoomComfirm.getAllrate()
								+ "\",\"roomtype\":\""
								+ hotelRoomComfirm.getRoomtype()
								+ "\",\"uid\":\""
								+ sp.getString(SPkeys.userid.getString(), "")
								+ "\",\"sid\":\"65\",\"cityname\":\"\",\"email\":\"\"}";
					} catch (Exception e) {
						e.printStackTrace();
					}
					String orgin = ma.getHm()
							.get(PackageKeys.ORGIN.getString()).toString();
					String param = "?action=hotelorder&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sitekey="
							+ MyApp.sitekey
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "hotelorder" + str) + "&orgin=" + orgin;
					// orderReturnJson =
					// HttpUtils.getJsonContent(ma.getServeUrl(),
					// param);
					// try {
					// str = URLEncoder.encode(str, "utf-8");
					// } catch (UnsupportedEncodingException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					orderReturnJson = HttpUtils.myPost(
							ma.getServeUrl() + param, "&str=" + str);
					Message msg = new Message();
					msg.what = ORDER_MSG;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("�����ύ���������Ժ�...");
		progressdialog.setCancelable(true);
		progressdialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		progressdialog.show();
	}

	private String getPassengers() {
		String string = "";
		int count = Integer.valueOf(room_count_tv.getText().toString()
				.substring(0, 1));
		string = name_cet1.getText().toString() + ","
				+ name_cet2.getText().toString() + ","
				+ name_cet3.getText().toString() + ","
				+ name_cet4.getText().toString() + ","
				+ name_cet5.getText().toString();
		if (count == 1) {
			string = string.replace(",,,,", "");
		} else if (count == 2) {
			string = string.replace(",,,", "");
		} else if (count == 3) {
			string = string.replace(",,", "");
		} else if (count == 4) {
			string = string.substring(0, string.length() - 2);
		}
		return string;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ROOMSRI_MSG:
				JSONTokener jsonParser;
				jsonParser = new JSONTokener(roomsriReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						if (jsonObject.getJSONObject("d").length() == 0) {
							// new
							// AlertDialog.Builder(context).setTitle("δ��ѯ������").setPositiveButton("ȷ��",
							// null).show();
							break;
						}
						jsonObject = jsonObject.getJSONObject("d");
						hotelRoomComfirm = new HotelRoomComfirm(jsonObject);

						if (hotelRoomComfirm.getPrices() != null) {
							float f = Float.valueOf(hotelRoomComfirm
									.getPrices().getTotalPrice());
							totalPrice = (int) f;
							// DecimalFormat df = new DecimalFormat("#.#");
							// totalPrice = Double.parseDouble(df
							// .format(totalPrice));
						}
						total_price_tv.setText("��" + totalPrice * room_count);
						if (!Boolean.valueOf(hotelRoomComfirm.getYuding()
								.trim())) {
							commit_order_tv.setEnabled(false);
							total_price_tv.setText("������");
							commit_order_tv.setBackgroundColor(getResources()
									.getColor(R.color.danhuise));
						}
						if (hotelName.contains("7������")
								&& hotelRoomComfirm.getRoomtype().equals("0")) {
							isSevenDayHotel = true;
							ruzhurenID_rl.setVisibility(View.VISIBLE);
						}
						if (hotelRoomComfirm.getGaranteeRule() != null) {
							guarantee_room_count = Integer
									.valueOf(hotelRoomComfirm.getGaranteeRule()
											.getRomms().equals("")
											|| Integer.valueOf(
													hotelRoomComfirm
															.getGaranteeRule()
															.getRomms())
													.equals(0)
											|| hotelRoomComfirm
													.getGaranteeRule()
													.getRomms() == null ? "6"
											: hotelRoomComfirm
													.getGaranteeRule()
													.getRomms().toString()
													.trim());
							// ����������ָ��ֵʱ��Ҫ����
							if (guarantee_room_count != 6)
								roomcount_need_guarantee = true;
							// ������ȫ�쵣��
							if (hotelRoomComfirm.getGaranteeRule().getStatus()
									.equals("1")
									&& hotelRoomComfirm.getGaranteeRule()
											.getNorule().equals("1")) {
								need_guarantee = true;
								Garantee_LL.setVisibility(View.VISIBLE);
								garantee_desc_tv.setText(hotelRoomComfirm
										.getGaranteeRule().getDesc());
							}
							// ��ָ���Ŀ�ʼ�ͽ���ʱ����ʱ�����Ҫ����
							else if (hotelRoomComfirm.getGaranteeRule()
									.getStatus().equals("1")
									&& (hotelRoomComfirm.getGaranteeRule()
											.getStattime().length() != 0 || hotelRoomComfirm
											.getGaranteeRule().getEndtime()
											.length() != 0)) {
								time_need_guarantee = true;

								arriveTimeList.clear();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("title", hotelRoomComfirm
										.getGaranteeRule().getStattime() + "֮ǰ");
								arriveTimeList.add(map);
								map = new HashMap<String, Object>();
								map.put("title", hotelRoomComfirm
										.getGaranteeRule().getStattime()
										+ "-"
										+ hotelRoomComfirm.getGaranteeRule()
												.getEndtime());
								arriveTimeList.add(map);
								map = new HashMap<String, Object>();
								map.put("title", hotelRoomComfirm
										.getGaranteeRule().getEndtime() + "֮��");
								arriveTimeList.add(map);
							}
						}
						will_arrive_time_tv.setText(arriveTimeList.get(0)
								.get("title").toString());
					} else {
						// String message =
						// jsonObject.getJSONObject("d").getString("msg");
						// new
						// AlertDialog.Builder(context).setTitle("��ѯ�Ƶ귿����Ϣʧ��")
						// .setMessage(message)
						// .setPositiveButton("ȷ��", null).show();
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, true);
						cad.setTitle("��ѯ�Ƶ귿����Ϣʧ��");
						cad.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case ORDER_MSG:
				progressdialog.dismiss();
				jsonParser = new JSONTokener(orderReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						successOrderId = jsonObject.getString("d");
						// Toast.makeText(context,
						// "�����ύ�ɹ���OrderId��" + successOrderId, 0).show();
						// String orderID =
						// jsonObject.getJSONObject("d").getString("orderid");
						Intent intent = new Intent(context,
								ActivityHotelOrderDetail.class);
						intent.putExtra(ActivityHotelOrderDetail.ORDERRECEIPT,
								successOrderId);
						startActivityForResult(intent, 22);
					} else {
						String message = "";
						try {
							message = jsonObject.getJSONObject("d").getString(
									"msg");
						} catch (Exception e) {
							message = jsonObject.getString("msg");
						}
						// new AlertDialog.Builder(context).setTitle("�ύ����ʧ��")
						// .setMessage(message)
						// .setPositiveButton("ȷ��", null).show();
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, true);
						cad.setTitle(message);
						cad.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	OnClickListener room_count_tv_popupClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			room_count_tv.setText(btn.getText());
			if (v.getTag().equals("1")) {
				ruzhuren_rl2.setVisibility(View.GONE);
				ruzhuren_rl3.setVisibility(View.GONE);
				ruzhuren_rl4.setVisibility(View.GONE);
				ruzhuren_rl5.setVisibility(View.GONE);
				room_count = 1;
			} else if (v.getTag().equals("2")) {
				ruzhuren_rl2.setVisibility(View.VISIBLE);
				ruzhuren_rl3.setVisibility(View.GONE);
				ruzhuren_rl4.setVisibility(View.GONE);
				ruzhuren_rl5.setVisibility(View.GONE);
				room_count = 2;
			} else if (v.getTag().equals("3")) {
				ruzhuren_rl2.setVisibility(View.VISIBLE);
				ruzhuren_rl3.setVisibility(View.VISIBLE);
				ruzhuren_rl4.setVisibility(View.GONE);
				ruzhuren_rl5.setVisibility(View.GONE);
				room_count = 3;
			} else if (v.getTag().equals("4")) {
				ruzhuren_rl2.setVisibility(View.VISIBLE);
				ruzhuren_rl3.setVisibility(View.VISIBLE);
				ruzhuren_rl4.setVisibility(View.VISIBLE);
				ruzhuren_rl5.setVisibility(View.GONE);
				room_count = 4;
			} else if (v.getTag().equals("5")) {
				ruzhuren_rl2.setVisibility(View.VISIBLE);
				ruzhuren_rl3.setVisibility(View.VISIBLE);
				ruzhuren_rl4.setVisibility(View.VISIBLE);
				ruzhuren_rl5.setVisibility(View.VISIBLE);
				room_count = 5;
			}
			if (roomcount_need_guarantee
					&& room_count > guarantee_room_count - 1) {
				Garantee_LL.setVisibility(View.VISIBLE);
				garantee_desc_tv.setText(hotelRoomComfirm.getGaranteeRule()
						.getDesc());
				need_guarantee = true;
			} else {
				Garantee_LL.setVisibility(View.GONE);
				need_guarantee = false;
			}
			total_price_tv.setText("��" + totalPrice * room_count);
			popupWindow_order_room_count.dismiss();
		}
	};

	OnClickListener arrive_time_popupClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Button btn = (Button) v;
				will_arrive_time_tv.setText(btn.getText());
				if (time_need_guarantee && v.getTag().equals("2")) {// need_guarantee_btn
					Garantee_LL.setVisibility(View.VISIBLE);
					garantee_desc_tv.setText(hotelRoomComfirm.getGaranteeRule()
							.getDesc());
					need_guarantee = true;
				} else {
					Garantee_LL.setVisibility(View.GONE);
					need_guarantee = false;
				}
				popupWindow_order_will_arrive_time.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	OnClickListener hotel_guarantee_identification_type_popupClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Button btn = (Button) v;
				identificationType_tv.setText(btn.getText());
				if (v.getTag().equals("1")) {// ���֤
					guarantee_identification_type_isID = 0;
				} else if (v.getTag().equals("2")) {// ����
					guarantee_identification_type_isID = 1;
				} else
					// ����
					guarantee_identification_type_isID = 1;

				popupWindow_hotel_guarantee_identification_type.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private Boolean validInput() {
		if (room_count > 0
				&& name_cet1.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("��������ס������")
			// .setPositiveButton("ȷ��", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("��������ס������");
			cad.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		} else if (room_count > 1
				&& name_cet2.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("������ڶ�����ס������")
			// .setPositiveButton("ȷ��", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("������ڶ�����ס������");
			cad.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		} else if (room_count > 2
				&& name_cet3.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("�������������ס������")
			// .setPositiveButton("ȷ��", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("�������������ס������");
			cad.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		} else if (room_count > 3
				&& name_cet4.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("��������ĸ���ס������")
			// .setPositiveButton("ȷ��", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("��������ĸ���ס������");
			cad.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		} else if (room_count > 4
				&& name_cet5.getText().toString().trim().length() == 0) {
			// new AlertDialog.Builder(context).setTitle("������������ס������")
			// .setPositiveButton("ȷ��", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("������������ס������");
			cad.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (!CommonFunc.isMobileNO(contact_person_phone_cet.getText()
				.toString().trim())) {
			// new AlertDialog.Builder(context).setTitle("������Ϸ�����ϵ���ֻ���")
			// .setPositiveButton("ȷ��", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("������Ϸ�����ϵ���ֻ���");
			cad.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		} else {
			sp.edit()
					.putString(SPkeys.hoteContactPhone.getString(),
							contact_person_phone_cet.getText().toString())
					.commit();
		}
		if (need_guarantee) {
			if (creditCard_num_cet.getText().toString().trim().length() == 0) {
				// new AlertDialog.Builder(context).setTitle("�����뵣��ʹ�õ����ÿ�����")
				// .setPositiveButton("ȷ��", null).show();
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, true);
				cad.setTitle("�����뵣��ʹ�õ����ÿ�����");
				cad.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});
				return false;
			}
			if (creadit_card_validity_tv.getText().toString().trim().length() == 0) {
				// new AlertDialog.Builder(context).setTitle("��ѡ�����ÿ�����Ч��")
				// .setPositiveButton("ȷ��", null).show();
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, true);
				cad.setTitle("��ѡ�����ÿ�����Ч��");
				cad.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});
				return false;
			}
			if (cvv_num_cet.getText().toString().trim().length() == 0) {
				// new
				// AlertDialog.Builder(context).setTitle("�����뵣��ʹ�õ����ÿ���CVV2��")
				// .setPositiveButton("ȷ��", null).show();
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, true);
				cad.setTitle("�����뵣��ʹ�õ����ÿ���CVV2��");
				cad.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});
				return false;
			}
			if (chikaren_name_cet.getText().toString().trim().length() == 0) {
				// new
				// AlertDialog.Builder(context).setTitle("�����뵣��ʹ�õ����ÿ��ĳֿ�������")
				// .setPositiveButton("ȷ��", null).show();
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, true);
				cad.setTitle("�����뵣��ʹ�õ����ÿ��ĳֿ�������");
				cad.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});
				return false;
			}
			if (creditCard_identificationNum_cet.getText().toString().trim()
					.length() == 0) {
				// new AlertDialog.Builder(context).setTitle("������֤������")
				// .setPositiveButton("ȷ��", null).show();
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, true);
				cad.setTitle("������֤������");
				cad.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});
				return false;
			}
			if (guarantee_identification_type_isID == 0
					&& !(new IDCard().verify(creditCard_identificationNum_cet
							.getText().toString().trim()))) {
				// new AlertDialog.Builder(context).setTitle("���֤�Ų��Ϸ�")
				// .setMessage("�����뵣���˺Ϸ������֤���룡")
				// .setPositiveButton("ȷ��", null).show();
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, true);
				cad.setTitle("�����뵣���˺Ϸ������֤����");
				cad.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});
			}
		}
		if (!sp.getBoolean(SPkeys.loginState.getString(), false)) {
			startActivity(new Intent(context, Activity_Login.class));
			return false;
		}
		if (isSevenDayHotel) {
			if (!(new IDCard().verify(identificationNum_et.getText().toString()
					.trim()))) {
				// new AlertDialog.Builder(context).setTitle("���֤�Ų��Ϸ�")
				// .setMessage("������һ����ס�˵ĺϷ����֤���룡")
				// .setPositiveButton("ȷ��", null).show();
				final CustomerAlertDialog cad = new CustomerAlertDialog(
						context, true);
				cad.setTitle("������һ����ס�˵ĺϷ����֤����");
				cad.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cad.dismiss();
					}
				});
				return false;
			}
		}
		if (DateUtil.compareDateIsBefore(ruzhu_date_tv.getText().toString(),
				lidian_date_tv.getText().toString())) {
			// new AlertDialog.Builder(context).setTitle("��ס���ڲ��ܴ����������")
			// .setPositiveButton("֪����", null).show();
			final CustomerAlertDialog cad = new CustomerAlertDialog(context,
					true);
			cad.setTitle("��ס���ڲ��ܴ����������");
			cad.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					cad.dismiss();
				}
			});
			return false;
		}
		if (HttpUtils.showNetCannotUse(context)) {
			return false;
		}
		return true;
	}

	/*
	 * ���ں������Ե�����
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
					ruzhu_date = myDate;
					try {
						ruzhu_date_tv.setText(DateUtil.getMonthDayDate(myDate));
						if (DateUtil.compareDateIsBefore(myDate, lidian_date)) {
							lidian_date_tv.setText(DateUtil
									.getMonthDayDate(DateUtil
											.getSpecifiedDayAfter(myDate)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					startQueryRoomsRI();
				}
				break;
			case lidiandate:
				if (b != null && b.containsKey("pickedDate")) {
					myDate = b.getString("pickedDate");
					lidian_date = myDate;
					try {
						lidian_date_tv
								.setText(DateUtil.getMonthDayDate(myDate));
						if (DateUtil.compareDateIsBefore(ruzhu_date, myDate)) {
							if (DateUtil.IsMoreThanToday(myDate)) {
								ruzhu_date_tv
										.setText(DateUtil.getMonthDayDate(DateUtil
												.getSpecifiedDayBefore(myDate)));
							} else {
								ruzhu_date_tv.setText(DateUtil
										.getMonthDayDate(DateUtil
												.GetTodayDate()));
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					startQueryRoomsRI();
				}
				break;
			default:
				break;
			}

			if (requestCode == 13) {
				if (data != null && b != null
						&& b.containsKey("pickedPhoneNum")) {
					String myNum = b.getString("pickedPhoneNum");
					if (myNum.startsWith("17951")) {
						myNum = myNum.substring(5);
					} else if (myNum.startsWith("+86")) {
						myNum = myNum.substring(3);
					}
					contact_person_phone_cet.setText(myNum);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PopupWindow pwMyPopWindow;// popupwindow
	private ListView lvPopupList;
	private int currentID_FJ = 0;
	private int currentID_SJ = 0;

	/*
	 * fjOrSj 0:��������1������ʱ��
	 */
	private void iniPopupWindow(final int fjOrSj,
			final List<Map<String, Object>> list1) {
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.popupwindow_list_select, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout);
		pwMyPopWindow.setFocusable(true);// �������popupwindow�е�ListView�ſ��Խ��յ���¼�

		MyListAdapter adapter = new MyListAdapter(context, list1);
		adapter.setCurrentID(fjOrSj == 0 ? currentID_FJ : currentID_SJ);
		lvPopupList.setAdapter(adapter);
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					if (fjOrSj == 0) {// 0:����
						room_count_tv.setText(list1.get(position).get("title")
								.toString());
						currentID_FJ = position;

						if (currentID_FJ == 0) {
							ruzhuren_rl2.setVisibility(View.GONE);
							ruzhuren_rl3.setVisibility(View.GONE);
							ruzhuren_rl4.setVisibility(View.GONE);
							ruzhuren_rl5.setVisibility(View.GONE);
							room_count = 1;
						} else if (currentID_FJ == 1) {
							ruzhuren_rl2.setVisibility(View.VISIBLE);
							ruzhuren_rl3.setVisibility(View.GONE);
							ruzhuren_rl4.setVisibility(View.GONE);
							ruzhuren_rl5.setVisibility(View.GONE);
							room_count = 2;
						} else if (currentID_FJ == 2) {
							ruzhuren_rl2.setVisibility(View.VISIBLE);
							ruzhuren_rl3.setVisibility(View.VISIBLE);
							ruzhuren_rl4.setVisibility(View.GONE);
							ruzhuren_rl5.setVisibility(View.GONE);
							room_count = 3;
						} else if (currentID_FJ == 3) {
							ruzhuren_rl2.setVisibility(View.VISIBLE);
							ruzhuren_rl3.setVisibility(View.VISIBLE);
							ruzhuren_rl4.setVisibility(View.VISIBLE);
							ruzhuren_rl5.setVisibility(View.GONE);
							room_count = 4;
						} else if (currentID_FJ == 4) {
							ruzhuren_rl2.setVisibility(View.VISIBLE);
							ruzhuren_rl3.setVisibility(View.VISIBLE);
							ruzhuren_rl4.setVisibility(View.VISIBLE);
							ruzhuren_rl5.setVisibility(View.VISIBLE);
							room_count = 5;
						}
						if (roomcount_need_guarantee
								&& room_count > guarantee_room_count - 1) {
							Garantee_LL.setVisibility(View.VISIBLE);
							garantee_desc_tv.setText(hotelRoomComfirm
									.getGaranteeRule().getDesc());
							need_guarantee = true;
						} else {
							Garantee_LL.setVisibility(View.GONE);
							need_guarantee = false;
						}
						total_price_tv.setText("��" + totalPrice * room_count);
						pwMyPopWindow.dismiss();
					} else if (fjOrSj == 1) {// 1��ʱ��
						will_arrive_time_tv.setText(list1.get(position)
								.get("title").toString());
						currentID_SJ = position;
						if (time_need_guarantee && currentID_SJ == 1) {// need_guarantee_btn
							Garantee_LL.setVisibility(View.VISIBLE);
							garantee_desc_tv.setText(hotelRoomComfirm
									.getGaranteeRule().getDesc());
							need_guarantee = true;
						} else {
							Garantee_LL.setVisibility(View.GONE);
							need_guarantee = false;
						}
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

	private void getDate() {
		Calendar calendar = Calendar.getInstance();
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
	}

	private ArrayList<Map<String, Object>> initArriveTimeData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "18:00֮ǰ");
		if (hour < 18) {
			list.add(map);
		}
		map = new HashMap<String, Object>();
		map.put("title", "18:00-06:00");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "06:00֮��");
		list.add(map);
		return list;
	}

	private ArrayList<Map<String, Object>> initRoomCountData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "1��");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "2��");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "3��");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "4��");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "5��");
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

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityHotelBooking");
		MobclickAgent.onPause(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ActivityHotelBooking"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
	}

}
