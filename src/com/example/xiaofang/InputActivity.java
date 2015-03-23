package com.example.xiaofang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import com.baidu.mapapi.model.LatLng;
import com.example.xiaofang.util.DatabaseHelper;
import com.example.xiaofang.util.DatabaseManager;
import com.example.xiaofang.util.FileServiceUtil;
import com.example.xiaofang.util.MyDict;
import com.example.xiaofang.util.WaterUtil;

import android.R.bool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InputActivity extends Activity {

	Button btnSave;
	Button btnReturn;
	Button btnSelectFile;
	Button btnSync;
	ImageView imgAddPhoto;
	
	EditText txtSourceName; 
	EditText txtLatitude; 
	EditText txtLongtitude; 
	EditText editTextContact;
	EditText editTextContactTel; 
	TextView txtPhotoFileName; 
	
	Spinner spinSourceType;
	
	private List<MyDict> spinSourceTypelist = new ArrayList<MyDict>();    
	ArrayAdapter spinSourceTypelistadapter;
	Spinner spinnerSourceDescription;
	private List<MyDict> spinSourceDescriptionlist = new ArrayList<MyDict>();   
	ArrayAdapter spinnerSourceDescriptionadapter;
	Spinner spinnerSourcePressure;
	private List<MyDict> spinnerSourcePressurelist = new ArrayList<MyDict>();   
	ArrayAdapter spinnerSourcePressureadapter;
	
	Spinner spinnerSourceDiameter;
	private List<MyDict> SpinnerSourceDiameterlist = new ArrayList<MyDict>();   
	ArrayAdapter SpinnerSourceDiameteradapter;
	
	
	EditText editTextUpdateDate;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		
		//为了解决网络异常
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
        .detectDiskReads()       
        .detectDiskWrites()       
        .detectNetwork()   // or .detectAll() for all detectable problems       
        .penaltyLog()       
        .build());       
         StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
                .detectLeakedSqlLiteObjects()    
                .penaltyLog()       
                .penaltyDeath()       
                .build());  
         
         
		btnSave = (Button) findViewById(R.id.btnUpload);

		
		Intent intent1 = getIntent();
        
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
		String strLongtitude = intent1.getStringExtra("Longtitude");
        String strLatitude = intent1.getStringExtra("Latitude");  
        
        
        txtLongtitude=(EditText)findViewById(R.id.editTextLontitude);        
        txtLongtitude.setText(strLongtitude);
        
        txtLatitude=(EditText)findViewById(R.id.editTextLatitude);
        txtLatitude.setText(strLatitude);
       
        txtSourceName = (EditText) findViewById(R.id.editTextSourceName);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        editTextUpdateDate= (EditText) findViewById(R.id.editTextUpdateDate);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        editTextUpdateDate.setText(sdf.format(new Date()));
        txtPhotoFileName=(TextView)findViewById(R.id.SelectPictureFileName); 
        editTextContactTel=(EditText)findViewById(R.id.editTextContactTel); 

		      
        
        imgAddPhoto=(ImageView) findViewById(R.id.imageViewImageFile);
        imgAddPhoto.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // TODO Auto-generated method stub
                        	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                			startActivityForResult(intent, 1);
                			
                            //Toast toast = Toast.makeText(getApplicationContext(), "I am Clicked", Toast.LENGTH_LONG);//提示被点击了
                            //toast.show();
                        }
                });
        
        
        
		btnReturn = (Button) findViewById(R.id.btnReturn);		
		
		//返回地图按钮点击事件
		btnReturn.setOnClickListener(new Button.OnClickListener()
	    {
	      @Override
	      public void onClick(View v)
	      {
	    	  Intent mainIntent = new Intent(InputActivity.this, XiaofangActivity.class); 
	    	  startActivity(mainIntent); 
	    	  finish(); 
	      }
	    });
		
		btnSave= (Button) findViewById(R.id.btnUpload);
		//保存数据按钮点击事件
		btnSave.setOnClickListener(new Button.OnClickListener()
	    {
	      @Override
	      public void onClick(View v)
	      {
	    	  //=========开始保存数据==========
	    	  DatabaseHelper mydbhelper=new DatabaseHelper(InputActivity.this);
	    	  DatabaseManager mydb=new DatabaseManager(mydbhelper);
	    	  //操作员编号
	    	  int userid=0;
	    	  //水源名称
	    	  String strtitle=txtSourceName.getText().toString();
	    	  //水源类型
	    	  int itype=((MyDict)spinSourceType.getSelectedItem()).getId();
	    	  String strType= ((MyDict)spinSourceType.getSelectedItem()).getText();
	    	  //水源情况
	    	  int istatus=((MyDict)spinnerSourceDescription.getSelectedItem()).getId();
	    	  String strStatus= ((MyDict)spinnerSourceDescription.getSelectedItem()).getText();
	    	  //水压
	    	  int ipressure=((MyDict)spinnerSourcePressure.getSelectedItem()).getId();
	    	  String strPressure= ((MyDict)spinnerSourcePressure.getSelectedItem()).getText();
	    	  
	    	  //管径
	    	  int icalibre=((MyDict)spinnerSourceDiameter.getSelectedItem()).getId();
	    	  String strCalibre= ((MyDict)spinnerSourceDiameter.getSelectedItem()).getText();
	    	  
	    	  //联系人
	    	  String strcontacts=editTextContact.getText().toString();
	    	  //联系电话
	    	  String strphone=editTextContactTel.getText().toString();
	    	  //更新日期
	    	  String strdate=editTextUpdateDate.getText().toString();
	    	  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    	 
	    	  try {
				Date date = sdf.parse(strdate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//提取格式中的日期
	    	  
	    	  //经度
	    	  String strLatitude=txtLatitude.getText().toString();
	    	  //纬度
	    	  String strLongtitude=txtLongtitude.getText().toString();
	    	  //图片名字
	    	  String strphotofilename=txtPhotoFileName.getText().toString();
	    	  String strResult="";
	    	  strResult=mydb.saveWaterResult(userid, strtitle, strType, strStatus, strPressure, strCalibre, strcontacts, strphone, strdate, 
	    			   strLongtitude,strLatitude, strphotofilename);
	    	  if("".equals(strResult))
	    	  {
	    		  new AlertDialog.Builder(InputActivity.this) 
	    		  .setTitle("提示信息") 
	    		  .setMessage("保存水源信息成功")
	              .setPositiveButton("确定", 
	                             new DialogInterface.OnClickListener(){ 
	                                     public void onClick(DialogInterface dialoginterface, int i){ 
	                                         //按钮事件 
	                                    	 dialoginterface.dismiss();
	                                    	 
	                                      } 
	                              }) 
	              .show();
	    	  }
	    	  else
	    	  {
	    		  new AlertDialog.Builder(InputActivity.this)
	    		  .setTitle("提示信息") 
	    		  .setMessage("保存水源信息失败")
	    		  	.setPositiveButton("确定", null)
	    		  	.show();
	    	  }
	    	  
	    	  
	    	 
	      }
	    });
		 //=========保存数据结束==========
	
		
		//上传数据按钮点击事件
		btnSync= (Button) findViewById(R.id.buttonSync);
		
		btnSync.setOnClickListener(new Button.OnClickListener()
	    {
	      @Override
	      public void onClick(View v)
	      {
	    	  //=========上传数据==========
	    	  DatabaseHelper mydbhelper=new DatabaseHelper(InputActivity.this);
	    	  DatabaseManager mydb=new DatabaseManager(mydbhelper);
	    	  
	    	  WaterUtil water =new WaterUtil();
	    	  //操作员编号
	    	  int userid=0;
	    	  //水源名称
	    	  String strtitle=txtSourceName.getText().toString();
	    	  //水源类型
	    	  int itype=((MyDict)spinSourceType.getSelectedItem()).getId();
	    	  String strType= ((MyDict)spinSourceType.getSelectedItem()).getText();
	    	  //水源情况
	    	  int istatus=((MyDict)spinnerSourceDescription.getSelectedItem()).getId();
	    	  String strStatus= ((MyDict)spinnerSourceDescription.getSelectedItem()).getText();
	    	  //水压
	    	  int ipressure=((MyDict)spinnerSourcePressure.getSelectedItem()).getId();
	    	  String strPressure= ((MyDict)spinnerSourcePressure.getSelectedItem()).getText();
	    	  
	    	  //管径
	    	  int icalibre=((MyDict)spinnerSourceDiameter.getSelectedItem()).getId();
	    	  String strCalibre= ((MyDict)spinnerSourceDiameter.getSelectedItem()).getText();
	    	  //联系人
	    	  String strcontacts=editTextContact.getText().toString();
	    	  //联系电话
	    	  String strphone=editTextContactTel.getText().toString();
	    	  //更新日期
	    	  String strdate=editTextUpdateDate.getText().toString();
	    	  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    	 
	    	  try {
				Date date = sdf.parse(strdate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//提取格式中的日期
	    	  
	    	  //纬度
	    	  String strLatitude=txtLatitude.getText().toString();
	    	  //经度
	    	  String strLongtitude=txtLongtitude.getText().toString();
	    	  //图片名字
	    	  String strphotofilename=txtPhotoFileName.getText().toString();
	    	  //判断网络连接是否有效
	    	  if(isConnectInternet())
	    	  {
	    		  
	    	 
	    	  //上传文件
	    	  /** 
	    		 * 通过ftp上传文件 
	    		 * @param url ftp服务器地址 如： 192.168.1.110 
	    		 * @param port 端口如 ： 21 
	    		 * @param username  登录名 
	    		 * @param password   密码 
	    		 * @param remotePath  上到ftp服务器的磁盘路径 
	    		 * @param fileNamePath  要上传的文件路径 
	    		 * @param fileName      要上传的文件名 
	    		 * @return 
	    		 */ 
	    		//public String ftpUpload(String url, String port, String username,String password, String remotePath, String fileNamePath,String fileName) ;
	    	  	MyApplication  myapp = (MyApplication) getApplicationContext();	
	    	  	String ftpurl=myapp.getFtpServerIP().toString();
	    	  	String port=myapp.getFTPPort().toString();
	    	  	String username=myapp.getFtpUserName();
	    	  	String password=myapp.getFtpPassword();
	    	  	String remotePath="/";
	    	  	String fileNamePath=Environment.getExternalStorageDirectory().getPath()+"//xiaofang//Images/";
	    	  	String fileName=strphotofilename;
	    	  	try
	    	  	{
	    		String strResult=ftpUpload(ftpurl, port, username,password, remotePath, fileNamePath,fileName) ;
	    		 if("1".equals(strResult))
		    	  {
	    			 //删除已上传的文件
	    			 File myfile=new File(fileNamePath+File.separator+fileName);
	    			 myfile.delete();
	    			
		    		  new AlertDialog.Builder(InputActivity.this) 
		    		  .setTitle("提示信息") 
		    		  .setMessage("上传文件成功")
		              .setPositiveButton("确定", 
		                             new DialogInterface.OnClickListener(){ 
		                                     public void onClick(DialogInterface dialoginterface, int i){ 
		                                         //按钮事件 
		                                    	 dialoginterface.dismiss();
		                                    	 
		                                      } 
		                              }) 
		              .show();
		    	  }
		    	  else
		    	  {
		    		  new AlertDialog.Builder(InputActivity.this)
		    		  .setTitle("提示信息") 
		    		  .setMessage("上传文件失败")
		    		  	.setPositiveButton("确定", null)
		    		  	.show();
		    	  }
	    	  	}
	    	  	catch(Exception ee)
	    	  	{
	    	  		 new AlertDialog.Builder(InputActivity.this)
		    		  .setTitle("提示信息") 
		    		  .setMessage("上传文件失败")
		    		  	.setPositiveButton("确定", null)
		    		  	.show();
	    	  	}
	    	  }
	    	  else
	    	  {
	    		  new AlertDialog.Builder(InputActivity.this)
	    		  .setTitle("提示信息") 
	    		  .setMessage("网络连接错误，不能上传文件")
	    		  	.setPositiveButton("确定", null)
	    		  	.show();
	    	  }
	    	  boolean   bResult = false;	    
	    	  bResult=mydb.syncWater(userid, strtitle, strType, strStatus, strPressure, strCalibre, strcontacts, strphone, strdate, 
	    			   strLongtitude,strLatitude, strphotofilename);
	    	  
	    	  
	    	  if(bResult)
	    	  {
	    		  new AlertDialog.Builder(InputActivity.this) 
	    		  .setTitle("提示信息") 
	    		  .setMessage("上传水源信息成功")
	              .setPositiveButton("确定", 
	                             new DialogInterface.OnClickListener(){ 
	                                     public void onClick(DialogInterface dialoginterface, int i){ 
	                                         //按钮事件 
	                                    	 dialoginterface.dismiss();
	                                    	 
	                                      } 
	                              }) 
	              .show();
	    	  }
	    	  else
	    	  {
	    		  new AlertDialog.Builder(InputActivity.this)
	    		  .setTitle("提示信息") 
	    		  .setMessage("上传水源信息失败")
	    		  	.setPositiveButton("确定", null)
	    		  	.show();
	    	  }
	    	  
	    	  
	    	  //=========上传数据结束==========
	      }
	    });
		
		//下拉列表
		spinSourceType=(Spinner)findViewById(R.id.spinnerSourceType);
		spinSourceTypelist.clear();
		spinSourceTypelist.add(new MyDict(1,"地上式"));
		spinSourceTypelist.add(new MyDict(2,"地下式"));
		spinSourceTypelist.add(new MyDict(3,"室内消火栓"));
		spinSourceTypelist.add(new MyDict(4,"消防水鹤"));
		spinSourceTypelist.add(new MyDict(5,"消防水池"));
		spinSourceTypelist.add(new MyDict(6,"天然水源"));
		
		 //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
		spinSourceTypelistadapter = new ArrayAdapter<MyDict>(this,android.R.layout.simple_spinner_item, spinSourceTypelist);    
		//第三步：为适配器设置下拉列表下拉时的菜单样式。    
		spinSourceTypelistadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		//第四步：将适配器添加到下拉列表上    
		spinSourceType.setAdapter(spinSourceTypelistadapter);    
		
		//第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
		spinSourceType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
		                
				/* 将所选mySpinner 的值带入myTextView 中*/    
					//editTextContact.setText("您选择的是："+ spinSourceTypelistadapter.getItem(arg2));    
						/* 将mySpinner 显示*/    
						//arg0.setVisibility(View.VISIBLE);    
						 // 获取键的方法：mySpinner.getSelectedItem().toString()或((Dict)mySpinner.getSelectedItem()).getId()
		                // 获取值的方法：((Dict)mySpinner.getSelectedItem()).getText();
		               // Toast.makeText(InputActivity.this,
		               //         "键:" + spinSourceType.getSelectedItem().toString() + "、" + ((MyDict) spinSourceType.getSelectedItem()).getId() + 
		               //         "，值:" + ((MyDict) spinSourceType.getSelectedItem()).getText(),
		                //        Toast.LENGTH_LONG).show();

					}    
		public void onNothingSelected(AdapterView<?> arg0) {    
		               txtSourceName.setText("NONE");  
		                arg0.setVisibility(View.VISIBLE);    
		            }    
		        });    
		/*下拉菜单弹出的内容选项触屏事件处理*/    
		spinSourceType.setOnTouchListener(new Spinner.OnTouchListener(){    
		            public boolean onTouch(View v, MotionEvent event) {    
		                // TODO Auto-generated method stub    
		              
		                return false;    
		            }

					
		        });    
		/*下拉菜单弹出的内容选项焦点改变事件处理*/    
		spinSourceType.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
		        public void onFocusChange(View v, boolean hasFocus) {    
		            // TODO Auto-generated method stub    
		  
		       }    
		       });    
		       

	
	//下拉列表
	spinnerSourceDescription=(Spinner)findViewById(R.id.spinnerSourceDescription);
	spinSourceDescriptionlist.clear();
	spinSourceDescriptionlist.add(new MyDict(1,"全天有水"));
	spinSourceDescriptionlist.add(new MyDict(2,"非全天有水"));
	spinSourceDescriptionlist.add(new MyDict(3,"损坏"));
	
	//第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
	spinnerSourceDescriptionadapter = new ArrayAdapter<MyDict>(this,android.R.layout.simple_spinner_item, spinSourceDescriptionlist);    
	//第三步：为适配器设置下拉列表下拉时的菜单样式。    
	spinnerSourceDescriptionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	//第四步：将适配器添加到下拉列表上    
	spinnerSourceDescription.setAdapter(spinnerSourceDescriptionadapter);  
	
	//第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
	spinnerSourceDescription.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
	                
			// 将所选mySpinner 的值带入myTextView 中   
				//editTextContact.setText("您选择的是："+ spinnerSourceDescriptionadapter.getItem(arg2));    
					// 将mySpinner 显示   
					//arg0.setVisibility(View.VISIBLE);    
					 // 获取键的方法：mySpinner.getSelectedItem().toString()或((Dict)mySpinner.getSelectedItem()).getId()
	                // 获取值的方法：((Dict)mySpinner.getSelectedItem()).getText();
	                //Toast.makeText(InputActivity.this,
	                //        "键:" + spinnerSourceDescription.getSelectedItem().toString() + "、" + ((MyDict) spinnerSourceDescription.getSelectedItem()).getId() + 
	                //        "，值:" + ((MyDict) spinnerSourceDescription.getSelectedItem()).getText(),
	                 //       Toast.LENGTH_LONG).show();

				}    
	public void onNothingSelected(AdapterView<?> arg0) {    
	               //txtSourceName.setText("NONE");  
	                arg0.setVisibility(View.VISIBLE);    
	            }    
	        });    
	//下拉菜单弹出的内容选项触屏事件处理   
	spinnerSourceDescription.setOnTouchListener(new Spinner.OnTouchListener(){    
	            public boolean onTouch(View v, MotionEvent event) {    
	                // TODO Auto-generated method stub    
	               
	                return false;    
	            }

				
	        });    
	//下拉菜单弹出的内容选项焦点改变事件处理   
	spinnerSourceDescription.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
	        public void onFocusChange(View v, boolean hasFocus) {    
	            // TODO Auto-generated method stub    
	  
	       }    
	  });    
		
	
	//下拉列表
	spinnerSourcePressure=(Spinner)findViewById(R.id.spinnerSourcePressure);
	spinnerSourcePressurelist.clear();
	spinnerSourcePressurelist.add(new MyDict(1,"正常"));
	spinnerSourcePressurelist.add(new MyDict(2,"水压低"));
	
	
	 //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
	spinnerSourcePressureadapter = new ArrayAdapter<MyDict>(this,android.R.layout.simple_spinner_item, spinnerSourcePressurelist);    
	//第三步：为适配器设置下拉列表下拉时的菜单样式。    
	spinnerSourcePressureadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
	
	//第四步：将适配器添加到下拉列表上    
	spinnerSourcePressure.setAdapter(spinnerSourcePressureadapter);    
	
	//第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
	spinnerSourcePressure.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
	                
			// 将所选mySpinner 的值带入myTextView 中   
				//editTextContact.setText("您选择的是："+ spinnerSourcePressureadapter.getItem(arg2));    
					// 将mySpinner 显示   
					//arg0.setVisibility(View.VISIBLE);    
					 // 获取键的方法：mySpinner.getSelectedItem().toString()或((Dict)mySpinner.getSelectedItem()).getId()
	                // 获取值的方法：((Dict)mySpinner.getSelectedItem()).getText();
	                //Toast.makeText(InputActivity.this,
	                //        "键:" + spinnerSourcePressure.getSelectedItem().toString() + "、" + ((MyDict) spinnerSourcePressure.getSelectedItem()).getId() + 
	                //        "，值:" + ((MyDict) spinnerSourcePressure.getSelectedItem()).getText(),
	                //        Toast.LENGTH_LONG).show();
				}    
	public void onNothingSelected(AdapterView<?> arg0) {    
	              // txtSourceName.setText("NONE");  
	                arg0.setVisibility(View.VISIBLE);    
	            }    
	        });    
	//下拉菜单弹出的内容选项触屏事件处理    
	spinnerSourcePressure.setOnTouchListener(new Spinner.OnTouchListener(){    
	            public boolean onTouch(View v, MotionEvent event) {    
	                // TODO Auto-generated method stub    
	               
	                return false;    
	            }

				
	        });    
	//下拉菜单弹出的内容选项焦点改变事件处理    
	spinnerSourcePressure.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
	        public void onFocusChange(View v, boolean hasFocus) {    
	            // TODO Auto-generated method stub    
	  
	       }    
	       });   
	
	//下拉列表
	spinnerSourceDiameter=(Spinner)findViewById(R.id.SpinnerSourceDiameter);
	SpinnerSourceDiameterlist.clear();
	SpinnerSourceDiameterlist.add(new MyDict(1,"65mm"));
	SpinnerSourceDiameterlist.add(new MyDict(2,"100mm"));
	
	//第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
	SpinnerSourceDiameteradapter = new ArrayAdapter<MyDict>(this,android.R.layout.simple_spinner_item, SpinnerSourceDiameterlist);    
	//第三步：为适配器设置下拉列表下拉时的菜单样式。    
	SpinnerSourceDiameteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	//第四步：将适配器添加到下拉列表上    
	spinnerSourceDiameter.setAdapter(SpinnerSourceDiameteradapter);  
	
	//第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
	spinnerSourceDiameter.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
	                
			// 将所选mySpinner 的值带入myTextView 中   
				//editTextContact.setText("您选择的是："+ SpinnerSourceDiameteradapter.getItem(arg2));    
					// 将mySpinner 显示   
					//arg0.setVisibility(View.VISIBLE);    
					 // 获取键的方法：mySpinner.getSelectedItem().toString()或((Dict)mySpinner.getSelectedItem()).getId()
	                // 获取值的方法：((Dict)mySpinner.getSelectedItem()).getText();
	                //Toast.makeText(InputActivity.this,
	                //        "键:" + spinnerSourceDescription.getSelectedItem().toString() + "、" + ((MyDict) spinnerSourceDescription.getSelectedItem()).getId() + 
	                //        "，值:" + ((MyDict) spinnerSourceDescription.getSelectedItem()).getText(),
	                 //       Toast.LENGTH_LONG).show();

				}    
	public void onNothingSelected(AdapterView<?> arg0) {    
	              // txtSourceName.setText("NONE");  
	                arg0.setVisibility(View.VISIBLE);    
	            }    
	        });    
	//下拉菜单弹出的内容选项触屏事件处理   
	spinnerSourceDiameter.setOnTouchListener(new Spinner.OnTouchListener(){    
	            public boolean onTouch(View v, MotionEvent event) {    
	                // TODO Auto-generated method stub    
	               
	                return false;    
	            }

				
	        });    
	//下拉菜单弹出的内容选项焦点改变事件处理   
	spinnerSourceDiameter.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){    
	        public void onFocusChange(View v, boolean hasFocus) {    
	            // TODO Auto-generated method stub    
	  
	       }    
	  });
	 
	 
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode==Activity.RESULT_OK) {//照相机OK

			String sdStatus = Environment.getExternalStorageState();

			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用

				Log.v("SystemInfo","SD card is not avaiable/writeable right now.");

				return;

			}
			
			
			Bundle bundle = data.getExtras();

			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

			FileOutputStream b = null;

			File file = new File(Environment.getExternalStorageDirectory().getPath()
					+"//xiaofang//Images");

			file.mkdirs();// 创建文件夹
			SimpleDateFormat sdf3=new SimpleDateFormat("yyyyMMddHHmmss");
			//文件名
			String fileName = Environment.getExternalStorageDirectory().getPath().toString()+"//xiaofang//Images//"+sdf3.format(new Date())+".jpg";
			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			try {

				b.flush();

				b.close();

				} catch (IOException e) {

					e.printStackTrace();

				}
				txtPhotoFileName.setText(sdf3.format(new Date())+".jpg");
				((ImageView) findViewById(R.id.imageViewImageFile)).setImageBitmap(bitmap);// 将图片显示在ImageView里
		}//照相机OK
}//拍照回调函数结束


	/** 
	 * 通过ftp上传文件 
	 * @param url ftp服务器地址 如： 192.168.1.110 
	 * @param port 端口如 ： 21 
	 * @param username  登录名 
	 * @param password   密码 
	 * @param remotePath  上到ftp服务器的磁盘路径 
	 * @param fileNamePath  要上传的文件路径 
	 * @param fileName      要上传的文件名 
	 * @return 
	 */ 
	public String ftpUpload(String url, String port, String username,String password, String remotePath, String fileNamePath,String fileName) {  
	 FTPClient ftpClient = new FTPClient();  
	 FileInputStream fis = null;  
	 String returnMessage = "0";  
	 try {  
	     ftpClient.connect(url, Integer.parseInt(port));  
	     boolean loginResult = ftpClient.login(username, password);  
	     int returnCode = ftpClient.getReplyCode();  
	     if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功  
	         ftpClient.makeDirectory(remotePath);  
	         // 设置上传目录  
	         ftpClient.changeWorkingDirectory(remotePath);  
	         ftpClient.setBufferSize(1024);  
	         ftpClient.enterLocalPassiveMode();  
	          fis = new FileInputStream(fileNamePath + fileName);
	          ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	         ftpClient.storeFile(fileName, fis);  
	        
	         
	         returnMessage = "1";   //上传成功        
	     } else {// 如果登录失败  
	         returnMessage = "0";  
	         }  
	                
	   
	 } catch (IOException e) {  
	     e.printStackTrace();  
	     throw new RuntimeException("FTP客户端出错！", e);  
	 } finally {  
	     
	 try {  
	     ftpClient.disconnect();  
	 } catch (IOException e) {  
	        e.printStackTrace();  
	        throw new RuntimeException("关闭FTP连接发生异常！", e);  
	    }  
	 }  
	 return returnMessage;  
	} 
	/**

     * 判断网络状态是否可用

     * @return true: 网络可用 ; false: 网络不可用

     */

 public boolean isConnectInternet() {

         ConnectivityManager conManager=(ConnectivityManager)InputActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE );

         NetworkInfo networkInfo = conManager.getActiveNetworkInfo(); 
         
         if (networkInfo == null || !networkInfo.isConnected()) {

          return false;

         }
         if (networkInfo.isConnected()){

                   return true;

         }

         return false ;

 }
}
