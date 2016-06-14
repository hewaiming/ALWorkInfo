package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.dayTable;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ShowCraftLineActivity extends Activity implements OnClickListener {

	private Button backBtn;
	private TextView tv_title;
	private String PotNo;
	// private LineChart mLineChart;
	private String SelDate;
	private List<dayTable> list_daytable = null;
	private XAxis xAxis;
	private YAxis yAxis_left, yAxis_right;
	private LineChart[] mCharts = new LineChart[12];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_craft_line);
		PotNo = getIntent().getStringExtra("PotNo");
		SelDate = getIntent().getStringExtra("Begin_End_Date");
		list_daytable = new ArrayList<dayTable>();
		list_daytable = (List<dayTable>) getIntent().getSerializableExtra("list_daytable");
		init_title();

		mCharts[0] = (LineChart) findViewById(R.id.chart_SetV);
		mCharts[1] = (LineChart) findViewById(R.id.chart_WorkV);
		mCharts[2] = (LineChart) findViewById(R.id.chart_AvgV);
		mCharts[3] = (LineChart) findViewById(R.id.chart_Noise);
		mCharts[4] = (LineChart) findViewById(R.id.chart_SetALF);
		mCharts[5] = (LineChart) findViewById(R.id.chart_ALF);
		mCharts[6] = (LineChart) findViewById(R.id.chart_AeCnt);
		mCharts[7] = (LineChart) findViewById(R.id.chart_YhlCnt);
		mCharts[8] = (LineChart) findViewById(R.id.chart_ALCntZSL);
		mCharts[9] = (LineChart) findViewById(R.id.chart_FZB);
		mCharts[10] = (LineChart) findViewById(R.id.chart_DJWD);
		mCharts[11] = (LineChart) findViewById(R.id.chart_LSP);

		for (int i = 0; i < mCharts.length; i++) {
			LineData mLineData = getLineData(i, list_daytable.size(), 1);
			showChart(mCharts[i], mLineData, Color.rgb(255, 255, 255));
		}

	}

	private void init_title() {
		// mLineChart = (LineChart) findViewById(R.id.chart_Craft);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setTextSize(12);
		tv_title.setText(PotNo + "槽工艺曲线  " + SelDate);
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

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
		xAxis = lineChart.getXAxis();
		yAxis_left = lineChart.getAxisLeft();
		yAxis_right = lineChart.getAxisRight();

		yAxis_left.setEnabled(false);
		yAxis_left.setDrawAxisLine(false);
		yAxis_left.setDrawLimitLinesBehindData(true);

		yAxis_right.setEnabled(false);
		yAxis_right.setDrawAxisLine(false);
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

		// // 左边Y轴 槽压
		// YAxis yAxis_potv = lineChart.getAxisLeft();
		// yAxis_potv.setEnabled(true);
		// yAxis_potv.setDrawAxisLine(true);
		// yAxis_potv.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		// yAxis_potv.setTextSize(4f);
		// yAxis_potv.setTextColor(Color.BLUE);
		// yAxis_potv.setAxisMaxValue(6000);
		// yAxis_potv.setAxisMinValue(0);
		// yAxis.setLabelRotationAngle(90f);;

		// // 右边Y轴 系列电流
		// YAxis yAxis_cur = lineChart.getAxisRight();
		// yAxis_cur.setEnabled(true);
		// yAxis_cur.setDrawAxisLine(true);
		// yAxis_cur.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		// yAxis_cur.setTextSize(4f);
		// yAxis_cur.setAxisMaxValue(2300);
		// yAxis_cur.setAxisMinValue(0);
		// yAxis_cur.setTextColor(Color.RED);

		lineChart.setData(mLineData); // 设置数据 一定要放在CHART设定参数之后
		lineChart.animateX(200); // 立即执行的动画,x轴
	}

	private LineData getLineData(int id, int count, float range) {

		List<String> xValues = new ArrayList<String>();
		List<Entry> yValues = new ArrayList<Entry>(); // y轴的数据		

		for (int i = 0; i < count; i++) {
			xValues.add(list_daytable.get(i).getDdate().toString());// x轴显示的数据，这里默认使用数字下标显示
			switch (id) {
			case 0:
				float yvalue_SetV = (float) (list_daytable.get(i).getSetV() * range);
				yValues.add(new Entry(yvalue_SetV, i)); // y轴的 设定电压
				break;
			case 1:
				float yvalue_WorkV = (float) (list_daytable.get(i).getWorkV() * range);
				yValues.add(new Entry(yvalue_WorkV, i)); // y轴的 工作电压
				break;
			case 2:
				float value_AvgV = (float) (list_daytable.get(i).getAverageV() * range);
				yValues.add(new Entry(value_AvgV, i)); // y轴的 平均电压
				break;
			case 3:
				float value_Noise = (float) (list_daytable.get(i).getZF() * range);
				yValues.add(new Entry(value_Noise, i)); // y轴的 噪音
				break;
			case 4:
				// float value_SetALF = (float) (list_daytable.get(i).getgetZF()
				// * range);
				// yValues.add(new Entry(value_Noise, i)); // y轴的 设定氟化铝
				break;
			case 5:
				float value_ALF = (float) (list_daytable.get(i).getFhlCnt() * range);
				yValues.add(new Entry(value_ALF, i)); // y轴的 氟化铝料量
				break;
			case 6:
				float value_AeCnt = (float) (list_daytable.get(i).getAeCnt() * range);
				yValues.add(new Entry(value_AeCnt, i)); // y轴的 效应次数
				break;
			case 7:
				float value_YhlCnt = (float) (list_daytable.get(i).getYhlCnt() * range);
				yValues.add(new Entry(value_YhlCnt, i)); // y轴的 氧化铝量
				break;

			case 8:
				float value_AlZSL = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_AlZSL, i)); // y轴的 出铝指示量
				break;
			case 9:
				float value_FZB = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_FZB, i)); // y轴的 分子比
				break;
			case 10:
				float value_DJWD = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_DJWD, i)); // y轴的 电解温度
				break;
			case 11:
				float value_LSP = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_LSP, i)); // y轴的 铝水平
				break;
			}

		}

		// y轴的数据集合
		LineDataSet mlineDataSet = new LineDataSet(yValues, "");/* 显示在比例图上 */
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);
		// 用y轴的集合来设置参数
		// lineDataSet_SetV.setAxisDependency(AxisDependency.LEFT);
		mlineDataSet.setLineWidth(1.5f); // 线宽
		mlineDataSet.setCircleSize(1f);// 显示的圆形大小
		mlineDataSet.setColor(Color.BLUE);// 显示颜色
		mlineDataSet.setCircleColor(Color.BLUE);// 圆形的颜色
		mlineDataSet.setHighLightColor(Color.BLUE); // 高亮的线的颜色

		List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
		lineDataSets.add(mlineDataSet); // add 设定电压

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
		}
	}

}
