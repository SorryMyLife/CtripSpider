package com.CtripSpider.info;

public class CtripScenicMoney {
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	String person ,ticket , money;

	public CtripScenicMoney(String person, String ticket, String money) {
		super();
		this.person = person;
		this.ticket = ticket;
		this.money = money;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
}
