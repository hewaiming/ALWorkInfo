package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.config.DemoBase;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.socket.SocketTransceiver;
import com.hewaiming.ALWorkInfo.socket.TcpClient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import bean.PotStatus;
import bean.RealTime;
import bean.RequestAction;

public class RealTimeLineActivity extends DemoBase implements OnClickListener, OnChartValueSelectedListener {
	String hostIP = "192.168.15.18";
	int port = 1234;
	private LineChart mChart;
	private TextView tv_title;
	private Button backBtn;
	private ImageButton isShowingBtn;
	private LinearLayout showArea;
	private String PotNo;
	private boolean hideAction;
	private Spinner spinner_area, spinner_begindate, spinner_enddate;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter;
	private List<String> PotNoList = null;
	private AbsSpinner spinner_potno;
	private ArrayAdapter<String> PotNo_adapter;
	private Button findBtn;
	private View include_selector;
	protected boolean DoRun = true;
	private Timer timer = null;
	private TimerTask timerTask = null;

	private Handler handler = new Handler(Looper.getMainLooper());
	private TcpClient client = new TcpClient() {

		@Override
		public void onConnect(SocketTransceiver transceiver) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onConnectFailed() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(RealTimeLineActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
				}
			});

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, String s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, final RealTime realTime) {
//			System.out.println(realTime.toString());
			handler.post(new Runnable() {
				@Override
				public void run() {
					LineData data = mChart.getData();
					if (data != null) {
						ILineDataSet set = data.getDataSetByIndex(0);
				
						 ArrayList<Entry> yValueList = new ArrayList<>();
						// set.addEntry(...); // can be called as well
						ILineDataSet setCUR = data.getDataSetByIndex(1);

						if (set == null) {
							set = createSet();
							data.addDataSet(set);
						}
						//实时系列电流
						if (setCUR == null) {
							setCUR = createSet_CUR();
							data.addDataSet(setCUR);
						}

						Calendar c = Calendar.getInstance();

						data.addXValue(String.valueOf(c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)) + "." + c.get(Calendar.SECOND));
					
						data.addEntry(new Entry((float) realTime.getPotv(), set.getEntryCount()), 0);//实时槽压数据
					
						data.addEntry(new Entry((float) realTime.getCur(), setCUR.getEntryCount()), 1);//实时系列电流
						// let the chart know it's data has changed
						mChart.notifyDataSetChanged();
						mChart.invalidate();
						// limit the number of visible entries
						mChart.setVisibleXRangeMaximum(30);
						// mChart.setVisibleYRange(30, AxisDependency.LEFT);

						// move to the latest entry
						mChart.moveViewToX(data.getXValCount() - 31);
					
					}

				}
			});

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, ArrayList<PotStatus> potStatus) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDisconnect(SocketTransceiver transceiver) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_realtime_linechart);
		GetDataFromIntent();
		init_title();
		init_area();
		init_potNo();
		connect();
		timer = new Timer();
	
		if (hideAction) {
			include_selector = findViewById(R.id.include_select_all);
			include_selector.setVisibility(View.GONE);
			GetDataFromNet();
		}

		mChart = (LineChart) findViewById(R.id.chart1);
		mChart.setOnChartValueSelectedListener(this);
		LineData linedata = new LineData();
		showRealTimeLine(linedata);

	}

	private void GetDataFromNet() {
		SendActionToServer();
		
	}

	private void showRealTimeLine(LineData data) {
		// no description text
		mChart.setDescription("");
		mChart.setNoDataTextDescription("图表需要数据.");

		// enable touch gestures
		mChart.setTouchEnabled(true);

		// enable scaling and dragging
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setDrawGridBackground(false);

		// if disabled, scaling can be done on x- and y-axis separately
		mChart.setPinchZoom(true);
		

		// set an alternative background color
		mChart.setBackgroundColor(Color.WHITE);

		data.setValueTextColor(Color.DKGRAY);
		

		// add empty data
		mChart.setData(data);	
        
		// Typeface tf = Typeface.createFromAsset(getAssets(),
		// "OpenSans-Regular.ttf");

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);  
		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.LINE);
		// l.setTypeface(tf);
		l.setTextColor(Color.DKGRAY);

		XAxis xl = mChart.getXAxis();
		// xl.setTypeface(tf);
		xl.setTextColor(Color.BLACK);
		xl.setDrawGridLines(false);
		xl.setAvoidFirstLastClipping(true);
		xl.setSpaceBetweenLabels(5);
		xl.setEnabled(true);

		YAxis leftAxis = mChart.getAxisLeft();
		// leftAxis.setTypeface(tf);
		leftAxis.setTextColor(Color.BLUE);
		leftAxis.setAxisMaxValue(7100f);
		leftAxis.setAxisMinValue(0f);
		leftAxis.setDrawGridLines(false);

		YAxis rightAxis = mChart.getAxisRight();
		rightAxis.setTextColor(Color.RED);
		rightAxis.setAxisMaxValue(2500f);
		rightAxis.setAxisMinValue(0f);
		rightAxis.setDrawGridLines(false);
		rightAxis.setEnabled(true);
	}

	private void GetDataFromIntent() {
		PotNo = getIntent().getStringExtra("PotNo");
		hideAction = getIntent().getBooleanExtra("Hide_Action", false);
		// JXList = (List<Map<String, Object>>)
		// getIntent().getSerializableExtra("JXList");

	}

	private void init_title() {
		// layout_list=findViewById(R.id.Layout_AeRecord);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(PotNo + "实时曲线");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);

		spinner_begindate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_begindate.setVisibility(View.GONE);
		spinner_enddate = (Spinner) findViewById(R.id.spinner_Enddate);
		spinner_enddate.setVisibility(View.GONE);

		findBtn = (Button) findViewById(R.id.btn_ok);
		findBtn.setOnClickListener(this);

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
		// PotNoList.add(0, "全部槽号");

		spinner_potno.setSelection(0);
		PotNo = PotNoList.get(0).toString();
		PotNo_adapter.notifyDataSetChanged();// 通知数据改变
	}

	private void init_potNo() {
		spinner_potno = (Spinner) findViewById(R.id.spinner_PotNo);
		PotNoList = new ArrayList<String>();
		for (int i = 1101; i <= 1136; i++) {
			PotNoList.add(i + "");
		}
		// PotNoList.add(0, "全部槽号");
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

//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		switch (item.getItemId()) {
//		case R.id.actionAdd: {
//			addEntry();
//			break;
//		}
//		case R.id.actionClear: {
//			mChart.clearValues();
//			Toast.makeText(this, "Chart cleared!", Toast.LENGTH_SHORT).show();
//			break;
//		}
//		case R.id.actionFeedMultiple: {
//			SendActionToServer();
//			break;
//		}
//		}
//		return true;
//	}

//	private void addEntry() {
//
//		LineData data = mChart.getData();
//		if (data != null) {
//			ILineDataSet set = data.getDataSetByIndex(0);
//			// set.addEntry(...); // can be called as well
//
//			if (set == null) {
//				set = createSet();
//				data.addDataSet(set);
//			}
//
//			Calendar c = Calendar.getInstance();
//
//			data.addXValue(String.valueOf(c.get(Calendar.MINUTE)) + ':' + c.get(Calendar.MILLISECOND));
//			data.addEntry(new Entry((float) (Math.random() * 40) + 30f, set.getEntryCount()), 0);
//
//			// let the chart know it's data has changed
//			mChart.notifyDataSetChanged();
//
//			// limit the number of visible entries
//			mChart.setVisibleXRangeMaximum(30);
//			// mChart.setVisibleYRange(30, AxisDependency.LEFT);
//
//			// move to the latest entry
//			mChart.moveViewToX(data.getXValCount() - 31);
//			// mChart.invalidate();
//			// this automatically refreshes the chart (calls invalidate())
//			// mChart.moveViewTo(data.getXValCount()-7, 55f,
//			// AxisDependency.LEFT);
//		}
//	}

	private LineDataSet createSet() {

		LineDataSet set = new LineDataSet(null, PotNo + "实时槽压");
		set.setAxisDependency(AxisDependency.LEFT);
		set.setColor(ColorTemplate.getHoloBlue());
		set.setCircleColor(Color.BLUE);
		set.setLineWidth(2f);
		set.setCircleRadius(1f);
		set.setFillAlpha(65);
		set.setFillColor(ColorTemplate.getHoloBlue());
		set.setHighLightColor(Color.rgb(244, 117, 117));
		set.setValueTextColor(Color.BLUE);
		set.setValueTextSize(9f);
		set.setDrawValues(false);
		set.setDrawCircles(false);
		return set;
	}

	private LineDataSet createSet_CUR() {

		LineDataSet set = new LineDataSet(null, "系列电流");
		set.setAxisDependency(AxisDependency.RIGHT);
		set.setColor(Color.RED);
		set.setCircleColor(Color.RED);
		set.setLineWidth(2f);
		set.setCircleRadius(1f);
		set.setFillAlpha(65);
		set.setFillColor(Color.RED);
		set.setHighLightColor(Color.rgb(244, 117, 117));
		set.setValueTextColor(Color.RED);
		set.setValueTextSize(9f);
		set.setDrawValues(false);
		set.setDrawCircles(false);
		return set;
	}
	
	private void SendActionToServer() {
		if (timer == null) {
			timer = new Timer();
		}
		if (timerTask != null) {
			timerTask.cancel();
		}
		timerTask = new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						sendRealTimeAction();
						// addEntry();
					}
				});

			}
		};
		timer.schedule(timerTask, 0, 3000);

	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		Log.i("Entry selected", e.toString());
	}

	@Override
	public void onNothingSelected() {
		Log.i("Nothing selected", "Nothing selected.");
	}

	@Override
	protected void onStop() {
		client.disconnect();
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			if (timer != null) {
				timer.cancel();
			}
			// DoRun=false;
			finish();
			break;
		case R.id.btn_isSHOW: // 显示或隐藏
			if (showArea.getVisibility() == View.GONE) {
				showArea.setVisibility(View.VISIBLE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_up));
			} else {
				showArea.setVisibility(View.GONE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_down));
			}
			break;
		case R.id.btn_ok:
			tv_title.setText(PotNo + "实时曲线");
			if (mChart != null) {
				mChart.clearValues();
				// mChart.notifyDataSetChanged();
			}
			LineData linedata = new LineData();
			showRealTimeLine(linedata);
			SendActionToServer();
			break;
		}

	}

	private void sendRealTimeAction() {
		try {
			RequestAction action = new RequestAction();
			action.setActionId(1);
			action.setPotNo_Area(PotNo);
			client.getTransceiver().send(action);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置IP和端口地址,连接或断开
	 */
	private void connect() {
		if (client.isConnected()) {
			// 断开连接
			client.disconnect();
		} else {
			try {
				client.connect(hostIP, port);
			} catch (NumberFormatException e) {
				Toast.makeText(this, "端口错误", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}

}