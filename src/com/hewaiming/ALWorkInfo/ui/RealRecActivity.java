package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.FaultRecord_Adapter;
import com.hewaiming.ALWorkInfo.adapter.Params_Adapter;
import com.hewaiming.ALWorkInfo.adapter.PotAge_Adapter;
import com.hewaiming.ALWorkInfo.adapter.RealRecord_Adapter;
import com.hewaiming.ALWorkInfo.adapter.dayTable_Adapter;
import com.hewaiming.ALWorkInfo.bean.FaultRecord;
import com.hewaiming.ALWorkInfo.bean.PotAge;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
import com.hewaiming.ALWorkInfo.bean.SetParams;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpGetData_date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;

import com.hewaiming.ALWorkInfo.net.HttpPost_area;
import com.hewaiming.ALWorkInfo.net.HttpPost_area_date;
import com.hewaiming.ALWorkInfo.view.HeaderListView_AlarmRecord;
import com.hewaiming.ALWorkInfo.view.HeaderListView_Params;
import com.hewaiming.ALWorkInfo.view.HeaderListView_PotAge;
import com.hewaiming.ALWorkInfo.view.HeaderListView_RealRecord;
import com.hewaiming.ALWorkInfo.view.HeaderListView_dayTable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RealRecActivity extends Activity implements HttpGetListener, OnClickListener {
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ListView lv_realRec;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;

	private HttpPost_BeginDate_EndDate http_post;
	private HeaderListView_RealRecord headerView;
	private String potno_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/RealRecordTable_potno_date.php";
	private String area_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/RealRecordTable_area_date.php";

	private String PotNo, BeginDate, EndDate;

	private List<String> dateBean = new ArrayList<String>();
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<String> PotNoList;
	private List<RealRecord> listBean = null;
	private RealRecord_Adapter realRec_Adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_record);
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
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
		PotNoList.add(0, "ȫ���ۺ�");
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

			}

		});

	}

	private void init_date() {
		spinner_beginDate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_endDate = (Spinner) findViewById(R.id.spinner_Enddate);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// ���������б��ķ��
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

		// ��ֹʱ��
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
		tv_title.setText("������¼");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void init_area() {
		lv_realRec = (ListView) findViewById(R.id.lv_RealRec);
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// ���������б��ķ��
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ���ӵ�spinner��
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
		PotNoList.add(0, "ȫ���ۺ�");
		PotNo_adapter.notifyDataSetChanged();// ֪ͨ���ݸı�
		spinner_potno.setId(0);
	}

	@Override
	public void GetDataUrl(String data) {
	
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "�ӷ�������û���ҵ�������¼��", Toast.LENGTH_LONG).show();
			if (listBean.size() > 0) { 
				listBean.clear();		// ���LISTVIEW ��ǰ������
				realRec_Adapter.onDateChange(listBean);
			}

		} else {
			if (lv_realRec.getHeaderViewsCount() > 0) {
				lv_realRec.removeHeaderView(headerView);
			}

			headerView = new HeaderListView_RealRecord(this);// ���ӱ�ͷ
			headerView.setTvPotNo("�ۺ�");
			headerView.setTvRecordNo("��¼����");
			headerView.setTvParam1("����1");
			headerView.setTvParam2("����2");
			headerView.setTvRecTime("����ʱ��");
			
			listBean = new ArrayList<RealRecord>();
			lv_realRec.addHeaderView(headerView);
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToRealRecordBean(data, JXList);
			realRec_Adapter = new RealRecord_Adapter(this, listBean);
			lv_realRec.setAdapter(realRec_Adapter);
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
				Toast.makeText(getApplicationContext(), "����ѡ�񲻶ԣ���ֹ����С�ڿ�ʼ����", 1).show();
			} else {
				if (PotNo == "ȫ���ۺ�") {
					http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(area_url, 1,
							Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				} else {

					http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo,
							BeginDate, EndDate, this, this).execute();
				}
			}
			break;
		}
	}

}