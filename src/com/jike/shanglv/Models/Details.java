package com.jike.shanglv.Models;

import java.io.Serializable;

public class Details implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderno;
	private String cashtradeapplication;
	private String inamount;
	private String currentsettlementbalance;
	private String createtime;
	private String outorderno;
	private String remark;

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getCashtradeapplication() {
		return cashtradeapplication;
	}

	public void setCashtradeapplication(String cashtradeapplication) {
		this.cashtradeapplication = cashtradeapplication;
	}

	public String getInamount() {
		return inamount;
	}

	public void setInamount(String inamount) {
		this.inamount = inamount;
	}

	public String getCurrentsettlementbalance() {
		return currentsettlementbalance;
	}

	public void setCurrentsettlementbalance(String currentsettlementbalance) {
		this.currentsettlementbalance = currentsettlementbalance;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getOutorderno() {
		return outorderno;
	}

	public void setOutorderno(String outorderno) {
		this.outorderno = outorderno;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
