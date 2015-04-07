package com.example.xiaofang;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.xiaofang.util.BusGet;
import com.example.xiaofang.util.BusLineInfo;
import com.example.xiaofang.util.LineInfo;
import com.example.xiaofang.util.RealTimeArea;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class FatherMapActivity extends Activity  implements OnGetRoutePlanResultListener{
	
	protected MapView mMapView = null; 
	//MKOfflineMap mOffline = null;   //离线地图变量
	
	BaiduMap mBaiduMap;
	RoutePlanSearch myRouteSearch = null;
	
	
	//
	protected MyApplication  myapp;
	
	//地图标注
	Marker marker;

	
	List<Marker>  makerWater =  new ArrayList<Marker>();
	
	//步行的每一步；
	List<WalkingRouteLine.WalkingStep> walkStep = new ArrayList<WalkingRouteLine.WalkingStep>();
	List<WalkingRouteLine.WalkingStep> wStep = new ArrayList<WalkingRouteLine.WalkingStep>();
	public int  getWalkingRouteResultTimes = 0;
	
	//班车信息；
	LinkedList<BusLineInfo> buses = null;
	HttpUtils http = new HttpUtils();
	Type listType;
	Gson gson = new Gson();
	
	boolean isLocation = false; //是否定位水源
	boolean isFirstLoc = true;// 是否首次定位
	
	ImageButton offlineButton;
	ImageButton modeButton;
	ImageButton enlargeButton;
	ImageButton zoominButton;
	

	private int mapMode = 1;
	
	// 百度地图定位相关	
	protected LocationClient mLocClient;
	MyLocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	
	
	//路径搜索
	protected RoutePlanSearch mSearch = null; 
	
	private UiSettings mUiSettings;
	
	public BusGet bus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myapp = (MyApplication) getApplicationContext();		
		

	}//onCreate end....
	
	/*
	 * 初始化定位模块
	 */
	protected void locationinit(){
		
		//===================在线地图定位初始化===================
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
	
		
		LocationClientOption Locoption = new LocationClientOption();
		Locoption.setOpenGps(true);// 打开gps
		Locoption.setCoorType("bd09ll"); // 设置坐标类型
		Locoption.setScanSpan(1000);
		Locoption.setPriority(LocationClientOption.GpsFirst);  //设置GPS优先 

		mLocClient.setLocOption(Locoption);
	}
	
	/*
	 * 初始化地图模块
	 */
	protected void mapinit(){
		

	    // ============在线模式地图初始化============
		
		//判断网络连接是否有效
		//if(isConnectInternet())
		//{//网络连接正常
		
		//}//网络连接正常
		//else
		//{//网络连接异常
		//============离线模式初始化============
		//	LatLng p = new LatLng(default_offline_Latitude, default_offline_Lontitude);
			//mMapView = new MapView(this,
				//	new BaiduMapOptions().mapStatus(new MapStatus.Builder()
					//		.target(p).zoom(12).build()));
			
		//	mMapView = new MapView(findViewById(R.id.bmapView).getContext(),
		//			new BaiduMapOptions().mapStatus(new MapStatus.Builder()
		//					.target(p).zoom(12).build()));
			//============离线模式初始化============
		//}//网络连接异常
	  	  
		//不显示缩放比例尺
		mMapView.showZoomControls(false);  
		
		//不显示百度地图Logo
		mMapView.removeViewAt(1);
		
		
			 // ============在线模式地图初始化============
	        			
			mBaiduMap = mMapView.getMap();
			

			mUiSettings = mBaiduMap.getUiSettings();
			mUiSettings.setCompassEnabled(true);
			mUiSettings.setZoomGesturesEnabled(true);	
			
			
			//普通地图  
			mapMode = 1;
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);  
			//mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			
			MapStatus mMapStatus = new MapStatus.Builder().zoom(16).build();
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
			 //改变地图状态        
			mBaiduMap.setMapStatus(mMapStatusUpdate);

			
			 mBaiduMap
	         .setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

	                 @Override
	                 public void onMapStatusChangeStart(MapStatus arg0) {
	                         
	                         //String msg = "start-" + arg0.toString();   
	     					 //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	                 }

	                 @Override
	                 public void onMapStatusChangeFinish(MapStatus status) {
	                 
//	     				LatLng ll = new LatLng( mBaiduMap.getMapStatus().target.latitude,
//							mBaiduMap.getMapStatus().target.longitude);
	                	 
	             		LatLng ll = new LatLng( status.target.latitude,
	   						status.target.longitude);
	             	

	     				if (ll != null){
	     					
//	     					marker.setPosition(ll);
	    					//Log.d("maker", ll.toString());
	    					
	    					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
	    					//MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, mBaiduMap.getMaxZoomLevel());
	    					mBaiduMap.animateMapStatus(u);
	     
	     					
	     				} 
					
						
	                         //String msg = "finish-" + arg0.toString();   
	     					 //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	                 }

	                 @Override
	                 public void onMapStatusChange(MapStatus arg0) {

	                 }
	         });
			 
	   
