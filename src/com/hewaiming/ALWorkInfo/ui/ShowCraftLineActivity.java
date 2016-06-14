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
		tv_title.setText(PotNo + "�۹�������  " + SelDate);
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void showChart(LineChart lineChart, LineData mLineData, int color) {
		lineChart.setDrawBorders(false); // �Ƿ�������ͼ����ӱ߿�

		lineChart.setDescription("");// ��������
		// ���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview
		lineChart.setNoDataTextDescription("����ҪΪ����ͼ�ṩ����.");
		lineChart.setDrawGridBackground(false); // �Ƿ���ʾ�����ɫ
		lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ���ĵ���ɫ�����������Ǹ���ɫ����һ��͸����

		lineChart.setTouchEnabled(true); // �����Ƿ���Դ���

		lineChart.setDragEnabled(true);// �Ƿ������ק
		lineChart.setScaleEnabled(true);// �Ƿ��������
		xAxis = lineChart.getXAxis();
		yAxis_left = lineChart.getAxisLeft();
		yAxis_right = lineChart.getAxisRight();

		yAxis_left.setEnabled(false);
		yAxis_left.setDrawAxisLine(false);
		yAxis_left.setDrawLimitLinesBehindData(true);

		yAxis_right.setEnabled(false);
		yAxis_right.setDrawAxisLine(false);
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

		// // ���Y�� ��ѹ
		// YAxis yAxis_potv = lineChart.getAxisLeft();
		// yAxis_potv.setEnabled(true);
		// yAxis_potv.setDrawAxisLine(true);
		// yAxis_potv.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		// yAxis_potv.setTextSize(4f);
		// yAxis_potv.setTextColor(Color.BLUE);
		// yAxis_potv.setAxisMaxValue(6000);
		// yAxis_potv.setAxisMinValue(0);
		// yAxis.setLabelRotationAngle(90f);;

		// // �ұ�Y�� ϵ�е���
		// YAxis yAxis_cur = lineChart.getAxisRight();
		// yAxis_cur.setEnabled(true);
		// yAxis_cur.setDrawAxisLine(true);
		// yAxis_cur.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		// yAxis_cur.setTextSize(4f);
		// yAxis_cur.setAxisMaxValue(2300);
		// yAxis_cur.setAxisMinValue(0);
		// yAxis_cur.setTextColor(Color.RED);

		lineChart.setData(mLineData); // �������� һ��Ҫ����CHART�趨����֮��
		lineChart.animateX(200); // ����ִ�еĶ���,x��
	}

	private LineData getLineData(int id, int count, float range) {

		List<String> xValues = new ArrayList<String>();
		List<Entry> yValues = new ArrayList<Entry>(); // y�������		

		for (int i = 0; i < count; i++) {
			xValues.add(list_daytable.get(i).getDdate().toString());// x����ʾ�����ݣ�����Ĭ��ʹ�������±���ʾ
			switch (id) {
			case 0:
				float yvalue_SetV = (float) (list_daytable.get(i).getSetV() * range);
				yValues.add(new Entry(yvalue_SetV, i)); // y��� �趨��ѹ
				break;
			case 1:
				float yvalue_WorkV = (float) (list_daytable.get(i).getWorkV() * range);
				yValues.add(new Entry(yvalue_WorkV, i)); // y��� ������ѹ
				break;
			case 2:
				float value_AvgV = (float) (list_daytable.get(i).getAverageV() * range);
				yValues.add(new Entry(value_AvgV, i)); // y��� ƽ����ѹ
				break;
			case 3:
				float value_Noise = (float) (list_daytable.get(i).getZF() * range);
				yValues.add(new Entry(value_Noise, i)); // y��� ����
				break;
			case 4:
				// float value_SetALF = (float) (list_daytable.get(i).getgetZF()
				// * range);
				// yValues.add(new Entry(value_Noise, i)); // y��� �趨������
				break;
			case 5:
				float value_ALF = (float) (list_daytable.get(i).getFhlCnt() * range);
				yValues.add(new Entry(value_ALF, i)); // y��� ����������
				break;
			case 6:
				float value_AeCnt = (float) (list_daytable.get(i).getAeCnt() * range);
				yValues.add(new Entry(value_AeCnt, i)); // y��� ЧӦ����
				break;
			case 7:
				float value_YhlCnt = (float) (list_daytable.get(i).getYhlCnt() * range);
				yValues.add(new Entry(value_YhlCnt, i)); // y��� ��������
				break;

			case 8:
				float value_AlZSL = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_AlZSL, i)); // y��� ����ָʾ��
				break;
			case 9:
				float value_FZB = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_FZB, i)); // y��� ���ӱ�
				break;
			case 10:
				float value_DJWD = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_DJWD, i)); // y��� ����¶�
				break;
			case 11:
				float value_LSP = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_LSP, i)); // y��� ��ˮƽ
				break;
			}

		}

		// y������ݼ���
		LineDataSet mlineDataSet = new LineDataSet(yValues, "");/* ��ʾ�ڱ���ͼ�� */
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);
		// ��y��ļ��������ò���
		// lineDataSet_SetV.setAxisDependency(AxisDependency.LEFT);
		mlineDataSet.setLineWidth(1.5f); // �߿�
		mlineDataSet.setCircleSize(1f);// ��ʾ��Բ�δ�С
		mlineDataSet.setColor(Color.BLUE);// ��ʾ��ɫ
		mlineDataSet.setCircleColor(Color.BLUE);// Բ�ε���ɫ
		mlineDataSet.setHighLightColor(Color.BLUE); // �������ߵ���ɫ

		List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
		lineDataSets.add(mlineDataSet); // add �趨��ѹ

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
