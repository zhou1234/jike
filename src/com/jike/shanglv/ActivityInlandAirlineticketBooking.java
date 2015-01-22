package com.jike.shanglv;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jike.shanglv.Common.ClearEditText;
import com.jike.shanglv.Common.CommonFunc;
import com.jike.shanglv.Common.CustomProgressDialog;
import com.jike.shanglv.Common.CustomerAlertDialog;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Common.IdType;
import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.SPkeys;
import com.jike.shanglv.Enums.SingleOrDouble;
import com.jike.shanglv.LazyList.ImageLoader;
import com.jike.shanglv.Models.CabList;
import com.jike.shanglv.Models.CuandanFlight;
import com.jike.shanglv.Models.CuandanPassenger;
import com.jike.shanglv.Models.InlandAirlineInfo;
import com.jike.shanglv.Models.Passenger;
import com.jike.shanglv.Models.PolicyList;
import com.jike.shanglv.Models.VerifyFlightPrice;
import com.jike.shanglv.NetAndJson.HttpUtils;
import com.jike.shanglv.NetAndJson.JSONHelper;
import com.umeng.analytics.MobclickAgent;

public class ActivityInlandAirlineticketBooking extends Activity {

	protected static final String ALLPASSENGERSLIST = "ALL_PASSENGERS_LIST";
	protected static final String SELECTEDPASSENGERSLIST = "SELECTED_PASSENGERS_LIST";
	protected static final String SELECTED_CABIN_INDEX = "selectCabinListIndex";// ����ʱ���û�ѡ��Ĳ�λ
	protected static final String SELECTED_CABIN_INDEX1 = "selectCabinListIndex1"; // ���̻�����ȥ�̲�λ���
	protected static final String SELECTED_CABIN_INDEX2 = "selectCabinListIndex2"; // �������̲�λ���
	protected static final String ORDERWAYTYPE = "ORDER_WAY_TYPE";

	protected static final int ADD_PASSENGERS_FORRESULET_CODE = 13;
	protected static final int BAOXIAN_MSG_CODE = 5;
	protected static final int COMMIT_ORDER_MSG_CODE = 6;
	protected static final int COMMIT_ORDER_MSG_CODE3 = 16;
	protected static final int CHECK_PRICE_MSG_CODE = 15;
	protected static final int CHECK_PRICE_MSG_CODE3 = 18;
	protected static final int NEW_ORDER_DETAIL_CODE = 7;
	protected static final int TUIGAIQIAN_MSG_CODE = 8;
	protected static final int TUIGAIQIAN_MSG_CODE3 = 9;
	protected static final int SELECTPOLICYREQUESTCODE = 10;
	protected static final int SELECTPOLICYREQUESTCODE3 = 11;

	private Context context;
	private ImageButton back_imgbtn, home_imgbtn, lianxiren_icon_imgbtn,
			baoxian_check_imgbtn;
	private TextView total_price_tv, baoxian_price_and_count_tv,
			order_totalmoney_tv, add_passager_tv,

			startoff_date_tv, CarrinerName_tv, FlightNo_tv, canbin_grade_tv,
			startoff_time_tv, arrive_time_tv, start_port_tv, arrive_port_tv,
			start_port_tv2, arrive_port_tv2, jipiaojia_tv, jijian_price_tv,
			shoujia_tv, ranyou_price_tv, fanMoney_tv, tuiGaiQian_state_tv,
			zhengcefandian_tv, runtime_tv,

			startoff_date_tv3, CarrinerName_tv3, FlightNo_tv3,
			canbin_grade_tv3, startoff_time_tv3, arrive_time_tv3,
			start_port_tv3, arrive_port_tv3, start_port_tv23, arrive_port_tv23,
			jipiaojia_tv3, shoujia_tv3, jijian_price_tv3, ranyou_price_tv3,
			fanMoney_tv3, tuiGaiQian_state_tv3, zhengcefandian_tv3,
			runtime_tv3,

			total_jipiaojia_tv, total_jijian_price_tv, total_ranyou_price_tv,
			total_fanMoney_tv;
	private ClearEditText contact_person_phone_et;
	private Button order_now_btn;
	private RelativeLayout add_passager_rl, tuiGaiQian_rl, zhengce_rl,
			baoxian_rl;
	private ImageView up_down_arrow_iv, flight_company_logo;
	private ListView passenger_listview;
	private View passenger_head_divid_line;
	private SharedPreferences sp;
	private float totalPrice = 0, totalPrice3 = 0, baoxian_unitPrice = 0;

	private JSONObject jsonObject;
	private int selectCabinListIndex = 0;
	private float piaomianjia = 0,// Ʊ��
			shoujia = 0, youhui = 0, jijianfei = 0, ranyoufei = 0;

	private JSONObject jsonObject3;
	private int selectCabinListIndex3 = 0;
	private float piaomianjia3 = 0, shoujia3 = 0, youhui3 = 0, jijianfei3 = 0,
			ranyoufei3 = 0;
	private RelativeLayout tuiGaiQian_rl3, zhengce_rl3;
	private ImageView up_down_arrow_iv3;

	private Boolean needBaoxianBoolean = true;// �Ƿ�����
	private String baoxianReturnJson = "", commitReturnJson = "",
			commitReturnJson3 = "", checkReturnJson = "", checkReturnJson3,
			tuigaiqianReturnJson = "", cabin = "", cabin3 = "",
			startcity_code = "", arrivecity_code = "";
	private ArrayList<Passenger> passengerList;// ѡ��ĳ˻����б�
	private ArrayList<Passenger> allPassengerList;// ��ǰ���г˻��˵��б�����˺��û������ģ�
	private PolicyList selectedPolicyB;// B2B�û��Զ���ѡ���������Ϣ
	private CustomProgressDialog progressdialog;
	private ImageLoader imageLoader;
	private Boolean hasCommited = false;

	private String flightno = "", fare = "", sd = "", ischd = "0", isspe = "0";

