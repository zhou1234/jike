package com.jike.shanglv.Models;

public class TrainOrderPassenger {
	private String CardNo,// ֤���� �ش�
			CardType,// ֤�����ͣ��������֤�� �ش�
			Phone,// �ֻ��� �ش�
			PsgName,// �˳������� �ش�
			IncAmount,// ���� �ش�
			SeatType,// ϯ��(Ӳ��...) �ش�
			Saleprice,// ϯ�𵥼� �ش�
			TicketType; // ����Ʊ����ͯƱ����ֻ֧�ֳ���Ʊ�� �ش�

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public String getCardType() {
		return CardType;
	}

	public void setCardType(String cardType) {
		CardType = cardType;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getPsgName() {
		return PsgName;
	}

	public void setPsgName(String psgName) {
		PsgName = psgName;
	}

	public String getIncAmount() {
		return IncAmount;
	}

	public void setIncAmount(String incAmount) {
		IncAmount = incAmount;
	}

	public String getSeatType() {
		return SeatType;
	}

	public void setSeatType(String seatType) {
		SeatType = seatType;
	}

	public String getSaleprice() {
		return Saleprice;
	}

	public void setSaleprice(String saleprice) {
		Saleprice = saleprice;
	}

	public String getTicketType() {
		return TicketType;
	}

	public void setTicketType(String ticketType) {
		TicketType = ticketType;
	}
	
}
