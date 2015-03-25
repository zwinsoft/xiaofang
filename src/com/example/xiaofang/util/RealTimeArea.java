package com.example.xiaofang.util;

public class RealTimeArea {
	
	private int id,lineId;
	private double lng,lat;
	private long time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLineId() {
		return lineId;
	}
	public void setLineId(int lineId) {
		this.lineId = lineId;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}	
}
