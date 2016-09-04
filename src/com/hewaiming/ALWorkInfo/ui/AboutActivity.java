package com.hewaiming.ALWorkInfo.ui;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.config.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Activity {
	private Button btnCheck,btnFinish;	

	private String TAG="=Setting=";
	private TextView tv_title;
	private Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_about);
		MyApplication.getInstance().addActivity(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("¹ØÓÚ");
		btnCheck = (Button) findViewById(R.id.btn_check_ver);		
		btnFinish=(Button) findViewById(R.id.btn_back);
		btnFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});

		btnCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
}
