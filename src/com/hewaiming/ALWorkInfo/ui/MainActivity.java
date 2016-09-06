package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetJXRecord_Listener;
import com.hewaiming.ALWorkInfo.Popup.ActionItem;
import com.hewaiming.ALWorkInfo.Popup.TitlePopup;
import com.hewaiming.ALWorkInfo.banner.SlideShowView;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpGetData_JXRecord;
import com.hewaiming.ALWorkInfo.net.HttpGetData_date;
import com.hewaiming.ALWorkInfo.net.NetDetector;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
		implements OnItemClickListener, OnClickListener, HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private String ip;
	private int port;
	private SharedPreferences sp;
	private GridView gridView;
	private Button btnMore;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;
	private List<String> date_record = null; // ��¼����
	private List<String> date_table = null; // ��������
	private List<Map<String, Object>> JXList = null; // ��¼����

	private String get_dateTable_url = ":8000/scgy/android/odbcPhP/getDate.php";
	private String get_JXName_url = ":8000/scgy/android/odbcPhP/getJXRecordName.php";
	private HttpGetData_date mhttpgetdata_date;
	private HttpGetData_JXRecord mHttpGetData_JXRecord;
	private Context mContext;
	private TitlePopup titlePopup;
	private TextView tv_title;
	private ImageView iv_wifi;
	private SlideShowView bannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		init();
		NetDetector netDetector = new NetDetector(mContext);
		if (netDetector.isConnectingToInternetNoShow() == 1) {
			iv_wifi.setVisibility(View.GONE);
			bannerView.setVisibility(View.VISIBLE);
		}else{
			iv_wifi.setVisibility(View.VISIBLE);
			bannerView.setVisibility(View.GONE);			
		}
		if (NetStatus() != 0) {
			if (!initdate(mContext)) { // ȡԶ�̷�������ַ�Ͷ˿�	
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);  
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 				
				startActivity(intent);// û������Զ�̷�����ip�Ͷ˿�
				
			} else {
				get_dateTable_url = "http://" + ip + get_dateTable_url;
				get_JXName_url = "http://" + ip + get_JXName_url;
				init_commData();
			}

		} else {
			Toast.makeText(getApplicationContext(), "�����쳣��", Toast.LENGTH_LONG).show();
		}
	}

	private int NetStatus() {
		NetDetector netDetector = new NetDetector(mContext);
		return netDetector.isConnectingToInternet();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog_exit();			
			return true;
		}
		return true;
	}

	protected void dialog_exit() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("ȷ��Ҫ�˳���?");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MyApplication.getInstance().exit();							
				
			}
		});
		builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void init_commData() {		
		if (JXList == null) {
			mHttpGetData_JXRecord = (HttpGetData_JXRecord) new HttpGetData_JXRecord(get_JXName_url, this, this)
					.execute();
		}
		if (date_table == null) {
			mhttpgetdata_date = (HttpGetData_date) new HttpGetData_date(get_dateTable_url, this, this).execute();
		}
		
	}

	private void init() {
		gridView = (GridView) findViewById(R.id.gridView);
		dataList = new ArrayList<Map<String, Object>>();
		adapter = new SimpleAdapter(this, getData(), R.layout.item_action, new String[] { "pic", "name" },
				new int[] { R.id.pic, R.id.name });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Popup_initData();
		btnMore = (Button) findViewById(R.id.btn_more);
		btnMore.setVisibility(View.VISIBLE);
		btnMore.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_wifi=(ImageView) findViewById(R.id.iv_NoWiFi);
		bannerView=(com.hewaiming.ALWorkInfo.banner.SlideShowView)findViewById(R.id.bannerwView);

	}

	private void Popup_initData() {
		// �������������������
		titlePopup.addAction(new ActionItem(this, "����Զ�̷�����", R.drawable.settings));
		titlePopup.addAction(new ActionItem(this, "����", R.drawable.mm_title_btn_keyboard_normal));
		//titlePopup.addAction(new ActionItem(this, "ɨһɨ", R.drawable.mm_title_btn_qrcode_normal));
	}

	private List<Map<String, Object>> getData() {

		for (int i = 0; i < MyConst.drawable.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", MyConst.drawable[i]);
			map.put("name", MyConst.iconName[i]);
			dataList.add(map);
		}
		Log.i("Main", "size=" + dataList.size());
		return dataList;
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		boolean DoRun=false;
		if(position==8 || position==15){
			DoRun=true;
		}
		if ((JXList != null && date_record != null) || DoRun) {
			switch (position) {
			case 0:
				Intent Paramsintent = new Intent(MainActivity.this, ParamsActivity.class);
				Bundle paramBundle = new Bundle();
				paramBundle.putString("ip", ip);
				paramBundle.putInt("port", port);
				Paramsintent.putExtras(paramBundle);
				startActivity(Paramsintent); // ���ò���
				break;
			case 1:
				Intent DayTable_intent = new Intent(MainActivity.this, DayTableActivity.class);
				Bundle DayTablebundle = new Bundle();
				DayTablebundle.putStringArrayList("date_table", (ArrayList<String>) date_table);
				DayTablebundle.putSerializable("JXList", (Serializable) JXList);
				DayTablebundle.putString("ip", ip);
				DayTablebundle.putInt("port", port);
				DayTable_intent.putExtras(DayTablebundle);
				startActivity(DayTable_intent); // ���ձ�
				break;
			case 2:
				Intent Ae5day_intent = new Intent(MainActivity.this, Ae5DayActivity.class);
				Bundle bundle_Ae5 = new Bundle();
				bundle_Ae5.putSerializable("JXList", (Serializable) JXList);
				bundle_Ae5.putString("ip", ip);
				bundle_Ae5.putInt("port", port);
				Ae5day_intent.putExtras(bundle_Ae5);
				startActivity(Ae5day_intent); // ЧӦ�鱨��
				break;
			case 3:
				Intent Potage_intent = new Intent(MainActivity.this, PotAgeActivity.class);
				Bundle PotageBundle = new Bundle();
				PotageBundle.putString("ip", ip);
				PotageBundle.putInt("port", port);
				Potage_intent.putExtras(PotageBundle);
				startActivity(Potage_intent); // �����
				break;
			case 4:
				Intent potv_intent = new Intent(MainActivity.this, PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // ��ѹ����
				break;
			case 5:
				Intent faultRec_intent = new Intent(MainActivity.this, FaultRecActivity.class);
				Bundle bundle_faultRec = new Bundle();
				bundle_faultRec.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_faultRec.putBoolean("Hide_Action", false);
				bundle_faultRec.putString("PotNo", "1101");
				bundle_faultRec.putString("Begin_Date", date_record.get(0));
				bundle_faultRec.putString("End_Date", date_record.get(0));
				bundle_faultRec.putSerializable("JXList", (Serializable) JXList);
				bundle_faultRec.putString("ip", ip);
				bundle_faultRec.putInt("port", port);
				faultRec_intent.putExtras(bundle_faultRec);
				startActivity(faultRec_intent); // ���ϼ�¼
				break;
			case 6:
				Intent realRec_intent = new Intent(MainActivity.this, RealRecActivity.class);
				Bundle realbundle = new Bundle();
				realbundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				realbundle.putSerializable("JXList", (Serializable) JXList);
				realbundle.putString("ip", ip);
				realbundle.putInt("port", port);
				realRec_intent.putExtras(realbundle);
				startActivity(realRec_intent); // ʵʱ��¼
				break;
			case 7:
				Intent operate_intent = new Intent(MainActivity.this, OperateRecActivity.class);
				Bundle operateBundle = new Bundle();
				operateBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				operateBundle.putString("ip", ip);
				operateBundle.putInt("port", port);
				operate_intent.putExtras(operateBundle);
				startActivity(operate_intent); // ������¼
				break;
			case 8:
				Intent PotStatus_intent = new Intent(MainActivity.this, PotStatusActivity.class);
				Bundle PotStatusBundle = new Bundle();
				// PotStatusBundle.putStringArrayList("date_record",
				// (ArrayList<String>) date_record);
				PotStatusBundle.putSerializable("JXList", (Serializable) JXList);
				PotStatusBundle.putString("ip", ip);
				PotStatusBundle.putInt("port", port);
				PotStatus_intent.putExtras(PotStatusBundle);
				startActivity(PotStatus_intent); // ��״̬��
				break;
			case 9:
				Intent measue_intent = new Intent(MainActivity.this, MeasueTableActivity.class);
				Bundle measueBundle = new Bundle();
				measueBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				measueBundle.putString("ip", ip);
				measueBundle.putInt("port", port);
				measue_intent.putExtras(measueBundle);
				startActivity(measue_intent); // ��������
				break;

			case 10:
				Intent aemost_intent = new Intent(MainActivity.this, AeMostActivity.class);
				Bundle aemostBundle = new Bundle();
				aemostBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				aemostBundle.putSerializable("JXList", (Serializable) JXList);
				aemostBundle.putString("ip", ip);
				aemostBundle.putInt("port", port);
				aemost_intent.putExtras(aemostBundle);
				startActivity(aemost_intent); // ЧӦ��
				break;
			case 11:
				Intent aeRec_intent = new Intent(MainActivity.this, AeRecActivity.class);
				Bundle bundle_AeRec = new Bundle();
				bundle_AeRec.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_AeRec.putBoolean("Hide_Action", false);
				bundle_AeRec.putString("PotNo", "1101");
				bundle_AeRec.putString("Begin_Date", date_record.get(0));
				bundle_AeRec.putString("End_Date", date_record.get(0));
				bundle_AeRec.putSerializable("JXList", (Serializable) JXList);
				bundle_AeRec.putString("ip", ip);
				bundle_AeRec.putInt("port", port);
				aeRec_intent.putExtras(bundle_AeRec);
				startActivity(aeRec_intent); // ЧӦ��¼
				break;
			case 12:
				Intent faultmost_intent = new Intent(MainActivity.this, FaultMostActivity.class);
				Bundle bundle_faultmost = new Bundle();
				bundle_faultmost.putSerializable("JXList", (Serializable) JXList);
				bundle_faultmost.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_faultmost.putString("ip", ip);
				bundle_faultmost.putInt("port", port);
				faultmost_intent.putExtras(bundle_faultmost);
				startActivity(faultmost_intent); // ����������
				break;
			case 13:
				Intent craft_intent = new Intent(MainActivity.this, CraftLineActivity.class);
				Bundle craftBundle = new Bundle();
				craftBundle.putStringArrayList("date_table", (ArrayList<String>) date_table);
				craftBundle.putString("ip", ip);
				craftBundle.putInt("port", port);
				craft_intent.putExtras(craftBundle);
				startActivity(craft_intent); // ��������
				break;
			case 14:
				Intent alarmRec_intent = new Intent(MainActivity.this, AlarmRecActivity.class);
				Bundle bundle_alarm = new Bundle();
				bundle_alarm.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_alarm.putSerializable("JXList", (Serializable) JXList);
				bundle_alarm.putString("ip", ip);
				bundle_alarm.putInt("port", port);
				alarmRec_intent.putExtras(bundle_alarm);
				startActivity(alarmRec_intent); // ������¼
				break;
			case 15:
				Intent realtime_intent = new Intent(MainActivity.this, RealTimeLineActivity.class);
				Bundle bundle_realtime = new Bundle();
				// bundle_realtime.putStringArrayList("date_record",
				// (ArrayList<String>) date_record);
				bundle_realtime.putBoolean("Hide_Action", false);
				bundle_realtime.putString("PotNo", "1101");
				bundle_realtime.putString("ip", ip);
				bundle_realtime.putInt("port", port);
				// realtime_intent.putStringArrayListExtra("date_table",
				// (ArrayList<String>) date_table);
				realtime_intent.putExtras(bundle_realtime);
				startActivity(realtime_intent); // ʵʱ����
				break;
			}
		} else {
            Toast.makeText(mContext, "���Ժ��ٵ�������ݳ�ʼ��....", 1).show();
		}
	}

	public boolean initdate(Context ctx) {
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		if (sp != null) {
			ip = sp.getString("ipstr", ip);
			if (ip != null) {
				if (sp.getString("port", String.valueOf(port)) != null) {
					port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
				} else {
					Toast.makeText(ctx, "������Զ�̷������˿�", 0).show();
					return false;
				}
			} else {
				Toast.makeText(ctx, "������Զ�̷�����IP", 0).show();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void GetALLDayUrl(String data) {
		// �õ�����
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[����]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();
			tv_title.setTextSize(14);
			tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
		} else {
			date_table = new ArrayList<String>();
			date_table = JsonToBean_Area_Date.JsonArrayToDate(data);
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todayValue = sdf.format(dt);
			date_record = new ArrayList<String>(date_table); // ��¼����
			date_record.add(0, todayValue);
		}

	}

	@Override
	public void GetJXRecordUrl(String data) {
		if (data.equals("")) {
			tv_title.setTextSize(14);
			tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[������]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();
		} else {
			JXList = new ArrayList<Map<String, Object>>();
			JXList = JsonToBean_Area_Date.JsonArrayToJXRecord(data);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_more:		
			titlePopup.show(v);
			break;
		}

	}
}
