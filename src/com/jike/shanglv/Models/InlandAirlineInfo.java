package com.jike.shanglv.Models;

import org.json.JSONArray;

import org.json.JSONObject;

public class InlandAirlineInfo {
	private String ByPass, PlaneModel, MinTicketCount, PlaneType, CarrinerName,
			AirLineCode, Oil, JoinDate, RunTime, StartPortName, OffTime,
			MinCabin, FlightNo, StartT, StartPort, Distance, Tax, ETicket,
			MinFare, EndT, EndPortName, JoinPort, Meat, StaFare, ArriveTime,
			EndPort, MinDiscount, IndexID;
	private JSONArray cablist;
	private String YouHui,CabinName;

	public String getYouHui() {
		return YouHui;
	}

	public String getCabinName() {
		return CabinName;
	}

	public JSONObject getJson() {
		return json;
	}

	private JSONObject json;
	
	public InlandAirlineInfo(){}

	public InlandAirlineInfo(JSONObject json) {
		this.json = json;
		try {
			ByPass = json.getJSONObject("Base").getString("ByPass");
			PlaneModel = json.getJSONObject("Base").getString("PlaneModel");
			MinTicketCount = json.getJSONObject("Base").getString("MinTicketCount");
			PlaneType = json.getJSONObject("Base").getString("PlaneType");
			CarrinerName = json.getJSONObject("Base").getString("CarrinerName");
			AirLineCode = json.getJSONObject("Base").getString("AirLineCode");
			Oil = json.getJSONObject("Base").getString("Oil");
			JoinDate = json.getJSONObject("Base").getString("JoinDate");
			RunTime = json.getJSONObject("Base").getString("RunTime");
			StartPortName = json.getJSONObject("Base").getString("StartPortName");
			OffTime = json.getJSONObject("Base").getString("OffTime");
			MinCabin = json.getJSONObject("Base").getString("MinCabin");
			FlightNo = json.getJSONObject("Base").getString("FlightNo");
			StartT = json.getJSONObject("Base").getString("StartT");
			StartPort = json.getJSONObject("Base").getString("StartPort");
			Distance = json.getJSONObject("Base").getString("Distance");
			Tax = json.getJSONObject("Base").getString("Tax");
			ETicket = json.getJSONObject("Base").getString("ETicket");
			EndT = json.getJSONObject("Base").getString("EndT");
			EndPortName = json.getJSONObject("Base").getString("EndPortName");
			JoinPort = json.getJSONObject("Base").getString("JoinPort");
			Meat = json.getJSONObject("Base").getString("Meat");
			StaFare = json.getJSONObject("Base").getString("StaFare");
			ArriveTime = json.getJSONObject("Base").getString("ArriveTime");
			EndPort = json.getJSONObject("Base").getString("EndPort");
			MinDiscount = json.getJSONObject("Base").getString("MinDiscount");
			IndexID = json.getJSONObject("Base").getString("IndexID");
			cablist=json.getJSONArray("CabList");
			
//			MinFare = json.getJSONObject("Base").getString("MinFare");//��ͼ۸��Ʊ���
			//20141017   ��ͱ��۸�Ϊȡ�ۼ۵���ͼۣ�������Ʊ��
			MinFare = json.getJSONArray("CabList").getJSONObject(0).getString("Sale");
			YouHui=json.getJSONArray("CabList").getJSONObject(0).getString("YouHui");
			CabinName=json.getJSONArray("CabList").getJSONObject(0).getString("CabinName");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONArray getCablist() {
		return cablist;
	}

	public String getByPass() {
		return ByPass;
	}

	public String getPlaneModel() {
		return PlaneModel;
	}

	public String getMinTicketCount() {
		return MinTicketCount;
	}

	public String getPlaneType() {
		return PlaneType;
	}

	public String getCarrinerName() {
		return CarrinerName;
	}

	public String getAirLineCode() {
		return AirLineCode;
	}

	public String getOil() {
		return Oil;
	}

	public String getJoinDate() {
		return JoinDate;
	}

	public String getRunTime() {
		return RunTime;
	}

	public String getStartPortName() {
		return StartPortName;
	}

	public String getOffTime() {
		return OffTime;
	}

	public String getMinCabin() {
		return MinCabin;
	}

	public String getFlightNo() {
		return FlightNo;
	}

	public String getStartT() {
		return StartT;
	}

	public String getStartPort() {
		return StartPort;
	}

	public String getDistance() {
		return Distance;
	}

	public String getTax() {
		return Tax;
	}

	public String getETicket() {
		return ETicket;
	}

	public String getMinFare() {
		return MinFare;
	}

	public String getEndT() {
		return EndT;
	}

	public String getEndPortName() {
		return EndPortName;
	}

	public String getJoinPort() {
		return JoinPort;
	}

	public String getMeat() {
		return Meat;
	}

	public String getStaFare() {
		return StaFare;
	}

	public String getArriveTime() {
		return ArriveTime;
	}

	public String getEndPort() {
		return EndPort;
	}

	public String getMinDiscount() {
		return MinDiscount;
	}

	public String getIndexID() {
		return IndexID;
	}
}

/*ʾ��
 {
    "c": "0000",
    "num": 50,
    "ft": "1",
    "db": "1",
    "al": [
        {
            "value": "CZ",
            "name": "�Ϸ�����"
        },
        {
            "value": "HO",
            "name": "���麽��"
        },
        {
            "value": "MU",
            "name": "��������"
        },
        {
            "value": "FM",
            "name": "�Ϻ�����"
        },
        {
            "value": "CA",
            "name": "�й�����"
        },
        {
            "value": "HU",
            "name": "���Ϻ���"
        },
        {
            "value": "KN",
            "name": "���Ϻ���"
        },
        {
            "value": "MF",
            "name": "���ź���"
        }
    ],
    "d": [
        {
            "Base": {
                "AirLineCode": "CZ",
                "ArriveTime": "2014-9-11 08:45:00",
                "ByPass": "0",
                "CarrinerName": "�Ϸ�����",
                "Distance": "1178",
                "ETicket": "1",
                "EndPort": "SHA",
                "EndPortName": "�Ϻ�����",
                "EndT": "T2",
                "FlightNo": "CZ6412",
                "IndexID": "412645",
                "JoinDate": "",
                "JoinPort": "",
                "Meat": "1",
                "MinCabin": "W",
                "MinDiscount": "100",
                "MinFare": "1130",
                "MinTicketCount": "10",
                "OffTime": "2014-9-11 06:35:00",
                "Oil": "120",
                "PlaneModel": "��",
                "PlaneType": "321",
                "RunTime": "2Сʱ10����",
                "StaFare": "1130",
                "StartPort": "PEK",
                "StartPortName": "�����׶�",
                "StartT": "T2",
                "Tax": "50"
            },
            "CabList": [
                {
                    "AirLineCode": null,
                    "Cabin": "W",
                    "CabinName": "�߶˾��ò�",
                    "Discount": "100",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "1130",
                    "FareEx": "1130",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": true,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "23300521",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.30",
                    "RateInfo": "3.40,0.9,0.0,0,0",
                    "Sale": "1092",
                    "SaleEx": "1092",
                    "Supplier": "jinri",
                    "TicketCount": "10",
                    "UserRate": "3.40",
                    "VTWorteTime": "",
                    "VerifyInfo": null,
                    "WorkTime": "08:30:00-21:30:00",
                    "YouHui": "38",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "Y",
                    "CabinName": "ȫ�۲�",
                    "Discount": "100",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "1130",
                    "FareEx": "1130",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "23300521",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.30",
                    "RateInfo": "3.40,0.9,0.0,0,0",
                    "Sale": "1092",
                    "SaleEx": "1092",
                    "Supplier": "jinri",
                    "TicketCount": "10",
                    "UserRate": "3.40",
                    "VTWorteTime": "",
                    "VerifyInfo": null,
                    "WorkTime": "08:30:00-21:30:00",
                    "YouHui": "38",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "F",
                    "CabinName": "ͷ�Ȳ�",
                    "Discount": "280",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "3160",
                    "FareEx": "3160",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "True",
                    "PolicyID": "ffffffff-fff0-3040-1834-201405091729",
                    "PolicyRemark": "100%ͷ�ȳ�Ʊ��Ʊ��Ϳ�����Ʊ�治������۲��˻��������뿪Ʊ,��Ҫ����ͷ�������λP��Ʊ�����ܵ���ͷɻ���һ�д������䣬��ͷ�������λ�޷���Ʊ�������½⣬���𶩵���Ʊ��Ʊ�����Ϊ�㲻���֣��κ��������Ʊ����Ӧ�̳�Ʊ��λ�۸��Ӧ���չ�˾�͹����5%�����������ϣ�����Ӧ��ʵ�ʳ�Ʊ��λ�͹���ڣ���ӭ�ɹ�����Ʊ10�������ң���ӭ�ɹ�������,��Ʊ�ο�ʱ�䣺2355  RMK TJ AUTH PEK513",
                    "PriceProvider": "1",
                    "Rate": "42.10",
                    "RateInfo": "33.70,8.4,0.0,0,0",
                    "Sale": "2096",
                    "SaleEx": "2096",
                    "Supplier": "Piaomeng",
                    "TicketCount": "10",
                    "UserRate": "33.70",
                    "VTWorteTime": "",
                    "VerifyInfo": null,
                    "WorkTime": "08:00-23:55",
                    "YouHui": "1064",
                    "generalFare": null
                }
            ],
            "Error": null
        },
        {
            "Base": {
                "AirLineCode": "HO",
                "ArriveTime": "2014-9-11 09:05:00",
                "ByPass": "0",
                "CarrinerName": "���麽��",
                "Distance": "1178",
                "ETicket": "1",
                "EndPort": "SHA",
                "EndPortName": "�Ϻ�����",
                "EndT": "T2",
                "FlightNo": "HO1252",
                "IndexID": "139797",
                "JoinDate": "",
                "JoinPort": "",
                "Meat": "1",
                "MinCabin": "W",
                "MinDiscount": "50",
                "MinFare": "570",
                "MinTicketCount": "10",
                "OffTime": "2014-9-11 06:50:00",
                "Oil": "120",
                "PlaneModel": "��",
                "PlaneType": "320",
                "RunTime": "2Сʱ15����",
                "StaFare": "1130",
                "StartPort": "PEK",
                "StartPortName": "�����׶�",
                "StartT": "T3",
                "Tax": "50"
            },
            "CabList": [
                {
                    "AirLineCode": null,
                    "Cabin": "W",
                    "CabinName": "���ò�",
                    "Discount": "50",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "570",
                    "FareEx": "570",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": true,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "595236703",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.50",
                    "RateInfo": "3.60,0.9,0.0,0,0",
                    "Sale": "550",
                    "SaleEx": "550",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.60",
                    "VTWorteTime": "00:00:00-23:59:00",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "20",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "V",
                    "CabinName": "���ò�",
                    "Discount": "60",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "680",
                    "FareEx": "680",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "595236703",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.50",
                    "RateInfo": "3.60,0.9,0.0,0,0",
                    "Sale": "656",
                    "SaleEx": "656",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.60",
                    "VTWorteTime": "00:00:00-23:59:00",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "24",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "E",
                    "CabinName": "���ò�",
                    "Discount": "70",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "790",
                    "FareEx": "790",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "595236703",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.50",
                    "RateInfo": "3.60,0.9,0.0,0,0",
                    "Sale": "762",
                    "SaleEx": "762",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.60",
                    "VTWorteTime": "00:00:00-23:59:00",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "28",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "M",
                    "CabinName": "���ò�",
                    "Discount": "80",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "900",
                    "FareEx": "900",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "595236738",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.50",
                    "RateInfo": "3.60,0.9,0.0,0,0",
                    "Sale": "868",
                    "SaleEx": "868",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.60",
                    "VTWorteTime": "00:00:00-23:59:00",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "32",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "L",
                    "CabinName": "���ò�",
                    "Discount": "85",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "960",
                    "FareEx": "960",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "595236738",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.50",
                    "RateInfo": "3.60,0.9,0.0,0,0",
                    "Sale": "926",
                    "SaleEx": "926",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.60",
                    "VTWorteTime": "00:00:00-23:59:00",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "34",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "B",
                    "CabinName": "���ò�",
                    "Discount": "90",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "1020",
                    "FareEx": "1020",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "595236738",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.50",
                    "RateInfo": "3.60,0.9,0.0,0,0",
                    "Sale": "984",
                    "SaleEx": "984",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.60",
                    "VTWorteTime": "00:00:00-23:59:00",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "36",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "Y",
                    "CabinName": "ȫ�۲�",
                    "Discount": "100",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "1130",
                    "FareEx": "1130",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "595236738",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.50",
                    "RateInfo": "3.60,0.9,0.0,0,0",
                    "Sale": "1090",
                    "SaleEx": "1090",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.60",
                    "VTWorteTime": "00:00:00-23:59:00",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "40",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "F",
                    "CabinName": "ͷ�Ȳ�",
                    "Discount": "280",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "3160",
                    "FareEx": "3160",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "True",
                    "PolicyID": "ffffffff-fff0-2198-6024-201404240213",
                    "PolicyRemark": "�����룬 ����Ʊ ���ܴ���������ж����䣬��Ʊ���ǰ�ٷ�֮5����ɺ�ٷ�֮10��ͬ�Ȳ�λ������ѣ����򵥣�Ʊ���в��,���ֿ�����Ҫ����λ,��Ʊ�ο�ʱ�䣺2355  RMK TJ AUTH ctu215",
                    "PriceProvider": "1",
                    "Rate": "40.60",
                    "RateInfo": "32.50,8.1,0.0,0,0",
                    "Sale": "2133",
                    "SaleEx": "2133",
                    "Supplier": "Piaomeng",
                    "TicketCount": "8",
                    "UserRate": "32.50",
                    "VTWorteTime": "",
                    "VerifyInfo": null,
                    "WorkTime": "08:00-23:55",
                    "YouHui": "1027",
                    "generalFare": null
                }
            ],
            "Error": null
        },
        {
            "Base": {
                "AirLineCode": "MU",
                "ArriveTime": "2014-9-11 09:10:00",
                "ByPass": "0",
                "CarrinerName": "��������",
                "Distance": "1178",
                "ETicket": "1",
                "EndPort": "SHA",
                "EndPortName": "�Ϻ�����",
                "EndT": "T2",
                "FlightNo": "MU5138",
                "IndexID": "989720",
                "JoinDate": "",
                "JoinPort": "",
                "Meat": "1",
                "MinCabin": "Z",
                "MinDiscount": "42",
                "MinFare": "480",
                "MinTicketCount": "10",
                "OffTime": "2014-9-11 07:00:00",
                "Oil": "120",
                "PlaneModel": "��",
                "PlaneType": "333",
                "RunTime": "2Сʱ10����",
                "StaFare": "1130",
                "StartPort": "PEK",
                "StartPortName": "�����׶�",
                "StartT": "T2",
                "Tax": "50"
            },
            "CabList": [
                {
                    "AirLineCode": null,
                    "Cabin": "Z",
                    "CabinName": "���ò�",
                    "Discount": "42",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "480",
                    "FareEx": "480",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": true,
                    "IsSpe": "1",
                    "IsSpePolicy": "False",
                    "PolicyID": "612362754",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "4.00",
                    "RateInfo": "3.20,0.8,0.0,0,0",
                    "Sale": "465",
                    "SaleEx": "465",
                    "Supplier": "19e",
                    "TicketCount": "10",
                    "UserRate": "3.20",
                    "VTWorteTime": "08:30:00-22:30",
                    "VerifyInfo": null,
                    "WorkTime": "00:00-23:59",
                    "YouHui": "15",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "S",
                    "CabinName": "���ò�",
                    "Discount": "50",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "570",
                    "FareEx": "570",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "23568897",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "5.50",
                    "RateInfo": "4.40,1.1,0.0,0,0",
                    "Sale": "545",
                    "SaleEx": "545",
                    "Supplier": "jinri",
                    "TicketCount": "10",
                    "UserRate": "4.40",
                    "VTWorteTime": "",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "25",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "R",
                    "CabinName": "���ò�",
                    "Discount": "60",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "680",
                    "FareEx": "680",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe": "0",
                    "IsSpePolicy": "False",
                    "PolicyID": "23568897",
                    "PolicyRemark": "",
                    "PriceProvider": "1",
                    "Rate": "5.50",
                    "RateInfo": "4.40,1.1,0.0,0,0",
                    "Sale": "651",
                    "SaleEx": "651",
                    "Supplier": "jinri",
                    "TicketCount": "10",
                    "UserRate": "4.40",
                    "VTWorteTime": "",
                    "VerifyInfo": null,
                    "WorkTime": "00:00:00-23:59:00",
                    "YouHui": "29",
                    "generalFare": null
                },
                {
                    "AirLineCode": null,
                    "Cabin": "N",
                    "CabinName": "���ò�",
                    "Discount": "69",
                    "ExtFee": "0",
                    "ExtFee2": null,
                    "Fare": "780",
                    "FareEx": "780",
                    "Flag": "",
                    "FlagEn": "WU",
                    "FlightNo": null,
                    "GeneralCabin": null,
                    "IsGeneralMinFare": false,
                    "IsSpe"...
 */
