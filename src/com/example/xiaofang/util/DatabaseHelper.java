/**
 * DatabaseHelper.java
 * Copyright 2013 Zwin(Tianjin) High Technology Development Ltd. 
 * All rights reserved. 
 * Created on 2013-4-8 下午02:31:25
 */
package com.example.xiaofang.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * sqlite数据库辅助类.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	//数据库版本号
	private static final int DATABASE_VERSION = 1;
	
	//数据库文件
	private static final String DATABASE_FILE = "xiaofang_water.db";

	//创建用户列表
	private final String CREATE_TABLES_USER_SQL = "CREATE TABLE xiaofang_user ( " +
			" id          INTEGER," +
			" username    TEXT," +
			" password    TEXT," +
			" level       INTEGER," +
			" isDeleted   INTEGER," +
			" loginTime   TEXT," +
			" regIp       TEXT," +
			" lastLoginIp TEXT);";
	 
	//创建水源信息表
	private final String CREATE_TABLES_WATER_SQL = "CREATE TABLE xiaofang_water ( " +
			" id              	INTEGER primary key autoincrement,"+//序号
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
			");";

	

	
	public DatabaseHelper(Context context) {
		
		super(context, Environment.getExternalStorageDirectory().getPath()
				+"//xiaofang//"
				+"//"+DATABASE_FILE, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_TABLES_USER_SQL);
		db.execSQL(CREATE_TABLES_WATER_SQL);
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	}

}
