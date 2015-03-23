package com.example.xiaofang.util;


/*
 * 班车线路有关信息；
 */
public class BusLineInfo {
	public int id;
	public int lineId,lineOrder,isDeleted;
	public String stopName,time;
	public float lng,lat;
	public int getId() {
		return id;
	}
	public void setid(int id) {
		this.id = id;
	}
	public int getlineId() {
		return lineId;
	}
	public void setlineId(int lineId) {
		this.lineId = lineId;
	}
	public int getLineOrder() {
		return lineOrder;
	}
	public void setLineOrder(int lineOrder) {
		this.lineOrder = lineOrder;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
}
