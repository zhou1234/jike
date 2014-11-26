package com.jike.shanglv.Models;

import java.io.Serializable;
import java.text.ParseException;


import org.json.JSONObject;

import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.Common.IdType;

/**
 * @author Administrator
 *
 */
public class Passenger  implements Serializable {
	private static final long serialVersionUID = 1L;
	String passengerType,passengerName,identificationType,identificationNum;
//	String mobile; //"�ֻ����룬��ѡ",
	String isunum; //"�������������Ĭ���봫0",
	String addto="1"; //�Ƿ񱣴浽ϵͳ�еĳ��ó˻��ˣ����0�����棬1����"
	
	String CName; //������
	String CusCardNo; //���֤
	String EName; //Ӣ����
	String Huzhao; //����
	String Mobie; //�绰                                                               //"�ֻ����룬��ѡ",
	String Qita; //����֤����
	String Gangao; //�۰�ͨ��֤
	String Ggdate; //�۰�ͨ��֤��Ч��
	String Hzdate; //������Ч��;
	String Tbdate; //̨����Ч��
	String CusBirth; //����
	String Taibao; //̨��֤
	String Qianfadi; //ǩ����
	String Country; //����
	String Sex; //�Ա�
	
	String TicketNumber;//�ύ�������Ʊ��
	
	String gender;//�Ա�
	String nation;//����
	String IDdeadline;//֤����Ч��
	String birthDay;//����
	String issueAt;//ǩ����
	
