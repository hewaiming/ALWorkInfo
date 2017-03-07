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
	private List<String> date_record = null; // ��¼����
	private List<String> date_table = null; // ��������
	private List<Map<String, Object>> JXList = null; // ��¼����

	private String get_dateTable_url = ":8000/scgy/android/odbcPhP/getDate.php";
	private String get_JXName_url = ":8000/scgy/android/odbcPhP/getJXRecordName.php";
	private AsyTask_HttpGetDate mhttpgetdata_date;
	private AsyTask_HttpGetJXRecord mHttpGetData_JXRecord;
	private Context mContext;
	private TitlePopup titlePopup;
	private TextView tv_title, tv_aeTitle;
	private ImageView iv_wifi;
	private SlideShowView bannerView;
	private int[] NormPotS = { 0, 0, 0, 0, 0, 0 }; // ��������������
	private int[] AeCnt = { 0, 0, 0, 0, 0, 0 }; // ����ЧӦ����

	private String AeCnt_url = ":8000/scgy/android/odbcPhP/AeCnt_area_date.php"; // ЧӦ����
	private List<AeRecord> listBean_AeCnt = null; // ЧӦ�����б�

	private String get_NormPots1_url = ":8000/scgy/android/odbcPhP/GetNormPots1.php";
	private String get_NormPots2_url = ":8000/scgy/android/odbcPhP/GetNormPots2.php";

	private List<PotCtrl> NormPotsList1 = null; // һ������״̬ �б�
	private List<PotCtrl> NormPotsList2 = null; // ��������״̬ �б�

	private BarChart mBarChart;
	private Handler handler = new Handler(Looper.getMainLooper());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		init(); // ��ʼ�����ؼ�
		NetDetector netDetector = new NetDetector(mContext);
		if (netDetector.isConnectingToInternetNoShow() == 1) {
			iv_wifi.setVisibility(View.GONE);
			bannerView.setVisibility(View.VISIBLE);
		} else {
			iv_wifi.setVisibility(View.VISIBLE);
			bannerView.setVisibility(View.GONE);
		}
		if (NetStatus() != 0) {
			if (!initdate(mContext)) { // ȡԶ�̷�������ַ�Ͷ˿�
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);// û������Զ�̷�����ip�Ͷ˿�

			} else {
				get_dateTable_url = "http://" + ip + get_dateTable_url;
				get_JXName_url = "http://" + ip + get_JXName_url;
				get_NormPots1_url = "http://" + ip + get_NormPots1_url;
				get_NormPots2_url = "http://" + ip + get_NormPots2_url;
				AeCnt_url = "http://" + ip + AeCnt_url;
				init_commData();
				init_EC();// ��ʾ��ǰ����ЧӦϵ��
				checkUpDate(); // ���汾����
			}

		} else {
			Toast.makeText(getApplicationContext(), "�����쳣��", Toast.LENGTH_LONG).show();
		}
	}

	private void init_EC() {
		ExecutorService exec = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
			@Override
			public void run() {
				System.out.println("��ȡ����ЧӦϵ������OK����ʼ��ʾ����ͼ������happyȥ");
				CalcPotsNorm(NormPotsList1);
				CalcPotsNorm(NormPotsList2);
				CalcAeCnt(listBean_AeCnt);
				showEcBar(); // ��ʾ����ЧӦ��״ͼ
			}
		});

		exec.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("��ȡһ��������������OK������������");
				HttpPost_JsonArray jsonParser1 = new HttpPost_JsonArray();
				JSONArray json1 = jsonParser1.makeHttpRequest(get_NormPots1_url, "POST");
				if (json1 != null) {
					Log.d("һ����������������", json1.toString());// �ӷ���������������
					NormPotsList1 = new ArrayList<PotCtrl>();
					NormPotsList1 = JsonToBean_GetPublicData.JsonArrayToNormPots(json1.toString());
				} else {
					Log.i("һ���������������� ---", "��PHP�����������ݷ��أ�");
					tv_title.setTextSize(14);
					tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
					Toast.makeText(getApplicationContext(), "û�л�ȡ��һ��������������������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG)
							.show();
				}
				try {
					barrier.await();// �ȴ���������
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
				System.out.println("��ȡ����������������OK������������");
				HttpPost_JsonArray jsonParser2 = new HttpPost_JsonArray();
				JSONArray json2 = jsonParser2.makeHttpRequest(get_NormPots2_url, "POST");
				if (json2 != null) {
					Log.d("������������������", json2.toString());// �ӷ���������������
					NormPotsList2 = new ArrayList<PotCtrl>();
					NormPotsList2 = JsonToBean_GetPublicData.JsonArrayToNormPots(json2.toString());
				} else {
					Log.i("������������������ ---", "��PHP�����������ݷ��أ�");
					tv_title.setTextSize(14);
					tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
					Toast.makeText(getApplicationContext(), "û�л�ȡ������������������������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG)
							.show();
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		});

		// ��ȡЧӦ����
		exec.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("��ȡ����ЧӦ����OK������������");
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String todayValue = sdf.format(dt);
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // ȫ���ۺ�
				mparams.add(new BasicNameValuePair("BeginDate", todayValue));
				mparams.add(new BasicNameValuePair("EndDate", todayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(AeCnt_url, "POST", mparams);
				if (json != null) {
					Log.d("������ЧӦ����", json.toString());// �ӷ���������������
					listBean_AeCnt = new ArrayList<AeRecord>(); // ��ʼ��ЧӦ����������
					// listBean_AeCnt.clear();
					listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());

				} else {
					Log.i("������ЧӦ���� ---", "��PHP�����������ݷ��أ�");
					tv_title.setTextSize(14);
					tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
					Toast.makeText(getApplicationContext(), "û�л�ȡ������ЧӦ����������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();
				}
				try {
					barrier.await();// �ȴ���������
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
				int potno = aeCnt.getPotNo(); // �ۺ�
				int aeS = aeCnt.getWaitTime();// ЧӦ����

				if (potno >= 1101 && potno <= 1136) {
					AeCnt[0] = AeCnt[0] + aeS; // һ����һ��ЧӦ�����ܺ�
				} else if (potno >= 1201 && potno <= 1237) {
					AeCnt[1] = AeCnt[1] + aeS;
				} else if (potno >= 1301 && potno <= 1337) {
					AeCnt[2] = AeCnt[2] + aeS;
				} else if (potno >= 2101 && potno <= 2136) {
					AeCnt[3] = AeCnt[3] + aeS; // ������һ��ЧӦ�����ܺ�
				} else if (potno >= 2201 && potno <= 2237) {
					AeCnt[4] = AeCnt[4] + aeS;
				} else if (potno >= 2301 && potno <= 2337) {
					AeCnt[5] = AeCnt[5] + aeS;
				}

			} else {
				System.out.println("�� " + i + " �� ЧӦ������Ϊ�գ�");
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
				int potno = mPotCtrl.getPotNo(); // �ۺ�
				int mCtrl = mPotCtrl.getCtrls(); // ��״̬��
				// �����λ�������ƣ�00���� ������01 ����Ԥ�ȣ�10����������11����ͣ��
				if ((mCtrl & 0x00) == 0) {
					if (potno >= 1101 && potno <= 1136) {
						NormPotS[0]++;
					} else if (potno >= 1201 && potno <= 1237) {
						NormPotS[1]++;
					} else if (potno >= 1301 && potno <= 1337) {
						NormPotS[2]++;
					} else if (potno >= 2101 && potno <= 2136) {
						NormPotS[3]++; // ������һ��
					} else if (potno >= 2201 && potno <= 2237) {
						NormPotS[4]++;
					} else if (potno >= 2301 && potno <= 2337) {
						NormPotS[5]++;
					}
				}

			} else {
				System.out.println("��״̬����ΪNULL!");
			}
		}

	}

	protected void showEcBar() {
		// ͼ����������
		ArrayList<BarEntry> yVals11 = new ArrayList<>();// Y�᷽��һ����һ������
		ArrayList<BarEntry> yVals12 = new ArrayList<>();// Y�᷽��һ������������
		ArrayList<BarEntry> yVals13 = new ArrayList<>();// Y�᷽��һ������������
		ArrayList<BarEntry> yVals1 = new ArrayList<>();// Y�᷽��һ����
		ArrayList<BarEntry> yVals21 = new ArrayList<>();// Y�᷽�������һ������
		ArrayList<BarEntry> yVals22 = new ArrayList<>();// Y�᷽���������������
		ArrayList<BarEntry> yVals23 = new ArrayList<>();// Y�᷽���������������
		ArrayList<BarEntry> yVals2 = new ArrayList<>();// Y�᷽�����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�
		ArrayList<String> xVals = new ArrayList<>();// X������
		int AeTotal1, AeTotal2, AeTotal, PotS1, PotS2, PotS;
		AeTotal1 = AeCnt[0] + AeCnt[1] + AeCnt[2];// һ����ЧӦ����
		AeTotal2 = AeCnt[3] + AeCnt[4] + AeCnt[5];// ������ЧӦ����
		AeTotal = AeTotal1 + AeTotal2;// ����ЧӦ����
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		xVals.add("");
		// �������Դ
		yVals11.add(new BarEntry((float) AeCnt[0] / NormPotS[0], 0));
		yVals12.add(new BarEntry((float) AeCnt[1] / NormPotS[1], 0));
		yVals13.add(new BarEntry((float) AeCnt[2] / NormPotS[2], 0));
		yVals21.add(new BarEntry((float) AeCnt[3] / NormPotS[3], 0));
		yVals22.add(new BarEntry((float) AeCnt[4] / NormPotS[4], 0));
		yVals23.add(new BarEntry((float) AeCnt[5] / NormPotS[5], 0));

		yVals1.add(new BarEntry((float) AeTotal1 / PotS1, 0)); // һ����ЧӦϵ��
		yVals2.add(new BarEntry((float) AeTotal2 / PotS2, 0));// ������ЧӦϵ��
		yVals.add(new BarEntry((float) AeTotal / PotS, 0));// ����ЧӦϵ��
		BarDataSet barDataSet11 = new BarDataSet(yVals11, "һ����1��");
		barDataSet11.setColor(Color.RED);// ���õ�һ��������ɫ
		barDataSet11.setDrawValues(true); // ��ʾ��ֵ
		barDataSet11.setBarSpacePercent(38f);//
		barDataSet11.setValueTextSize(7f);		
		barDataSet11.setValueFormatter(new ValueFormatter() {
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return "1/1�� " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet12 = new BarDataSet(yVals12, "һ����2��");
		barDataSet12.setColor(Color.GREEN);// ���õڶ���������ɫ
		barDataSet12.setBarSpacePercent(38f);
		barDataSet12.setDrawValues(true); // ��ʾ��ֵ
		barDataSet12.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet13 = new BarDataSet(yVals13, "һ����3��");
		barDataSet13.setColor(Color.YELLOW);// ���õ�����������ɫ
		barDataSet13.setBarSpacePercent(38f);
		barDataSet13.setDrawValues(true); // ��ʾ��ֵ
		barDataSet13.setValueFormatter(new ValueFormatter() {			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet1 = new BarDataSet(yVals1, "һ����ƽ��");
		barDataSet1.setColor(Color.BLUE);// ���õ�����������ɫ
		barDataSet1.setBarSpacePercent(38f);
		barDataSet1.setDrawValues(true); // ��ʾ��ֵ
		barDataSet1.setValueTextSize(7f);
		barDataSet1.setValueTextColor(Color.RED);
		barDataSet1.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return "1���� " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet21 = new BarDataSet(yVals21, "������1��");
		barDataSet21.setColor(Color.rgb(204, 102, 0));// ���õ�һ��������ɫ
		barDataSet21.setDrawValues(true); // ��ʾ��ֵ
		barDataSet21.setBarSpacePercent(38f);//
		barDataSet21.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet22 = new BarDataSet(yVals22, "������2��");
		barDataSet22.setColor(Color.rgb(22, 169, 81));// ���õڶ���������ɫ
		barDataSet22.setBarSpacePercent(38f);
		barDataSet22.setDrawValues(true); // ��ʾ��ֵ
		barDataSet22.setValueTextSize(7f);
		barDataSet22.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return "2/2�� " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet23 = new BarDataSet(yVals23, "������3��");
		barDataSet23.setColor(Color.rgb(204, 204, 0));// ���õ�����������ɫ
		barDataSet23.setBarSpacePercent(38f);
		barDataSet23.setDrawValues(true); // ��ʾ��ֵ
		barDataSet23.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet2 = new BarDataSet(yVals2, "������ƽ��");
		barDataSet2.setColor(Color.rgb(51, 51, 102));// ���õ�����������ɫ
		barDataSet2.setBarSpacePercent(38f);
		barDataSet2.setDrawValues(true); // ��ʾ��ֵ
		barDataSet2.setValueTextSize(7f);
		barDataSet2.setValueTextColor(Color.RED);
		barDataSet2.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return "2���� " + decimalFormat.format(value);
			}
		});

		BarDataSet barDataSet = new BarDataSet(yVals, "����ƽ��");
		barDataSet.setColor(Color.rgb(153, 0, 51));// ���õ�����������ɫ
		barDataSet.setBarSpacePercent(38f);
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(7f);
		barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return "���� " + decimalFormat.format(value);
			}
		});

		ArrayList<IBarDataSet> multibardata = new ArrayList<>();// IBarDataSet
																// �ӿںܹؼ�������Ӷ������ݵĹؼ��ṹ��LineChartҲ�ǿ��Բ��ö�Ӧ�Ľӿ��࣬Ҳ������Ӷ�������
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
		mBarChart.setData(multi_bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChart.animateXY(1000, 2000);// ���ö���
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
		builder.setMessage("ȷ��Ҫ�˳���?");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MyApplication.getInstance().exit();

			}
		});
		builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
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
					.execute(); // ִ�д�Զ�̻�ý�����¼����
		}
		if (date_table == null) {
			// ִ�д�Զ�̻����������
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
		tv_aeTitle.setText("����ЧӦϵ����" + todayValue);
		init_BarCHART();

	}

	private void init_BarCHART() {
		// ͼ����ʾ����
		mBarChart.setTouchEnabled(true);
		mBarChart.getLegend().setPosition(LegendPosition.BELOW_CHART_LEFT);// ����ע���λ�������Ϸ�
		mBarChart.getLegend().setForm(LegendForm.SQUARE);// ���������ʾСͼ�����״
		mBarChart.getLegend().setWordWrapEnabled(true);
		mBarChart.getLegend().setTextSize(3f);

		mBarChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// ����X���λ��
		mBarChart.getXAxis().setDrawGridLines(false);// ����ʾ����

		mBarChart.getAxisRight().setEnabled(false);// �Ҳ಻��ʾY��
		mBarChart.getAxisLeft().setEnabled(false);
		mBarChart.getAxisLeft().setDrawLabels(false); // ���Y���겻��ʾ���ݿ̶�
		mBarChart.getAxisLeft().setAxisMinValue(0.0f);// ����Y����ʾ��Сֵ����Ȼ0������п�϶
		// mBarChart.getAxisLeft().setAxisMaxValue(2.0f);// ����Y����ʾ���ֵ
		mBarChart.getAxisLeft().setDrawGridLines(true);// ������Y������

		mBarChart.setNoDataTextDescription("û�л�ȡ��ЧӦ��������");
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
				startActivity(aemost_intent); // ЧӦ��
				return true;
			}
		});

	}

	private void Popup_initData() {
		// �������������������
		titlePopup.addAction(new ActionItem(this, "����Զ�̷�����", R.drawable.settings));
		titlePopup.addAction(new ActionItem(this, "����", R.drawable.mm_title_btn_keyboard_normal));
		// titlePopup.addAction(new ActionItem(this, "ɨһɨ",
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
				startActivity(Paramsintent); // ���ò���
				break;
			case 1:
				Intent DayTable_intent = new Intent(MainActivity.this, DayTableActivity.class);
				Bundle DayTablebundle = new Bundle();
				DayTablebundle.putStringArrayList("date_table", (ArrayList<String>) date_table);
				DayTablebundle.putSerializable("JXList", (Serializable) JXList);
				DayTablebundle.putString("ip", ip);
				DayTablebundle.putInt("port", port);
				DayTable_intent.putExtras(DayTablebundle);
				startActivity(DayTable_intent); // ���ձ�
				break;
			case 2:
				Intent Ae5day_intent = new Intent(MainActivity.this, Ae5DayActivity.class);
				Bundle bundle_Ae5 = new Bundle();
				bundle_Ae5.putSerializable("JXList", (Serializable) JXList);
				bundle_Ae5.putString("ip", ip);
				bundle_Ae5.putInt("port", port);
				Ae5day_intent.putExtras(bundle_Ae5);
				startActivity(Ae5day_intent); // ЧӦ�鱨��
				break;
			case 3:
				Intent Potage_intent = new Intent(MainActivity.this, PotAgeActivity.class);
				Bundle PotageBundle = new Bundle();
				PotageBundle.putString("ip", ip);
				PotageBundle.putInt("port", port);
				Potage_intent.putExtras(PotageBundle);
				startActivity(Potage_intent); // �����
				break;
			case 4:
				Intent potv_intent = new Intent(MainActivity.this, PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // ��ѹ����
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
				startActivity(faultRec_intent); // ���ϼ�¼
				break;
			case 6:
				Intent realRec_intent = new Intent(MainActivity.this, RealRecActivity.class);
				Bundle realbundle = new Bundle();
				realbundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				realbundle.putSerializable("JXList", (Serializable) JXList);
				realbundle.putString("ip", ip);
				realbundle.putInt("port", port);
				realRec_intent.putExtras(realbundle);
				startActivity(realRec_intent); // ʵʱ��¼
				break;
			case 7:
				Intent operate_intent = new Intent(MainActivity.this, OperateRecActivity.class);
				Bundle operateBundle = new Bundle();
				operateBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				operateBundle.putString("ip", ip);
				operateBundle.putInt("port", port);
				operate_intent.putExtras(operateBundle);
				startActivity(operate_intent); // ������¼
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
				startActivity(PotStatus_intent); // ��״̬��
				break;
			case 9:
				Intent measue_intent = new Intent(MainActivity.this, MeasueTableActivity.class);
				Bundle measueBundle = new Bundle();
				measueBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				measueBundle.putString("ip", ip);
				measueBundle.putInt("port", port);
				measue_intent.putExtras(measueBundle);
				startActivity(measue_intent); // ��������
				break;

			case 10:
				Intent aemost_intent = new Intent(MainActivity.this, AeMostActivity.class);
				Bundle aemostBundle = new Bundle();
				aemostBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
				aemostBundle.putSerializable("JXList", (Serializable) JXList);
				aemostBundle.putString("ip", ip);
				aemostBundle.putInt("port", port);
				aemost_intent.putExtras(aemostBundle);
				startActivity(aemost_intent); // ЧӦ��
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
				startActivity(aeRec_intent); // ЧӦ��¼
				break;
			case 12:
				Intent faultmost_intent = new Intent(MainActivity.this, FaultMostActivity.class);
				Bundle bundle_faultmost = new Bundle();
				bundle_faultmost.putSerializable("JXList", (Serializable) JXList);
				bundle_faultmost.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_faultmost.putString("ip", ip);
				bundle_faultmost.putInt("port", port);
				faultmost_intent.putExtras(bundle_faultmost);
				startActivity(faultmost_intent); // ����������
				break;
			case 13:
				Intent craft_intent = new Intent(MainActivity.this, CraftLineActivity.class);
				Bundle craftBundle = new Bundle();
				craftBundle.putStringArrayList("date_table", (ArrayList<String>) date_table);
				craftBundle.putString("PotNo_Selected", "1101");
				craftBundle.putString("ip", ip);
				craftBundle.putInt("port", port);
				craft_intent.putExtras(craftBundle);
				startActivity(craft_intent); // ��������
				break;
			case 14:
				Intent alarmRec_intent = new Intent(MainActivity.this, AlarmRecActivity.class);
				Bundle bundle_alarm = new Bundle();
				bundle_alarm.putStringArrayList("date_record", (ArrayList<String>) date_record);
				bundle_alarm.putSerializable("JXList", (Serializable) JXList);
				bundle_alarm.putString("ip", ip);
				bundle_alarm.putInt("port", port);
				alarmRec_intent.putExtras(bundle_alarm);
				startActivity(alarmRec_intent); // ������¼
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
				startActivity(realtime_intent); // ʵʱ����
				break;
			}
		} else {
			Toast.makeText(mContext, "���Ժ��ٵ�������ݳ�ʼ��....", 1).show();
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
					Toast.makeText(ctx, "������Զ�̷������˿�", 0).show();
					return false;
				}
			} else {
				Toast.makeText(ctx, "������Զ�̷�����IP", 0).show();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void GetALLDayUrl(String data) {
		// �õ�����
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[����]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();
			tv_title.setTextSize(14);
			tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
		} else {
			date_table = new ArrayList<String>();
			date_table = JsonToBean_GetPublicData.JsonArrayToDate(data);
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todayValue = sdf.format(dt);
			date_record = new ArrayList<String>(date_table); // ��¼����
			date_record.add(0, todayValue);
		}

	}

	@Override
	public void GetJXRecordUrl(String data) {
		if (data.equals("")) {
			tv_title.setTextSize(14);
			tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[������]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();
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
