package com.jike.shanglv.Enums;

/**��¼SharedPreferences�еļ�ֵ
 * */
public enum SPkeys {
	SPNAME("mySP_shanglvguanjia"),//SharedPreferences������
	
	userid("userid"),
	username("username"),
	userphone("userphone"),
	useremail("useremail"),
	amount("amount"),//�����˺Ž��
	siteid("siteid"),//ϵͳid
	showDealer("showDealer"),
	showCustomer("showCustomer"),
	utype("utype"),
	opensupperpay("opensupperpay"),
	
	UserInfoJson("UserInfoJson"),
	lastUsername("lastUsername"),
	lastPassword("lastPassword"),
	autoLogin("autoLogin"),
	loginState("loginState"),
	gnjpContactPhone("gnjpContactPhone"),	//���ڻ�Ʊ�µ����ϴε���ϵ���ֻ�����
	gjjpContactPhone("gjjpContactPhone"),	//���ʻ�Ʊ���󵥣��ϴε���ϵ���ֻ�����
	trainContactPhone("trainContactPhone"),	//��Ʊ���ϴε���ϵ���ֻ�����
	isFirstIn("isFirstIn"),   //�Ƿ��һ�ν���  ����ҳ����ӭҳ�л�
	
	chongZhiJinE("chongZhiJinE")
	
	;

	private String key;

	private SPkeys(String s) {
		key = s;
	}

	public String getString() {
		return key;
	}
};
