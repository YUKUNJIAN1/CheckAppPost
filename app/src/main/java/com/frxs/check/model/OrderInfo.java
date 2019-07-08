package com.frxs.check.model;


import java.io.Serializable;

public class OrderInfo implements Serializable
{
	private String OrderId;//:"201600098756",
	private String OrderType;//:订单类型,
	private String ShopCode;//:"门店编号",
	private String ShopName;//:"门店名",
	private String RevLinkMan;//门店联系人
	private String RevTelephone;//:"门店联系人电话",
	private String StationNumber;//站台号:59184,
	private String PickingBeginDate;//开始拣货时间:"2016-04-22T10:18:32.5873274+08:00",
	private String PickingEndDate;//完成拣货时间:"2016-04-22T10:18:32.5873274+08:00",
	private String ShopID;//
	private int Status;// 3正在拣货,不显示装箱  4//完成拣货，才显示装箱
	private String OrderDate;//
	private String NoCheckShelfAreaName;//"未对货货区"
	private String DoneCheckShelfAreaName;//"已对货货区"

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public String getShopCode() {
		return ShopCode;
	}

	public void setShopCode(String shopCode) {
		ShopCode = shopCode;
	}

	public String getShopName() {
		return ShopName;
	}

	public void setShopName(String shopName) {
		ShopName = shopName;
	}

	public String getRevTelephone() {
		return RevTelephone;
	}

	public void setRevTelephone(String revTelephone) {
		RevTelephone = revTelephone;
	}

	public String getStationNumber() {
		return StationNumber;
	}

	public void setStationNumber(String stationNumber) {
		StationNumber = stationNumber;
	}

	public String getPickingBeginDate() {
		return PickingBeginDate;
	}

	public void setPickingBeginDate(String pickingBeginDate) {
		PickingBeginDate = pickingBeginDate;
	}

	public String getPickingEndDate() {
		return PickingEndDate;
	}

	public void setPickingEndDate(String pickingEndDate) {
		PickingEndDate = pickingEndDate;
	}

	public String getShopID() {
		return ShopID;
	}

	public void setShopID(String shopID) {
		ShopID = shopID;
	}

	public String getRevLinkMan() {
		return RevLinkMan;
	}

	public void setRevLinkMan(String revLinkMan) {
		RevLinkMan = revLinkMan;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}

	public String getNoCheckShelfAreaName() {
		return NoCheckShelfAreaName;
	}

	public void setNoCheckShelfAreaName(String noCheckShelfAreaName) {
		NoCheckShelfAreaName = noCheckShelfAreaName;
	}

	public String getDoneCheckShelfAreaName() {
		return DoneCheckShelfAreaName;
	}

	public void setDoneCheckShelfAreaName(String doneCheckShelfAreaName) {
		DoneCheckShelfAreaName = doneCheckShelfAreaName;
	}
}
