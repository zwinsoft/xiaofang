package com.example.xiaofang;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.xiaofang.util.BusLineInfo;
import com.example.xiaofang.util.LineInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


public class RouteActivity extends ListActivity{

	MyApplication  myapp ;
	private List<LineInfo> allLines = new ArrayList<LineInfo>(); 
	private List<BusLineInfo> oneLineAll = new ArrayList<BusLineInfo>();
	ListView listview;
	HttpUtils http = new HttpUtils();
	Type listType;
	Gson gson = new Gson();
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*setContentView(R.layout.activity_route);*/
		
		
		myapp = (MyApplication) getApplicationContext();
		//从缓存中加载数据
		myapp.loadBusMap();
		
		allLines =myapp.getAllLines();
		
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.linedetails,
									new String[]{"linename","start","end"},
									new int[]{R.id.linetitle,R.id.linestart,R.id.lineend});
		
		setListAdapter(adapter);
		listview = getListView();  
		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int id = allLines.get(arg2).getId();
				
				//从缓存中加载
				oneLineAll = myapp.getBusMap(id);
				if (oneLineAll == null) {getLineDetails(id);}
				else {
					
					//跳转到地图页面显示本线路的相关信息；
					Intent myintent = new Intent(RouteActivity.this,RouteMapActivity.class);

					myintent.putExtra("id", id);
					
					String msg = "不耗费流量哦..source:"+id;   
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					startActivity(myintent);
						
					
				}
				
				
				
				
			}
		});
		
	}

	private List<Map<String,Object>> getData(){
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		for(LineInfo line:allLines){
			
			map.put("linename", line.getLineName());
			map.put("start", line.getStart());
			map.put("end", line.getEnd());
			map.put("id", line.getId());
			list.add(map);
			map = new HashMap<String,Object>();
			
		}
		
		return list;
	}
	
	private void getLineDetails(final int id){
		
				//从缓存中加载
				oneLineAll = myapp.getBusMap(id);
				
				if (oneLineAll == null){
		
				//获取线路
				String lineDetails="http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=Line";
				String url = lineDetails + "&id=" + id;
				http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {		
						Log.d("lineDetails", responseInfo.result);
						
						//如果返回 “null”  没有内容返回；
						if (responseInfo.result == "null"){
							String msg = "no this line's details";   
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							return;
						}
						
						
//						List<BusLineInfo> allLines = new ArrayList<BusLineInfo>();
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
							Log.d("getLineInfo--Success", line.id+"Stopname:"+line.getStopName()+"--start:"+line.getTime()+"-----msg\n");
						}
						
						
						//缓存数据
						myapp.addBusMap(id, oneLineAll);
						
						if (myapp.getBusMap(id) == null){
							
							Toast.makeText(getApplicationContext(), "没有成功缓存啊   哈哈哈 ", Toast.LENGTH_SHORT).show();
							
						}else{
						
							String msg = "已将线路缓存，下次加载将不耗费流量......";   
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
						}
//						
						//跳转到地图页面显示本线路的相关信息；
						Intent myintent = new Intent(RouteActivity.this,RouteMapActivity.class);
						//type == route 表示是从XiaofangActivity跳过去的 
						myintent.putExtra("id", id);
						startActivity(myintent);
						
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.v("getLineDetail--error", error.toString()+"%%%%%%"+msg);
					}			
				});
		
			}
				
				
		 
	}
	
	
}
