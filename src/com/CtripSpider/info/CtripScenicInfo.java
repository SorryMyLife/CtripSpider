package com.CtripSpider.info;

import java.util.List;

public class CtripScenicInfo {
	public String x, y, address, scenicName, date , text, imageLink, phone , SightId , districtId , cityName;
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSightId() {
		return SightId;
	}

	public void setSightId(String sightId) {
		SightId = sightId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public List<CtripScenicMoney> money;
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<CtripScenicMoney> getMoney() {
		return money;
	}

	public void setMoney(List<CtripScenicMoney> money) {
		this.money = money;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getScenicName() {
		return scenicName;
	}

	public void setScenicName(String scenicName) {
		this.scenicName = scenicName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
