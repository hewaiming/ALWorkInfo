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
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpGetData_JXRecord;
import com.hewaiming.ALWorkInfo.net.HttpGetData_date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity
		implements OnItemClickListener, HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private GridView gridView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;

	private List<String> date_record; // ��¼����
	private List<String> date_table; // ��������
	private List<Map<String, Object>> JXList; // ��¼����

	private String get_dateTable_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/getDate.php";
	private String get_JXName_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/getJXRecordName.php";
	private HttpGetData_date mhttpgetdata_date;
	private HttpGetData_JXRecord mHttpGetData_JXRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		init_commData();
	}

	private void init_commData() {
		mhttpgetdata_date = (HttpGetData_date) new HttpGetData_date(get_dateTable_url, this, this).execute();
		mHttpGetData_JXRecord = (HttpGetData_JXRecord) new HttpGetData_JXRecord(get_JXName_url, this, this).execute();
	}

	private void init() {
		gridView = (GridView) findViewById(R.id.gridView);
		dataList = new ArrayList<Map<String, Object>>();
		adapter = new SimpleAdapter(this, getData(), R.layout.item_action, new String[] { "pic", "name" },
				new int[] { R.id.pic, R.id.name });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);

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
			startActivity(Paramsintent); // ���ò���
			break;
		case 1:
			Intent DayTable_intent = new Intent(MainActivity.this, DayTableActivity.class);
			DayTable_intent.putStringArrayListExtra("date_table", (ArrayList<String>) date_table);
			startActivity(DayTable_intent); // ���ձ�
			break;
		case 2:

			break;
		case 3:
			Intent Potage_intent = new Intent(MainActivity.this, PotAgeActivity.class);
			startActivity(Potage_intent); // �����
			break;
		case 4:
			Intent potv_intent = new Intent(MainActivity.this, PotVLineActivity.class);
			potv_intent.putStringArrayListExtra("date_record", (ArrayList<String>) date_record);
			startActivity(potv_intent); // ��ѹ����
			break;
		case 5:
			Intent faultRec_intent = new Intent(MainActivity.this, FaultRecActivity.class);
			Bundle mbundle = new Bundle();
			mbundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
			mbundle.putSerializable("JXList", (Serializable) JXList);
			faultRec_intent.putExtras(mbundle);
			startActivity(faultRec_intent); // ���ϼ�¼
			break;
		case 6:
			Intent realRec_intent = new Intent(MainActivity.this, RealRecActivity.class);
			Bundle realbundle = new Bundle();
			realbundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
			realbundle.putSerializable("JXList", (Serializable) JXList);
			realRec_intent.putExtras(realbundle);
			startActivity(realRec_intent); // ʵʱ��¼
			break;
		case 7:
			Intent operate_intent = new Intent(MainActivity.this, OperateRecActivity.class);
			operate_intent.putStringArrayListExtra("date_record", (ArrayList<String>) date_record);
			startActivity(operate_intent); // ������¼
			break;
		case 11:
			Intent aeRec_intent = new Intent(MainActivity.this, AeRecActivity.class);
			aeRec_intent.putStringArrayListExtra("date_record", (ArrayList<String>) date_record);
			startActivity(aeRec_intent); // ������¼
			break;
		case 14:
			Intent alarmRec_intent = new Intent(MainActivity.this, AlarmRecActivity.class);
			Bundle bundle_alarm = new Bundle();
			bundle_alarm.putStringArrayList("date_record", (ArrayList<String>) date_record);
			bundle_alarm.putSerializable("JXList", (Serializable) JXList);
			alarmRec_intent.putExtras(bundle_alarm);
			startActivity(alarmRec_intent); // ������¼
			break;
		}

	}

	@Override
	public void GetALLDayUrl(String data) {
		// �õ�����
		date_table = new ArrayList<String>();
		date_table = JsonToBean_Area_Date.JsonArrayToDate(data);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayValue = sdf.format(dt);
		date_record = new ArrayList<String>(date_table); // ��¼����
		date_record.add(0, todayValue);

	}

	@Override
	public void GetJXRecordUrl(String data) {
		JXList = new ArrayList<Map<String, Object>>();
		JXList = JsonToBean_Area_Date.JsonArrayToJXRecord(data);

	}
}
