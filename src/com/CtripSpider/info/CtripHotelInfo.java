package com.CtripSpider.info;

import java.util.List;

/**
 * <p>
 * ProductId:酒店ID
 * <p>
 * Title:酒店标签
 * <p>
 * MainName:酒店完整标签
 * <p>
 * DepartureCityName:酒店所属城市
 * <p>
 * DestCityId:酒店所属城市ID
 * <p>
 * DestProvinceId:酒店所属省ID
 * <p>
 * DestProvinceName:酒店所属省名称
 * <p>
 * DestinationCountryId:酒店所属国家ID
 * <p>
 * DestinationCountryName:酒店所属国家名称
 * <p>
 * HotelName:酒店名称
 * <p>
 * HotelAddress:酒店地址
 * <p>
 * Latitude:酒店经纬度[Latitude]
 * <p>
 * Longitude:酒店经纬度[Longitude]
 * <p>
 * MinPrice:酒店价格/晚
 * <p>
 * MinPriceRemark:酒店价格说明
 * <p>
 * Introduction:酒店简介
 * <p>
 * HotelImages:酒店预览图片
 * <p>
 * HotelId:酒店跳转ID
 * <p>
 * HotelPhone:酒店联系方式
 */

public class CtripHotelInfo {
	/**
	 * <p>
	 * ProductId:酒店ID
	 * <p>
	 * Title:酒店标签
	 * <p>
	 * MainName:酒店完整标签
	 * <p>
	 * DepartureCityName:酒店所属城市
	 * <p>
	 * DestCityId:酒店所属城市ID
	 * <p>
	 * DestProvinceId:酒店所属省ID
	 * <p>
	 * DestProvinceName:酒店所属省名称
	 * <p>
	 * DestinationCountryId:酒店所属国家ID
	 * <p>
	 * DestinationCountryName:酒店所属国家名称
	 * <p>
	 * HotelName:酒店名称
	 * <p>
	 * HotelAddress:酒店地址
	 * <p>
	 * Latitude:酒店经纬度[Latitude]
	 * <p>
	 * Longitude:酒店经纬度[Longitude]
	 * <p>
	 * MinPrice:酒店价格/晚
	 * <p>
	 * MinPriceRemark:酒店价格说明
	 * <p>
	 * Introduction:酒店简介
	 * <p>
	 * HotelImages:酒店预览图片
	 * <p>
	 * HotelId:酒店跳转ID
	 * <p>
	 * HotelPhone:酒店联系方式
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
