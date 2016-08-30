package com.hewaiming.ALWorkInfo.ui;

import com.hewaiming.ALWorkInfo.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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

public class SettingActivity extends Activity {
	private Button btnsave, btnFinish;
	private EditText edtip;
	private EditText edtport;
	SharedPreferences sp;
	private String TAG = "=Setting=";
	private TextView tv_title;
	private Context ctx;
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ctx=this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("Զ�̷���������");

		btnsave = (Button) findViewById(R.id.btn_save);
		edtip = (EditText) findViewById(R.id.et_IP);
		edtport = (EditText) findViewById(R.id.et_PORT);		
		sp = this.getSharedPreferences("SP", MODE_PRIVATE);
		if(sp!=null){
			edtip.setText(sp.getString("ipstr", "125.64.59.11"));
			edtport.setText(sp.getString("port", "1234"));
		}		
		btnFinish = (Button) findViewById(R.id.btn_back);
		btnFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		btnsave.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				Log.i(TAG, "��ʼ�޸�");
				String ip = edtip.getText().toString();//
				String port = edtport.getText().toString();//
				Editor editor = sp.edit();
				editor.putString("ipstr", ip);
				editor.putString("port", port);
				if (editor.commit()) {
					Toast.makeText(getApplicationContext(), "Զ�̷��������óɹ�,����������", 1).show();
					
					Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);    //��һ����������
					
					/*ActivityManager am = (ActivityManager) SettingActivity.this.getSystemService(ACTIVITY_SERVICE);
					if( android.os.Build.VERSION.SDK_INT < 8){
					    am.restartPackage(getBaseContext().getPackageName());
					}else{
					    am.killBackgroundProcesses(getBaseContext().getPackageName()); //�ڶ�����������
					}	*/				
				} else {
					Toast.makeText(getApplicationContext(), "Զ�̷���������ʧ��", 1).show();
				}
				Log.i(TAG, "����ɹ�?" + sp.getString("ipstr", "") + ";" + sp.getString("port", ""));

			}
		});
	}

	public void initdate() {		
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		if(sp!=null){
			ip = sp.getString("ipstr", ip);
			port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
		}
	
		// MyLog.i(TAG, "��ȡ��ip�˿�:" + ip + ";" + port);
	}
}
