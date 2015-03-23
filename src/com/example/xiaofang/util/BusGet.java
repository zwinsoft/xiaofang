package com.example.xiaofang.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONObject;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class BusGet {
	
	//存储get 返回的汽车列表
	List<BusLineInfo> busList = new ArrayList<BusLineInfo>();
	//
	HttpUtils http = new HttpUtils();
	HttpClient httpClient = http.getHttpClient();
	JSONObject  json;
	JsonReader reader;
	Gson gson = new Gson();
	BusLineInfo bus;
	Type listType;
	LinkedList<BusLineInfo> buses = null;
	
	public final String NearestUrl = "http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=NearestLine";
	
	
	public  BusGet()
	{
	}
	
/*	最近班车信息接口
	http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=NearestLine
	GET传参，lng，lat
	返回值：最近一条班车线路的json格式信息*/
	public LinkedList<BusLineInfo> getNearest(double d,double e){
		
		String url = NearestUrl+"&lng="+d+"&lat="+e;
		http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				
				listType = (Type) new TypeToken<LinkedList<BusLineInfo>>(){}.getType();
				buses = gson.fromJson(responseInfo.result,listType);
				
				//bus = gson.fromJson(responseInfo.result, BusLineInfo.class);
			
				for(BusLineInfo bus : buses){
					Log.v("getNearest--Success", bus.id+"lat:"+bus.lat+"--lng:"+bus.lng+"-----msg\n");
				}
				
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Log.v("getNearest--error", error.toString()+"%%%%%%"+msg);
			}			
		});
		
		
		
		
		return buses;
	}
	
	
	
/*	线路详情接口
	http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=Line
	GET传参，线路id
	返回值：本线路的json格式信息
*/
	
	
/*	路线规划接口
	http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=Stops
	GET传参，lng，lat
	返回值：最近的几个停靠点*/

}
