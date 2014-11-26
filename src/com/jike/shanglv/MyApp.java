package com.jike.shanglv;

import java.util.HashMap;
import android.content.Context;

import com.jike.shanglv.Enums.PackageKeys;
import com.jike.shanglv.Enums.Platform;


//�ٶȵ�ͼ SHA1 0D:3C:EC:2E:C2:01:A0:E6:C7:AE:44:B4:05:17:9D:F8:BE:A9:70:9E
public class MyApp {
	private Context context;
	
	HashMap<String,Object> self_hm=new HashMap<String,Object>();//����B2C
	HashMap<String,Object> self_b_hm=new HashMap<String,Object>();//����B2B
	HashMap<String,Object> nanbei_hm=new HashMap<String,Object>();//
	HashMap<String,Object> nanbei_b_hm=new HashMap<String,Object>();
	HashMap<String,Object> menghang_hm=new HashMap<String,Object>();
	HashMap<String,Object> menghang_b_hm=new HashMap<String,Object>();

//	HashMap��ʹ��
//	put(K key, V value) 
//	hm.put(a,b); //����ֵΪb,keyֵΪa
//	hm.get(key); //����ֵΪvalue
	/**�����ͬ����ʱ���Ĵ˴�   ������ʱ��Ҫ���İٶȵ�ͼ��key
	 * ������ֻ�������������ֵ��RELEASE��hm��AndroidManifest�еĳ�������ͼ��
	 */
	public static boolean RELEASE = true;//����  or �������ӿ�
	private HashMap<String,Object> hm=self_hm;//menghang_hm;//self_hm
	
//	public static String userkey="5b13658a9fc945e34893f806027d467a";//5b13658a9fc945e34893f806027d467a��Ч�ڵ�2014.09.10
	public static String sitekey="";
	
	public MyApp(Context context){
		this.context=context;
		createValue();
	}
	
	public boolean isRELEASE() {
		return RELEASE;
	}
	
	public HashMap<String, Object> getHm() {
		return hm;
	}
	
	/**��ȡ�ӿڵĵ�ַ
	 */
	public String getServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_server_url);
		else
			return context.getResources().getString(R.string.test_server_url);
	
	}
	/**��ȡ֧���ӿڵĵ�ַ
	 */
	public String getPayServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_pay_server_url);
		else return context.getResources().getString(R.string.test_pay_server_url);
	}
	/**��ȡ��Ʊ��֤��ӿڵĵ�ַ
	 */
	public String getValidcodeServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_train_validcode);
		else return context.getResources().getString(R.string.test_train_validcode);
	}
	/**��ȡ���չ�˾logo
	 */
	public String getFlightCompanyLogo() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_flight_company_logo);
		else return context.getResources().getString(R.string.test_flight_company_logo);
	}
	/**�������˵��Url
	 */
	public String getAbout() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_about);
		else return context.getResources().getString(R.string.test_about);
	}
	/**��ȡupdate�ӿڵĵ�ַ
	 */
	public String getUpdateServeUrl() {
		if(RELEASE)
			return context.getResources().getString(R.string.formal_update_url);
		else return context.getResources().getString(R.string.test_update_url);
	}

	/**������ͬ���ҵĴ������
	 */
	public void createValue(){
		//���ùܼ�
		self_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(), R.drawable.welcome);
		self_hm.put(PackageKeys.APP_NAME.getString(), R.string.app_name);
		self_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(), R.drawable.menu_logo);
		self_hm.put(PackageKeys.UPDATE_NOTE.getString(), "jike");
		self_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2C);
		self_hm.put(PackageKeys.USERKEY.getString(),RELEASE?"ffdd14d2e6c26b70749c8b2c08067c69":"5b13658a9fc945e34893f806027d467a");
		self_hm.put(PackageKeys.ORGIN.getString(),0);//�ò�������20141104���������ֶ������������Դ
		/*Android���ùܼ�0 Android��������1 IOS���ùܼ�2 IOS��������3  �κ� �ܼ�4  �κ� ����5*/
		
		//��������
//		self_b_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(), R.drawable.welcome_b);
		self_b_hm.put(PackageKeys.APP_NAME.getString(), R.string.app_name_b);
		self_b_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(), R.drawable.menu_logo_b);
		self_b_hm.put(PackageKeys.UPDATE_NOTE.getString(), "jike_b");
		self_b_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2B);
		self_b_hm.put(PackageKeys.USERKEY.getString(),RELEASE?"ffdd14d2e6c26b70749c8b2c08067c69":"5b13658a9fc945e34893f806027d467a");
		self_b_hm.put(PackageKeys.ORGIN.getString(),1);
		
		//�κ�����
//		menghang_hm.put(PackageKeys.WELCOME_DRAWABLE.getString(), R.drawable.welcome_menghang);
		menghang_hm.put(PackageKeys.APP_NAME.getString(), R.string.app_name_menghang);
		menghang_hm.put(PackageKeys.MENU_LOGO_DRAWABLE.getString(), R.drawable.menu_logo_menghang);
		menghang_hm.put(PackageKeys.UPDATE_NOTE.getString(), "menghangshanglv");
		menghang_hm.put(PackageKeys.PLATFORM.getString(), Platform.B2C);
		menghang_hm.put(PackageKeys.USERKEY.getString(),RELEASE?"fc5865a78e9cb8b3d63c5428d4d32a4c":"5b13658a9fc945e34893f806027d467a");
		menghang_hm.put(PackageKeys.ORGIN.getString(),4);
	}
}
