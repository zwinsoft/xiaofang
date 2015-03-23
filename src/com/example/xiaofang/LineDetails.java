package com.example.xiaofang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.example.xiaofang.util.LineInfo;

/**
 * @author root
 *
 */
public class LineDetails extends ListActivity{


	MyApplication  myapp ;
	private List<LineInfo> allLines = new ArrayList<LineInfo>(); 
//	ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	
		
		myapp = (MyApplication) getApplicationContext();
		allLines = myapp.getAllLines();
		
		
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.linedetails,
									new String[]{"linename","start","end"},
									new int[]{R.id.linetitle,R.id.linestart,R.id.lineend});
		
		setListAdapter(adapter);
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	

}
