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
import com.hewaiming.ALWorkInfo.config.MyApplication;
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

	private Button backBtn;
	private TextView tv_title;

	private HttpPost_BeginDate_EndDate http_post;
	private String potno_url = ":8000/scgy/android/odbcPhP/PotVoltage.php";
	private String RealRec_URL = ":8000/scgy/android/odbcPhP/RealRecordTable_potno_date.php";
	private String PotNo, BeginDate, EndDate;

	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<PotV> listBean = null;
	private LineChart mLineChart;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;

	//private FloatView floatView = null; // 以下是FLOAT BUTTON
	//private WindowManager windowManager = null;
	//private WindowManager.LayoutParams windowManagerParams = null;
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
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_potv_line);
		MyApplication.getInstance().addActivity(this);
		mContext = this;
		GetDataFromIntent();
		init_title();
		init_HSView();
		hide_Layout();
		init_GetRemoteData();
		show_RealRec_btn = (FloatingActionButton) findViewById(R.id.floatBtn_show_realRec); // 创建浮动按钮
		show_RealRec_btn.setOnClickListener(this);
		sbv = (SlideBottomPanel) findViewById(R.id.sbv);// 创建浮动按钮
		
	}

	@SuppressWarnings("unchecked")
	private void GetDataFromIntent() {
		BeginDate = getIntent().getStringExtra("Begin_Date");
		EndDate = getIntent().getStringExtra("End_Date");
		PotNo = getIntent().getStringExtra("PotNo");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		potno_url = "http://" + ip + potno_url;
		RealRec_URL = "http://" + ip + RealRec_URL;
	}

	private void init_GetRemoteData() {
		mLineChart.setVisibility(View.VISIBLE);
		http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo, BeginDate, EndDate,
				this, this).execute();

	}

	private void hide_Layout() {
		Layout_select = findViewById(R.id.Layout_selection);
		Layout_ok = findViewById(R.id.Layout_OK);
		Layout_select.setVisibility(View.GONE);
		Layout_ok.setVisibility(View.GONE);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// windowManager.removeView(floatView); // 在程序退出(Activity销毁）时销毁悬浮窗口
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
		tv_title.setText(PotNo + "槽压曲线图");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);	

	}

	@Override
	public void GetDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[槽压]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
				}
			}
		} else {
			listBean = new ArrayList<PotV>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToPotVBean(data);
			LineData mLineData = getLineData(listBean.size(), 1);
			showChart(mLineChart, mLineData,Color.WHITE);
			show_RealRec_btn.setVisibility(View.VISIBLE);
		}
	}

	private void showChart(LineChart lineChart, LineData mLineData, int color) {
		lineChart.setDrawBorders(false); // 是否在折线图上添加边框
		
		lineChart.setDescription("");// 数据描述
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
		lineChart.setNoDataTextDescription("你需要为曲线图提供数据.");
		
		lineChart.setDrawGridBackground(false); // 是否显示表格颜色
		lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

		lineChart.setTouchEnabled(true); // 设置是否可以触摸

		lineChart.setDragEnabled(true);// 是否可以拖拽
		lineChart.setScaleEnabled(true);// 是否可以缩放
		// lineChart.getAxisRight().setEnabled(true); // 右边 的坐标轴
		// lineChart.getAxisLeft().setEnabled(true);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// 设置横坐标在底部
		lineChart.getXAxis().setGridColor(Color.TRANSPARENT);// 去掉网格中竖线的显示
		// if disabled, scaling can be done on x- and y-axis separately
		lineChart.setPinchZoom(false);//

		lineChart.setBackgroundColor(color);// 设置背景

		// get the legend (only possible after setting data)
		Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
		mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
		mLegend.setForm(LegendForm.CIRCLE);// 样式
		mLegend.setFormSize(5f);// 字体
		mLegend.setTextColor(Color.BLACK);// 颜色
		// mLegend.setTypeface(mTf);// 字体

		// 左边Y轴 槽压
		YAxis yAxis_potv = lineChart.getAxisLeft();
		yAxis_potv.setEnabled(true);
		yAxis_potv.setDrawAxisLine(true);
		yAxis_potv.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		yAxis_potv.setTextSize(4f);
		yAxis_potv.setTextColor(Color.BLUE);
		yAxis_potv.setAxisMaxValue(10000);
		yAxis_potv.setAxisMinValue(0);
		// yAxis.setLabelRotationAngle(90f);;

		// 右边Y轴 系列电流
		YAxis yAxis_cur = lineChart.getAxisRight();
		yAxis_cur.setEnabled(true);
		yAxis_cur.setDrawAxisLine(true);
		yAxis_cur.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		yAxis_cur.setTextSize(4f);
		yAxis_cur.setAxisMaxValue(2300);
		yAxis_cur.setAxisMinValue(0);
		yAxis_cur.setTextColor(Color.RED);

		lineChart.setData(mLineData); // 设置数据 一定要放在CHART设定参数之后
		lineChart.animateX(200); // 立即执行的动画,x轴
	}

	private LineData getLineData(int count, float range) {

		List<String> xValues = new ArrayList<String>();
		List<Entry> yValues = new ArrayList<Entry>(); // y轴的数据
		List<Entry> yValuesCur = new ArrayList<Entry>();
		for (int i = 0; i < count; i++) {
			xValues.add(listBean.get(i).getDdate().toString());// x轴显示的数据，这里默认使用数字下标显示
			float value = (float) (listBean.get(i).getPotV() * range);
			float temp = (float) (listBean.get(i).getCur() * range);
			yValues.add(new Entry(value, i)); // y轴的槽压数据
			yValuesCur.add(new Entry(temp, i));// y轴的系列电流

		}

		// y轴的数据集合
		LineDataSet lineDataSet = new LineDataSet(yValues, PotNo + "槽电压: mV ");/* 显示在比例图上 */
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);
		// 用y轴的集合来设置参数
		lineDataSet.setAxisDependency(AxisDependency.LEFT);
		lineDataSet.setLineWidth(0.7f); // 线宽
		lineDataSet.setCircleSize(0.5f);// 显示的圆形大小
		lineDataSet.setColor(Color.BLUE);// 显示颜色
		lineDataSet.setCircleColor(Color.BLUE);// 圆形的颜色
		lineDataSet.setHighLightColor(Color.BLUE); // 高亮的线的颜色
		lineDataSet.setDrawValues(true);

		LineDataSet CurlineDataSet = new LineDataSet(yValuesCur, "系列电流:100A");// 用y轴的集合来设置参数
		CurlineDataSet.setLineWidth(0.7f); // 线宽
		CurlineDataSet.setAxisDependency(AxisDependency.RIGHT);
		CurlineDataSet.setCircleSize(0.5f);// 显示的圆形大小
		CurlineDataSet.setColor(Color.RED);// 显示颜色
		CurlineDataSet.setCircleColor(Color.RED);// 圆形的颜色
		CurlineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色

		List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
		lineDataSets.add(lineDataSet); // add 槽电压数据
		lineDataSets.add(CurlineDataSet); // add 系列电流数据

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
		
		case R.id.floatBtn_show_realRec:			
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
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	@Override
	public void GetOtherDataUrl(String Data) {
		if (Data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[实时记录]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBean_RealRec != null) {
				if (listBean_RealRec.size() > 0) {
					listBean_RealRec.clear(); // 清除LISTVIEW 以前的内容
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
