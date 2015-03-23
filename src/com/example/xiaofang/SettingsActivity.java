package com.example.xiaofang;

import com.example.xiaofang.util.FileServiceUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	Button btnSaveSetting;
	Button btnReturnMain;
	Button btnOfflineSetting;
	Button btnMywater;
	
	EditText txtAppUserName; //APP的用户名
	EditText txtAppPWD; //APP的密码
	EditText txtServerIP; //FTPserver的IP地址
	EditText txtServerPort;
	EditText txtServerUserName;
	EditText txtServerPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		txtAppUserName=(EditText) findViewById(R.id.editTextUsername);
		txtAppPWD=(EditText) findViewById(R.id.editPassword);
		
		txtServerIP=(EditText) findViewById(R.id.editTextFTPServerIP);
		txtServerPort=(EditText) findViewById(R.id.editTextFTPServerPort);
		txtServerUserName=(EditText) findViewById(R.id.editTextFTPUserName);
		txtServerPassword=(EditText) findViewById(R.id.editTextFTPPassword);
		
		btnSaveSetting= (Button) findViewById(R.id.btnSaveSetting);
		//保存数据按钮点击事件
		btnSaveSetting.setOnClickListener(new Button.OnClickListener()
	    {
	      @Override
	      public void onClick(View v)
	      {
	    	  FileServiceUtil myservice=new FileServiceUtil(SettingsActivity.this);
	    	  myservice.deleteFileFromSDCard("config.ini");
	    	  String strtosave=
	    			  			txtAppUserName.getText().toString()+"-"+
	    					  	txtAppPWD.getText().toString()+"-"+
	    			  			txtServerIP.getText().toString()+"-"+
	    	  					txtServerPort.getText().toString()+"-"+
	    	  					txtServerUserName.getText().toString()+"-"+
	    	  					txtServerPassword.getText().toString()+"";
	    	  myservice.saveToSdcard("config.ini", strtosave);
	    	  
	    	  new AlertDialog.Builder(SettingsActivity.this) 
    		  .setTitle("提示信息") 
    		  .setMessage("保存配置成功")
              .setPositiveButton("确定", 
                             new DialogInterface.OnClickListener(){ 
                                     public void onClick(DialogInterface dialoginterface, int i){ 
                                         //按钮事件 
                                    	 dialoginterface.dismiss();
                                    	 
                                      } 
                              }) 
              .show();
	      }
	    });
		FileServiceUtil myservice=new FileServiceUtil(SettingsActivity.this);
		String strTmp=myservice.ReadTxtFile("config.ini");
		
		
		if(strTmp.length() == 0)
		{
			try {
		        Toast.makeText(getApplicationContext(),"参数为空",Toast.LENGTH_SHORT).show();
		    }
		    catch(Exception e) {
		        Toast.makeText(getApplicationContext(),"出问题了",Toast.LENGTH_SHORT).show();
		    }
		}
		else{
		
			String[] arrays=strTmp.split("-");
			
			if(arrays.length==3) {
				
				 txtServerIP.setText(arrays[0]);
				 txtServerPort.setText(arrays[1]);
				 txtServerUserName.setText(arrays[2]);
				
			}
			else if(arrays.length==4) {
				 txtServerIP.setText(arrays[0]);
				 txtServerPort.setText(arrays[1]);
				 txtServerUserName.setText(arrays[2]);
				 txtServerPassword.setText(arrays[3]);
			}
			else if(arrays.length==6){
			   
	            txtAppUserName.setText(arrays[0]);
	            txtAppPWD.setText(arrays[1]);
	            txtServerIP.setText(arrays[2]);
	            txtServerPort.setText(arrays[3]);
	            txtServerUserName.setText(arrays[4]);
	            txtServerPassword.setText(arrays[5]);
			}
			else {
			    try {
			        Toast.makeText(getApplicationContext(), arrays.length,Toast.LENGTH_SHORT).show();
			    }
			    catch(Exception e) {
			        Toast.makeText(getApplicationContext(),"出问题了",Toast.LENGTH_SHORT).show();
			    }
			}
			
		}
		
		//返回按钮点击事件
		btnReturnMain= (Button) findViewById(R.id.btnReturnToMain);		
		btnReturnMain.setOnClickListener(new Button.OnClickListener()
		    {
		      @Override
		      public void onClick(View v)
		      {
		    	  Intent mainIntent = new Intent(SettingsActivity.this, XiaofangActivity.class); 
		    	  startActivity(mainIntent); 
		    	  finish(); 
		      }
		    });
		 
		//查看我的水源按钮点击事件
		btnMywater= (Button) findViewById(R.id.btnMywater);		
		btnMywater.setOnClickListener(new Button.OnClickListener()
		    {
		      @Override
		      public void onClick(View v)
		      {
		    	  Intent mainIntent = new Intent(SettingsActivity.this, XiaofangActivity.class); 
		    	  startActivity(mainIntent); 
		    	  finish(); 
		      }
		    });
		 
		//离线地图按钮点击事件
		 btnOfflineSetting= (Button) findViewById(R.id.btnOffline);		 
		 btnOfflineSetting.setOnClickListener(new Button.OnClickListener()
		 {
		      @Override
		      public void onClick(View v)
		      {
		    	  	    	  
		    	  Intent mainIntent = new Intent(SettingsActivity.this, OfflineDemo.class); 
  				  startActivity(mainIntent); 
		    	  
		      }
		 });
		 
	}

	

}
