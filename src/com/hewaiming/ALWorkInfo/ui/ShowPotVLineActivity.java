package com.hewaiming.ALWorkInfo.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener_other;
import com.hewaiming.ALWorkInfo.SlideBottomPanel.SlideBottomPanel;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_RealRecordAdapter;
import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.floatButton.ALWorkInfoApplication;
import com.hewaiming.ALWorkInfo.floatButton.FloatView;
import com.hewaiming.ALWorkInfo.floatButton.FloatingActionButton;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPotVLineActivity extends Activity
		implements HttpGetListener, HttpGetListener_other, OnClickListener, OnScrollListener {
	
	private Button  backBtn;
	private TextView tv_title;	
	
	private HttpPost_BeginDate_EndDate http_post;
	private String potno_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/PotVoltage.php";
	private String RealRec_URL = "http://125.64.59.11:8000/scgy/android/odbcPhP/RealRecordTable_potno_date.php";
	private String PotNo, BeginDate, EndDate;

	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<PotV> listBean = null;
	private LineChart mLineChart;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;

	private FloatView floatView = null; // ������FLOAT BUTTON
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;
	private FloatingActionButton show_RealRec_btn;
	private ListView lv_realrec;
	private RelativeLayout mHead;
	private HttpPost_BeginDate_EndDate_other http_post_getRealRec;
	private Context mContext;
	private List<RealRecord> listBean_RealRec = null;
	private HSView_RealRecordAdapter realRec_Adapter;
	private com.hewaiming.ALWorkInfo.SlideBottomPanel.SlideBottomPanel sbv;
	private View Layout_select;
	private View Layout_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_potv_line);
		mContext = this;
		BeginDate=getIntent().getStringExtra("Begin_Date");
		EndDate=getIntent().getStringExtra("End_Date");
		PotNo=getIntent().getStringExtra("PotNo");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		init_title();
		init_HSView();
		hide_Layout();
		init_GetRemoteData();
		show_RealRec_btn = (FloatingActionButton) findViewById(R.id.floatBtn_show_realRec); // ����������ť
		show_RealRec_btn.setOnClickListener(this);
		sbv = (SlideBottomPanel) findViewById(R.id.sbv);
		// createView(); // ����������ť
	}

	private void init_GetRemoteData() {
		mLineChart.setVisibility(View.VISIBLE);
		http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo,
				BeginDate, EndDate, this, this).execute();
		
	}

	private void hide_Layout() {
	   Layout_select=findViewById(R.id.Layout_selection);
	   Layout_ok=findViewById(R.id.Layout_OK);
	   Layout_select.setVisibility(View.GONE);
	   Layout_ok.setVisibility(View.GONE);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// windowManager.removeView(floatView); // �ڳ����˳�(Activity���٣�ʱ������������
	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#ffffff"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());     
		lv_realrec = (ListView) findViewById(R.id.lv_realrec_potno);
		lv_realrec.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_realrec.setCacheColorHint(0);
		lv_realrec.setOnScrollListener(this);

	}

	

	

	private void init_title() {
		mLineChart = (LineChart) findViewById(R.id.chart_PotV);
		mLineChart.setVisibility(View.VISIBLE);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(PotNo+"��ѹ����ͼ");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
//		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
//		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
//		isShowingBtn.setOnClickListener(this);

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
			show_RealRec_btn.setVisibility(View.VISIBLE);
		}
	}

	private void showChart(LineChart lineChart, LineData mLineData, int color) {
		lineChart.setDrawBorders(false); // �Ƿ�������ͼ�����ӱ߿�

		// no description text
		lineChart.setDescription("");// ��������
		// ���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview
		lineChart.setNoDataTextDescription("����ҪΪ����ͼ�ṩ����.");

		// enable / disable grid background
		lineChart.setDrawGridBackground(false); // �Ƿ���ʾ������ɫ
		lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ����ĵ���ɫ�����������Ǹ���ɫ����һ��͸����

		lineChart.setTouchEnabled(true); // �����Ƿ���Դ���

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
		yAxis_potv.setAxisMaxValue(10000);
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
		lineDataSet.setDrawValues(true);

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

	private void createView() {
		floatView = new FloatView(getApplicationContext());
		floatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "show RealRECORD", 1).show();// ��ʾʵʱ��¼

			}
		});
		floatView.setImageResource(R.drawable.realrecord_text); // ����򵥵����Դ���icon������ʾ
		floatView.setAdjustViewBounds(true);
		floatView.setMaxWidth(100);
		floatView.setMaxHeight(50);
		// ��ȡWindowManager
		windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		// ����LayoutParams(ȫ�ֱ�������ز���
		windowManagerParams = ((ALWorkInfoApplication) getApplication()).getWindowParams();

		windowManagerParams.type = LayoutParams.TYPE_PHONE; // ����window type
		windowManagerParams.format = PixelFormat.TRANSPARENT; // ����ͼƬ��ʽ��Ч��Ϊ����͸��
		// ����Window flag
		windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * ע�⣬flag��ֵ����Ϊ�� �����flags���Ե�Ч����ͬ���������� ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
		 * LayoutParams.FLAG_NOT_TOUCH_MODAL ��Ӱ�������¼�
		 * LayoutParams.FLAG_NOT_FOCUSABLE ���ɾ۽� LayoutParams.FLAG_NOT_TOUCHABLE
		 * ���ɴ���
		 */
		// �����������������Ͻǣ����ڵ�������
		windowManagerParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ

		int width = windowManager.getDefaultDisplay().getWidth(); // ��ȡ��Ļ�ĸ߶ȺͿ���
		int height = windowManager.getDefaultDisplay().getHeight();

		windowManagerParams.width = width;
		windowManagerParams.height = height;
		// �����������ڳ�������
		windowManagerParams.width = LayoutParams.WRAP_CONTENT;
		windowManagerParams.height = LayoutParams.WRAP_CONTENT;
		// ��ʾmyFloatViewͼ��

		windowManager.addView(floatView, windowManagerParams);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
