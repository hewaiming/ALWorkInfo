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
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpGetData_JXRecord;
import com.hewaiming.ALWorkInfo.net.HttpGetData_date;
import com.hewaiming.ALWorkInfo.net.NetDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity
		implements OnItemClickListener,OnClickListener, HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private GridView gridView;
	private Button btnMore;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;

	private List<String> date_record = null; // 记录日期
	private List<String> date_table = null; // 报表日期
	private List<Map<String, Object>> JXList = null; // 记录号名

	private String get_dateTable_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/getDate.php";
	private String get_JXName_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/getJXRecordName.php";
	private HttpGetData_date mhttpgetdata_date;
	private HttpGetData_JXRecord mHttpGetData_JXRecord;
	private Context mContext;
	private TitlePopup titlePopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		init();
		if (NetStatus()!=0 ) {		
			init_commData();
		}else{
			Toast.makeText(getApplicationContext(), "网络异常！", Toast.LENGTH_LONG).show();
		}
	}

	private int NetStatus() {
		NetDetector netDetector=new NetDetector(mContext);
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
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// AccoutList.this.finish();
				// System.exit(1);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void init_commData() {
		if (date_table == null) {
			mhttpgetdata_date = (HttpGetData_date) new HttpGetData_date(get_dateTable_url, this, this).execute();
		}
		if (JXList == null) {
			mHttpGetData_JXRecord = (HttpGetData_JXRecord) new HttpGetData_JXRecord(get_JXName_url, this, this)
					.execute();
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
		btnMore=(Button) findViewById(R.id.btn_more);
		btnMore.setVisibility(View.VISIBLE);
		btnMore.setOnClickListener(this);

	}

	private void Popup_initData(){
		//给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, "设置远程服务器", R.drawable.mm_title_btn_compose_normal));
		titlePopup.addAction(new ActionItem(this, "检查版本", R.drawable.mm_title_btn_receiver_normal));
		titlePopup.addAction(new ActionItem(this, "关于", R.drawable.mm_title_btn_keyboard_normal));
		titlePopup.addAction(new ActionItem(this, "扫一扫",  R.drawable.mm_title_btn_qrcode_normal));
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
			Intent Paramsintent = new Intent(MainActivity.this, ParamsActivity.class);
			startActivity(Paramsintent); // 常用参数
			break;
		case 1:
			Intent DayTable_intent = new Intent(MainActivity.this, DayTableActivity.class);
			Bundle DayTablebundle = new Bundle();
			DayTablebundle.putStringArrayList("date_table", (ArrayList<String>) date_table);
			DayTablebundle.putSerializable("JXList", (Serializable) JXList);
			DayTable_intent.putExtras(DayTablebundle);
			startActivity(DayTable_intent); // 槽日报
			break;
		case 2:
			Intent Ae5day_intent = new Intent(MainActivity.this, Ae5DayActivity.class);
			Bundle bundle_Ae5 = new Bundle();
			bundle_Ae5.putSerializable("JXList", (Serializable) JXList);
			Ae5day_intent.putExtras(bundle_Ae5);
			startActivity(Ae5day_intent); // 效应情报表
			break;
		case 3:
			Intent Potage_intent = new Intent(MainActivity.this, PotAgeActivity.class);
			startActivity(Potage_intent); // 槽龄表
			break;
		case 4:
			Intent potv_intent = new Intent(MainActivity.this, PotVLineActivity.class);
			Bundle potv_bundle = new Bundle();
			potv_bundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
			potv_bundle.putSerializable("JXList", (Serializable) JXList);
			potv_intent.putExtras(potv_bundle);
			startActivity(potv_intent); // 槽压曲线
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
			faultRec_intent.putExtras(bundle_faultRec);
			startActivity(faultRec_intent); // 故障记录
			break;
		case 6:
			Intent realRec_intent = new Intent(MainActivity.this, RealRecActivity.class);
			Bundle realbundle = new Bundle();
			realbundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
			realbundle.putSerializable("JXList", (Serializable) JXList);
			realRec_intent.putExtras(realbundle);
			startActivity(realRec_intent); // 实时记录
			break;
		case 7:
			Intent operate_intent = new Intent(MainActivity.this, OperateRecActivity.class);
			operate_intent.putStringArrayListExtra("date_record", (ArrayList<String>) date_record);
			startActivity(operate_intent); // 操作记录
			break;
		case 8:
			Intent PotStatus_intent = new Intent(MainActivity.this, PotStatusActivity.class);
			Bundle PotStatusBundle = new Bundle();
//			PotStatusBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
			PotStatusBundle.putSerializable("JXList", (Serializable) JXList);
			PotStatus_intent.putExtras(PotStatusBundle);
			startActivity(PotStatus_intent); // 槽状态表
			break;
		case 9:
			Intent measue_intent = new Intent(MainActivity.this, MeasueTableActivity.class);
			measue_intent.putStringArrayListExtra("date_record", (ArrayList<String>) date_record);
			startActivity(measue_intent); // 测量数据
			break;
		
		case 10:
			Intent aemost_intent = new Intent(MainActivity.this, AeMostActivity.class);
			Bundle aemostBundle = new Bundle();
			aemostBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
			aemostBundle.putSerializable("JXList", (Serializable) JXList);
			aemost_intent.putExtras(aemostBundle);
			startActivity(aemost_intent); // 效应槽
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
			aeRec_intent.putExtras(bundle_AeRec);
			startActivity(aeRec_intent); // 效应记录
			break;
		case 12:
			Intent faultmost_intent = new Intent(MainActivity.this, FaultMostActivity.class);
			Bundle bundle_faultmost = new Bundle();
			bundle_faultmost.putSerializable("JXList", (Serializable) JXList);
			bundle_faultmost.putStringArrayList("date_record", (ArrayList<String>) date_record);
			faultmost_intent.putExtras(bundle_faultmost);
			startActivity(faultmost_intent); // 故障率排序
			break;
		case 13:
			Intent craft_intent = new Intent(MainActivity.this, CraftLineActivity.class);
			craft_intent.putStringArrayListExtra("date_table", (ArrayList<String>) date_table);
			startActivity(craft_intent); // 工艺曲线
			break;
		case 14:
			Intent alarmRec_intent = new Intent(MainActivity.this, AlarmRecActivity.class);
			Bundle bundle_alarm = new Bundle();
			bundle_alarm.putStringArrayList("date_record", (ArrayList<String>) date_record);
			bundle_alarm.putSerializable("JXList", (Serializable) JXList);
			alarmRec_intent.putExtras(bundle_alarm);
			startActivity(alarmRec_intent); // 报警记录
			break;
		case 15:
			Intent realtime_intent = new Intent(MainActivity.this, RealTimeLineActivity.class);
			Bundle bundle_realtime = new Bundle();
//			bundle_realtime.putStringArrayList("date_record", (ArrayList<String>) date_record);
			bundle_realtime.putBoolean("Hide_Action", false);
			bundle_realtime.putString("PotNo", "1101");
//			realtime_intent.putStringArrayListExtra("date_table", (ArrayList<String>) date_table);
			realtime_intent.putExtras(bundle_realtime);
			startActivity(realtime_intent); // 实时曲线
			break;
		}

	}

	@Override
	public void GetALLDayUrl(String data) {
		// 得到日期
		date_table = new ArrayList<String>();
		date_table = JsonToBean_Area_Date.JsonArrayToDate(data);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayValue = sdf.format(dt);
		date_record = new ArrayList<String>(date_table); // 记录日期
		date_record.add(0, todayValue);

	}

	@Override
	public void GetJXRecordUrl(String data) {
		JXList = new ArrayList<Map<String, Object>>();
		JXList = JsonToBean_Area_Date.JsonArrayToJXRecord(data);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_more:
//			startActivity(new Intent(MainActivity.this,DialogActivity.class));
			titlePopup.show(v);
			break;

		default:
			break;
		}
		
	}
}
