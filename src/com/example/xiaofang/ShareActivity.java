package com.example.xiaofang;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;




public class ShareActivity extends Activity{
	TextView tv1;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_share);
		
		tv1 = (TextView) findViewById(R.id.share);
		
		
	}

	
	
}
