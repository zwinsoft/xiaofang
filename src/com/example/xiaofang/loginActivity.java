package com.example.xiaofang;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class loginActivity extends Activity {

	EditText name;
	EditText pass;
	ImageButton login;
	Button tiao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		
		name = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);
		login = (ImageButton) findViewById(R.id.login);
		
		tiao = (Button) findViewById(R.id.intent);
		
		tiao.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myintent = new Intent(loginActivity.this,activity_front.class);				
				startActivity(myintent);
			}			
		});
		
	}

	
	
	
	
}
