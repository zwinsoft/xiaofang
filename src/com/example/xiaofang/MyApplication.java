package com.example.xiaofang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.example.xiaofang.util.BusLineInfo;
import com.example.xiaofang.util.LineInfo;

@SuppressLint("UseSparseArrays") 
public class MyApplication extends Application {
	
	private String AppUserName;
	private String AppPassword;
	private String FtpServerIP;
	private String FTPPort;
	private String FtpUserName;
	private String FtpPassword;
	
	private String source = null;
	
	public static String source_a;
	/*
	 * 所有线路信息
	 */
	private List<LineInfo> allLines;
	
//	private List<BusLineInfo> onebusLine = new ArrayList<BusLineInfo>();
	private Map<Integer,List<BusLineInfo>> busMap;
	
	
	/*SSS
	 * (non-Javadoc)
	 * 初始化：
	 * 			从缓存中提取数据
	 * 
	 * @see android.app.Application#onCreate()
	 */
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		  SDKInitializer.initialize(getApplicationContext()); 
		  
		allLines = new ArrayList<LineInfo>();
	
		busMap = new HashMap<Integer,List<BusLineInfo>>();
		
		source = "init";
		source_a = "INIT";
		
	}
	
	public List<LineInfo> getAllLines(){
		
		//如果非空证明不是第一次加载数据，此时可以直接返回；
		if (!allLines.isEmpty()) return allLines;
		//从缓存中加载；暂不写；
		
		return allLines;
	}
	
	public void setAllLines(List<LineInfo> all){
		
	allLines = all;
	
	}
	


	public void addBusMap(int i,List<BusLineInfo> list){	
		
		Log.d("application", "added one list:"+i);
		busMap.put(i, list);
		
		//加入缓存，待写
	}
	public void loadBusMap(){		
		//从缓存中加载，待写
		
	}
	
	
	public List<BusLineInfo> getBusMap(int i){
	   if (busMap.containsKey(i)){
		return  busMap.get(i);
	   } else return null;
	   
	}
	
	
	
	public String getAppUserName() {
		return AppUserName;
	}
	public void setAppUserName(String appUserName) {
		AppUserName = AppUserName;
	}
	
	public String getAppPassword() {
		return AppPassword;
	}
	public void setAppPassword(String appPassword) {
		AppPassword = appPassword;
	}
	
	public String getFtpServerIP() {
		return FtpServerIP;
	}
	public void setFtpServerIP(String ftpServerIP) {
		FtpServerIP = ftpServerIP;
	}
	public String getFTPPort() {
		return FTPPort;
	}
	public void setFTPPort(String fTPPort) {
		FTPPort = fTPPort;
	}
	public String getFtpUserName() {
		return FtpUserName;
	}
	public void setFtpUserName(String ftpUserName) {
		FtpUserName = ftpUserName;
	}
	public String getFtpPassword() {
		return FtpPassword;
	}
	public void setFtpPassword(String ftpPassword) {
		FtpPassword = ftpPassword;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	


	
}
