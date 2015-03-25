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
import android.view.Window;
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




public class ShareActivity extends ListActivity{

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		myapp = (MyApplication) getApplicationContext();
		//从缓存中加载数据
		myapp.loadBusMap();
		final double lng = this.getIntent().getDoubleExtra("lng",0.1);
		final double lat = this.getIntent().getDoubleExtra("lat",0.1);
		
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
				Toast.makeText(getApplicationContext(),"正要去分享"+lng+"$$$"+lat, Toast.LENGTH_SHORT).show();
				shareMsg(id,lng,lat);
				
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
	
	private void shareMsg(int id,Double lng,Double lat){
		//分享实时位置；
		String shareshishi="http://www.zwin.mobi/banche/?m=AndroidApi&c=Index&a=SharePosition";
		String url = shareshishi + "&lineId="+id+"&lng="+lng+"&lat="+lat;
		
		http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {		
				Log.d("lineDetails", responseInfo.result);
				
				
				Toast.makeText(getApplicationContext(),responseInfo.result, Toast.LENGTH_SHORT).show();
				
				//如果返回 “null”  没有内容返回；
				if (responseInfo.result == "ok"){
					String msg = "share successed";   
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					return;
				}
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.v("share--error", error.toString()+"%%%%%%"+msg);
			}			
		});
		
		
		
		
	}
	
	
}