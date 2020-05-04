package com.CtripSpider.info;

import java.util.List;

/**
 * <p>
 * ProductId:�Ƶ�ID
 * <p>
 * Title:�Ƶ��ǩ
 * <p>
 * MainName:�Ƶ�������ǩ
 * <p>
 * DepartureCityName:�Ƶ���������
 * <p>
 * DestCityId:�Ƶ���������ID
 * <p>
 * DestProvinceId:�Ƶ�����ʡID
 * <p>
 * DestProvinceName:�Ƶ�����ʡ����
 * <p>
 * DestinationCountryId:�Ƶ���������ID
 * <p>
 * DestinationCountryName:�Ƶ�������������
 * <p>
 * HotelName:�Ƶ�����
 * <p>
 * HotelAddress:�Ƶ��ַ
 * <p>
 * Latitude:�Ƶ꾭γ��[Latitude]
 * <p>
 * Longitude:�Ƶ꾭γ��[Longitude]
 * <p>
 * MinPrice:�Ƶ�۸�/��
 * <p>
 * MinPriceRemark:�Ƶ�۸�˵��
 * <p>
 * Introduction:�Ƶ���
 * <p>
 * HotelImages:�Ƶ�Ԥ��ͼƬ
 * <p>
 * HotelId:�Ƶ���תID
 * <p>
 * HotelPhone:�Ƶ���ϵ��ʽ
 */

public class CtripHotelInfo {
	/**
	 * <p>
	 * ProductId:�Ƶ�ID
	 * <p>
	 * Title:�Ƶ��ǩ
	 * <p>
	 * MainName:�Ƶ�������ǩ
	 * <p>
	 * DepartureCityName:�Ƶ���������
	 * <p>
	 * DestCityId:�Ƶ���������ID
	 * <p>
	 * DestProvinceId:�Ƶ�����ʡID
	 * <p>
	 * DestProvinceName:�Ƶ�����ʡ����
	 * <p>
	 * DestinationCountryId:�Ƶ���������ID
	 * <p>
	 * DestinationCountryName:�Ƶ�������������
	 * <p>
	 * HotelName:�Ƶ�����
	 * <p>
	 * HotelAddress:�Ƶ��ַ
	 * <p>
	 * Latitude:�Ƶ꾭γ��[Latitude]
	 * <p>
	 * Longitude:�Ƶ꾭γ��[Longitude]
	 * <p>
	 * MinPrice:�Ƶ�۸�/��
	 * <p>
	 * MinPriceRemark:�Ƶ�۸�˵��
	 * <p>
	 * Introduction:�Ƶ���
	 * <p>
	 * HotelImages:�Ƶ�Ԥ��ͼƬ
	 * <p>
	 * HotelId:�Ƶ���תID
	 * <p>
	 * HotelPhone:�Ƶ���ϵ��ʽ
	 */

	public CtripHotelInfo() {
	}

	public String ProductId, Title, MainName, DepartureCityName, DestCityId, DestProvinceId, DestProvinceName,
			DestinationCountryId, DestinationCountryName, HotelName, HotelAddress, Latitude, Longitude,
			MinPriceRemark, Introduction, HotelImages,HotelId,HotelPhone;
	
	public List<String> MinPrice;
	
	public String getHotelPhone() {
		return HotelPhone;
	}

	public void setHotelPhone(String hotelPhone) {
		HotelPhone = hotelPhone;
	}

	public String getHotelId() {
		return HotelId;
	}

	public void setHotelId(String hotelId) {
		HotelId = hotelId;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getMainName() {
		return MainName;
	}

	public void setMainName(String mainName) {
		MainName = mainName;
	}

	public String getDepartureCityName() {
		return DepartureCityName;
	}

	public void setDepartureCityName(String departureCityName) {
		DepartureCityName = departureCityName;
	}

	public String getDestCityId() {
		return DestCityId;
	}

	public void setDestCityId(String destCityId) {
		DestCityId = destCityId;
	}

	public String getDestProvinceId() {
		return DestProvinceId;
	}

	public void setDestProvinceId(String destProvinceId) {
		DestProvinceId = destProvinceId;
	}

	public String getDestProvinceName() {
		return DestProvinceName;
	}

	public void setDestProvinceName(String destProvinceName) {
		DestProvinceName = destProvinceName;
	}

	public String getDestinationCountryId() {
		return DestinationCountryId;
	}

	public void setDestinationCountryId(String destinationCountryId) {
		DestinationCountryId = destinationCountryId;
	}

	public String getDestinationCountryName() {
		return DestinationCountryName;
	}

	public void setDestinationCountryName(String destinationCountryName) {
		DestinationCountryName = destinationCountryName;
	}

	public String getHotelName() {
		return HotelName;
	}

	public void setHotelName(String hotelName) {
		HotelName = hotelName;
	}

	public String getHotelAddress() {
		return HotelAddress;
	}

	public void setHotelAddress(String hotelAddress) {
		HotelAddress = hotelAddress;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public List<String> getMinPrice() {
		return MinPrice;
	}

	public void setMinPrice(List<String> minPrice) {
		MinPrice = minPrice;
	}

	public String getMinPriceRemark() {
		return MinPriceRemark;
	}

	public void setMinPriceRemark(String minPriceRemark) {
		MinPriceRemark = minPriceRemark;
	}

	public String getIntroduction() {
		return Introduction;
	}

	public void setIntroduction(String introduction) {
		Introduction = introduction;
	}

	public String getHotelImages() {
		return HotelImages;
	}

	public void setHotelImages(String hotelImages) {
		HotelImages = hotelImages;
	}
}
