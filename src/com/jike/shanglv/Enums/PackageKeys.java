package com.jike.shanglv.Enums;

public enum PackageKeys {
	WELCOME_DRAWABLE("WELCOME_DRAWABLE"),//��ӭҳ��
	MENU_LOGO_DRAWABLE("MENU_LOGO_DRAWABLE"),//��ҳ����title
	APP_NAME("APP_NAME"),//��������
	UPDATE_NOTE("UPDATE_NOTE"),//���������ڵ�
	RECOMMAND_CODE("RECOMMAND_CODE"),//Ĭ���Ƽ���
	PLATFORM("PLATFORM"),//ƽ̨��B2B��B2C
	USERKEY("USERKEY"),//userkey
	ORGIN("ORGIN"),//orgin
	
	phoneNum("phoneNum")//
	
	;

	private String key;
	private PackageKeys(String s) {
		key = s;
	}

	public String getString() {
		return key;
	}
}
