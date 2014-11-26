package com.jike.shanglv.Models;


import org.json.JSONObject;

public class OrderList_AirlineTicket {
	private String OrderID,//B1409121507597434",
		    Amount,//671.00",
		    StartOffDate,//2014-9-12 15:07:59",
		    OrderStatus,//�¶���",
		    StartCity,
		    OrderTime,
		    endCity;
	/**
	 * @param object
	 * @param inland_inter_train 1:���ڻ�Ʊ   2:���ʻ�Ʊ����    3:��Ʊ
	 */
	public OrderList_AirlineTicket(JSONObject object,int inland_inter_train){
		if (inland_inter_train==1) {//���ڻ�Ʊ����
			try {
				this.OrderID=object.getString("OrderID");
				this.Amount=object.getString("Amount");
				this.OrderStatus=object.getString("OrderStatus");
				this.StartCity=object.getJSONArray("FlightInfo").getJSONObject(0).getString("sCityName");
				this.endCity=object.getJSONArray("FlightInfo").getJSONObject(0).getString("eCityName");
				this.StartOffDate=object.getJSONArray("FlightInfo").getJSONObject(0).getString("beginDate");
				this.OrderTime=object.getString("OrderTime");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(inland_inter_train==2) {//��������
			try {
				this.OrderID=object.getString("Orderid");
				this.Amount=object.getString("Yusuan");
				this.OrderStatus=object.getString("Status");
				this.StartCity=object.getString("Startname");
				this.endCity=object.getString("Endname");
				this.StartOffDate=object.getString("Startdate");
				this.OrderTime=object.getString("OrderTime");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (inland_inter_train==3) {//��Ʊ
			try {
				this.OrderID=object.getString("OrderID");
				this.Amount=object.getString("Amount");
				this.OrderStatus=object.getString("Status");
				this.StartCity=object.getString("SCity");
				this.endCity=object.getString("ECity");
				this.StartOffDate=object.getString("SDate");
				this.OrderTime=object.getString("OrderTime");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (inland_inter_train==4) {//���ʻ�Ʊ
			try {
//{"OrderState":"�¶���","orderPrice":"3886.00","Info":[{"startTime":"2014-09-18 11:00","startPortName":"����","endPortName":"����","OrderDate":"2014-9-15 18:19:11","OrderId":"B1409151818366050","endPort":"CAN","startPort":"BHY"},{"startTime":"2014-09-18 16:20","startPortName":"����","endPortName":"�׶�","endPort":"ICN","startPort":"CAN"},{"startTime":"2014-09-30 11:15","startPortName":"�׶�","endPortName":"����","endPort":"CAN","startPort":"ICN"},{"startTime":"2014-09-30 16:45","startPortName":"����","endPortName":"����","endPort":"BHY","startPort":"CAN"}]}
				this.OrderID=object.getString("OrderId");
				this.Amount=object.getString("orderPrice");
				this.OrderStatus=object.getString("OrderState");
				this.StartCity=object.getString("StartCity");
				this.endCity=object.getString("EndCity");
				this.StartOffDate=object.getJSONArray("Info")
						.getJSONObject(0).getString("startTime");
				this.OrderTime=object.getString("OrderDate");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public String getStartOffDate() {
		return StartOffDate;
	}

	public void setStartOffDate(String startOffDate) {
		StartOffDate = startOffDate;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	public String getStartCity() {
		return StartCity;
	}

	public void setStartCity(String startCity) {
		StartCity = startCity;
	}

	public String getEndCity() {
		return endCity;
	}

	public void setEndCity(String endCity) {
		this.endCity = endCity;
	}

	public String getOrderTime() {
		return OrderTime;
	}

	public void setOrderTime(String orderTime) {
		OrderTime = orderTime;
	}
	
	/*����
	 * "OrderID": "B1409121507597434",
    "Amount": "671.00",
    "OrderTime": "2014-9-12 15:07:59",
    "OrderStatus": "�¶���",
    "FlightInfo": [
        {
            "sCityName": "�����׶�",
            "sT": "T3",
            "eCityName": "�Ϻ�����",
            "eT": "T2",
            "flightNo": "HO1252",
            "beginDate": "2014-09-13T06:50:00",
            "beginTime": "06:50",
            "arrvTime": "09:05"
        }
    ]	}
    */
	 /*��������
	  *"Orderid": "BX1409121558527602",
      "Startname": "����",
      "Startcode": "BJS",
      "Backdate": "",
      "Iflighttype": "����",
      "Endname": "ŦԼ",
      "Backtime": "",
      "Endcode": "JFK",
      "Startdate": "2014-9-13 0:00:00",
      "Starttime": "",
      "Yusuan": "25.00",
      "PsgName": "<root><row customName=\"tang/xiao\"/></root>",
      "Status": "������"  
      */
}
