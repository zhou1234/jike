/**
 * Alipay.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.jike.shanglv.pos.alipay.android.mpos.demo.helper;

/**
 * 
 * @author jianmin.jiang
 * 
 * @version $Id: PosResult.java, v 0.1 2012-2-28 ����5:19:47 jianmin.jiang Exp $
 */
public enum PosError {

	ERR_DUPLICATED_OUT_TRADE_NO("ERR_DUPLICATED_OUT_TRADE_NO", "�ظ����ⲿ���׺�"),

	ERR_INVALID_SIGN("ERR_INVALID_SIGN", "ǩ������ȷ"),

	ERR_INVALID_PARTNER_STATUS("ERR_INVALID_PARTNER_STATUS", "�ⲿ�̻�״̬������"),

	ERR_COMMON("ERR_COMMON", "�޷���λ����ԭ��Ĵ���"),

	ERR_INVALID_PID("ERR_INVALID_PID", "���Ϸ�����Ȩ�룬���߸��̻�û��ǩԼ"),

	IND_ERR_INVALID_AID("IND_ERR_INVALID_AID", "�����߱�������տ�Ȩ�ޣ������Ƿ���ȷ��Ȩ��"),

	IND_ERR_NOT_EXIST_MID("IND_ERR_NOT_EXIST_MID", "��˾�Ų�����"),

	IND_ERR_NOT_EXIST_AID("IND_ERR_NOT_EXIST_AID", "��Ȩ�벻����"),

	IND_ERR_NOT_EXIST_SELLER("IND_ERR_NOT_EXIST_SELLER", "���Ҳ�����"),

	IND_ERR_NOT_EXIST_PARTNER("IND_ERR_NOT_EXIST_PARTNER", "�̻�������"),

	IND_ERR_INVALID_PARTNER("IND_ERR_INVALID_PARTNER", "�̻�û��ǩԼ�����տ�"),

	IND_ERR_INVALID_MPOS_ID("IND_ERR_INVALID_MPOS_ID", "�ͻ��˵���Ȩ�Ѿ�ʧЧ�������¼���"),

	IND_ERR_INVALID_MID("IND_ERR_INVALID_MID", "�����߱�������տ�Ȩ�ޣ������Ƿ���ȷ��Ȩ��"),

	IND_ERR_CLIENT_NEED_UPDATE("IND_ERR_CLIENT_NEED_UPDATE", "��ҵ�ͻ��˿ɸ���"),

	IND_ERR_INVALID_SIGN("IND_ERR_INVALID_SIGN", "ǩ��ʧ��");

	private String code;
	private String desc;

	private PosError(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PosError getEnumByCode(String code) {
		PosError[] list = values();
		for (PosError item : list) {
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
