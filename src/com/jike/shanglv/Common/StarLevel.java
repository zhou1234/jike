package com.jike.shanglv.Common;

import java.util.HashMap;

/**
 * �Ƶ��Ǽ����Ӧ�Ĵ���
 * @author Administrator
 *
 */
public class StarLevel {
	public static HashMap<String, String> Starlevel= new HashMap<String, String>();
	public static HashMap<String,String> StarlevelReverse= new HashMap<String,String>();
	static {
		Starlevel.put("2", "���Ǽ������¾���");
		Starlevel.put("3", "���Ǽ�/����");
		Starlevel.put("4", "���Ǽ�/�ߵ�");
		Starlevel.put("5", "���Ǽ�/����");
		
		StarlevelReverse.put("���Ǽ������¾���","2");
		StarlevelReverse.put("���Ǽ�/����","3");
		StarlevelReverse.put("���Ǽ�/�ߵ�","4");
		StarlevelReverse.put("���Ǽ�/����","5");
		StarlevelReverse.put("����","");
	}
}
