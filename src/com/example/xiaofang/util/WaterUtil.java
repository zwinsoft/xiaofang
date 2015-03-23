package com.example.xiaofang.util;


/*

" id              	INTEGER AUTOINCREMENT,"+//序号
" title         	TEXT,"+//水源名称
" type         		INTEGER,"+//水源类型
" status        	INTEGER,"+//水源情况
" pressure        	INTEGER,"+//水压
" calibre           INTEGER,"+//管径
" contacts       	TEXT,"+//联系人
" phone      		TEXT,"+//联系电话
" date   			TEXT,"+//更新日期
" longitude     	TEXT,"+//经度
" latitude          TEXT,"+//纬度
" userid            INTEGER,"+//操作员编号
" photofilename     TEXT"+//图片名字

*/

public class WaterUtil {

	private int id;
	private String title;
	private double longitude;
	private double latitude;
	
	public int getWaterid() {
		return id;
	}
	public void setWaterid(int id) {
		this.id = id;
	}

	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
	public WaterUtil() {
		super();
	}
	
}