	private String IsTeHui = "", IsTeHui3 = "";
	private Boolean isB2Boolean;
	private Boolean isCheck = false;// ����Ƿ���,�ж�ȥ���Ƿ����ͨ��.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			if (getOrderWayType() == SingleOrDouble.singleWay) {
				setContentView(R.layout.activity_inland_airlineticket_booking);
			} else {
				setContentView(R.layout.activity_inland_airlineticket_booking_doubleway);
			}

			try {// ���̡�����ȥ����Ϣ
				initView();
				if (getOrderWayType() == SingleOrDouble.doubleWayBack) {
					initView3();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// ��B2B�ɽ�������ѡ��
			if (!isB2Boolean) {
				zhengce_rl.setVisibility(View.GONE);
				if (getOrderWayType() == SingleOrDouble.doubleWayBack)
					zhengce_rl3.setVisibility(View.GONE);
			} else {
				zhengce_rl.setVisibility(View.VISIBLE);
				if (getOrderWayType() == SingleOrDouble.doubleWayBack)
					zhengce_rl3.setVisibility(View.VISIBLE);
			}
			((MyApplication) getApplication()).addActivity(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		context = ActivityInlandAirlineticketBooking.this;
		sp = getSharedPreferences(SPkeys.SPNAME.getString(), 0);
		imageLoader = new ImageLoader(context.getApplicationContext());
		passengerList = new ArrayList<Passenger>();
		allPassengerList = new ArrayList<Passenger>();
		VerifyFlightPrice vfp = new VerifyFlightPrice();
		isB2Boolean = sp.getString(SPkeys.utype.getString(), "0").equals("1") ? true
				: false;// b2b utype:1

		total_jipiaojia_tv = (TextView) findViewById(R.id.total_jipiaojia_tv);
		total_jijian_price_tv = (TextView) findViewById(R.id.total_jijian_price_tv);
		total_ranyou_price_tv = (TextView) findViewById(R.id.total_ranyou_price_tv);
		total_fanMoney_tv = (TextView) findViewById(R.id.total_fanMoney_tv);

		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(btnClickListner);
		home_imgbtn.setOnClickListener(btnClickListner);
		passenger_head_divid_line = findViewById(R.id.passenger_head_divid_line);

		lianxiren_icon_imgbtn = (ImageButton) findViewById(R.id.lianxiren_icon_imgbtn);
		lianxiren_icon_imgbtn.setOnClickListener(btnClickListner);
		startoff_date_tv = (TextView) findViewById(R.id.startoff_date_tv);
		CarrinerName_tv = (TextView) findViewById(R.id.CarrinerName_tv);
		FlightNo_tv = (TextView) findViewById(R.id.FlightNo_tv);
		canbin_grade_tv = (TextView) findViewById(R.id.canbin_grade_tv);
		startoff_time_tv = (TextView) findViewById(R.id.startoff_time_tv);
		arrive_time_tv = (TextView) findViewById(R.id.arrive_time_tv);
		start_port_tv2 = (TextView) findViewById(R.id.start_port_tv2);
		arrive_port_tv2 = (TextView) findViewById(R.id.arrive_port_tv2);
		jipiaojia_tv = (TextView) findViewById(R.id.jipiaojia_tv);
		shoujia_tv = (TextView) findViewById(R.id.shoujia_tv);
		jijian_price_tv = (TextView) findViewById(R.id.jijian_price_tv);
		ranyou_price_tv = (TextView) findViewById(R.id.ranyou_price_tv);
		fanMoney_tv = (TextView) findViewById(R.id.fanMoney_tv);
		total_price_tv = (TextView) findViewById(R.id.total_price_tv);
		tuiGaiQian_state_tv = (TextView) findViewById(R.id.tuiGaiQian_state_tv);
		start_port_tv = (TextView) findViewById(R.id.start_port_tv);
		arrive_port_tv = (TextView) findViewById(R.id.arrive_port_tv);
		runtime_tv = (TextView) findViewById(R.id.runtime_tv);
		order_totalmoney_tv = (TextView) findViewById(R.id.order_totalmoney_tv);
		baoxian_price_and_count_tv = (TextView) findViewById(R.id.baoxian_price_and_count_tv);
		add_passager_tv = (TextView) findViewById(R.id.add_passager_tv);
		zhengcefandian_tv = (TextView) findViewById(R.id.zhengcefandian_tv);
		flight_company_logo = (ImageView) findViewById(R.id.flight_company_logo);

		contact_person_phone_et = (ClearEditText) findViewById(R.id.contact_person_phone_et);
		order_now_btn = (Button) findViewById(R.id.order_now_btn);
		order_now_btn.setOnClickListener(btnClickListner);

		add_passager_rl = (RelativeLayout) findViewById(R.id.add_passager_rl);
		add_passager_rl.setOnClickListener(btnClickListner);

		tuiGaiQian_rl = (RelativeLayout) findViewById(R.id.tuiGaiQian_rl);
		tuiGaiQian_rl.setOnClickListener(btnClickListner);
		zhengce_rl = (RelativeLayout) findViewById(R.id.zhengce_rl);
		zhengce_rl.setOnClickListener(btnClickListner);
		passenger_listview = (ListView) findViewById(R.id.passenger_listview);
		up_down_arrow_iv = (ImageView) findViewById(R.id.up_down_arrow_iv);

		// B2C���չ̶�Ϊ20��B2B���ռ۸�ӷ�������ȡ
		MyApp ma = new MyApp(context);
		// Platform pf = (Platform) ma.getHm().get(
		// PackageKeys.PLATFORM.getString());
		if (!isB2Boolean) {
			baoxian_unitPrice = 20;
		} else {
			startQueryBaoxian();
		}
		// ���ڳ�����ϵ�ˣ�ֱ�ӷ����ϴζ�Ʊʱ����ϵ���ֻ��ţ����������򷵻ر����ֻ�����
		if (sp.getString(SPkeys.gnjpContactPhone.getString(), "").equals("")) {
			if (CommonFunc.getPhoneNumber(context).equals("")) {
				contact_person_phone_et.setText(sp.getString(
						SPkeys.userphone.getString(), ""));
			} else {
				contact_person_phone_et.setText(CommonFunc
						.getPhoneNumber(context));
			}
		} else {
			contact_person_phone_et.setText(sp.getString(
					SPkeys.gnjpContactPhone.getString(), ""));
		}

		InlandAirlineInfo ia = new InlandAirlineInfo();
		// if (getOrderWayType()==SingleOrDouble.singleWay) {
		ia = getIntentFlightInfo(SingleOrDouble.singleWay);
		// }else if (getOrderWayType()==SingleOrDouble.doubleWayBack) {
		// ia =getIntentFlightInfo(SingleOrDouble.doubleWayBack);
		// }
		try {
			sd = DateUtil.getDate(ia.getOffTime());
			startoff_date_tv.setText(sd);
			startoff_time_tv.setText(DateUtil.getTime(ia.getOffTime()));
			arrive_time_tv.setText(DateUtil.getTime(ia.getArriveTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		start_port_tv.setText(ia.getStartPortName() + ia.getStartT());
		arrive_port_tv.setText(ia.getEndPortName() + ia.getEndT());
		start_port_tv2.setText(ia.getStartPortName());
		arrive_port_tv2.setText(ia.getEndPortName());
		runtime_tv.setText(ia.getRunTime());
		jijianfei = Float.valueOf(ia.getTax());
		ranyoufei = Float.valueOf(ia.getOil());
		jijian_price_tv.setText("��" + String.valueOf(jijianfei));
		ranyou_price_tv.setText("��" + String.valueOf(ranyoufei));
		CarrinerName_tv.setText(ia.getCarrinerName());
		flightno = ia.getFlightNo();
		FlightNo_tv.setText(flightno);
		try {
			shoujia = Float.parseFloat(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("Sale"));

			// fare = jsonObject.getJSONArray("CabList")
			// .getJSONObject(selectCabinListIndex).getString("Fare");
			piaomianjia = Float.parseFloat(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("Fare"));
			youhui = Float.parseFloat(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("YouHui"));
			canbin_grade_tv.setText(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("CabinName")
					+ jsonObject.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex)
							.getString("Cabin"));
			cabin = jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("Cabin");
			startQueryTuigaiqian(
					ia.getFlightNo(),
					jsonObject.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex)
							.getString("Cabin"),
					jsonObject.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex)
							.getString("IsTeHui"), startoff_date_tv.getText()
							.toString(), TUIGAIQIAN_MSG_CODE);
			vfp.setFlightno(ia.getFlightNo());
			vfp.setCabin(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("Cabin"));
			vfp.setE(ia.getEndPort());
			vfp.setFare(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("Fare"));
			vfp.setIschd("0");
			vfp.setIsspe("0");
			vfp.setS(ia.getStartPort());
			vfp.setSd(DateUtil.getDate(ia.getOffTime()));
			vfp.setSiteid(sp.getString(SPkeys.siteid.getString(), ""));
			vfp.setIstehui(IsTeHui);
			vfp.setUserrate(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("UserRate"));
			vfp.setStime(DateUtil.getTime(ia.getOffTime()));
			vfp.setIskx(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("IsKx"));
			vfp.setKxsiteid(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("KxSiteID"));
			vfp.setKxspvalue(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex).getString("KxSpValue"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		jipiaojia_tv.setText("��" + String.valueOf(piaomianjia));
		fanMoney_tv.setText("����" + youhui);
		shoujia_tv.setText("��" + String.valueOf(shoujia));

		String picN = ia.getFlightNo().substring(0, 2);
		String imgUrl = String.format(ma.getFlightCompanyLogo(), picN);
		imageLoader.DisplayImage(imgUrl, flight_company_logo);

		baoxian_rl = (RelativeLayout) findViewById(R.id.baoxian_rl);
		baoxian_rl.setOnClickListener(btnClickListner);
		baoxian_check_imgbtn = (ImageButton) findViewById(R.id.baoxian_check_imgbtn);
		baoxian_check_imgbtn.setOnClickListener(btnClickListner);
		baoxian_price_and_count_tv.setOnClickListener(btnClickListner);
		caculateBaoxian(baoxian_unitPrice);
		baoxian_check_imgbtn.setSelected(true);
		baoxian_price_and_count_tv.setTextColor(getResources().getColor(
				R.color.price_yellow));
		if (IsTeHui.equals("1")) {
			baoxian_rl.setClickable(false);
			baoxian_check_imgbtn.setClickable(false);
			baoxian_check_imgbtn.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.checkmark_icon_selected1));
			baoxian_price_and_count_tv.setClickable(false);
			baoxian_price_and_count_tv.setTextColor(getResources().getColor(
					R.color.danhuise));
		}
		checkPrice(vfp);
	}

	private String getPolicyStr() {
		String string = "";
		String siteid = sp.getString(SPkeys.siteid.getString(), "65");
		string = "{\"fare\":\""
				+ piaomianjia
				+ "\",\"scode\":\""
				+ startcity_code
				+ "\",\"IsTeHui\":\""
				+ IsTeHui
				+ "\",\"ecode\":\""
				+ arrivecity_code
				+ "\",\"godate\":\""
				+ startoff_date_tv.getText().toString().trim()
				+ "\",\"flightno\":\""
				+ FlightNo_tv.getText().toString().trim()
				+ "\",\"userid\":\""
				+ sp.getString(SPkeys.userid.getString(), "")
				+ "\",\"stime\":\""
				+ startoff_time_tv.getText().toString().trim()
				+ "\",\"etime\":\""
				+ arrive_time_tv.getText().toString().trim()
				+ "\",\"st\":\""
				+ start_port_tv
						.getText()
						.toString()
						.trim()
						.substring(
								start_port_tv.getText().toString().trim()
										.length() - 2)
				+ "\",\"et\":\""
				+ arrive_port_tv
						.getText()
						.toString()
						.trim()
						.substring(
								arrive_port_tv.getText().toString().trim()
										.length() - 2) + "\",\"tax\":\""
				+ jijianfei + "\",\"oil\":\"" + ranyoufei + "\",\"cabin\":\""
				+ cabin + "\",\"psgtype\":\"" + 1 + "\",\"siteid\":\"" + siteid
				+ "\"}";
		return string;
	}

	private String getPolicyStr3() {
		String string = "";
		String siteid = sp.getString(SPkeys.siteid.getString(), "65");
		string = "{\"fare\":\""
				+ piaomianjia3
				+ "\",\"scode\":\""
				+ arrivecity_code
				+ "\",\"IsTeHui\":\""
				+ IsTeHui3
				+ "\",\"ecode\":\""
				+ startcity_code
				+ "\",\"godate\":\""
				+ startoff_date_tv3.getText().toString().trim()
				+ "\",\"flightno\":\""
				+ FlightNo_tv3.getText().toString().trim()
				+ "\",\"userid\":\""
				+ sp.getString(SPkeys.userid.getString(), "")
				+ "\",\"stime\":\""
				+ startoff_time_tv3.getText().toString().trim()
				+ "\",\"etime\":\""
				+ arrive_time_tv3.getText().toString().trim()
				+ "\",\"st\":\""
				+ start_port_tv3
						.getText()
						.toString()
						.trim()
						.substring(
								start_port_tv3.getText().toString().trim()
										.length() - 2)
				+ "\",\"et\":\""
				+ arrive_port_tv3
						.getText()
						.toString()
						.trim()
						.substring(
								arrive_port_tv3.getText().toString().trim()
										.length() - 2) + "\",\"tax\":\""
				+ jijianfei3 + "\",\"oil\":\"" + ranyoufei3 + "\",\"cabin\":\""
				+ cabin3 + "\",\"psgtype\":\"" + 1 + "\",\"siteid\":\""
				+ siteid + "\"}";
		return string;
	}

	private void initView3() {
		startoff_date_tv3 = (TextView) findViewById(R.id.startoff_date_tv3);
		CarrinerName_tv3 = (TextView) findViewById(R.id.CarrinerName_tv3);
		FlightNo_tv3 = (TextView) findViewById(R.id.FlightNo_tv3);
		canbin_grade_tv3 = (TextView) findViewById(R.id.canbin_grade_tv3);
		startoff_time_tv3 = (TextView) findViewById(R.id.startoff_time_tv3);
		arrive_time_tv3 = (TextView) findViewById(R.id.arrive_time_tv3);
		start_port_tv3 = (TextView) findViewById(R.id.start_port_tv3);
		arrive_port_tv3 = (TextView) findViewById(R.id.arrive_port_tv3);
		start_port_tv23 = (TextView) findViewById(R.id.start_port_tv23);
		arrive_port_tv23 = (TextView) findViewById(R.id.arrive_port_tv23);
		jipiaojia_tv3 = (TextView) findViewById(R.id.jipiaojia_tv3);
		shoujia_tv3 = (TextView) findViewById(R.id.shoujia_tv3);
		jijian_price_tv3 = (TextView) findViewById(R.id.jijian_price_tv3);
		ranyou_price_tv3 = (TextView) findViewById(R.id.ranyou_price_tv3);
		fanMoney_tv3 = (TextView) findViewById(R.id.fanMoney_tv3);
		tuiGaiQian_state_tv3 = (TextView) findViewById(R.id.tuiGaiQian_state_tv3);
		runtime_tv3 = (TextView) findViewById(R.id.runtime_tv3);
		up_down_arrow_iv3 = (ImageView) findViewById(R.id.up_down_arrow_iv3);
		zhengcefandian_tv3 = (TextView) findViewById(R.id.zhengcefandian_tv3);
		VerifyFlightPrice vfp = new VerifyFlightPrice();

		InlandAirlineInfo ia = new InlandAirlineInfo();
		// if (getOrderWayType()==SingleOrDouble.singleWay) {
		// ia =getIntentFlightInfo(SingleOrDouble.singleWay);
		// }else if (getOrderWayType()==SingleOrDouble.doubleWayBack)
		{
			ia = getIntentFlightInfo(SingleOrDouble.doubleWayBack);
		}
		try {
			sd = DateUtil.getDate(ia.getOffTime());
			startoff_date_tv3.setText(sd);
			startoff_time_tv3.setText(DateUtil.getTime(ia.getOffTime()));
			arrive_time_tv3.setText(DateUtil.getTime(ia.getArriveTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		start_port_tv3.setText(ia.getStartPortName() + ia.getStartT());
		arrive_port_tv3.setText(ia.getEndPortName() + ia.getEndT());
		start_port_tv23.setText(ia.getStartPortName());
		arrive_port_tv23.setText(ia.getEndPortName());
		runtime_tv3.setText(ia.getRunTime());
		jijianfei3 = Float.valueOf(ia.getTax());
		ranyoufei3 = Float.valueOf(ia.getOil());
		jijian_price_tv3.setText("��" + String.valueOf(jijianfei3));
		ranyou_price_tv3.setText("��" + String.valueOf(ranyoufei3));
		flightno = ia.getCarrinerName();
		CarrinerName_tv3.setText(flightno);
		FlightNo_tv3.setText(ia.getFlightNo());
		try {
			shoujia3 = Float.parseFloat(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("Sale"));
			// fare = jsonObject.getJSONArray("CabList")
			// .getJSONObject(selectCabinListIndex).getString("Fare");
			piaomianjia3 = Float.parseFloat(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("Fare"));
			youhui3 = Float.parseFloat(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("YouHui"));
			canbin_grade_tv3.setText(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3)
					.getString("CabinName")
					+ jsonObject3.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex3)
							.getString("Cabin"));
			cabin3 = jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("Cabin");
			startQueryTuigaiqian(
					ia.getFlightNo(),
					jsonObject3.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex3)
							.getString("Cabin"),
					jsonObject3.getJSONArray("CabList")
							.getJSONObject(selectCabinListIndex3)
							.getString("IsTeHui"), startoff_date_tv3.getText()
							.toString(), TUIGAIQIAN_MSG_CODE3);

			vfp.setFlightno(ia.getFlightNo());
			vfp.setCabin(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("Cabin"));// URLEncoder.encode(ia.getCabinName(),
																				// "utf-8")
			vfp.setE(ia.getEndPort());
			vfp.setFare(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("Fare"));
			vfp.setIschd("0");
			vfp.setIsspe("0");
			vfp.setS(ia.getStartPort());
			vfp.setSd(DateUtil.getDate(ia.getOffTime()));
			vfp.setSiteid(sp.getString(SPkeys.siteid.getString(), ""));
			vfp.setIstehui(IsTeHui3);
			vfp.setUserrate(jsonObject3.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("UserRate"));
			vfp.setStime(DateUtil.getTime(ia.getOffTime()));
			vfp.setIskx(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("IsKx"));
			vfp.setKxsiteid(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3).getString("KxSiteID"));
			vfp.setKxspvalue(jsonObject.getJSONArray("CabList")
					.getJSONObject(selectCabinListIndex3)
					.getString("KxSpValue"));
			checkPrice3(vfp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jipiaojia_tv3.setText("��" + String.valueOf(piaomianjia3));
		shoujia_tv3.setText("��" + String.valueOf(shoujia3));
		fanMoney_tv3.setText("����" + youhui3);

		tuiGaiQian_rl3 = (RelativeLayout) findViewById(R.id.tuiGaiQian_rl3);
		tuiGaiQian_rl3.setOnClickListener(btnClickListner);
		zhengce_rl3 = (RelativeLayout) findViewById(R.id.zhengce_rl3);
		zhengce_rl3.setOnClickListener(btnClickListner);

		caculateBaoxian(baoxian_unitPrice);
	}

	private SingleOrDouble getOrderWayType() {
		SingleOrDouble sd = SingleOrDouble.singleWay;
		Bundle bundle = this.getIntent().getExtras();
		try {
			if (bundle != null) {
				MyApplication application = (MyApplication) getApplication();
				startcity_code = application.getStartcity_code();
				arrivecity_code = application.getArrivecity_code();
				sd = (SingleOrDouble) bundle.get(ORDERWAYTYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sd;
	}

	/**
	 * SingleOrDouble.doubleWayGo����ѡ�񷵳̺󱻴���ΪSingleOrDouble.doubleWayBack
	 * ���Բ���ֻ����ΪSingleOrDouble.singleWay��SingleOrDouble.doubleWayBack������������Ʊ��
	 * 
	 * @param sd
	 * @return
	 */
	private InlandAirlineInfo getIntentFlightInfo(SingleOrDouble sd) {
		Bundle bundle = this.getIntent().getExtras();
		InlandAirlineInfo ia = null;
		try {
			if (sd == SingleOrDouble.singleWay) {
				selectCabinListIndex = Integer.parseInt(bundle.get(
						SELECTED_CABIN_INDEX1).toString());
				jsonObject = new JSONObject(bundle.get(
						ActivityInlandAirlineticketSelectCabin.TOKEN_NAME1)
						.toString());
				ia = new InlandAirlineInfo(jsonObject);
				IsTeHui = jsonObject.getJSONArray("CabList")
						.getJSONObject(selectCabinListIndex)
						.getString("IsTeHui");
				if (IsTeHui.equals("1"))
					isB2Boolean = false;
			} else if (sd == SingleOrDouble.doubleWayBack) {
				selectCabinListIndex3 = Integer.parseInt(bundle.get(
						SELECTED_CABIN_INDEX2).toString());
				jsonObject3 = new JSONObject(bundle.get(
						ActivityInlandAirlineticketSelectCabin.TOKEN_NAME2)
						.toString());
				ia = new InlandAirlineInfo(jsonObject3);
				IsTeHui3 = jsonObject3.getJSONArray("CabList")
						.getJSONObject(selectCabinListIndex3)
						.getString("IsTeHui");
				;
				if (IsTeHui3.equals("1"))
					isB2Boolean = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ia;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			JSONTokener jsonParser;
			switch (msg.what) {
			case BAOXIAN_MSG_CODE:
				jsonParser = new JSONTokener(baoxianReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						JSONObject baoxianObject = jsonObject
								.getJSONObject("d");
						baoxian_unitPrice = Float.parseFloat(baoxianObject
								.getString("sale"));
						caculateBaoxian(baoxian_unitPrice);
					} else {
						Toast.makeText(context, "�����쳣����ȡ���ռ۸�ʧ�ܣ�", 0).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case COMMIT_ORDER_MSG_CODE:
				jsonParser = new JSONTokener(commitReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						hasCommited = true;
						String orderID = jsonObject.getJSONObject("d")
								.getString("orderid");
						Intent intent = new Intent(context,
								ActivityInlandAirlineticketOrderDetail.class);
						intent.putExtra(
								ActivityInlandAirlineticketOrderDetail.ORDERRECEIPT,
								orderID);
						startActivityForResult(intent, NEW_ORDER_DETAIL_CODE);
						ActivityInlandAirlineticketBooking.this.finish();
					} else {
						Toast.makeText(context, "�����쳣���ύ����ʧ�ܣ�", 0).show();
					}
					progressdialog.dismiss();
				} catch (Exception e) {
					progressdialog.dismiss();
					e.printStackTrace();
				}
				break;
			case COMMIT_ORDER_MSG_CODE3:
				jsonParser = new JSONTokener(commitReturnJson3);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						hasCommited = true;
						String orderID = jsonObject.getJSONObject("d")
								.getString("orderid");
						Intent intent = new Intent(context,
								ActivityInlandAirlineticketOrderDetail.class);
						intent.putExtra(
								ActivityInlandAirlineticketOrderDetail.ORDERRECEIPT,
								orderID);
						startActivityForResult(intent, NEW_ORDER_DETAIL_CODE);
						ActivityInlandAirlineticketBooking.this.finish();
					} else {
						Toast.makeText(context, "�����쳣���ύ����ʧ�ܣ�", 0).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case TUIGAIQIAN_MSG_CODE:
				jsonParser = new JSONTokener(tuigaiqianReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						String tgq = jsonObject.getJSONObject("d").getString(
								"visor");
						if (tgq.endsWith("<br/>"))
							tgq.substring(0, tgq.length() - 5);
						tuiGaiQian_state_tv.setText(tgq
								.replace("<br/>", "\r\n"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case TUIGAIQIAN_MSG_CODE3:
				jsonParser = new JSONTokener(tuigaiqianReturnJson);
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {
						String tgq = jsonObject.getJSONObject("d").getString(
								"visor");
						if (tgq.endsWith("<br/>"))
							tgq.substring(0, tgq.length() - 5);
						tuiGaiQian_state_tv3.setText(tgq.replace("<br/>",
								"\r\n"));
					}
				} catch (Exception e) {
				}
				break;
			case CHECK_PRICE_MSG_CODE:
				try {
					JSONObject jsonObject = new JSONObject(checkReturnJson);
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {// ����Ԥ�����۸�����б仯
						String canbooking = jsonObject.getString("canbook");
						order_now_btn.setVisibility(View.VISIBLE);
						if (canbooking.equals("true")) {
							String price = jsonObject.getString("fare");
							order_now_btn.setVisibility(View.VISIBLE);
							isCheck = true;
							// if (getOrderWayType() ==
							// SingleOrDouble.singleWay) {
							if (Float.parseFloat(price) != piaomianjia) {
								piaomianjia = Float.parseFloat(price);
								shoujia = Float.parseFloat(jsonObject
										.getString("sale"));
								jipiaojia_tv.setText("��"
										+ String.valueOf(piaomianjia));
								shoujia_tv.setText("��"
										+ String.valueOf(shoujia));
								caculateBaoxian(baoxian_unitPrice);
								final CustomerAlertDialog cad2 = new CustomerAlertDialog(
										context, true);
								cad2.setTitle("�û�Ʊ��ʵ��Ʊ��۸�Ϊ����" + piaomianjia);
								cad2.setPositiveButton("ȷ��",
										new OnClickListener() {
											@Override
											public void onClick(View arg0) {
												cad2.dismiss();
											}
										});
							}
							// }
							// else {
							// if (Float.parseFloat(price) != piaomianjia3) {
							// piaomianjia3 = Float.parseFloat(price);
							// shoujia3 = Float.parseFloat(jsonObject
							// .getString("sale"));
							// jipiaojia_tv3.setText("��"
							// + String.valueOf(piaomianjia3));
							// shoujia_tv3.setText("��"
							// + String.valueOf(shoujia3));
							// caculateBaoxian(baoxian_unitPrice);
							// final CustomerAlertDialog cad2 = new
							// CustomerAlertDialog(
							// context, true);
							// cad2.setTitle("�û�Ʊ��ʵ��Ʊ��۸�Ϊ����"
							// + piaomianjia3);
							// cad2.setPositiveButton("ȷ��",
							// new OnClickListener() {
							// @Override
							// public void onClick(View arg0) {
							// cad2.dismiss();
							// }
							// });
							// }
							// }
						} else {// false ����Ԥ��
							isCheck = false;
							order_now_btn.setVisibility(View.GONE);
							final CustomerAlertDialog cad2 = new CustomerAlertDialog(
									context, true);
							cad2.setTitle("�ò�λ�����꣬��ѡ��������λ");
							cad2.setPositiveButton("ȷ��", new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									cad2.dismiss();
									ActivityInlandAirlineticketBooking.this
											.finish();
								}
							});
						}
					} else {// ��֤�۸���ִ���
						isCheck = false;
						order_now_btn.setVisibility(View.GONE);
						final CustomerAlertDialog cad2 = new CustomerAlertDialog(
								context, true);
						cad2.setTitle("�ò�λ�����꣬��ѡ��������λ");
						cad2.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad2.dismiss();
								ActivityInlandAirlineticketBooking.this
										.finish();
							}
						});
					}
					progressdialog.dismiss();
				} catch (Exception e) {
					if (progressdialog != null) {
						progressdialog.dismiss();
					}

					e.printStackTrace();
				}
				break;
			case CHECK_PRICE_MSG_CODE3:
				try {
					JSONObject jsonObject = new JSONObject(checkReturnJson3);
					String state = jsonObject.getString("c");

					if (state.equals("0000")) {// ����Ԥ�����۸�����б仯
						String canbooking = jsonObject.getString("canbook");
						order_now_btn.setVisibility(View.VISIBLE);
						if (canbooking.equals("true")) {
							String price = jsonObject.getString("fare");
							if (isCheck) {
								order_now_btn.setVisibility(View.VISIBLE);
							}
							if (Float.parseFloat(price) != piaomianjia3) {
								piaomianjia3 = Float.parseFloat(price);
								shoujia3 = Float.parseFloat(jsonObject
										.getString("sale"));
								jipiaojia_tv3.setText("��"
										+ String.valueOf(piaomianjia3));
								shoujia_tv3.setText("��"
										+ String.valueOf(shoujia3));
								caculateBaoxian(baoxian_unitPrice);
								final CustomerAlertDialog cad2 = new CustomerAlertDialog(
										context, true);
								cad2.setTitle("�û�Ʊ��ʵ��Ʊ��۸�Ϊ����" + piaomianjia3);
								cad2.setPositiveButton("ȷ��",
										new OnClickListener() {
											@Override
											public void onClick(View arg0) {
												cad2.dismiss();
											}
										});
							}
						} else {// false ����Ԥ��
							order_now_btn.setVisibility(View.GONE);
							final CustomerAlertDialog cad2 = new CustomerAlertDialog(
									context, true);
							cad2.setTitle("�ò�λ�����꣬��ѡ��������λ");
							cad2.setPositiveButton("ȷ��", new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									cad2.dismiss();
									ActivityInlandAirlineticketBooking.this
											.finish();
								}
							});
						}
					} else {// ��֤�۸���ִ���
						order_now_btn.setVisibility(View.GONE);
						final CustomerAlertDialog cad2 = new CustomerAlertDialog(
								context, true);
						cad2.setTitle("�ò�λ�����꣬��ѡ��������λ");
						cad2.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad2.dismiss();
								ActivityInlandAirlineticketBooking.this
										.finish();
							}
						});
					}
					// progressdialog.dismiss();
				} catch (Exception e) {
					// progressdialog.dismiss();
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private void startQueryBaoxian() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// url?action=flightisu&sign=1232432&userkey=2bfc0c48923cf89de19f6113c127ce81&str={"userid":"","siteid":""}&sitekey=defage
					MyApp ma = new MyApp(context);
					String siteid = sp.getString(SPkeys.siteid.getString(), "");
					String str = "{\"userid\":\""
							+ sp.getString(SPkeys.userid.getString(), "")
							+ "\",\"siteid\":\"" + siteid + "\"}";
					String param = "action=flightisu&str="
							+ str
							+ "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "flightisu" + str);
					baoxianReturnJson = HttpUtils.getJsonContent(
							ma.getServeUrl(), param);
					Message msg = new Message();
					msg.what = BAOXIAN_MSG_CODE;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void startQueryTuigaiqian(final String flightNo,
			final String cabin, final String IsTeHui, final String fdate,
			final int TUIGAIQIAN_mc) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MyApp ma = new MyApp(context);
					String str = "{\"alcode\":\"" + flightNo
							+ "\",\"cabin\":\"" + cabin + "\",\"IsTeHui\":\""
							+ IsTeHui + "\",\"fdate\":\"" + fdate + "\"}";
					String param = "action=visor&str="
							+ str
							+ "&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "visor" + str);
					tuigaiqianReturnJson = HttpUtils.getJsonContent(
							ma.getServeUrl(), param);
					Message msg = new Message();
					msg.what = TUIGAIQIAN_mc;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ���
	 */
	private void checkPrice(final VerifyFlightPrice vfp) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(context);
				String action = "action=cabinverify&str=";
				String str = JSONHelper.toJSON(vfp);
				// String siteid = sp.getString(SPkeys.siteid.getString(),
				// "65");
				// String str = "{\"flightno\":\"" + flightno + "\",\"s\": \""
				// + startcity_code + "\",\"e\":\"" + arrivecity_code
				// + "\",\"sd\": \"" + sd + "\",\"cabin\": \"" + cabin
				// + "\",\"fare\": \"" + fare + "\",\"ischd\": \"" + ischd
				// + "\",\"isspe\": \"" + isspe + "\",\"siteid\": \""
				// + siteid + "\"}";
				String param = action
						+ str
						+ "&userkey="
						+ ma.getHm().get(PackageKeys.USERKEY.getString())
								.toString()
						+ "&sign="
						+ CommonFunc.MD5(ma.getHm()
								.get(PackageKeys.USERKEY.getString())
								.toString()
								+ "cabinverify" + str);

				checkReturnJson = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Message msg = new Message();
				msg.what = CHECK_PRICE_MSG_CODE;
				handler.sendMessage(msg);
			}
		}).start();
		progressdialog = CustomProgressDialog.createDialog(context);
		progressdialog.setMessage("������ۣ����Ժ�...");
		progressdialog.setCancelable(true);
		progressdialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		progressdialog.show();
	}

	/**
	 * ���
	 */
	private void checkPrice3(final VerifyFlightPrice vfp) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyApp ma = new MyApp(context);
				String action = "action=cabinverify&str=";
				String str = JSONHelper.toJSON(vfp);
				// String siteid = sp.getString(SPkeys.siteid.getString(),
				// "65");
				// String str = "{\"flightno\":\"" + flightno + "\",\"s\": \""
				// + startcity_code + "\",\"e\":\"" + arrivecity_code
				// + "\",\"sd\": \"" + sd + "\",\"cabin\": \"" + cabin
				// + "\",\"fare\": \"" + fare + "\",\"ischd\": \"" + ischd
				// + "\",\"isspe\": \"" + isspe + "\",\"siteid\": \""
				// + siteid + "\"}";
				String param = action
						+ str
						+ "&userkey="
						+ ma.getHm().get(PackageKeys.USERKEY.getString())
								.toString()
						+ "&sign="
						+ CommonFunc.MD5(ma.getHm()
								.get(PackageKeys.USERKEY.getString())
								.toString()
								+ "cabinverify" + str);

				checkReturnJson3 = HttpUtils.getJsonContent(ma.getServeUrl(),
						param);
				Message msg = new Message();
				msg.what = CHECK_PRICE_MSG_CODE3;
				handler.sendMessage(msg);
			}
		}).start();
		// progressdialog = CustomProgressDialog.createDialog(context);
		// progressdialog.setMessage("������ۣ����Ժ�...");
		// progressdialog.setCancelable(true);
		// progressdialog.setOnCancelListener(new OnCancelListener() {
		// @Override
		// public void onCancel(DialogInterface dialog) {
		// }
		// });
		// progressdialog.show();
	}

	/*
	 * �ύȥ�̶���
	 */
	private void commitOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (getOrderWayType() == SingleOrDouble.doubleWayBack) {
						commitOrder3();
					}
					// url?action=forder& "userid": "649","amount":
					// "682","origin":
					// "6","orderremark": "������ע",
					MyApp ma = new MyApp(context);
					String flightString = "";
					flightString = getFlightsJsonString(SingleOrDouble.singleWay);
					/*
					 * if (getOrderWayType() == SingleOrDouble.singleWay) {
					 * flightString =
					 * getFlightsJsonString(SingleOrDouble.singleWay); } if
					 * (getOrderWayType() == SingleOrDouble.doubleWayBack) {
					 * flightString =
					 * getFlightsJsonString(SingleOrDouble.singleWay) + "," +
					 * getFlightsJsonString(SingleOrDouble.doubleWayBack); }
					 */
					String siteid = sp.getString(SPkeys.siteid.getString(),
							"65");
					String str = "{\"userid\":\""
							+ sp.getString(SPkeys.userid.getString(), "")
							+ "\",\"amount\": \""
							+ totalPrice
							+ "\",\"origin\":\"2\",\"orderremark\":\"Android�ͻ���\""
							+ ",\"flights\":[" + flightString
							+ "],\"passenger\":" + getPassengerJsonString()
							+ ",\"content\":" + getContentJsonString()
							+ ",\"itinerary\":{}" + ",\"siteid\":\"" + siteid
							+ "\"}";
					str = str.replace("null", "");
					String orgin = ma.getHm()
							.get(PackageKeys.ORGIN.getString()).toString();
					String param = "?action=forder&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "forder" + str) + "&orgin=" + orgin;
					// try {
					// str = URLEncoder.encode(str, "utf-8");
					// } catch (UnsupportedEncodingException e) {
					// e.printStackTrace();
					// }
					commitReturnJson = HttpUtils.myPost(ma.getServeUrl()
							+ param, "&str=" + str);
					Message msg = new Message();
					msg.what = COMMIT_ORDER_MSG_CODE;
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

	/*
	 * �ύ���̶���
	 */
	private void commitOrder3() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// url?action=forder& "userid": "649","amount":
					// "682","origin":
					// "6","orderremark": "������ע",
					MyApp ma = new MyApp(context);
					String flightString = "";
					flightString = getFlightsJsonString(SingleOrDouble.doubleWayBack);
					/*
					 * if (getOrderWayType() == SingleOrDouble.singleWay) {
					 * flightString =
					 * getFlightsJsonString(SingleOrDouble.singleWay); } if
					 * (getOrderWayType() == SingleOrDouble.doubleWayBack) {
					 * flightString =
					 * getFlightsJsonString(SingleOrDouble.singleWay) + "," +
					 * getFlightsJsonString(SingleOrDouble.doubleWayBack); }
					 */
					String siteid = sp.getString(SPkeys.siteid.getString(),
							"65");
					String str = "{\"userid\":\""
							+ sp.getString(SPkeys.userid.getString(), "")
							+ "\",\"amount\": \""
							+ totalPrice3
							+ "\",\"origin\":\"2\",\"orderremark\":\"Android�ͻ���\""
							+ ",\"flights\":[" + flightString
							+ "],\"passenger\":" + getPassengerJsonString()
							+ ",\"content\":" + getContentJsonString()
							+ ",\"itinerary\":{}" + ",\"siteid\":\"" + siteid
							+ "\"}";
					str = str.replace("null", "");
					String orgin = ma.getHm()
							.get(PackageKeys.ORGIN.getString()).toString();
					String param = "?action=forder&userkey="
							+ ma.getHm().get(PackageKeys.USERKEY.getString())
									.toString()
							+ "&sign="
							+ CommonFunc.MD5(ma.getHm()
									.get(PackageKeys.USERKEY.getString())
									.toString()
									+ "forder" + str) + "&orgin=" + orgin;
					// try {
					// str = URLEncoder.encode(str, "utf-8");
					// } catch (UnsupportedEncodingException e) {
					// e.printStackTrace();
					// }
					commitReturnJson3 = HttpUtils.myPost(ma.getServeUrl()
							+ param, "&str=" + str);
					Message msg = new Message();
					msg.what = COMMIT_ORDER_MSG_CODE3;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		// progressdialog = CustomProgressDialog.createDialog(context);
		// progressdialog.setMessage("�����ύ���������Ժ�...");
		// progressdialog.setCancelable(true);
		// progressdialog.setOnCancelListener(new OnCancelListener() {
		// @Override
		// public void onCancel(DialogInterface dialog) {
		// }
		// });
		// progressdialog.show();
	}

	/*
	 * ���ݳ˻����������յ��ۼ��㱣�ռ۸���ʾ��ҳ�� B2C�۸�̶�Ϊ��20/��
	 */
	private void caculateBaoxian(Float unitPrice) {
		int personCount = 0;
		if (passengerList == null) {
			personCount = 0;
		} else {
			personCount = passengerList.size();
		}

		if (getOrderWayType() == SingleOrDouble.singleWay) {
			baoxian_price_and_count_tv.setText("��" + baoxian_unitPrice + "x"
					+ personCount + "��");
			if (needBaoxianBoolean)
				totalPrice = personCount
						* (piaomianjia + jijianfei + ranyoufei
								+ baoxian_unitPrice - youhui);
			else
				totalPrice = personCount
						* (piaomianjia + jijianfei + ranyoufei - youhui);
		} else {
			baoxian_price_and_count_tv.setText("��" + baoxian_unitPrice + "x"
					+ personCount + "��x2(����)");
			if (needBaoxianBoolean) {
				totalPrice = personCount
						* (piaomianjia + jijianfei + ranyoufei
								+ baoxian_unitPrice - youhui);
				totalPrice3 = personCount
						* (piaomianjia3 + jijianfei3 + ranyoufei3
								+ baoxian_unitPrice - youhui3);
			} else {
				totalPrice = personCount
						* (piaomianjia + jijianfei + ranyoufei - youhui);
				totalPrice3 = personCount
						* (piaomianjia3 + jijianfei3 + ranyoufei3 - youhui3);
			}
			total_jipiaojia_tv.setText("��" + (piaomianjia + piaomianjia3));
			total_jijian_price_tv.setText("��" + (jijianfei + jijianfei));
			total_ranyou_price_tv.setText("��" + (ranyoufei + ranyoufei3));
			total_fanMoney_tv.setText("��" + (youhui + youhui3));
		}

		total_price_tv.setText("��" + String.valueOf(totalPrice + totalPrice3));
		order_totalmoney_tv.setText("��"
				+ String.valueOf(totalPrice + totalPrice3));
	}

	// ���� ������Ϣ
	private String getFlightsJsonString(SingleOrDouble sd) {
		String str = "";
		InlandAirlineInfo flight = new InlandAirlineInfo();
		CabList cabin = null;
		if (sd == SingleOrDouble.singleWay) {
			flight = getIntentFlightInfo(SingleOrDouble.singleWay);
			try {
				JSONObject object = flight.getCablist().getJSONObject(
						selectCabinListIndex);
				cabin = new CabList(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (sd == SingleOrDouble.doubleWayBack) {
			flight = getIntentFlightInfo(SingleOrDouble.doubleWayBack);
			try {
				JSONObject object = flight.getCablist().getJSONObject(
						selectCabinListIndex3);
				cabin = new CabList(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		CuandanFlight cf = new CuandanFlight();
		cf.setCabin(cabin.getCabin());
		cf.setCabinname(cabin.getCabinName());
		cf.setCarrname(flight.getCarrinerName());
		cf.setCrrier(cabin.getAirLineCode());
		cf.setDiscount(cabin.getDiscount());
		cf.setEcname(flight.getEndPortName());
		cf.setEcode(flight.getEndPort());
		try {
			cf.setEdate(DateUtil.getDate(flight.getArriveTime()));
			cf.setEtime(DateUtil.getTime(flight.getArriveTime()));
			cf.setSdate(DateUtil.getDate(flight.getOffTime()));
			cf.setStime(DateUtil.getTime(flight.getOffTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cf.setEt(flight.getEndT());
		cf.setFare(cabin.getFare());
		cf.setFlightno(flight.getFlightNo());
		cf.setIsspe(cabin.getIsSpe());
		cf.setOil(flight.getOil());
		cf.setPlane(flight.getPlaneModel());
		cf.setIsTeHui(cabin.getIsTeHui());
		cf.setFareProviderStr(cabin.getFareProviderStr());
		cf.setPriceProvider(cabin.getPriceProvider());
		cf.setFlagEn(cabin.getFlagEn());
		if (!isB2Boolean) {
			cf.setPolicyid(cabin.getPolicyID());
			cf.setPolicytype(cabin.getIsSpePolicy() == "true" ? "1" : "0");
			cf.setRate(cabin.getRate());
			cf.setRateinfo(cabin.getRateInfo().replace("\n", "")
					.replace("\r", "").replace(" ", ""));
			cf.setRebate(cabin.getRate());
			cf.setRemark("");
			cf.setUserrebate(cabin.getUserRate());
		} else {
			cf.setPolicyid(selectedPolicyB.getPolicyid());
			cf.setPolicytype(selectedPolicyB.getIsspepolicy());
			cf.setRate(selectedPolicyB.getUserrate());
			cf.setRateinfo(selectedPolicyB.getRateinfo());
			cf.setRebate(selectedPolicyB.getTotalrate());// �ܷ���
			cf.setRemark("");
			cf.setUserrebate(selectedPolicyB.getUserrate());
		}

		cf.setRt("");// ��Ӧ����Ʊʱ��
		cf.setRuntime(flight.getRunTime());
		cf.setScname(flight.getStartPortName());
		cf.setScode(flight.getStartPort());
		cf.setStafare(flight.getStaFare());
		cf.setSupplier(cabin.getSupplier());
		cf.setSt(flight.getStartT());
		cf.setTax(flight.getTax());
		cf.setType("1");// ����
		cf.setWt("");// "��Ӧ�����°�ʱ��"

		str = JSONHelper.toJSON(cf);
		return str;
	}

	// ���� �˻�����Ϣ
	private String getPassengerJsonString() {
		String str = "";
		ArrayList<CuandanPassenger> cpList = new ArrayList<CuandanPassenger>();
		for (int i = 0; i < passengerList.size(); i++) {
			CuandanPassenger cp = new CuandanPassenger();
			cp.setAddto(passengerList.get(i).getAddto());
			cp.setIdno(passengerList.get(i).getIdentificationNum());
			String idtypeString = passengerList.get(i).getIdentificationType();
			cp.setIdtype(String.valueOf(IdType.IdTypeReverse.get(idtypeString)));
			cp.setIsunum(needBaoxianBoolean ? "1" : "0");
			cp.setMobile(passengerList.get(i).getMobie() == null ? ""
					: passengerList.get(i).getMobie());
			try {// URLEncoder.encode((passengerList.get(i).getPassengerName()),
					// "utf-8")
				cp.setPname(passengerList.get(i).getPassengerName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			cp.setPtype("1");
			cpList.add(cp);
		}
		str = JSONHelper.toJSON(cpList);
		return str;
	}

	// ���� ��ϵ����Ϣ
	private String getContentJsonString() {
		String str = "";
		str = "{\"name\": \"" + sp.getString(SPkeys.username.getString(), "")
				+ "\",\"mobile\": \""
				+ contact_person_phone_et.getText().toString().trim()
				+ "\",\"tel\": \"\",\"email\": \"\"}";
		return str;
	}

	View.OnClickListener btnClickListner = new View.OnClickListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onClick(View v) {
			try {
				Intent intent1 = new Intent(context,
						SelectZhengceActivity.class);
				switch (v.getId()) {
				case R.id.zhengce_rl:
					intent1.putExtra(SelectZhengceActivity.PLICYLISTSTR,
							getPolicyStr());
					startActivityForResult(intent1, SELECTPOLICYREQUESTCODE);
					break;
				case R.id.zhengce_rl3:
					intent1.putExtra(SelectZhengceActivity.PLICYLISTSTR,
							getPolicyStr3());
					startActivityForResult(intent1, SELECTPOLICYREQUESTCODE3);
					break;
				case R.id.back_imgbtn:
					if (!hasCommited) {
						final CustomerAlertDialog cad = new CustomerAlertDialog(
								context, false);
						cad.setTitle("���Ķ�����δ��ɣ�ȷ�Ϸ�����д��");
						cad.setPositiveButton("ȷ�Ϸ���", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								finish();
								cad.dismiss();
							}
						});
						cad.setNegativeButton1("������д", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad.dismiss();
							}
						});
					} else {
						finish();
					}
					break;
				case R.id.home_imgbtn:
					if (!hasCommited) {
						final CustomerAlertDialog cad1 = new CustomerAlertDialog(
								context, false);
						cad1.setTitle("���Ķ�����δ��ɣ�ȷ�Ϸ�����д��");
						cad1.setPositiveButton("������ҳ", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(context,
										MainActivityN.class));
								cad1.dismiss();
							}
						});
						cad1.setNegativeButton1("������д", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad1.dismiss();
							}
						});
					} else {
						startActivity(new Intent(context, MainActivityN.class));
					}
					break;
				case R.id.lianxiren_icon_imgbtn:
					startActivityForResult(
							new Intent(
									context,
									com.jike.shanglv.SeclectCity.ContactListActivity.class),
							13);
					break;
				case R.id.tuiGaiQian_rl:
					up_down_arrow_iv
							.setSelected(!up_down_arrow_iv.isSelected());
					if (tuiGaiQian_state_tv.getVisibility() == View.VISIBLE) {
						tuiGaiQian_state_tv.setVisibility(View.GONE);
					} else if (tuiGaiQian_state_tv.getVisibility() == View.GONE) {
						tuiGaiQian_state_tv.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.tuiGaiQian_rl3:
					up_down_arrow_iv3.setSelected(!up_down_arrow_iv
							.isSelected());
					if (tuiGaiQian_state_tv3.getVisibility() == View.VISIBLE) {
						tuiGaiQian_state_tv3.setVisibility(View.GONE);
					} else if (tuiGaiQian_state_tv3.getVisibility() == View.GONE) {
						tuiGaiQian_state_tv3.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.add_passager_rl:
					Intent intent = new Intent(context,
							ActivityInlandAirlineticketSelectPassengers.class);
					intent.putExtra(
							ActivityInlandAirlineticketSelectPassengers.SYSTYPE,
							"0");
					intent.putExtra(
							ActivityInlandAirlineticketSelectPassengers.TITLE_NAME,
							"ѡ��˻���");
					intent.putExtra(ALLPASSENGERSLIST,
							JSONHelper.toJSON(allPassengerList));
					intent.putExtra(SELECTEDPASSENGERSLIST,
							JSONHelper.toJSON(passengerList));
					startActivityForResult(intent,
							ADD_PASSENGERS_FORRESULET_CODE);
					break;
				case R.id.baoxian_price_and_count_tv:
				case R.id.baoxian_check_imgbtn:
				case R.id.baoxian_rl:
					if (!baoxian_check_imgbtn.isSelected()) {
						baoxian_check_imgbtn.setSelected(true);
						needBaoxianBoolean = true;
						baoxian_price_and_count_tv.setTextColor(getResources()
								.getColor(R.color.price_yellow));
					} else if (baoxian_check_imgbtn.isSelected()) {
						baoxian_check_imgbtn.setSelected(false);
						needBaoxianBoolean = false;
						baoxian_price_and_count_tv.setTextColor(getResources()
								.getColor(R.color.danhuise));
					}
					caculateBaoxian(baoxian_unitPrice);
					break;
				case R.id.order_now_btn:
					if (passengerList.size() == 0) {
						final CustomerAlertDialog cad3 = new CustomerAlertDialog(
								context, true);
						cad3.setTitle("����ӳ˻���");
						cad3.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad3.dismiss();
							}
						});
						break;
					}
					if (!CommonFunc.isMobileNO(contact_person_phone_et
							.getText().toString().trim())) {
						final CustomerAlertDialog cad2 = new CustomerAlertDialog(
								context, true);
						cad2.setTitle("�ֻ������ʽ����ȷ");
						cad2.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad2.dismiss();
							}
						});
						break;
					} else {
						sp.edit()
								.putString(
										SPkeys.gnjpContactPhone.getString(),
										contact_person_phone_et.getText()
												.toString()).commit();
					}
					if (isB2Boolean && selectedPolicyB == null) {
						final CustomerAlertDialog cad3 = new CustomerAlertDialog(
								context, true);
						cad3.setTitle("��ѡ������");
						cad3.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad3.dismiss();
							}
						});
						break;
					}
					if (!hasCommited)
						commitOrder();
					// checkPrice();
					else {
						final CustomerAlertDialog cad3 = new CustomerAlertDialog(
								context, true);
						cad3.setTitle("�벻Ҫ�ظ��ύ����");
						cad3.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								cad3.dismiss();
							}
						});
					}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			switch (resultCode) {
			case SelectZhengceActivity.SELECTED_POLICY_CODE:
				Bundle b1 = null;
				if (data != null) {
					b1 = data.getExtras();
				} else
					break;
				String policyString = "";
				if (b1 != null
						&& b1.containsKey(SelectZhengceActivity.SELECTEDPOLICY)) {
					policyString = b1
							.getString(SelectZhengceActivity.SELECTEDPOLICY);
					try {
						selectedPolicyB = JSONHelper.parseObject(policyString,
								PolicyList.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (requestCode == SELECTPOLICYREQUESTCODE) {
						piaomianjia = Float.valueOf(selectedPolicyB.getFare());
						youhui = Float.valueOf(selectedPolicyB.getFare())
								- Float.valueOf(selectedPolicyB.getSale());
						zhengcefandian_tv.setText("���㣺"
								+ selectedPolicyB.getUserrate() + "%");
						jipiaojia_tv.setText("��" + String.valueOf(piaomianjia));
						shoujia_tv.setText("��" + String.valueOf(shoujia));
						fanMoney_tv.setText("����" + youhui);
						caculateBaoxian(baoxian_unitPrice);
					} else if (requestCode == SELECTPOLICYREQUESTCODE3) {
						piaomianjia3 = Float.valueOf(selectedPolicyB.getFare());
						youhui3 = Float.valueOf(selectedPolicyB.getFare())
								- Float.valueOf(selectedPolicyB.getSale());
						zhengcefandian_tv3.setText("���㣺"
								+ selectedPolicyB.getUserrate() + "%");
						jipiaojia_tv3.setText("��"
								+ String.valueOf(piaomianjia3));
						shoujia_tv3.setText("��" + String.valueOf(shoujia3));
						fanMoney_tv3.setText("����" + youhui3);
						caculateBaoxian(baoxian_unitPrice);
					}
				}
				break;
			case ActivityInlandAirlineticketSelectPassengers.SELECTED_FINISH:
				Bundle b = null;
				if (data != null) {
					b = data.getExtras();
				} else
					break;
				String passengerListString = "",
				allPassengerListString = "";
				if (b != null && b.containsKey(SELECTEDPASSENGERSLIST)) {
					passengerListString = b.getString(SELECTEDPASSENGERSLIST);
				}
				if (b != null && b.containsKey(ALLPASSENGERSLIST)) {
					allPassengerListString = b.getString(ALLPASSENGERSLIST);
				} else
					break;
				try {
					passengerList.clear();
					passengerList = (ArrayList<Passenger>) JSONHelper
							.parseCollection(passengerListString, List.class,
									Passenger.class);
					allPassengerList = (ArrayList<Passenger>) JSONHelper
							.parseCollection(allPassengerListString,
									List.class, Passenger.class);
					passengerList = removeDuplictePassengers(passengerList);
					if (passengerList.size() > 0) {
						add_passager_tv.setText(getResources().getString(
								R.string.modify_passenger));
						passenger_head_divid_line.setVisibility(View.VISIBLE);
					} else if (passengerList.size() == 0) {
						add_passager_tv.setText(getResources().getString(
								R.string.add_passenger));
						passenger_head_divid_line.setVisibility(View.GONE);
					}
					caculateBaoxian(baoxian_unitPrice);
					ListAdapter adapter = new PassengerListAdapter(context,
							passengerList);
					passenger_listview.setAdapter(adapter);
					setListViewHeightBasedOnChildren(passenger_listview);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
				break;
			default:
				break;
			}
			if (requestCode == 13) {
				if (data == null)
					return;
				Bundle b = data.getExtras();
				if (b != null && b.containsKey("pickedPhoneNum")) {
					String myNum = b.getString("pickedPhoneNum");
					if (myNum.startsWith("17951")) {
						myNum = myNum.substring(5);
					} else if (myNum.startsWith("+86")) {
						myNum = myNum.substring(3);
					}
					contact_person_phone_et.setText(myNum);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class PassengerListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<Passenger> str;

		public PassengerListAdapter(Context context, List<Passenger> list1) {
			this.inflater = LayoutInflater.from(context);
			this.str = list1;
		}

		@Override
		public int getCount() {
			return str.size();
		}

		@Override
		public Object getItem(int position) {
			return str.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater
						.inflate(
								R.layout.item_inland_airlineticket_passenger_list,
								null);
			}
			View divid_line = convertView.findViewById(R.id.divid_line);
			TextView passengerName_tv = (TextView) convertView
					.findViewById(R.id.passengerName_tv);
			TextView identificationType_tv = (TextView) convertView
					.findViewById(R.id.identificationType_tv);
			TextView identificationNum_tv = (TextView) convertView
					.findViewById(R.id.identificationNum_tv);
			TextView passengerType_tv = (TextView) convertView
					.findViewById(R.id.passengerType_tv);

			passengerName_tv.setText(str.get(position).getPassengerName());
			identificationType_tv.setText(str.get(position)
					.getIdentificationType());
			identificationNum_tv.setText(str.get(position)
					.getIdentificationNum());
			passengerType_tv.setText("(" + str.get(position).getPassengerType()
					+ ")");
			RelativeLayout passenger_rl = (RelativeLayout) convertView
					.findViewById(R.id.passenger_rl);
			passenger_rl.setTag(position + "");
			if (position == passengerList.size() - 1) {
				divid_line.setVisibility(View.GONE);
			}

			ImageButton delete_imgbtn = (ImageButton) convertView
					.findViewById(R.id.delete_imgbtn);
			delete_imgbtn.setTag(position + "");// ��Item�е�button����tag������tag�ж��û�����˵ڼ���
			delete_imgbtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int index = Integer.parseInt(v.getTag().toString());
					passengerList.remove(index);
					notifyDataSetChanged();
					setListViewHeightBasedOnChildren(passenger_listview);
					if (passengerList.size() == 0) {
						add_passager_tv.setText(getResources().getString(
								R.string.add_passenger));
						passenger_head_divid_line.setVisibility(View.GONE);
					}
					caculateBaoxian(baoxian_unitPrice);
				}
			});
			return convertView;
		}
	}

	// ȥ���ظ��ĳ˻���
	public static ArrayList<Passenger> removeDuplictePassengers(
			ArrayList<Passenger> userList) {
		Set<Passenger> s = new TreeSet<Passenger>(new Comparator<Passenger>() {

			@Override
			public int compare(Passenger o1, Passenger o2) {
				return o1.getPassengerName().compareTo(o2.getPassengerName());
			}
		});
		s.addAll(userList);
		return new ArrayList<Passenger>(s);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("passengerList", JSONHelper.toJSON(passengerList));
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			String pl = savedInstanceState.getString("passengerList");
			try {
				passengerList.clear();
				passengerList = (ArrayList<Passenger>) JSONHelper
						.parseCollection(pl, List.class, Passenger.class);
				PassengerListAdapter adapter = new PassengerListAdapter(
						context, passengerList);
				passenger_listview.setAdapter(adapter);
				setListViewHeightBasedOnChildren(passenger_listview);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���ScrollViewǶ��ListViewֻ��ʾһ�е�����
	 * ��������ListView��Adapter�󣬸���ListView������Ŀ���¼���ListView�ĸ߶�
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityInlandAirlineticketBooking");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ActivityInlandAirlineticketBooking"); // ͳ��ҳ��
		MobclickAgent.onResume(this); // ͳ��ʱ��
	}

}
