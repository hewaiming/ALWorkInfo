package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hewaiming.ALWorkInfo.R;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetJXRecord_Listener;

import com.hewaiming.ALWorkInfo.Popup.ActionItem;
import com.hewaiming.ALWorkInfo.Popup.TitlePopup;
import com.hewaiming.ALWorkInfo.Update.UpdateManager;
import com.hewaiming.ALWorkInfo.banner.SlideShowView;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;
import com.hewaiming.ALWorkInfo.bean.PotCtrl;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.json.JsonToBean_GetPublicData;
import com.hewaiming.ALWorkInfo.json.JsonToMultiList;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpGetJXRecord;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpGetDate;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpPost_Area;
import com.hewaiming.ALWorkInfo.net.HttpPost_JsonArray;
import com.hewaiming.ALWorkInfo.net.NetDetector;
import com.hewaiming.ALWorkInfo.view.HeaderListView_Params;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
		implements OnItemClickListener, OnClickListener, HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private String ip;
	private int port;
	private SharedPreferences sp;
	private GridView gridView;
	private Button btnMore;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;
	private List<String> date_record = null; // 记录日期
	private List<String> date_table = null; // 报表日期
	private List<Map<String, Object>> JXList = null; // 记录号名

	private String get_dateTable_url = ":8000/scgy/android/odbcPhP/getDate.php";
	private String get_JXName_url = ":8000/scgy/android/odbcPhP/getJXRecordName.php";
	private AsyTask_HttpGetDate mhttpgetdata_date;
	private AsyTask_HttpGetJXRecord mHttpGetData_JXRecord;
	private Context mContext;
	private TitlePopup titlePopup;
	private TextView tv_title, tv_aeTitle;
	private ImageView iv_wifi;
	private SlideShowView bannerView;
	private int[] NormPotS = { 0, 0, 0, 0, 0, 0 }; // 各区正常槽数量
	private int[] AeCnt = { 0, 0, 0, 0, 0, 0 }; // 各区效应次数

	private String AeCnt_url = ":8000/scgy/android/odbcPhP/AeCnt_area_date.php"; // 效应次数
	private List<AeRecord> listBean_AeCnt = null; // 效应次数列表

	private String get_NormPots1_url = ":8000/scgy/android/odbcPhP/GetNormPots1.php";
	private String get_NormPots2_url = ":8000/scgy/android/odbcPhP/GetNormPots2.php";

	private List<PotCtrl> NormPotsList1 = null; // 一厂房槽状态 列表
	private List<PotCtrl> NormPotsList2 = null; // 二厂房槽状态 列表

	private BarChart mBarChart;
	private Handler handler = new Handler(Looper.getMainLooper());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		init(); // 初始化各控件
		NetDetector netDetector = new NetDetector(mContext);
		if (netDetector.isConnectingToInternetNoShow() == 1) {
			iv_wifi.setVisibility(View.GONE);
			bannerView.setVisibility(View.VISIBLE);
		} else {
			iv_wifi.setVisibility(View.VISIBLE);
			bannerView.setVisibility(View.GONE);
		}
		if (NetStatus() != 0) {
			if (!initdate(mContext)) { // 取远程服务器地址和端口
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);// 没有设置远程服务器ip和端口

			} else {
				get_dateTable_url = "http://" + ip + get_dateTable_url;
				get_JXName_url = "http://" + ip + get_JXName_url;
				get_NormPots1_url = "http://" + ip + get_NormPots1_url;
				get_NormPots2_url = "http://" + ip + get_NormPots2_url;
				AeCnt_url = "http://" + ip + AeCnt_url;
				init_commData();
				init_EC();// 显示当前各区效应系数
				checkUpDate(); // 检测版本升级
			}

		} else {
			Toast.makeText(getApplicationContext(), "网络异常！", Toast.LENGTH_LONG).show();
		}
	}

	private void init_EC() {
		ExecutorService exec = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
			@Override
			public void run() {
				System.out.println("获取各区效应系数数据OK，开始显示柱形图表啦，happy去");
				CalcPotsNorm(NormPotsList1);
				CalcPotsNorm(NormPotsList2);
				CalcAeCnt(listBean_AeCnt);
				showEcBar(); // 显示各区效应柱状图
			}
		});

		exec.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("获取一厂房正常槽数量OK，其他数据呢");
				HttpPost_JsonArray jsonParser1 = new HttpPost_JsonArray();
				JSONArray json1 = jsonParser1.makeHttpRequest(get_NormPots1_url, "POST");
				if (json1 != null) {
					Log.d("一厂房：正常槽数量", json1.toString());// 从服务器返回有数据
					NormPotsList1 = new ArrayList<PotCtrl>();
					NormPotsList1 = JsonToBean_GetPublicData.JsonArrayToNormPots(json1.toString());
				} else {
					Log.i("一厂房：正常槽数量 ---", "从PHP服务器无数据返回！");
					tv_title.setTextSize(14);
					tv_title.setText("工作站:" + "请检查远程服务器IP和端口是否正确！");
					Toast.makeText(getApplicationContext(), "没有获取到一厂房正常槽数量，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG)
							.show();
				}
				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		});
		exec.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("获取二厂房正常槽数量OK，其他数据呢");
				HttpPost_JsonArray jsonParser2 = new HttpPost_JsonArray();
				JSONArray json2 = jsonParser2.makeHttpRequest(get_NormPots2_url, "POST");
				if (json2 != null) {
					Log.d("二厂房：正常槽数量", json2.toString());// 从服务器返回有数据
					NormPotsList2 = new ArrayList<PotCtrl>();
					NormPotsList2 = JsonToBean_GetPublicData.JsonArrayToNormPots(json2.toString());
				} else {
					Log.i("二厂房：正常槽数量 ---", "从PHP服务器无数据返回！");
					tv_title.setTextSize(14);
					tv_title.setText("工作站:" + "请检查远程服务器IP和端口是否正确！");
					Toast.makeText(getApplicationContext(), "没有获取到二厂房正常槽数量，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG)
							.show();
				}
				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		});

		// 获取效应次数
		exec.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("获取厂房效应次数OK，其他数据呢");
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String todayValue = sdf.format(dt);
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // 全部槽号
				mparams.add(new BasicNameValuePair("BeginDate", todayValue));
				mparams.add(new BasicNameValuePair("EndDate", todayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(AeCnt_url, "POST", mparams);
				if (json != null) {
					Log.d("厂房：效应次数", json.toString());// 从服务器返回有数据
					listBean_AeCnt = new ArrayList<AeRecord>(); // 初始化效应次数适配器
					// listBean_AeCnt.clear();
					listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());

				} else {
					Log.i("厂房：效应次数 ---", "从PHP服务器无数据返回！");
					tv_title.setTextSize(14);
					tv_title.setText("工作站:" + "请检查远程服务器IP和端口是否正确！");
					Toast.makeText(getApplicationContext(), "没有获取到厂房效应次数，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG).show();
				}
				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		});

		exec.shutdown();

	}

	protected void CalcAeCnt(List<AeRecord> listBean_AeCnt) {
		/*for(int i=0;i<AeCnt.length;i++){
			AeCnt[i]=0;
		}*/
		for (int i = 0; i < listBean_AeCnt.size(); i++) {
			AeRecord aeCnt = new AeRecord();
			aeCnt = listBean_AeCnt.get(i);
			if (aeCnt != null) {
				int potno = aeCnt.getPotNo(); // 槽号
				int aeS = aeCnt.getWaitTime();// 效应次数

				if (potno >= 1101 && potno <= 1136) {
					AeCnt[0] = AeCnt[0] + aeS; // 一厂房一区效应次数总和
				} else if (potno >= 1201 && potno <= 1237) {
					AeCnt[1] = AeCnt[1] + aeS;
				} else if (potno >= 1301 && potno <= 1337) {
					AeCnt[2] = AeCnt[2] + aeS;
				} else if (potno >= 2101 && potno <= 2136) {
					AeCnt[3] = AeCnt[3] + aeS; // 二厂房一区效应次数总和
				} else if (potno >= 2201 && potno <= 2237) {
					AeCnt[4] = AeCnt[4] + aeS;
				} else if (potno >= 2301 && potno <= 2337) {
					AeCnt[5] = AeCnt[5] + aeS;
				}

			} else {
				System.out.println("第 " + i + " 项 效应次数，为空！");
			}

		}
	}

	protected void CalcPotsNorm(List<PotCtrl> normPotsList) {
		/*for(int i=0;i<NormPotS.length;i++){
			NormPotS[i]=0;
		}*/
		for (int i = 0; i < normPotsList.size(); i++) {
			PotCtrl mPotCtrl = new PotCtrl();
			mPotCtrl = normPotsList.get(i);
			if (mPotCtrl != null) {
				int potno = mPotCtrl.getPotNo(); // 槽号
				int mCtrl = mPotCtrl.getCtrls(); // 槽状态字
				// 最后两位（二进制）00代表 正常，01 代表预热，10代表启动，11代表停槽
				if ((mCtrl & 0x00) == 0) {
					if (potno >= 1101 && potno <= 1136) {
						NormPotS[0]++;
					} else if (potno >= 1201 && potno <= 1237) {
						NormPotS[1]++;
					} else if (potno >= 1301 && potno <= 1337) {
						NormPotS[2]++;
					} else if (potno >= 2101 && potno <= 2136) {
						NormPotS[3]++; // 二厂房一区
					} else if (potno >= 2201 && potno <= 2237) {
						NormPotS[4]++;
					} else if (potno >= 2301 && potno <= 2337) {
						NormPotS[5]++;
					}
				}

			} else {
				System.out.println("槽状态数据为NULL!");
			}
		}

	}

	protected void showEcBar() {
		// 图表数据设置
		ArrayList<BarEntry> yVals11 = new ArrayList<>();// Y轴方向一厂房一区数组
		ArrayList<BarEntry> yVals12 = new ArrayList<>();// Y轴方向一厂房二区数组
		ArrayList<BarEntry> yVals13 = new ArrayList<>();// Y轴方向一厂房三区数组
		ArrayList<BarEntry> yVals1 = new ArrayList<>();// Y轴方向一厂房
		ArrayList<BarEntry> yVals21 = new ArrayList<>();// Y轴方向二厂房一区数组
		ArrayList<BarEntry> yVals22 = new ArrayList<>();// Y轴方向二厂房二区数组
		ArrayList<BarEntry> yVals23 = new ArrayList<>();// Y轴方向二厂房三区数组
		ArrayList<BarEntry> yVals2 = new ArrayList<>();// Y轴方向二厂房数组
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y轴方向厂房
		ArrayList<String> xVals = new ArrayList<>();// X轴数据
		int AeTotal1, AeTotal2, AeTotal, PotS1, PotS2, PotS;
		AeTotal1 = AeCnt[0] + AeCnt[1] + AeCnt[2];// 一厂房效应总数
		AeTotal2 = AeCnt[3] + AeCnt[4] + AeCnt[5];// 二厂房效应总数
		AeTotal = AeTotal1 + AeTotal2;// 厂房效应总数
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // 一厂房正常槽总数
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // 二厂房正常槽总数
		PotS = PotS1 + PotS2; // 厂房正常槽总数
		xVals.add("");
		// 添加数据源
		yVals11.add(new BarEntry((float) AeCnt[0] / NormPotS[0], 0));
		yVals12.add(new BarEntry((float) AeCnt[1] / NormPotS[1], 0));
		yVals13.add(new BarEntry((float) AeCnt[2] / NormPotS[2], 0));
		yVals21.add(new BarEntry((float) AeCnt[3] / NormPotS[3], 0));
		yVals22.add(new BarEntry((float) AeCnt[4] / NormPotS[4], 0));
		yVals23.add(new BarEntry((float) AeCnt[5] / NormPotS[5], 0));

		yVals1.add(new BarEntry((float) AeTotal1 / PotS1, 0)); // 一厂房效应系数
		yVals2.add(new BarEntry((float) AeTotal2 / PotS2, 0));// 二厂房效应系数
		yVals.add(new BarEntry((float) AeTotal / PotS, 0));// 厂房效应系数
		BarDataSet barDataSet11 = new BarDataSet(yVals11, "一厂房1区");
		barDataSet11.setColor(Color.RED);// 设置第一组数据颜色
		barDataSet11.setDrawValues(true); // 显示数值
		barDataSet11.setBarSpacePercent(38f);//
		barDataSet11.setValueTextSize(7f);		
		barDataSet11.setValueFormatter(new ValueFormatter() {
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return "1/1区 " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet12 = new BarDataSet(yVals12, "一厂房2区");
		barDataSet12.setColor(Color.GREEN);// 设置第二组数据颜色
		barDataSet12.setBarSpacePercent(38f);
		barDataSet12.setDrawValues(true); // 显示数值
		barDataSet12.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet13 = new BarDataSet(yVals13, "一厂房3区");
		barDataSet13.setColor(Color.YELLOW);// 设置第三组数据颜色
		barDataSet13.setBarSpacePercent(38f);
		barDataSet13.setDrawValues(true); // 显示数值
		barDataSet13.setValueFormatter(new ValueFormatter() {			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet1 = new BarDataSet(yVals1, "一厂房平均");
		barDataSet1.setColor(Color.BLUE);// 设置第三组数据颜色
		barDataSet1.setBarSpacePercent(38f);
		barDataSet1.setDrawValues(true); // 显示数值
		barDataSet1.setValueTextSize(7f);
		barDataSet1.setValueTextColor(Color.RED);
		barDataSet1.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return "1厂房 " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet21 = new BarDataSet(yVals21, "二厂房1区");
		barDataSet21.setColor(Color.rgb(204, 102, 0));// 设置第一组数据颜色
		barDataSet21.setDrawValues(true); // 显示数值
		barDataSet21.setBarSpacePercent(38f);//
		barDataSet21.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet22 = new BarDataSet(yVals22, "二厂房2区");
		barDataSet22.setColor(Color.rgb(22, 169, 81));// 设置第二组数据颜色
		barDataSet22.setBarSpacePercent(38f);
		barDataSet22.setDrawValues(true); // 显示数值
		barDataSet22.setValueTextSize(7f);
		barDataSet22.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return "2/2区 " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet23 = new BarDataSet(yVals23, "二厂房3区");
		barDataSet23.setColor(Color.rgb(204, 204, 0));// 设置第三组数据颜色
		barDataSet23.setBarSpacePercent(38f);
		barDataSet23.setDrawValues(true); // 显示数值
		barDataSet23.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet2 = new BarDataSet(yVals2, "二厂房平均");
		barDataSet2.setColor(Color.rgb(51, 51, 102));// 设置第三组数据颜色
		barDataSet2.setBarSpacePercent(38f);
		barDataSet2.setDrawValues(true); // 显示数值
		barDataSet2.setValueTextSize(7f);
		barDataSet2.setValueTextColor(Color.RED);
		barDataSet2.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return "2厂房 " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet = new BarDataSet(yVals, "厂房平均");
		barDataSet.setColor(Color.rgb(153, 0, 51));// 设置第三组数据颜色
		barDataSet.setBarSpacePercent(38f);
		barDataSet.setDrawValues(true); // 显示数值
		barDataSet.setValueTextSize(7f);
		barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
				return "厂房 " + decimalFormat.format(value);
			}
		});

		ArrayList<IBarDataSet> multibardata = new ArrayList<>();// IBarDataSet
																// 接口很关键，是添加多组数据的关键结构，LineChart也是可以采用对应的接口类，也可以添加多组数据
		multibardata.add(barDataSet11);
		multibardata.add(barDataSet12);
		multibardata.add(barDataSet13);
		multibardata.add(barDataSet1);
		multibardata.add(barDataSet21);
		multibardata.add(barDataSet22);
		multibardata.add(barDataSet23);
		multibardata.add(barDataSet2);
		multibardata.add(barDataSet);

		BarData multi_bardata = new BarData(xVals, multibardata);
		mBarChart.setData(multi_bardata); // 设置数据
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChart.animateXY(1000, 2000);// 设置动画
			}
		});
	}

	private void checkUpDate() {
		UpdateManager manager = new UpdateManager(MainActivity.this, false);
		manager.checkUpdate();
	}

	private int NetStatus() {
		NetDetector netDetector = new NetDetector(mContext);
		return netDetector.isConnectingToInternet();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog_exit();
			return true;
		}
		return true;
	}

	protected void dialog_exit() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MyApplication.getInstance().exit();

			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void init_commData() {
		if (JXList == null) {
			mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new AsyTask_HttpGetJXRecord(get_JXName_url, this, this)
					.execute(); // 执行从远程获得解析记录数据
		}
		if (date_table == null) {
			// 执行从远程获得日期数据
			mhttpgetdata_date = (AsyTask_HttpGetDate) new AsyTask_HttpGetDate(get_dateTable_url, this, this).execute();
		}

	}

	private void init() {
		gridView = (GridView) findViewById(R.id.gridView);
		dataList = new ArrayList<Map<String, Object>>();
		adapter = new SimpleAdapter(this, getData(), R.layout.item_action, new String[] { "pic", "name" },
				new int[] { R.id.pic, R.id.name });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Popup_initData();
		btnMore = (Button) findViewById(R.id.btn_more);
		btnMore.setVisibility(View.VISIBLE);
		btnMore.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_wifi = (ImageView) findViewById(R.id.iv_NoWiFi);
		bannerView = (com.hewaiming.ALWorkInfo.banner.SlideShowView) findViewById(R.id.bannerwView);
		mBarChart = (BarChart) findViewById(R.id.Ae_chart);
		tv_aeTitle = (TextView) findViewById(R.id.tv_AeTitle);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String todayValue = sdf.format(dt);
		tv_aeTitle.setText("当日效应系数：" + todayValue);
		init_BarCHART();

	}

	private void init_BarCHART() {
		// 图表显示设置
		mBarChart.setTouchEnabled(true);
		mBarChart.getLegend().setPosition(LegendPosition.BELOW_CHART_LEFT);// 设置注解的位置在左上方
		mBarChart.getLegend().setForm(LegendForm.SQUARE);// 这是左边显示小图标的形状
		mBarChart.getLegend().setWordWrapEnabled(true);
		mBarChart.getLegend().setTextSize(3f);

		mBarChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// 设置X轴的位置
		mBarChart.getXAxis().setDrawGridLines(false);// 不显示网格

		mBarChart.getAxisRight().setEnabled(false);// 右侧不显示Y轴
		mBarChart.getAxisLeft().setEnabled(false);
		mBarChart.getAxisLeft().setDrawLabels(false); // 左侧Y坐标不显示数据刻度
		mBarChart.getAxisLeft().setAxisMinValue(0.0f);// 设置Y轴显示最小值，不然0下面会有空隙
		// mBarChart.getAxisLeft().setAxisMaxValue(2.0f);// 设置Y轴显示最大值
		mBarChart.getAxisLeft().setDrawGridLines(true);// 不设置Y轴网格

		mBarChart.setNoDataTextDescription("没有获取到效应次数数据");
		mBarChart.setDescription("");
		mBarChart.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				Intent aemost_intent = new Intent(MainActivity.this, AeMostActivity.class);
				Bundle aemostBundle = new Bundle();
				aemostBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				aemostBundle.putSerializable("JXList", (Serializable) JXList);
				aemostBundle.putString("ip", ip);
				aemostBundle.putInt("port", port);
				aemost_intent.putExtras(aemostBundle);
				startActivity(aemost_intent); // 效应槽
				return true;
			}
		});

	}

	private void Popup_initData() {
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, "设置远程服务器", R.drawable.settings));
		titlePopup.addAction(new ActionItem(this, "关于", R.drawable.mm_title_btn_keyboard_normal));
		// titlePopup.addAction(new ActionItem(this, "扫一扫",
		// R.drawable.mm_title_btn_qrcode_normal));
	}

	private List<Map<String, Object>> getData() {

		for (int i = 0; i < MyConst.drawable.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", MyConst.drawable[i]);
			map.put("name", MyConst.iconName[i]);
			dataList.add(map);
		}
		Log.i("Main", "size=" + dataList.size());
		return dataList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		boolean DoRun = false;
		if (position == 8 || position == 15) {
			DoRun = true;
		}
		if ((JXList != null && date_record != null) || DoRun) {
			switch (position) {
			case 0:
				Intent Paramsintent = new Intent(MainActivity.this, ParamsActivity.class);
				Bundle paramBundle = new Bundle();
				paramBundle.putString("ip", ip);
				paramBundle.putInt("port", port);
				Paramsintent.putExtras(paramBundle);
				startActivity(Paramsintent); // 常用参数
				break;
			case 1:
				Intent DayTable_intent = new Intent(MainActivity.this, DayTableActivity.class);
				Bundle DayTablebundle = new Bundle();
				DayTablebundle.putStringArrayList("date_table", (ArrayList<String>) date_table);
				DayTablebundle.putSerializable("JXList", (Serializable) JXList);
				DayTablebundle.putString("ip", ip);
				DayTablebundle.putInt("port", port);
				DayTable_intent.putExtras(DayTablebundle);
				startActivity(DayTable_intent); // 槽日报
				break;
			case 2:
				Intent Ae5day_intent = new Intent(MainActivity.this, Ae5DayActivity.class);
				Bundle bundle_Ae5 = new Bundle();
				bundle_Ae5.putSerializable("JXList", (Serializable) JXList);
				bundle_Ae5.putString("ip", ip);
				bundle_Ae5.putInt("port", port);
				Ae5day_intent.putExtras(bundle_Ae5);
				startActivity(Ae5day_intent); // 效应情报表
				break;
			case 3:
				Intent Potage_intent = new Intent(MainActivity.this, PotAgeActivity.class);
				Bundle PotageBundle = new Bundle();
				PotageBundle.putString("ip", ip);
				PotageBundle.putInt("port", port);
				Potage_intent.putExtras(PotageBundle);
				startActivity(Potage_intent); // 槽龄表
				break;
			case 4:
				Intent potv_intent = new Intent(MainActivity.this, PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // 槽压曲线
				break;
			case 5:
				Intent faultRec_intent = new Intent(MainActivity.this, FaultRecActivity.class);
				Bundle bundle_faultRec = new Bundle();
				bundle_faultRec.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_faultRec.putBoolean("Hide_Action", false);
				bundle_faultRec.putString("PotNo", "1101");
				bundle_faultRec.putString("Begin_Date", date_record.get(0));
				bundle_faultRec.putString("End_Date", date_record.get(0));
				bundle_faultRec.putSerializable("JXList", (Serializable) JXList);
				bundle_faultRec.putString("ip", ip);
				bundle_faultRec.putInt("port", port);
				faultRec_intent.putExtras(bundle_faultRec);
				startActivity(faultRec_intent); // 故障记录
				break;
			case 6:
				Intent realRec_intent = new Intent(MainActivity.this, RealRecActivity.class);
				Bundle realbundle = new Bundle();
				realbundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				realbundle.putSerializable("JXList", (Serializable) JXList);
				realbundle.putString("ip", ip);
				realbundle.putInt("port", port);
				realRec_intent.putExtras(realbundle);
				startActivity(realRec_intent); // 实时记录
				break;
			case 7:
				Intent operate_intent = new Intent(MainActivity.this, OperateRecActivity.class);
				Bundle operateBundle = new Bundle();
				operateBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				operateBundle.putString("ip", ip);
				operateBundle.putInt("port", port);
				operate_intent.putExtras(operateBundle);
				startActivity(operate_intent); // 操作记录
				break;
			case 8:
				Intent PotStatus_intent = new Intent(MainActivity.this, PotStatusActivity.class);
				Bundle PotStatusBundle = new Bundle();
				// PotStatusBundle.putStringArrayList("date_record",
				// (ArrayList<String>) date_record);
				PotStatusBundle.putSerializable("JXList", (Serializable) JXList);
				PotStatusBundle.putString("ip", ip);
				PotStatusBundle.putInt("port", port);
				PotStatus_intent.putExtras(PotStatusBundle);
				startActivity(PotStatus_intent); // 槽状态表
				break;
			case 9:
				Intent measue_intent = new Intent(MainActivity.this, MeasueTableActivity.class);
				Bundle measueBundle = new Bundle();
				measueBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				measueBundle.putString("ip", ip);
				measueBundle.putInt("port", port);
				measue_intent.putExtras(measueBundle);
				startActivity(measue_intent); // 测量数据
				break;

			case 10:
				Intent aemost_intent = new Intent(MainActivity.this, AeMostActivity.class);
				Bundle aemostBundle = new Bundle();
				aemostBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				aemostBundle.putSerializable("JXList", (Serializable) JXList);
				aemostBundle.putString("ip", ip);
				aemostBundle.putInt("port", port);
				aemost_intent.putExtras(aemostBundle);
				startActivity(aemost_intent); // 效应槽
				break;
			case 11:
				Intent aeRec_intent = new Intent(MainActivity.this, AeRecActivity.class);
				Bundle bundle_AeRec = new Bundle();
				bundle_AeRec.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_AeRec.putBoolean("Hide_Action", false);
				bundle_AeRec.putString("PotNo", "1101");
				bundle_AeRec.putString("Begin_Date", date_record.get(0));
				bundle_AeRec.putString("End_Date", date_record.get(0));
				bundle_AeRec.putSerializable("JXList", (Serializable) JXList);
				bundle_AeRec.putString("ip", ip);
				bundle_AeRec.putInt("port", port);
				aeRec_intent.putExtras(bundle_AeRec);
				startActivity(aeRec_intent); // 效应记录
				break;
			case 12:
				Intent faultmost_intent = new Intent(MainActivity.this, FaultMostActivity.class);
				Bundle bundle_faultmost = new Bundle();
				bundle_faultmost.putSerializable("JXList", (Serializable) JXList);
				bundle_faultmost.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_faultmost.putString("ip", ip);
				bundle_faultmost.putInt("port", port);
				faultmost_intent.putExtras(bundle_faultmost);
				startActivity(faultmost_intent); // 故障率排序
				break;
			case 13:
				Intent craft_intent = new Intent(MainActivity.this, CraftLineActivity.class);
				Bundle craftBundle = new Bundle();
				craftBundle.putStringArrayList("date_table", (ArrayList<String>) date_table);
				craftBundle.putString("PotNo_Selected", "1101");
				craftBundle.putString("ip", ip);
				craftBundle.putInt("port", port);
				craft_intent.putExtras(craftBundle);
				startActivity(craft_intent); // 工艺曲线
				break;
			case 14:
				Intent alarmRec_intent = new Intent(MainActivity.this, AlarmRecActivity.class);
				Bundle bundle_alarm = new Bundle();
				bundle_alarm.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_alarm.putSerializable("JXList", (Serializable) JXList);
				bundle_alarm.putString("ip", ip);
				bundle_alarm.putInt("port", port);
				alarmRec_intent.putExtras(bundle_alarm);
				startActivity(alarmRec_intent); // 报警记录
				break;
			case 15:
				Intent realtime_intent = new Intent(MainActivity.this, RealTimeLineActivity.class);
				Bundle bundle_realtime = new Bundle();
				// bundle_realtime.putStringArrayList("date_record",
				// (ArrayList<String>) date_record);
				bundle_realtime.putBoolean("Hide_Action", false);
				bundle_realtime.putString("PotNo", "1101");
				bundle_realtime.putString("ip", ip);
				bundle_realtime.putInt("port", port);
				// realtime_intent.putStringArrayListExtra("date_table",
				// (ArrayList<String>) date_table);
				realtime_intent.putExtras(bundle_realtime);
				startActivity(realtime_intent); // 实时曲线
				break;
			}
		} else {
			Toast.makeText(mContext, "请稍后再点击，数据初始化....", 1).show();
		}
	}

	public boolean initdate(Context ctx) {
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		if (sp != null) {
			ip = sp.getString("ipstr", ip);
			if (ip != null) {
				if (sp.getString("port", String.valueOf(port)) != null) {
					port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
				} else {
					Toast.makeText(ctx, "请设置远程服务器端口", 0).show();
					return false;
				}
			} else {
				Toast.makeText(ctx, "请设置远程服务器IP", 0).show();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void GetALLDayUrl(String data) {
		// 得到日期
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[日期]初始数据，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG).show();
			tv_title.setTextSize(14);
			tv_title.setText("工作站:" + "请检查远程服务器IP和端口是否正确！");
		} else {
			date_table = new ArrayList<String>();
			date_table = JsonToBean_GetPublicData.JsonArrayToDate(data);
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todayValue = sdf.format(dt);
			date_record = new ArrayList<String>(date_table); // 记录日期
			date_record.add(0, todayValue);
		}

	}

	@Override
	public void GetJXRecordUrl(String data) {
		if (data.equals("")) {
			tv_title.setTextSize(14);
			tv_title.setText("工作站:" + "请检查远程服务器IP和端口是否正确！");
			Toast.makeText(getApplicationContext(), "没有获取到[解析号]初始数据，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG).show();
		} else {
			JXList = new ArrayList<Map<String, Object>>();
			JXList = JsonToBean_GetPublicData.JsonArrayToJXRecord(data);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_more:
			titlePopup.show(v);
			break;
		}

	}

}
