package com.example.xiaofang;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NearstStation extends Activity{
	TextView tv1;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_state);
		
		tv1 = (TextView) findViewById(R.id.state);
		
	}

}
