package com.example.xiaofang;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.example.xiaofang.XiaofangActivity.MyLocationListenner;
import com.example.xiaofang.util.BusLineInfo;
import com.example.xiaofang.util.LineInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


public class activity_front extends Activity implements BDLocationListener{
	
	ImageButton shishi,luxian,fenxiang,zhandian,guanyu;
	
	private final String NearestUrl = "http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=NearestLine";
	LinkedList<BusLineInfo> buses = null;
	HttpUtils http = new HttpUtils();
	Type listType;
	Gson gson = new Gson();
	
	//判定收到定位信息后，是否跳转到其他页面或者跳转到哪个页面；
	Boolean tofenxiang = false; 
	Boolean toNearstStation = false;
	Boolean Jumpfenxiang = false;
	Boolean JumpRoute = false;
	Boolean JumpShiShi = false;

	
	// 百度地图定位相关	
	LocationClient mLocClient;
	MyLocationData locData = null;
	public MyLocationListenner myListener;
	BDLocation mylocation;
	
	MyApplication  myapp;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.front);
		
		
		shishi = (ImageButton) findViewById(R.id.shishi);
		luxian = (ImageButton) findViewById(R.id.luxian);
		guanyu = (ImageButton) findViewById(R.id.guanyu);
		fenxiang = (ImageButton) findViewById(R.id.fenxiang);
		zhandian = (ImageButton) findViewById(R.id.zhandian);
		
		myapp = (MyApplication) getApplicationContext();
		
		//===================定位客户端初始化===================
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(this);
	
		LocationClientOption Locoption = new LocationClientOption();
		Locoption.setOpenGps(true);// 打开gps
		Locoption.setCoorType("bd09ll"); // 设置坐标类型
		Locoption.setScanSpan(1000);
		Locoption.setPriority(LocationClientOption.GpsFirst);  //设置GPS优先 

		mLocClient.setLocOption(Locoption);
