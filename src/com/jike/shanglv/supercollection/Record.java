package com.jike.shanglv.supercollection;

public class Record {
	String no,// 256565566,//订单号
			bn,// 建设银行,//银行名称
			bc,// 6222021001256695548,//信用卡卡号
			a,// 1000.00,//收款金额
			ra,// 994.00, //实际到账金额
			pf,// 6.00,//手续费
			ct,// 2014-09-26 15:23:22,//订单创建日期
			pt,// 2014-09-26 15:23:22, //支付日期
			st,// ,预计成功日期
			rst,//实际到账日期
			s,// 处理中,//订单状态
			sr,// 0,//到账日期T+N
			qt,////取消时间
			ono;// 5645646545645646  外部订单号好

	public String getRst() {
		return rst;
	}

	public void setRst(String rst) {
		this.rst = rst;
	}

	public String getQt() {
		return qt;
	}

	public void setQt(String qt) {
		this.qt = qt;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public String getBc() {
		return bc;
	}

	public void setBc(String bc) {
		this.bc = bc;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getRa() {
		return ra;
	}

	public void setRa(String ra) {
		this.ra = ra;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getSr() {
		return sr;
	}

	public void setSr(String sr) {
		this.sr = sr;
	}

	public String getOno() {
		return ono;
	}

	public void setOno(String ono) {
		this.ono = ono;
	}
}
