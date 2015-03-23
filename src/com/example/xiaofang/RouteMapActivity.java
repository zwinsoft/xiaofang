package com.example.xiaofang;

import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;

public class RouteMapActivity extends FatherMapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
	    //注意该方法要再setContentView方法之前实现  
	    SDKInitializer.initialize(getApplicationContext()); 
	    
		
	    setContentView(R.layout.activity_xiaofang);
	    
		super.mMapView = (MapView) findViewById(R.id.bmapView);
		/*
		 * 初始化地图模块
		 */
		mapinit();
		
		//注册路径规划监听器
		super.mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener((OnGetRoutePlanResultListener) this);
		
		super.myapp = (MyApplication) getApplicationContext();				
		
		
			locationinit();
			//打开activity 就启动定位，定位好后去申请最近的站点信息；
			mLocClient.start();
			
			//初始化病，设置地图模式改变的监听方法；	
			initMapModel();
			
		super.loadRoute(2);
		
	}
	
}
