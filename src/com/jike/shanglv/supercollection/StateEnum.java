package com.jike.shanglv.supercollection;

/**�տ�״ֵ̬
 * */
public enum StateEnum {
    neworder("�¶���"),//"Blue"
    yishoukuan("���տ�"),//"Blue"
    ruzhangzhong("������"),//"Green"
    yiwancheng("�����"),// "Red"
    yiquxiao("��ȡ��");// "Red"

	private String key;

	private StateEnum(String s) {
		key = s;
	}

	public String getString() {
		return key;
	}
};