//		打开activity 就启动定位，定位好后去申请最近的站点信息；
//		mLocClient.start();
		/*
		 * 主页面点击实时按钮后
		 * 将会查看实时信息；
		 * 
		 */
		shishi.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			 //确认下一页加载所有的线路
				JumpShiShi = true ;
			
				loadAllLine();	
				
			}
		});
		
		
		luxian.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				JumpRoute = true;
				//保证被跳转的页面，含有所有的线路信息；
				loadAllLine();			
				
			}
		});
		
		guanyu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				
				
			}
		});
		
		fenxiang.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Jumpfenxiang = true;
				if (!toNearstStation)  tofenxiang = true ;				
				//分享当前班车的地理位置
				if (!mLocClient.isStarted())
					mLocClient.start();
			}
		});
		
		zhandian.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if(!tofenxiang) toNearstStation = true ;				
				//最近站点
				if (!mLocClient.isStarted())
					mLocClient.start();
			}
		});		
	}
	
	
	
	
	/*
	 * 最近的线路信息
	 */
	public void getNearest(double d,double e){
		
		String url = NearestUrl+"&lng="+d+"&lat="+e;
		http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				
				Log.v("get ReaponseInfo", responseInfo.result);
				
				listType = (Type) new TypeToken<LinkedList<BusLineInfo>>(){}.getType();
				buses = gson.fromJson(responseInfo.result,listType);
				
				for(BusLineInfo bus : buses){
					Log.v("getNearest--Success", bus.id+"lat:"+bus.lat+"--lng:"+bus.lng+"-----msg\n");
				}
				
				//将最近的公交信息标注在地图上；
//				loadMarker();
				
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.v("getNearest--error", error.toString()+"%%%%%%"+msg);
			}			
		});
		
		
	}	
		



	@Override
	public void onReceiveLocation(BDLocation loc) {
		
		Intent myintent;
		if(tofenxiang) {
			tofenxiang = false;
			mylocation = loc;
			//保证被跳转的页面，含有所有的线路信息；
			loadAllLine();
			
			
		}else if (toNearstStation){
			//保证缓存中含有最近站点
			toNearstStation=false;
			loadNearstStation(loc.getLongitude(),loc.getLatitude());
		}
		
		//定位信息只用一次，就可以关闭了；
		mLocClient.stop();
	}

	@Override
	public void onReceivePoi(BDLocation arg0) {
		
		
	}
	
	/*
	 * 负责分享，路线页面的跳转
	 */
	private void goIntent(){
		Intent myintent;
		
		if (Jumpfenxiang){
			//跳转到分享页面
			myintent = new Intent(activity_front.this, ShareActivity.class);
		
			myintent.putExtra("lng", mylocation.getLongitude());
			myintent.putExtra("lat", mylocation.getLatitude());
			
	
			startActivity(myintent);
			
			Jumpfenxiang = false;
		}
		if(JumpRoute){
			
			//跳转到路线页面
			myintent = new Intent(activity_front.this, RouteActivity.class);
			startActivity(myintent);
			JumpRoute = false;
			
		}
		
	if(JumpShiShi){
			
			JumpShiShi = false;
			
			//跳转到路线页面
			myintent = new Intent(activity_front.this, ShiShiActivity.class);
			startActivity(myintent);
		}
		
	}
	
	private void loadAllLine(){
		List<LineInfo> allLines = myapp.getAllLines();
		
		//如果已经有缓存，返回
		if (allLines != null) 
		if (!allLines.isEmpty()) goIntent();
		
		//否则，从网络中加载；
		String msg = "正在加载所有线路......";   
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		
		//  &dept=1&page=1
		//获取列表详情
		String urlMain = "http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=AllLines";
		String url = urlMain+"&dept=1"+"&page=1";
		http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				List<LineInfo> allLines = new ArrayList<LineInfo>();
				listType = (Type) new TypeToken<LinkedList<LineInfo>>(){}.getType();
				allLines = gson.fromJson(responseInfo.result,listType);
				
				//输出便于调试
				for(LineInfo line : allLines){
					Log.d("getLine--Success", line.id+"name:"+line.lineName+"--start:"+line.start+"-----msg\n");
				}
				
				
				//缓存数据
				myapp.setAllLines(allLines);
				
				String msg = "已将线路缓存，下次加载将不耗费流量......";   
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				
				
				goIntent();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.v("getNearest--error", error.toString()+"%%%%%%"+msg);
			}			
		});
	}
	
	private void loadNearstStation(double lng,double lat){
		
		String  nearestLine ="http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=NearestLine";
		String url = nearestLine + "&lng="+lng+"&lat="+lat;
		
	
		
		http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {		
				Log.d("lineDetails", responseInfo.result);
				
				//如果返回 “null”  没有内容返回；
				if ((responseInfo.result =="resultIsNull")||(responseInfo.result.endsWith("resultIsNull"))){
					String msg = "no this line's details";   
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					return;
				}
				
				 List<BusLineInfo> oneLineAll = new ArrayList<BusLineInfo>();
//				List<BusLineInfo> allLines = new ArrayList<BusLineInfo>();
				listType = (Type) new TypeToken<LinkedList<BusLineInfo>>(){}.getType();
				oneLineAll = gson.fromJson(responseInfo.result,listType);
				
				//如果解析后是 “null”  没有内容返回；
				
				if (oneLineAll == null){
					String msg = "no this line's details";   
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				
				//输出便于调试
				for(BusLineInfo line : oneLineAll){
					Log.d("getNearstStation--Success", line.id+"Stopname:"+line.getStopName()+"--start:"+line.getTime()+"-----msg\n");
				}
				
				myapp.setNearstInfo(oneLineAll);
				
				Intent myintent = new Intent(activity_front.this,RouteMapActivity.class);
				
				myintent.putExtra("id", -2);
				startActivity(myintent);
				String msg = "已将最近站点缓存";   
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
										
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.v("getLineDetail--error", error.toString()+"%%%%%%"+msg);
			}			
		});

		
		
		
	}
	
	
	

}