//			mBaiduMap.setMyLocationEnabled(true);	
		
		
	}
	
	
	/*
	 * 地图标注点处理
	 */
	
	protected void handleMarker(){
		
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {

				OnInfoWindowClickListener listener = null;
				
					
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							
							mBaiduMap.hideInfoWindow();
						}
					};

					Button btn = new Button(getApplicationContext());  
					                      
					                    btn.setBackgroundColor(Color.GRAY);  
					                    btn.setText("测试水源");  
					                    // btn 变成 View 图片  
					                    BitmapDescriptor descriptor = BitmapDescriptorFactory  
					                            .fromView(btn); 
					                    
					LatLng ll = marker.getPosition();
					InfoWindow  mInfoWindow = new InfoWindow(descriptor, ll, -47, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				

				
				return true;
			}
		});
		
	}
	
	
	/*
	 * 初始化，并处理地图模式
	 */
	protected void initMapModel(){
		
		// 地图模式切换
		modeButton = (ImageButton) findViewById(R.id.buttonmode);		
		
		OnClickListener btnModeClickListener = new OnClickListener() {
			public void onClick(View v) {		
				//获取当前地图状态
				if(mapMode ==1)
				{
					mapMode = 2;
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);  
					String msg = "切换地图模式为正常模式";   
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
				else
				{
					mapMode = 1;
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					String msg = "切换地图模式为卫星图模式";   
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
		};
		modeButton.setOnClickListener(btnModeClickListener);	
		
	
		// 地图放大
		enlargeButton = (ImageButton) findViewById(R.id.buttonenlarge);		
		
		OnClickListener btnEnlargeClickListener = new OnClickListener() {
			public void onClick(View v) {		
				//获取当前地图状态
				float zoomLevel = mBaiduMap.getMapStatus().zoom;
				float minZoomLevel = mBaiduMap.getMinZoomLevel();
				float maxZoomLevel = mBaiduMap.getMaxZoomLevel();
				
				if(zoomLevel>=maxZoomLevel){
					zoomLevel = maxZoomLevel;
		            //inBtn.setEnabled(false);
		        }else{
		        	zoomLevel = zoomLevel+1;
		            //inBtn.setEnabled(true);
		        }
				
				
				MapStatus mMapStatus = new MapStatus.Builder().zoom(zoomLevel).build();
				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
				 //改变地图状态        
				mBaiduMap.setMapStatus(mMapStatusUpdate);
				
				
			}
		};
		enlargeButton.setOnClickListener(btnEnlargeClickListener);	
		

		
		// 地图缩小
		zoominButton = (ImageButton) findViewById(R.id.buttonsmall);		
		
		OnClickListener btnZoominClickListener = new OnClickListener() {
			public void onClick(View v) {		
				
				//获取当前地图状态
				float zoomLevel = mBaiduMap.getMapStatus().zoom;
				float minZoomLevel = mBaiduMap.getMinZoomLevel();
				float maxZoomLevel = mBaiduMap.getMaxZoomLevel();
				
				if(zoomLevel<=minZoomLevel){
					zoomLevel = minZoomLevel;
		            //inBtn.setEnabled(false);
		        }else{
		        	zoomLevel = zoomLevel-1;
		            //inBtn.setEnabled(true);
		        }
				
				
				MapStatus mMapStatus = new MapStatus.Builder().zoom(zoomLevel).build();
				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
				 //改变地图状态        
				mBaiduMap.setMapStatus(mMapStatusUpdate);
				
				
			}
		};
		zoominButton.setOnClickListener(btnZoominClickListener);	
		
	}
	
	/*
	 * 加载显示所有路线
	 */
	protected void loadAllLines(){
		
		// 显示所有的路线；
//		  myapp = (MyApplication) getApplicationContext();
		final List<LineInfo> allLines = myapp.getAllLines();
		
		
		class routeListener implements OnClickListener {

			private List<LineInfo> allLines = new ArrayList<LineInfo>();
			
			
			public routeListener(List<LineInfo> all){
				
				this.allLines = all;
			}
			@Override
			public void onClick(View v) {
				//如果没有缓存就去网络中加载；
				if(allLines.isEmpty()){
					String msg = "正在加载所有线路......";   
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					
					//  &dept=1&page=1
					//获取列表详情
					String urlMain = "http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=AllLines";
					String url = urlMain+"&dept=1"+"&page=1";
					http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							
							Log.v("get ReaponseInfo", responseInfo.result);
							
							listType = (Type) new TypeToken<LinkedList<LineInfo>>(){}.getType();
							allLines = gson.fromJson(responseInfo.result,listType);
							
							for(LineInfo line : allLines){
								Log.d("getLine--Success", line.id+"name:"+line.lineName+"--start:"+line.start+"-----msg\n");
							}
							
							//将所有的线路标注在地图上；
//							loadLineMarker();
							//缓存数据
							MyApplication  app = (MyApplication) getApplicationContext();
							app.setAllLines(allLines);
							String msg = "已将线路缓存，下次加载将不耗费流量......";   
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Log.v("getNearest--error", error.toString()+"%%%%%%"+msg);
						}			
					});
					
					
				}
				
//				//打开线路详情的activity
//				
//				Intent lineDetails = new Intent(XiaofangActivity.this, LineDetails.class); 
//				startActivity(lineDetails);
				
			}
		}
		
	}
	
	
	
	/*
	 * 标注附近的公交站点
	 */
	protected void loadMarker(List<BusLineInfo>	buses){
		
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_gcoding);  
		
		OverlayOptions overlayOption;
		for (BusLineInfo bus:buses){
					overlayOption = new MarkerOptions()
						.position(new LatLng(bus.getLat(),bus.getLng()))
						.icon(bitmap)
						.zIndex(1)
						.draggable(true);
					mBaiduMap.addOverlay(overlayOption);
					
					if (buses.get(buses.size()-1) == bus){
						
//						 定义地图状况
				        MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(bus.getLat(),bus.getLng())).build();
				        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
						//MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, mBaiduMap.getMaxZoomLevel());
						//改变地图状态        		
				        
						mBaiduMap.setMapStatus(mMapStatusUpdate);
						mBaiduMap.animateMapStatus(mMapStatusUpdate);
						mMapView.invalidate();
						
						Toast.makeText(getApplicationContext(), "附近的公交站点，标注完毕", Toast.LENGTH_SHORT).show();
					}
			}
		
	}
	
	protected void loadMarker(List<RealTimeArea> realAreas,int a){
		
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_gcoding);  
		if (realAreas == null) return;
		OverlayOptions overlayOption;
		for (RealTimeArea bus:realAreas){
					overlayOption = new MarkerOptions().position(new LatLng(bus.getLat(),bus.getLng())).icon(bitmap).zIndex(1).draggable(true);
					mBaiduMap.addOverlay(overlayOption);
					
					if (realAreas.get(realAreas.size()-1) == bus){
						
//						 定义地图状况
				        MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(bus.getLat(),bus.getLng())).build();
				        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
						//MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, mBaiduMap.getMaxZoomLevel());
						//改变地图状态        				
						mBaiduMap.setMapStatus(mMapStatusUpdate);
						mBaiduMap.animateMapStatus(mMapStatusUpdate);
						mMapView.invalidate();
						
						Toast.makeText(getApplicationContext(), "附近的公交站点，标注完毕", Toast.LENGTH_SHORT).show();
					}
			}
		
	}
	
	
	/*
 	* @Marker 提交从当前位置到Marker位置的走路路由规划
 	*/	
	protected void applySearch(Marker marker){
		
		WalkingRoutePlanOption work;
		work = new WalkingRoutePlanOption();
		if (locData!=null){
			work.from(PlanNode.withLocation(new LatLng(locData.latitude,locData.longitude)));
			work.to(PlanNode.withLocation(marker.getPosition()));
			mSearch.walkingSearch(work);		
		}
		
		
	}
	
	/*
	 *   根据线路id画通过所有站点的折线图；
	 *   
	 *   id：application中缓存的具体线路的id，可以得到这条线路所有站点；
	 */
	protected void applyDriving(int id){
		
		List<BusLineInfo> oneLineAll = new ArrayList<BusLineInfo>();
		try {
			oneLineAll = myapp.getBusMap(id);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		//如果没有还两个点那么就，不用规划路径了；
		if ((oneLineAll == null)||(!(oneLineAll.size() >= 2))) return;
		
		List<LatLng> lll = new ArrayList<LatLng>();
		LatLng latlng;
		for(BusLineInfo bus:oneLineAll){
			latlng = new LatLng(bus.getLat(),bus.getLng());
			lll.add(latlng);
		}
				
		// 添加折线
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(0xAAFF0000).points(lll);
		mBaiduMap.addOverlay(ooPolyline);
	}
	
	/*
	 * 根据路径id 在地图上加载路线；
	 */
	protected void loadRoute(int id){
		List<BusLineInfo> oneLineAll = new ArrayList<BusLineInfo>();
		myapp = (MyApplication) getApplicationContext();
		
		try {
			oneLineAll = myapp.getBusMap(id);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (oneLineAll == null) return;
		
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_gcoding);  
		
		OverlayOptions overlayOption;
		int size = oneLineAll.size();
		for (  BusLineInfo bus:oneLineAll){
					size--;
					overlayOption = new MarkerOptions().position(new LatLng(bus.getLat(),bus.getLng())).icon(bitmap).zIndex(1).draggable(true);
					mBaiduMap.addOverlay(overlayOption);
					
					Log.d("add", "add_station_line");
					
				if(size<=0){	
			        MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(bus.getLat(),bus.getLng())).build();
			        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
					//MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, mBaiduMap.getMaxZoomLevel());
					//改变地图状态        				
					mBaiduMap.setMapStatus(mMapStatusUpdate);
					mBaiduMap.animateMapStatus(mMapStatusUpdate);
					mMapView.invalidate();
					
					Toast.makeText(getApplicationContext(), "路线"+id+"标注完毕", Toast.LENGTH_SHORT).show();
				}
					
			}
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			
			
			/*
			 * 第一次定位将附近公交站的标注显示出来；
			 */
			if(isFirstLoc){
				isFirstLoc = false;
				
				
				Log.d("longitude", ""+locData.longitude);
				Log.d("latitude", ""+locData.latitude);
			}
			
		
			
			if (location.getLocType() == BDLocation.TypeGpsLocation){  
				String msg = "利用GPS定位......";   
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	             
	        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){ 
	        	//String msg = "利用网络定位......";   
				//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	        	
	        }
	        else
	        {
	        	//String msg = "其他定位方式";   
				//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	        }
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
		
		
	}
	
	
	
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
        }
    
 
    @Override  
    protected void onDestroy() {  
    	
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
		
		//mOffline.destroy();
		
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
 
    }  
  
    /**

     * 判断网络状态是否可用

     * @return true: 网络可用 ; false: 网络不可用

     */

 public boolean isConnectInternet() {

         ConnectivityManager conManager=(ConnectivityManager)FatherMapActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE );

         NetworkInfo networkInfo = conManager.getActiveNetworkInfo(); 
         
         if (networkInfo == null || !networkInfo.isConnected()) {

          return false;

         }
         if (networkInfo.isConnected()){

                   return true;

         }

         return false ;

 }


	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
		
	}


	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		
	}


	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		
		//倒计时添加图层；
		getWalkingRouteResultTimes--;
		
		
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(FatherMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

        	  WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
              
              overlay.setData(result.getRouteLines().get(0));
              overlay.addToMap();
              overlay.zoomToSpan();  
        	
            //往图层上添加第一条规划线路；
             
        		
           
        }
        
		
		
		
	}
	
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            }
            return null;
        }
    }

}