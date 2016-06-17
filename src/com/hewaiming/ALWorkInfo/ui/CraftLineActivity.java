package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.InterFace.LoadAeTimeInterface;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_Latch;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_other;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_other_Latch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CraftLineActivity extends Activity
		implements HttpGetListener, LoadAeTimeInterface, OnClickListener, OnCheckedChangeListener {
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;
	private String potno_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/dayTable_Craft.php";
	private String measue_potno_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/MeasueTable_potno_date.php";
	private String PotNo, BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	private List<String> PotNoList = null;
	private List<dayTable> listBean_daytable = null;
	private List<CheckBox> list_cb = new ArrayList<CheckBox>();
	private CheckBox cb0, cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15;
	private String selitems = "";
	private HttpPost_BeginDate_EndDate daytable_http_post;
	private HttpPost_BeginDate_EndDate_other measuetable_http_post;
	private List<MeasueTable> listBean_measuetable = null;
	private CountDownLatch latch;
	private JSONDayTable work_day;
	private JSONMeasueTable work_measue;
	private String measuedata;
	private String daydata;
	private HttpPost_BeginDate_EndDate_Latch LATCH_daytable_http_post;
	private HttpPost_BeginDate_EndDate_other_Latch LATCH_measuetable_http_post;

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
		init_items();
	}

	private void init_items() {
		cb0 = (CheckBox) findViewById(R.id.chkbox_SetV);
		cb1 = (CheckBox) findViewById(R.id.chkbox_WorkV);
		cb2 = (CheckBox) findViewById(R.id.chkbox_AvgV);
		cb3 = (CheckBox) findViewById(R.id.chkbox_Noise);
		cb4 = (CheckBox) findViewById(R.id.chkbox_SetALF);
		cb5 = (CheckBox) findViewById(R.id.chkbox_ALF);
		cb6 = (CheckBox) findViewById(R.id.chkbox_AeCnt);
		cb7 = (CheckBox) findViewById(R.id.chkbox_ALO);
		cb8 = (CheckBox) findViewById(R.id.chkbox_ALzs);
		cb9 = (CheckBox) findViewById(R.id.chkbox_ALjh);
		cb10 = (CheckBox) findViewById(R.id.chkbox_FeCnt);
		cb11 = (CheckBox) findViewById(R.id.chkbox_SiCnt);
		cb12 = (CheckBox) findViewById(R.id.chkbox_FZB);
		cb13 = (CheckBox) findViewById(R.id.chkbox_DJWD);
		cb14 = (CheckBox) findViewById(R.id.chkbox_LSP);
		cb15 = (CheckBox) findViewById(R.id.chkbox_DJZSP);
		list_cb.add(cb0);
		list_cb.add(cb1);
		list_cb.add(cb2);
		list_cb.add(cb3);
		list_cb.add(cb4);
		list_cb.add(cb5);
		list_cb.add(cb6);
		list_cb.add(cb7);
		list_cb.add(cb8);
		list_cb.add(cb9);
		list_cb.add(cb10);
		list_cb.add(cb11);
		list_cb.add(cb12);
		list_cb.add(cb13);
		list_cb.add(cb14);
		list_cb.add(cb15);
		for (CheckBox cb : list_cb) {
			cb.setOnCheckedChangeListener(this);
		}
		// for (int i = 0; i < chkbox.length; i++) {
		// chkbox[i].setOnCheckedChangeListener(this);
		// }

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
				Toast.makeText(getApplicationContext(), "û��ѡ��ۺ�", 1).show();
			}

		});
	}

	private void init_date() {
		spinner_beginDate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_endDate = (Spinner) findViewById(R.id.spinner_Enddate);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// ���������б�ķ��
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
		tv_title.setText("��������");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// ���������б�ķ��
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
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
		PotNo_adapter.notifyDataSetChanged();// ֪ͨ���ݸı�
	}

	@Override
	public void GetDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[���ղ���]�ձ����ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();

		} else {
			listBean_daytable = new ArrayList<dayTable>();
			listBean_daytable = JsonToBean_Area_Date.JsonArrayToDayTableBean(data);
			// work_day = new
			// JSONDayTable("DayTable",latch,listBean_daytable,data);
			// work_day.start();
			// listBean_daytable =
			// JsonToMultiList.JsonArrayToDayTableBean(data);
			// latch.countDown();
			/*
			 * Intent show_intent = new Intent(CraftLineActivity.this,
			 * ShowCraftLineActivity.class); Bundle mbundle = new Bundle();
			 * mbundle.putString("PotNo", PotNo);
			 * mbundle.putString("Begin_End_Date", BeginDate + " �� " + EndDate);
			 * mbundle.putSerializable("list_daytable", (Serializable)
			 * listBean_daytable); mbundle.putString("SELITEMS", selitems);
			 * show_intent.putExtras(mbundle); startActivity(show_intent); //
			 * ��ʾ��������ͼ
			 */
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
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date bdate = df.parse(BeginDate);
					Date edate = df.parse(EndDate);
					long TIME_DAY_MILLISECOND = 86400000;
					Long days = (edate.getTime() - bdate.getTime()) / (TIME_DAY_MILLISECOND);
					if (days >= 70) {
						Toast.makeText(getApplicationContext(), "������̫�󣺽�ֹ����-��ʼ����>=70,������ѡ������", 1).show();
					} else {
						// ѡ���������߲�λ
						selitems = "";
						for (CheckBox cBox : list_cb) {
							if (cBox.isChecked()) {
								selitems += cBox.getText() + ",";
							}
						}
						if (selitems.equals("")) {
							Toast.makeText(getApplicationContext(), "û��ѡ���κ�һ��ղ�������ѡ���ղ����", 1).show();
							break;
						} else {
							latch = new CountDownLatch(2);						
							daytable_http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url,
									2, PotNo, BeginDate, EndDate, this, this);
							measuetable_http_post = (HttpPost_BeginDate_EndDate_other) new HttpPost_BeginDate_EndDate_other(
									measue_potno_url, 2, PotNo, BeginDate, EndDate, this, this); // �Ӳ�������ȡ����
							new Thread(new Runnable() {
								@Override
								public void run() {
									System.out.println("daytable_http_post.execute()....");
									daytable_http_post.execute();
									latch.countDown();
								}
							}).start();
							new Thread(new Runnable() {
								@Override
								public void run() {
									System.out.println("measuetable_http_post.execute....");
									measuetable_http_post.execute();
									latch.countDown();
								}
							}).start();

							// LATCH_daytable_http_post =
							// (HttpPost_BeginDate_EndDate_Latch) new
							// HttpPost_BeginDate_EndDate_Latch(
							// latch, potno_url, 2, PotNo, BeginDate, EndDate,
							// this, this).execute(); // ���ձ�ȡ����
							//
							// LATCH_measuetable_http_post =
							// (HttpPost_BeginDate_EndDate_other_Latch) new
							// HttpPost_BeginDate_EndDate_other_Latch(
							// latch, measue_potno_url, 2, PotNo, BeginDate,
							// EndDate, this, this).execute(); // �Ӳ�������ȡ����
							// LATCH_daytable_http_post.execute();
							// LATCH_measuetable_http_post.execute();
							// work_day.start();
							// work_measue.start();
							latch.await();// �ȴ����й�����ɹ���
							Intent show_intent = new Intent(CraftLineActivity.this, ShowCraftLineActivity.class);
							Bundle mbundle = new Bundle();
							mbundle.putString("PotNo", PotNo);
							mbundle.putString("Begin_End_Date", BeginDate + " �� " + EndDate);
							mbundle.putSerializable("list_daytable", (Serializable) listBean_daytable);
							mbundle.putSerializable("list_measuetable", (Serializable) listBean_measuetable);
							mbundle.putString("SELITEMS", selitems);
							show_intent.putExtras(mbundle);
							startActivity(show_intent); // ��ʾ��������ͼ
						}
					}
				} catch (ParseException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.i("chkbox", buttonView.getText().toString() + isChecked);
	}

	@Override
	public void GetAeTimeDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[���ղ���]�������ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();

		} else {

			listBean_measuetable = new ArrayList<MeasueTable>();
			listBean_measuetable = JsonToBean_Area_Date.JsonArrayToMeasueTableBean(data);
			// work_measue = new
			// JSONMeasueTable("measueTable",latch,listBean_measuetable,data);
			// work_measue.start();
			// listBean_measuetable =
			// JsonToBean_Area_Date.JsonArrayToMeasueTableBean(data);
			// latch.countDown();

		}
	}

	// ��ȡ�ձ����� LIST ����
	static class JSONDayTable extends Thread {
		String workerName;
		CountDownLatch latch;
		List<dayTable> result;
		String data;

		public JSONDayTable(String workerName, CountDownLatch latch, List<dayTable> listDay, String remotedata) {
			this.workerName = workerName;
			this.latch = latch;
			this.result = listDay;
			this.data = remotedata;
			System.out.println(workerName + "��ʼ���ɹ�");
		}

		@Override
		public void run() {
			result = JsonToBean_Area_Date.JsonArrayToDayTableBean(data);
			latch.countDown();
		}
	}

	// ��ȡ�������� LIST ����
	static class JSONMeasueTable extends Thread {
		String workerName;
		CountDownLatch latch;
		List<MeasueTable> result;
		String data;

		public JSONMeasueTable(String workerName, CountDownLatch latch, List<MeasueTable> list, String remotedata) {
			this.workerName = workerName;
			this.latch = latch;
			this.result = list;
			this.data = remotedata;
			System.out.println(workerName + "��ʼ���ɹ�");
		}

		@Override
		public void run() {
			result = JsonToBean_Area_Date.JsonArrayToMeasueTableBean(data);
			latch.countDown();
		}
	}
}
