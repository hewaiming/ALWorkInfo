package com.hewaiming.ALWorkInfo.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetJXRecord_Listener;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpGetData_JXRecord;
import com.hewaiming.ALWorkInfo.net.HttpGetData_date;
import com.hewaiming.allwork.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener,HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private GridView gridView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;

	private List<String> date_record; // 记录日期
	private List<String> date_table; // 报表日期
	private List<Map<String, Object>> jXList; // 记录号名

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
		mhttpgetdata_date = (HttpGetData_date) new HttpGetData_date(get_dateTable_url,this,this).execute();
		mHttpGetData_JXRecord = (HttpGetData_JXRecord) new HttpGetData_JXRecord(get_JXName_url,this,this).execute();
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
			startActivity(Paramsintent);
			break;
		case 1:
			Intent DayTable_intent = new Intent(MainActivity.this, DayTableActivity.class);
			DayTable_intent.putStringArrayListExtra("date_table", (ArrayList<String>) date_table);
			startActivity(DayTable_intent);
			break;
		case 11:
			// Intent youtubeIntent = new Intent(MainActivity.this,
			// YoutubeActivity.class);
			// startActivity(youtubeIntent);
			break;
		case 3:
			Intent Potage_intent = new Intent(MainActivity.this, PotAgeActivity.class);
			startActivity(Potage_intent);
			break;
		}

	}

	@Override
	public void GetALLDayUrl(String data) {
		// 得到日期
		date_table = new ArrayList<String>();
		date_table = JsonToBean_Area_Date.JsonArrayToDate(data);
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayValue = sdf.format(dt);
		date_record = new ArrayList<String>(date_table); // 记录日期		
		date_record.add(0,todayValue);

	}

	@Override
	public void GetJXRecordUrl(String data) {
		jXList=new ArrayList<Map<String, Object>>();
		jXList=JsonToBean_Area_Date.JsonArrayToJXRecord(data);
		
	}
}
