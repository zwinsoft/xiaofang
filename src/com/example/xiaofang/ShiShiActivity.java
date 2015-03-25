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
import com.example.xiaofang.util.RealTimeArea;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class ShiShiActivity extends ListActivity{

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
			//获取实时	
			getShiShi(id);

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
	
	private void getShiShi(final int id){
		
				
		//获取实时位置；
		String getshishi="http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=GetPosition";
		String url = getshishi+"&lineId="+id;
		
				http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {	
						
						Log.d("get 实时", responseInfo.result);
						
						//如果返回 “null” 或者 paramWrong没有内容返回；
						if ((responseInfo.result == "null")||(responseInfo.result == "paramWrongnull")){
							String msg = "本线路没有实时信息";   
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							return;
						}
						
						List<RealTimeArea> allArea = new ArrayList<RealTimeArea>();
						listType = (Type) new TypeToken<LinkedList<RealTimeArea>>(){}.getType();
						allArea = gson.fromJson(responseInfo.result,listType);
						
						//如果解析后是 “null”  没有内容返回；
						if (allArea == null){
							
							String msg = "没有实时信息";   
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							return;
						}
						
						myapp.setAllArea(allArea);
						
					if (myapp.getAllArea() != null){	
						//跳转到地图页面显示本线路的相关信息；
						Intent myintent = new Intent(ShiShiActivity.this,RouteMapActivity.class);
						//type == route 表示是从XiaofangActivity跳过去的 
						myintent.putExtra("id", -3);
						startActivity(myintent);				
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.v("getLineDetail--error", error.toString()+"%%%%%%"+msg);
					}			
				});
		 
	}
	
	
}
