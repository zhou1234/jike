package com.jike.shanglv.supercollection;

/**收款状态值
 * */
public enum StateEnum {
    neworder("新订单"),//"Blue"
    yishoukuan("已收款"),//"Blue"
    ruzhangzhong("入账中"),//"Green"
    yiwancheng("已完成"),// "Red"
    yiquxiao("已取消");// "Red"

	private String key;

	private StateEnum(String s) {
		key = s;
	}

	public String getString() {
		return key;
	}
};
