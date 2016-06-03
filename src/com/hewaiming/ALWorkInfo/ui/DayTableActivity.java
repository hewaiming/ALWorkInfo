package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.Params_Adapter;
import com.hewaiming.ALWorkInfo.adapter.PotAge_Adapter;
import com.hewaiming.ALWorkInfo.adapter.dayTable_Adapter;
import com.hewaiming.ALWorkInfo.bean.PotAge;
import com.hewaiming.ALWorkInfo.bean.SetParams;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpGetData_date;
import com.hewaiming.ALWorkInfo.net.HttpPost_area;
import com.hewaiming.ALWorkInfo.net.HttpPost_area_date;
import com.hewaiming.ALWorkInfo.view.HeaderListView_Params;
import com.hewaiming.ALWorkInfo.view.HeaderListView_PotAge;
import com.hewaiming.ALWorkInfo.view.HeaderListView_dayTable;
import com.hewaiming.allwork.R;
import com.hewaiming.allwork.R.id;
import com.hewaiming.allwork.R.layout;
import com.hewaiming.allwork.R.menu;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DayTableActivity extends Activity implements HttpGetListener,HttpGetDate_Listener, OnClickListener {
	private Spinner spinner_area, spinner_date;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ListView lv_daytable;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private HttpPost_area_date http_post;
	private HeaderListView_dayTable headerView;
	private String url = "http://125.64.59.11:8000/scgy/android/odbcPhP/dayTableArea_date.php";
	private String getdate_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/getDate.php";
	private String ddate;
	private HttpGetData_date mhttpgetdata_date;
	private List<String> dateBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daytable);
		mhttpgetdata_date = (HttpGetData_date) new HttpGetData_date(getdate_url, this, this).execute();
		init();
		init_title();	
	}
	

	private void init_title() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("�ձ���");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void init() {
		lv_daytable = (ListView) findViewById(R.id.lv_daytable);
		spinner_area = (Spinner) findViewById(R.id.spinner_area);
		spinner_date=(Spinner) findViewById(R.id.spinner_date);
		
		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// ���������б�ķ��
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_area.setAdapter(Area_adapter);
		// ����¼�Spinner�¼�����
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
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}

		});
		findBtn = (Button) findViewById(R.id.btn_daytable);
		findBtn.setOnClickListener(this);
	}

	@Override
	public void GetDataUrl(String data) {
		if (data.equals(null) || data == "") {
			Toast.makeText(getApplicationContext(), "û���ҵ��ձ����ݣ�", Toast.LENGTH_LONG).show();
		} else {
			if (lv_daytable.getHeaderViewsCount() > 0) {
				lv_daytable.removeHeaderView(headerView);
			}
			// PotNo,
			// PotSt,RunTime,SetV,RealSetV,WorkV,AverageV,AeV,AeTime,AeCnt,DybTime,Ddate
			headerView = new HeaderListView_dayTable(this);// ��ӱ�ͷ
			headerView.setTvPotNo("�ۺ�");
			headerView.setTvPotSt("��״̬");
			headerView.setTvRunTime("����ʱ��");
			headerView.setTvSetV("�趨��ѹ");
			headerView.setTvRealSetV("ʵ���ѹ");
			headerView.setTvWorkV("������ѹ");
			headerView.setTvAverageV("ƽ����ѹ");
			headerView.setTvAeV("ЧӦ��ѹ");
			headerView.setTvAeTime("ЧӦʱ��");
			headerView.setTvAeCnt("ЧӦ����");
			headerView.setTvDybTime("��ѹ��ʱ��");
			headerView.setTvDdate("����");

			lv_daytable.addHeaderView(headerView);

			List<dayTable> listBean = new ArrayList<dayTable>();
			listBean = JsonToBean_Area_Date.JsonArrayToDayTableBean(data);

			// ArrayAdapter adapter = new ArrayAdapter<SetParams>(this,
			// android.R.layout.simple_list_item_1, listBean);
			// lv_params.setAdapter(adapter);
			// lv_params.setAdapter(sadapter);
			dayTable_Adapter daytable_Adapter = new dayTable_Adapter(this, listBean);
			lv_daytable.setAdapter(daytable_Adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_daytable:
			http_post = (HttpPost_area_date) new HttpPost_area_date(url, this, this, Integer.toString(areaId),ddate)
					.execute();
		}
	}

	@Override
	public void GetALLDayUrl(String data) {
		//��ʼ�����ڿؼ�	
		dateBean=new ArrayList<String>();
		dateBean = JsonToBean_Area_Date.JsonArrayToDate(data);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// ���������б�ķ��
		Date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_date.setAdapter(Date_adapter);		
		spinner_date.setVisibility(View.VISIBLE);
		ddate = spinner_date.getItemAtPosition(0).toString();	
		
		spinner_date.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ddate = spinner_date.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {			

			}
		});	
		
	}

}