	public String getTicketNumber() {
		return TicketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		TicketNumber = ticketNumber;
	}
	public Passenger(){}
	public Passenger(String jsonString,String systype){//"systype":"0���� 1���� 2��Ʊ"   ����֤������Ĭ��ȡ���գ����ڡ���Ĭ��ȡ���֤
		try {
			JSONObject json=new JSONObject(jsonString);
			if(jsonString.contains("passengerType"))this.passengerType=json.getString("passengerType");
			if(jsonString.contains("passengerName"))this.passengerName=json.getString("passengerName");
			else this.passengerName=json.getString("CName");
			if(jsonString.contains("identificationType"))this.identificationType=json.getString("identificationType");
			if(jsonString.contains("identificationNum"))this.identificationNum=json.getString("identificationNum");
//			if(jsonString.contains("mobile"))this.mobile=json.getString("mobile");
			if(jsonString.contains("addto"))this.addto=json.getString("addto");
			
			this.CName=json.getString("CName");
			this.CusCardNo=json.getString("CusCardNo");
			this.EName=json.getString("EName");
			this.Huzhao=json.getString("Huzhao");
			this.Mobie=json.getString("Mobie");
			this.Qita=json.getString("Qita");
			this.Gangao=json.getString("Gangao");
			this.Ggdate=json.getString("Ggdate");
			this.Hzdate=json.getString("Hzdate");
			this.Tbdate=json.getString("Tbdate");
			this.CusBirth=json.getString("CusBirth");
			this.Taibao=json.getString("Taibao");
			this.Qianfadi=json.getString("Qianfadi");
			this.Country=json.getString("Country");
			this.Sex=json.getString("Sex");
			
			if (this.passengerType==null) {
				this.passengerType="����";
			}
			if (this.passengerName==null||this.passengerName.equals("")) {
				this.passengerName=this.CName;
				if (this.CName.equals("")||this.CName.equals(null)||this.CName.equals("null")) {
					this.passengerName=this.EName;
				}
			}
			if (systype.equals("1")) {//���ʻ�Ʊ ȡӢ����
				this.passengerName=this.EName;
				if(jsonString.contains("Sex"))this.gender=json.getString("Sex");
				if(jsonString.contains("Country"))this.nation=json.getString("Country");
				if(jsonString.contains("CusBirth"))this.birthDay=json.getString("CusBirth");
				if(jsonString.contains("Qianfadi"))	this.issueAt=json.getString("Qianfadi");
			}
			int idtype=0;//֤������
			if (this.identificationType==null||this.identificationNum==null) {
				if (!systype.equals("1")&&!this.CusCardNo.equals("")) {//���ڻ�Ʊ����ƱĬ��ȡ���֤��
					this.identificationType=IdType.IdType.get(0);
					this.identificationNum=this.CusCardNo;
					idtype=0;
				}
				else if (!this.Huzhao.equals("")){//����
					this.identificationType=IdType.IdType.get(1);
					this.identificationNum=this.Huzhao;
					idtype=1;
				}
				else if (!this.Gangao.equals("")){//�۰�ͨ��֤
					this.identificationType=IdType.IdType.get(4);
					this.identificationNum=this.Gangao;
					idtype=4;
				}
				else if (!this.Taibao.equals("")){//̨��֤
					this.identificationType=IdType.IdType.get(5);
					this.identificationNum=this.Taibao;
					idtype=5;
				}
				else if (!this.Qita.equals("")){//����
					this.identificationType=IdType.IdType.get(9);
					this.identificationNum=this.Qita;
					idtype=9;
				}
			}
			if ((systype.equals("1"))) {//���ʻ�Ʊ   ����֤������ȡ��Ч��     IDdeadline;//֤����Ч��
				String d="";
				switch (idtype) {
				case 1:
					if(jsonString.contains("Hzdate"))d=json.getString("Hzdate");
					try {
						this.IDdeadline=DateUtil.getDate(d);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;
				case 5:
					if(jsonString.contains("Tbdate"))d=json.getString("Tbdate");
					try {
						this.IDdeadline=DateUtil.getDate(d);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;
				case 4:
					if(jsonString.contains("Ggdate"))d=json.getString("Ggdate");
					try {
						this.IDdeadline=DateUtil.getDate(d);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}

	public String getIsunum() {
		return isunum;
	}

	public void setIsunum(String isunum) {
		this.isunum = isunum;
	}

	public String getAddto() {
		return addto;
	}

	public void setAddto(String addto) {
		this.addto = addto;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public String getIdentificationNum() {
		return identificationNum;
	}

	public void setIdentificationNum(String identificationNum) {
		this.identificationNum = identificationNum;
	}
	
	public String getCName() {
		return CName;
	}
	public void setCName(String cName) {
		CName = cName;
	}
	public String getCusCardNo() {
		return CusCardNo;
	}
	public void setCusCardNo(String cusCardNo) {
		CusCardNo = cusCardNo;
	}
	public String getEName() {
		return EName;
	}
	public void setEName(String eName) {
		EName = eName;
	}
	public String getHuzhao() {
		return Huzhao;
	}
	public void setHuzhao(String huzhao) {
		Huzhao = huzhao;
	}
	public String getMobie() {
		return Mobie;
	}
	public void setMobie(String mobie) {
		Mobie = mobie;
	}
	public String getQita() {
		return Qita;
	}
	public void setQita(String qita) {
		Qita = qita;
	}
	public String getGangao() {
		return Gangao;
	}
	public void setGangao(String gangao) {
		Gangao = gangao;
	}
	public String getGgdate() {
		return Ggdate;
	}
	public void setGgdate(String ggdate) {
		Ggdate = ggdate;
	}
	public String getHzdate() {
		return Hzdate;
	}
	public void setHzdate(String hzdate) {
		Hzdate = hzdate;
	}
	public String getTbdate() {
		return Tbdate;
	}
	public void setTbdate(String tbdate) {
		Tbdate = tbdate;
	}
	public String getCusBirth() {
		return CusBirth;
	}
	public void setCusBirth(String cusBirth) {
		CusBirth = cusBirth;
	}
	public String getTaibao() {
		return Taibao;
	}
	public void setTaibao(String taibao) {
		Taibao = taibao;
	}
	public String getQianfadi() {
		return Qianfadi;
	}
	public void setQianfadi(String qianfadi) {
		Qianfadi = qianfadi;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getIDdeadline() {
		return IDdeadline;
	}
	public void setIDdeadline(String iDdeadline) {
		IDdeadline = iDdeadline;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getIssueAt() {
		return issueAt;
	}
	public void setIssueAt(String issueAt) {
		this.issueAt = issueAt;
	}
}