//
//		case R.id.btn_ok:
//			if (EndDate.compareTo(BeginDate) < 0) {
//				Toast.makeText(getApplicationContext(), "����ѡ�񲻶ԣ���ֹ����С�ڿ�ʼ����", 1).show();
//			} else {
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//				try {
//					Date bdate = df.parse(BeginDate);
//					Date edate = df.parse(EndDate);
//					long TIME_DAY_MILLISECOND = 86400000;
//					Long days = (edate.getTime() - bdate.getTime()) / (TIME_DAY_MILLISECOND);
//					if (days >= 3) {
//						Toast.makeText(getApplicationContext(), "������̫�󣺽�ֹ����-��ʼ����>2,������ѡ������", 1).show();
//					} else {
//						mLineChart.setVisibility(View.VISIBLE);
//						http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo,
//								BeginDate, EndDate, this, this).execute();
//					}
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
//			break;
		case R.id.floatBtn_show_realRec:
			// Toast.makeText(getApplicationContext(), "ok", 1).show();		
			sbv.displayPanel();
			http_post_getRealRec = (HttpPost_BeginDate_EndDate_other) new HttpPost_BeginDate_EndDate_other(RealRec_URL,
					2, PotNo, BeginDate, EndDate, this, this).execute();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (sbv.isPanelShowing()) {
			sbv.hide();
			return;
		} else {
			sbv.displayPanel();
		}
		super.onBackPressed();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			// ������ͷ �� listView�ؼ���touchʱ�������touch���¼��ַ��� ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	@Override
	public void GetOtherDataUrl(String Data) {
		if (Data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[ʵʱ��¼]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean_RealRec != null) {
				if (listBean_RealRec.size() > 0) {
					listBean_RealRec.clear(); // ���LISTVIEW ��ǰ������
					realRec_Adapter.onDateChange(listBean_RealRec);
				}
			}
		} else {
			listBean_RealRec = new ArrayList<RealRecord>();
			listBean_RealRec.clear();
			listBean_RealRec = JsonToBean_Area_Date.JsonArrayToRealRecordBean(Data, JXList);
			realRec_Adapter = new HSView_RealRecordAdapter(mContext, R.layout.item_hsview_real_record, listBean_RealRec,
					mHead);
			lv_realrec.setAdapter(realRec_Adapter);
		}
	}
}