package com.jike.shanglv.Models;

import org.json.JSONArray;

public class InterDemandStr {
	private String uid; //�û�ID
	private String sid;//��վID
	private String sCity;//":"��������";"
	private String sCode;//":"��������������";"
	private String sDate;//":"��������";"
	private String sTime;//":"����ʱ ��";"
	private String eCity;//":"�������";"
	private String eCode;//":"�������������";"
	private String eDate;//":"��������";"
	private String eTime;//":"����ʱ��";"
	private String fType;//":"����0����1";"
	private String yusuan;//":"Ԥ ����";"
	private String contactor;//":"��ϵ��";"
	private String mobile;//":"��ϵ���ֻ�";"
	private String email;//":"��ϵ������";"
	private String remark;//":"Ԥ����ע";"
	
	private JSONArray psgInfo;//":"�˻�����Ϣ"}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getsCity() {
		return sCity;
	}

	public void setsCity(String sCity) {
		this.sCity = sCity;
	}

	public String getsCode() {
		return sCode;
	}

	public void setsCode(String sCode) {
		this.sCode = sCode;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteCity() {
		return eCity;
	}

	public void seteCity(String eCity) {
		this.eCity = eCity;
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteDate() {
		return eDate;
	}

	public void seteDate(String eDate) {
		this.eDate = eDate;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getfType() {
		return fType;
	}

	public void setfType(String fType) {
		this.fType = fType;
	}

	public String getYusuan() {
		return yusuan;
	}

	public void setYusuan(String yusuan) {
		this.yusuan = yusuan;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public JSONArray getPsgInfo() {
		return psgInfo;
	}

	public void setPsgInfo(JSONArray psgInfo) {
		this.psgInfo = psgInfo;
	}
}
