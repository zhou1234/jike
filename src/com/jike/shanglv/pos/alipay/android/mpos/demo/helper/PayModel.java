/**
 * Alipay.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.jike.shanglv.pos.alipay.android.mpos.demo.helper;

/**
 * 
 * @author jianmin.jiang
 * 
 * @version $Id: PayModel.java, v 0.1 2012-2-21 ����5:48:30 jianmin.jiang Exp $
 */
public enum PayModel {

	ALIPAY_ACCOUNT("alipay_account", "֧�����˻��տ�"),

	TAOBAO_ACCOUNT("taobao_account", "�Ա��˻��տ�"),

	BANK_CARD("bank_card", "���п��տ�");

	private String code;
	private String desc;

	private PayModel(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PayModel getEnumByCode(String code) {
		PayModel[] list = values();
		for (PayModel item : list) {
			if (item.getCode().equalsIgnoreCase(code)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * @return Returns the code.
	 */
	public final String getCode() {
		return code;
	}

	/**
	 * @return Returns the desc.
	 */
	public final String getDesc() {
		return desc;
	}

}
