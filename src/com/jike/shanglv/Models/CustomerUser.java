package com.jike.shanglv.Models;

public class CustomerUser {
	String  UserName,// StoneMK, //�û���
			DealerLevel,// ��ʯ��,//�û�����
			LevelID,
			RealName,// ��ΰ,//��ʵ��Ϣ
			Phone,// 18502193643,//�绰����
			RegDate,// 2014-06-26 10:58,//ע������
			Status;// ����,//״̬
	
	String	CompanyName,
			ProvinceName,//ɽ��,
		    CityName,//����,
			StartDate,//2014-06-26,//�˻���Ч����ʼ
    	    EndDate;//2014-07-25//�˻���Ч�ڽ���
	
	public String getLevelID() {
		return LevelID;
	}

	public void setLevelID(String levelID) {
		LevelID = levelID;
	}

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getDealerLevel() {
		return DealerLevel;
	}

	public void setDealerLevel(String dealerLevel) {
		DealerLevel = dealerLevel;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getRegDate() {
		return RegDate;
	}

	public void setRegDate(String regDate) {
		RegDate = regDate;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
}
