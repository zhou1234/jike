package com.jike.shanglv.Common;

import java.util.HashMap;

/**
 * ֤���������Ӧ�Ĵ���
 * @author Administrator
 *
 */
public class IdType {
	public static HashMap<Integer, String> IdType= new HashMap<Integer, String>();
	public static HashMap<String,Integer> IdTypeReverse= new HashMap<String,Integer>();
	static {
		IdType.put(0, "���֤");
		IdType.put(1, "����");
		IdType.put(4, "�۰�ͨ��֤");
		IdType.put(5, "̨��֤");
		IdType.put(9, "����");
		
		IdTypeReverse.put("���֤",0);
		IdTypeReverse.put("����",1);
		IdTypeReverse.put("�۰�ͨ��֤",4);
		IdTypeReverse.put("̨��֤",5);
		IdTypeReverse.put("����",9);
	}
}
