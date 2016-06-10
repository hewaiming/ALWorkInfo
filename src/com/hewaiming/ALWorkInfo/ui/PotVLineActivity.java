package com.hewaiming.ALWorkInfo.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;

import android.app.Activity;
import android.graphics.Color;
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

public class PotVLineActivity extends Activity implements HttpGetListener, OnClickListener {
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;
	private HttpPost_BeginDate_EndDate http_post;
	private String potno_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/PotVoltage.php";
	private String PotNo, BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	private List<String> PotNoList = null;
	private List<PotV> listBean = null;
	private LineChart mLineChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_potv_line);
		dateBean = getIntent().getStringArrayListExtra("date_record");
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
		mLineChart = (LineChart) findViewById(R.id.chart_PotV);
		mLineChart.setVisibility(View.GONE);
		;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("��ѹ����ͼ");
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
		PotNo_adapter.notifyDataSetChanged();// ֪ͨ���ݸı�
		spinner_potno.setId(0);
		PotNo = PotNoList.get(0).toString();
	}

	@Override
	public void GetDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[��ѹ]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // ���LISTVIEW ��ǰ������
				}
			}
		} else {
			listBean = new ArrayList<PotV>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToPotVBean(data);
			LineData mLineData = getLineData(listBean.size(), 1);
			showChart(mLineChart, mLineData, Color.rgb(255, 255, 255));
		}
	}

	private void showChart(LineChart lineChart, LineData mLineData, int color) {
		lineChart.setDrawBorders(false); // �Ƿ�������ͼ����ӱ߿�

		// no description text
		lineChart.setDescription("");// ��������
		// ���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview
		lineChart.setNoDataTextDescription("����ҪΪ����ͼ�ṩ����.");

		// enable / disable grid background
		lineChart.setDrawGridBackground(false); // �Ƿ���ʾ�����ɫ
		lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ���ĵ���ɫ�����������Ǹ���ɫ����һ��͸����

		// enable touch gestures
		lineChart.setTouchEnabled(true); // �����Ƿ���Դ���

		// enable scaling and dragging
		lineChart.setDragEnabled(true);// �Ƿ������ק
		lineChart.setScaleEnabled(true);// �Ƿ��������
		// lineChart.getAxisRight().setEnabled(true); // �ұ� ��������
		// lineChart.getAxisLeft().setEnabled(true);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// ���ú������ڵײ�
		lineChart.getXAxis().setGridColor(Color.TRANSPARENT);// ȥ�����������ߵ���ʾ
		// if disabled, scaling can be done on x- and y-axis separately
		lineChart.setPinchZoom(false);//

		lineChart.setBackgroundColor(color);// ���ñ���

		// get the legend (only possible after setting data)
		Legend mLegend = lineChart.getLegend(); // ���ñ���ͼ��ʾ�������Ǹ�һ��y��value��
		mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
		mLegend.setForm(LegendForm.CIRCLE);// ��ʽ
		mLegend.setFormSize(5f);// ����
		mLegend.setTextColor(Color.BLACK);// ��ɫ
		// mLegend.setTypeface(mTf);// ����

		// ���Y�� ��ѹ
		YAxis yAxis_potv = lineChart.getAxisLeft();
		yAxis_potv.setEnabled(true);
		yAxis_potv.setDrawAxisLine(true);
		yAxis_potv.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		yAxis_potv.setTextSize(4f);
		yAxis_potv.setTextColor(Color.BLUE);
		yAxis_potv.setAxisMaxValue(6000);
		yAxis_potv.setAxisMinValue(0);
		// yAxis.setLabelRotationAngle(90f);;

		// �ұ�Y�� ϵ�е���
		YAxis yAxis_cur = lineChart.getAxisRight();
		yAxis_cur.setEnabled(true);
		yAxis_cur.setDrawAxisLine(true);
		yAxis_cur.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		yAxis_cur.setTextSize(4f);
		yAxis_cur.setAxisMaxValue(2300);
		yAxis_cur.setAxisMinValue(0);
		yAxis_cur.setTextColor(Color.RED);

		lineChart.setData(mLineData); // �������� һ��Ҫ����CHART�趨����֮��
		lineChart.animateX(200); // ����ִ�еĶ���,x��
	}

	private LineData getLineData(int count, float range) {

		List<String> xValues = new ArrayList<String>();
		List<Entry> yValues = new ArrayList<Entry>(); // y�������
		List<Entry> yValuesCur = new ArrayList<Entry>();
		for (int i = 0; i < count; i++) {
			xValues.add(listBean.get(i).getDdate().toString());// x����ʾ�����ݣ�����Ĭ��ʹ�������±���ʾ
			float value = (float) (listBean.get(i).getPotV() * range);
			float temp = (float) (listBean.get(i).getCur() * range);
			yValues.add(new Entry(value, i)); // y��Ĳ�ѹ����
			yValuesCur.add(new Entry(temp, i));// y���ϵ�е���

		}

		// y������ݼ���
		LineDataSet lineDataSet = new LineDataSet(yValues, PotNo + "�۵�ѹ: mV ");/* ��ʾ�ڱ���ͼ�� */
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);
		// ��y��ļ��������ò���
		lineDataSet.setAxisDependency(AxisDependency.LEFT);
		lineDataSet.setLineWidth(0.7f); // �߿�
		lineDataSet.setCircleSize(0.5f);// ��ʾ��Բ�δ�С
		lineDataSet.setColor(Color.BLUE);// ��ʾ��ɫ
		lineDataSet.setCircleColor(Color.BLUE);// Բ�ε���ɫ
		lineDataSet.setHighLightColor(Color.BLUE); // �������ߵ���ɫ

		LineDataSet CurlineDataSet = new LineDataSet(yValuesCur, "ϵ�е���: 100A");// ��y��ļ��������ò���
		CurlineDataSet.setLineWidth(0.7f); // �߿�
		CurlineDataSet.setAxisDependency(AxisDependency.RIGHT);
		CurlineDataSet.setCircleSize(0.5f);// ��ʾ��Բ�δ�С
		CurlineDataSet.setColor(Color.RED);// ��ʾ��ɫ
		CurlineDataSet.setCircleColor(Color.RED);// Բ�ε���ɫ
		CurlineDataSet.setHighLightColor(Color.RED); // �������ߵ���ɫ

		List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
		lineDataSets.add(lineDataSet); // add �۵�ѹ����
		lineDataSets.add(CurlineDataSet); // add ϵ�е�������

		// create a data object with the datasets
		LineData lineData = new LineData(xValues, lineDataSets);

		return lineData;
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
					if (days >= 3) {
						Toast.makeText(getApplicationContext(), "������̫�󣺽�ֹ����-��ʼ����>2,������ѡ������", 1).show();
					} else {
						mLineChart.setVisibility(View.VISIBLE);
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
