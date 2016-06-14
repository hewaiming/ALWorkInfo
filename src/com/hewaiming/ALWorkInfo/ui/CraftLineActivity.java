package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToMultiList;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CraftLineActivity extends Activity implements HttpGetListener, OnClickListener {
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;
	private HttpPost_BeginDate_EndDate http_post;
	private String potno_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/dayTable_Craft.php";
	private String PotNo, BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	private List<String> PotNoList = null;
	private List<dayTable> listBean_daytable = null;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_craft_line);
		dateBean = getIntent().getStringArrayListExtra("date_table");
		init_area();
		init_potNo();
		init_date();
		init_title();
	}

	private void init_potNo() {
		spinner_potno = (Spinner) findViewById(R.id.spinner_PotNo);
		PotNoList = new ArrayList<String>();
		for (int i = 1101; i <= 1136; i++) {
			PotNoList.add(i + "");
		}
		PotNo_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PotNoList);
		PotNo_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_potno.setAdapter(PotNo_adapter);
		spinner_potno.setVisibility(View.VISIBLE);
		spinner_potno.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PotNo = PotNoList.get(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(getApplicationContext(), "没有选择槽号", 1).show();
			}

		});
	}

	private void init_date() {
		spinner_beginDate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_endDate = (Spinner) findViewById(R.id.spinner_Enddate);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// 设置下拉列表的风格
		Date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_beginDate.setAdapter(Date_adapter);
		spinner_endDate.setAdapter(Date_adapter);
		spinner_beginDate.setVisibility(View.VISIBLE);
		spinner_endDate.setVisibility(View.VISIBLE);
		BeginDate = spinner_beginDate.getItemAtPosition(0).toString();
		EndDate = spinner_endDate.getItemAtPosition(0).toString();

		spinner_beginDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BeginDate = spinner_beginDate.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// 截止时间
		spinner_endDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				EndDate = spinner_endDate.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void init_title() {	
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("工艺曲线");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// 设置下拉列表的风格
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_area.setAdapter(Area_adapter);
		spinner_area.setVisibility(View.VISIBLE);
		spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					areaId = 11;
					break;
				case 1:
					areaId = 12;
					break;
				case 2:
					areaId = 13;
					break;
				case 3:
					areaId = 21;
					break;
				case 4:
					areaId = 22;
					break;
				case 5:
					areaId = 23;
					break;
				}

				PotNoChanged(areaId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		findBtn = (Button) findViewById(R.id.btn_ok);
		findBtn.setOnClickListener(this);
	}

	protected void PotNoChanged(int areaId2) {
		switch (areaId2) {
		case 11:
			PotNoList.clear();
			for (int i = 1101; i <= 1136; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 12:
			PotNoList.clear();
			for (int i = 1201; i <= 1237; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 13:
			PotNoList.clear();
			for (int i = 1301; i <= 1337; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 21:
			PotNoList.clear();
			for (int i = 2101; i <= 2136; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 22:
			PotNoList.clear();
			for (int i = 2201; i <= 2237; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 23:
			PotNoList.clear();
			for (int i = 2301; i <= 2337; i++) {
				PotNoList.add(i + "");
			}
			break;
		}
		spinner_potno.setSelection(0);
		PotNo = PotNoList.get(0).toString();
		PotNo_adapter.notifyDataSetChanged();// 通知数据改变		
	}

	@Override
	public void GetDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[工艺参数]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			
		} else {
			listBean_daytable = new ArrayList<dayTable>();			
			listBean_daytable = JsonToMultiList.JsonArrayToDayTableBean(data);	
			Intent show_intent=new Intent(CraftLineActivity.this, ShowCraftLineActivity.class);
			Bundle mbundle = new Bundle();
			mbundle.putString("PotNo",PotNo);
			mbundle.putString("Begin_End_Date",BeginDate+" 至 "+EndDate);
			mbundle.putSerializable("list_daytable", (Serializable) listBean_daytable);
    	    show_intent.putExtras(mbundle);		
			startActivity(show_intent);  //显示工艺曲线图
			
		}
	}	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_ok:
			if (EndDate.compareTo(BeginDate) < 0) {
				Toast.makeText(getApplicationContext(), "日期选择不对：截止日期小于开始日期", 1).show();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date bdate = df.parse(BeginDate);
					Date edate = df.parse(EndDate);
					long TIME_DAY_MILLISECOND = 86400000;
					Long days = (edate.getTime() - bdate.getTime()) / (TIME_DAY_MILLISECOND);
					if (days >= 70) {
						Toast.makeText(getApplicationContext(), "数据量太大：截止日期-开始日期>2,请重新选择日期", 1).show();
					} else {						
						http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo,
								BeginDate, EndDate, this, this).execute();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

}
