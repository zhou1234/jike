package com.jike.shanglv;

import java.util.HashMap;
import android.content.Context;

import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;

//http://211.152.50.208:8011/gateway.aspx?action=trainorderv2&userkey=5b13658a9fc945e34893f806027d467a&sitekey=&sign=33bdd88b4e5bf273de8c4e70d8c10519&orgin=0&str={"uid":"3296","Amount": "563.0","sid":"65","vcode":"urkx","TrainNo":"G105","SCity":"北京南","ECity":"上海虹桥","ETime":"13:21","STime":"07:36","SDate":"2014-12-18","Mobile":"13795311810","Email":"123@163.com","Name":"qunar","TicketCount":"1","PsgInfo":[{"CardNo":"330702197108020812","CardType":"身份证","IncAmount":"10.0","Phone":"15378645852","PsgName":"孙民华","Saleprice":"553.0","SeatType":"二等座","TicketType":"成人"}]}
//,"rebate":"8.6","remark":"正规出票：换编码，不废票，退票改期按照客规加收百分子30，有可能换舱位出票，不支持儿童、婴儿票。票面价格低于正常价格且差价不退还，有可能换舱位出票，不能签转外航,退票截止时间：2355","rt":"","runtime":"2小时15分钟","scname":"北京首都","scode":"PEK","sdate":"2014-12-17","st":"T2","stafare":"1130","stime":"17 07","supplier":"Piaomeng","tax":"50","type":"1","userrebate":"2.1","wt":""}],"passenger":[{"addto":"1","idno":"32036","idtype":"9","isunum":"1","mobile":"","pname":"小丸子","ptype":"1"}],"content":{"name": "tongcher","mobile": "13795311810","tel": "","email": ""},"itinerary":{},"siteid":"65"}

//百度地图 SHA1 0D:3C:EC:2E:C2:01:A0:E6:C7:AE:44:B4:05:17:9D:F8:BE:A9:70:9E
public class MyApp {
	private Context context;

	HashMap<String, Object> self_hm = new HashMap<String, Object>();// 际珂B2C
																	// 商旅管家
	HashMap<String, Object> self_b_hm = new HashMap<String, Object>();// 际珂B2B
																		// 商旅助手
	HashMap<String, Object> nanbei_hm = new HashMap<String, Object>();//
	HashMap<String, Object> nanbei_b_hm = new HashMap<String, Object>();
	HashMap<String, Object> menghang_hm = new HashMap<String, Object>();
	HashMap<String, Object> menghang_b_hm = new HashMap<String, Object>();

	// HashMap的使用
	// put(K key, V value)
	// hm.put(a,b); //插入值为b,key值为a
	// hm.get(key); //返回值为value
	/**   
	 * 打包不同程序时更改此处 另外打包时需要更改百度地图的key
	 * 此类中只需更改以下三个值：RELEASE、hm、AndroidManifest中的程序名及图标
	 */
	public static boolean RELEASE = true;// 测试 or 发布，接口
	private HashMap<String, Object> hm = self_hm;// menghang_hm;//self_hm

	public static String userkey = "ffdd14d2e6c26b70749c8b2c08067c69 ";// da0bee39709731f3f1baaff194f43d41
	public static String sitekey = "";
	public static String Pkey = "5D85BCAC5A85476380226ED6F7C34B8C";// 公钥

	public MyApp(Context context) {
		this.context = context;
		createValue();
	}

	public boolean isRELEASE() {
		return RELEASE;
	}

	public HashMap<String, Object> getHm() {
		return hm;
	}

	/**
	 * 获取接口的地址
	 */
	public String getMallServeUrl() {

		return context.getResources().getString(R.string.mall_server_url);

	}

	/**
	 * 获取接口的地址
	 */
	public String getServeUrl() {
		if (RELEASE)
			return context.getResources().getString(R.string.formal_server_url);
		else
			return context.getResources().getString(R.string.test_server_url);
	}

	/**
	 * 获取支付接口的地址
	 */
	public String getPayServeUrl() {
		if (RELEASE)
			return context.getResources().getString(
					R.string.formal_pay_server_url);
		else
			return context.getResources().getString(
					R.string.test_pay_server_url);
	}

	/**
	 * 获取火车票验证码接口的地址
	 */
	public String getValidcodeServeUrl() {
		if (RELEASE)
			return context.getResources().getString(
					R.string.formal_train_validcode);
		else
			return context.getResources().getString(
					R.string.test_train_validcode);
	}

	/**
	 * 获取航空公司logo
	 */
	public String getFlightCompanyLogo() {
		if (RELEASE)
			return context.getResources().getString(
					R.string.formal_flight_company_logo);
		else
			return context.getResources().getString(
					R.string.test_flight_company_logo);
	}

	/**
	 * 关于软件说明Url
	 */
	public String getAbout() {
		if (RELEASE)
			return context.getResources().getString(R.string.formal_about);
		else
			return context.getResources().getString(R.string.test_about);
	}

	/**
	 * 获取update接口的地址
	 */
	public String getUpdateServeUrl() {
		if (RELEASE)
			return context.getResources().getString(R.string.formal_update_url);
		else
			return context.getResources().getString(R.string.test_update_url);
	}

	/**
	 * 构建不同厂家的打包数据
	 */
	public void createValue() {
		// 商旅管家
		self_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(),
				R.drawable.welcome);
		self_hm.put(PackageKeys.APP_NAME.getString(), R.string.app_name);
		self_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(),
				R.drawable.menu_logo);
		self_hm.put(PackageKeys.UPDATE_NOTE.getString(), "jike");
		self_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2C);
		self_hm.put(PackageKeys.USERKEY.getString(),
				RELEASE ? "ffdd14d2e6c26b70749c8b2c08067c69"
						: "5b13658a9fc945e34893f806027d467a");
		self_hm.put(PackageKeys.ORGIN.getString(), 0);// 该参数加于20141104，用于区分订单、请求的来源
		/* Android商旅管家0 Android商旅助手1 IOS商旅管家2 IOS商旅助手3 梦航 管家4 梦航 助手5 */

		// 商旅助手
		// self_b_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(),
		// R.drawable.welcome_b);
		self_b_hm.put(PackageKeys.APP_NAME.getString(), R.string.app_name_b);
		self_b_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(),
				R.drawable.menu_logo_b);
		self_b_hm.put(PackageKeys.UPDATE_NOTE.getString(), "jike_b");
		self_b_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2B);
		self_b_hm.put(PackageKeys.USERKEY.getString(),
				RELEASE ? "ffdd14d2e6c26b70749c8b2c08067c69"
						: "5b13658a9fc945e34893f806027d467a");
		self_b_hm.put(PackageKeys.ORGIN.getString(), 1);

		// 梦航商旅
		// menghang_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(),
		// R.drawable.welcome_menghang);
		menghang_hm.put(PackageKeys.APP_NAME.getString(),
				R.string.app_name_menghang);
		// menghang_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(),
		// R.drawable.menu_logo_menghang);
		menghang_hm.put(PackageKeys.UPDATE_NOTE.getString(), "menghangshanglv");
		menghang_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2C);
		menghang_hm.put(PackageKeys.USERKEY.getString(),
				RELEASE ? "fc5865a78e9cb8b3d63c5428d4d32a4c"
						: "5b13658a9fc945e34893f806027d467a");
		menghang_hm.put(PackageKeys.ORGIN.getString(), 4);
	}
}
