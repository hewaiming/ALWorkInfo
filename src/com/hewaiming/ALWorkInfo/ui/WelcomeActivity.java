package com.hewaiming.ALWorkInfo.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.config.ImageConfig;
import com.hewaiming.ALWorkInfo.config.ImageLoadOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	// private Animation animation;
	private Button okBtn;
	private ImageView mImage;
	private DisplayImageOptions options;
	private static final String image_url="http://125.64.59.11:8000/scgy/android/banner/face.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.welcome);
		okBtn=(Button) findViewById(R.id.btn_ok);
		mImage=(ImageView) findViewById(R.id.welcome);
		ImageConfig config=new ImageConfig(this);
		config.initImageLoader();
		options=new ImageLoadOptions().getOptions();
		ImageLoader.getInstance().displayImage(image_url, mImage,options);
		
		
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				
			}
		});

		(new Timer()).schedule(new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 3000);
	}

}
