/**
 * DatabaseManager.java
 * Copyright 2013 taochen. 
 * All rights reserved. 
 * Created on 2013-4-9 上午10:39:18
 */
package com.example.xiaofang.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * 数据库管理类.
 * 20150114 taochen
 * 
 */
@SuppressLint("SimpleDateFormat")
public class DatabaseManager {


	private DatabaseHelper dbHelper;

	public DatabaseManager(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	
	/**
	 * 数据库管理类.
	 * 20150114 taochen
	 * 读取本地数据库文件的水源列表
	 * 
	 */
	public List<WaterUtil> searchWaterList(){
		
		List<WaterUtil> list = new ArrayList<WaterUtil>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		//查询全部水源信息
		Cursor cursor = null;
		try {
			 
			cursor = db.rawQuery("select [id],title,longitude,latitude FROM xiaofang_water",null);
			String strName="";
			WaterUtil util = null;
			while (cursor.moveToNext()) {
				util = new WaterUtil();
				util.setWaterid(cursor.getInt(0));
				strName=cursor.getString(1);				
				util.setTitle(strName);
				double longitude = cursor.getDouble(2);
				util.setLongitude(longitude);
				double latitude = cursor.getDouble(3);
				util.setLatitude(latitude);							
				
				list.add(util);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	
	/*
	public boolean saveReviewResult(int userid,int companyid,int evalutecategory,
			double sum,List<SelectItemUtil> list,String strNote)
	{
		boolean resultflag=false;
		//首先判断该项目的记录是否存在，如果存在，删除此项目的记录（更新）
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		int count = 0;
		try {
				cursor = db.rawQuery(
				"select count(*) from tjac_review where userid  = ?" +
				" and companyid=? and type=? "
						,new String[] { 
						String.valueOf(userid),String.valueOf(companyid),
						String.valueOf(evalutecategory)});
				cursor.moveToFirst();
				count = cursor.getInt(0);
				
				if(count>0)
				{//将评分记录isDeleted标志置为1
					
					Log.v("db","update tjac_review value isDeleted ");

					//删除评分记录表
					db.execSQL("delete from tjac_review where userid  = ?" +
				" and companyid=? and type=? "
						,new String[] { 
						String.valueOf(userid),String.valueOf(companyid),
						String.valueOf(evalutecategory)});
					//将评分记录isDeleted标志置为1
					//db.execSQL("update tjac_review set isDeleted=1 where userid  = ?" +
					//" and companyid=? and type=? "
					//		,new String[] { 
					//		String.valueOf(userid),String.valueOf(companyid),
					//		String.valueOf(evalutecategory)});
				}//将评分记录isDeleted标志置为1
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strNowTime=sdf.format(new java.util.Date());
				//取最大值
				cursor = db.rawQuery(
						"select max(id) from tjac_review ",null);
				cursor.moveToFirst();
				int maxid = cursor.getInt(0);
				maxid=maxid+1;
				//开始构建评分记录数据
				ContentValues values = new ContentValues();
				values.put("id", maxid);
				values.put("userid", userid);				
				values.put("companyid", companyid);	
				values.put("type", evalutecategory);
				values.put("sum", sum);
				values.put("reviewtime", strNowTime);
				
				//写入评分值
				for(int i=0;i<list.size();i++)
				{
					values.put("value"+String.valueOf(i+1), list.get(i).getInputmark());
				}				
				
				
				//写入加分说明
				values.put("memo1", strNote);

				try {
					db.insert("tjac_review", null, values);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				resultflag=true;
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return resultflag;
	}
	*/
	
	


	/*title         	TEXT,"+//水源名称
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
	
	public String saveWaterResult(int userid,String  title,String type,String status,
			String pressure,String calibre,String contacts,String phone,String date,
			String longitude,String latitude,String photofilename)
	{
				String strresultflag="";
				Cursor cursor = null;
				SQLiteDatabase db = dbHelper.getReadableDatabase();
		try
		{
				//开始构建数据
				ContentValues values = new ContentValues();
				
								
				values.put("title", title);	
				values.put("type", type);
				values.put("status", status);
				values.put("pressure", pressure);
				values.put("calibre", calibre);
				values.put("contacts", contacts);
				values.put("phone", phone);
				values.put("date", userid);
				values.put("longitude", longitude);
				values.put("latitude", latitude);
				values.put("calibre", calibre);
				values.put("userid", userid);	
				values.put("photofilename", photofilename);
				try {
					db.insert("xiaofang_water", null, values);
				} catch (SQLException e) {
					e.printStackTrace();
					Log.i("xiaofang","xiaofang_water insert error!");
				}
				
				strresultflag="";
				
		} catch (Exception e) {
			strresultflag=e.getMessage().toString();
			Log.i("xiaofang",strresultflag);
		
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return strresultflag;
	}
	
	public boolean syncWater(int userid,String  title,String type,String status,
			String pressure,String calibre,String contacts,String phone,String date,
			String longitude,String latitude,String photofilename) 
	{
		boolean bResult=true;
	    	
		
		String url="http://www.zwin.mobi/xiaofang/index.php?m=Home&c=Json&a=sync";
		
				
		HttpPost httpPost = new HttpPost(url);        
		HttpClient client = new DefaultHttpClient();        
		StringBuilder str = new StringBuilder();        
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();		   
		
		params.add(new BasicNameValuePair("title",title));
		params.add(new BasicNameValuePair("type",type));   
		params.add(new BasicNameValuePair("status", status));   
		params.add(new BasicNameValuePair("pressure", pressure));   
		params.add(new BasicNameValuePair("calibre",calibre)); 
		
		params.add(new BasicNameValuePair("contacts", contacts));   
		params.add(new BasicNameValuePair("phone", phone));  
		params.add(new BasicNameValuePair("date", date));  
		params.add(new BasicNameValuePair("longitude", longitude));   
		params.add(new BasicNameValuePair("latitude", latitude));  
		params.add(new BasicNameValuePair("userId", "0"));  
		params.add(new BasicNameValuePair("photo", photofilename));  
		
		
 
		
              
        BufferedReader buffer = null;
        try
        {
                httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
                HttpResponse httpRes = client.execute(httpPost);
                if(httpRes.getStatusLine().getStatusCode() == 200)
                {
                        buffer = new BufferedReader(new InputStreamReader(httpRes.getEntity().getContent()));
                        for(String s = buffer.readLine(); s != null ; s = buffer.readLine())
                        {
                                str.append(s);
                        }
                        //String out = EntityUtils.toString(httpRes.getEntity().getContent(), "UTF-8");
                        //StringBuilder sb = new StringBuilder()
                        Log.i("xiaofang",str.toString());
                        
                        buffer.close();
                        //JSONObject json = new JSONObject(str.toString());
                        //String status = json.getString("status");
                        //Log.i("xiaofang",status);
                            
                }
        }
        catch(Exception e)
        {
        	    bResult = false;
        		e.printStackTrace();
                if(buffer != null)
                {
                        try {
                                    buffer.close();
                            } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                            }
                }
        }
	
		return bResult;
	}
	
	
	
	public int checkUserValid(String UserName,String Password)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = null;
		int UserId=0;
		int count = 0;
		try {
				cursor = db.rawQuery(
				"select count(*) from xiaofang_user where username  = ? "
						+ " and password=? and level=8 and isDeleted=0 ",
							new String[] { UserName,Password});
				cursor.moveToFirst();
				count = cursor.getInt(0);
				if(count>0)
				{
					cursor = db.rawQuery(
							"select id from xiaofang_user where username  = ? "
									+ " and password=? and level=8 and isDeleted=0 ",
										new String[] { UserName,Password});
					cursor.moveToFirst();
					UserId = cursor.getInt(0);
				}
				else
				{
					UserId=0;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return UserId;
	}
	
	
	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
