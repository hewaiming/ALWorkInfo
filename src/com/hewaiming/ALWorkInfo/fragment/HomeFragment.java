package com.hewaiming.ALWorkInfo.fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
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
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hewaiming.ALWorkInfo.R;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetJXRecord_Listener;

import com.hewaiming.ALWorkInfo.Popup.ActionItem;
import com.hewaiming.ALWorkInfo.Popup.MyProgressDialog;
import com.hewaiming.ALWorkInfo.Popup.TitlePopup;

import com.hewaiming.ALWorkInfo.Update.UpdateManager;
import com.hewaiming.ALWorkInfo.banner.SlideShowView;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.bean.AvgV;
import com.hewaiming.ALWorkInfo.bean.DJWD;
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
import com.hewaiming.ALWorkInfo.ui.Ae5DayActivity;
import com.hewaiming.ALWorkInfo.ui.AeMostActivity;
import com.hewaiming.ALWorkInfo.ui.AeRecActivity;
import com.hewaiming.ALWorkInfo.ui.AlarmRecActivity;
import com.hewaiming.ALWorkInfo.ui.CraftLineActivity;
import com.hewaiming.ALWorkInfo.ui.DayTableActivity;
import com.hewaiming.ALWorkInfo.ui.FaultMostActivity;
import com.hewaiming.ALWorkInfo.ui.FaultRecActivity;
import com.hewaiming.ALWorkInfo.ui.MeasueTableActivity;
import com.hewaiming.ALWorkInfo.ui.OperateRecActivity;
import com.hewaiming.ALWorkInfo.ui.ParamsActivity;
import com.hewaiming.ALWorkInfo.ui.PotAgeActivity;
import com.hewaiming.ALWorkInfo.ui.PotStatusActivity;
import com.hewaiming.ALWorkInfo.ui.PotVLineActivity;
import com.hewaiming.ALWorkInfo.ui.RealRecActivity;
import com.hewaiming.ALWorkInfo.ui.RealTimeLineActivity;
import com.hewaiming.ALWorkInfo.ui.SettingActivity;
import com.hewaiming.ALWorkInfo.view.HeaderListView_Params;

import android.R.integer;
import android.annotation.SuppressLint;
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
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

@SuppressLint("SimpleDateFormat")
public class HomeFragment extends Fragment implements OnClickListener, HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private String ip;
	private int port;
	private SharedPreferences sp;
	// private GridView gridView;
	private Button btnMore;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;
	private List<String> date_record = null; // ��¼����
	private List<String> date_table = null; // ��������
	private List<Map<String, Object>> JXList = null; // ��¼����

	private String get_dateTable_url = ":8000/scgy/android/odbcPhP/getDate.php";
	private String get_JXName_url = ":8000/scgy/android/odbcPhP/getJXRecordName.php";
	// private AsyTask_HttpGetDate mhttpgetdata_date;
	// private AsyTask_HttpGetJXRecord mHttpGetData_JXRecord;
	private Context mContext;
	private TitlePopup titlePopup;
	private TextView tv_title, tv_aeTitle, tv_avgVTitle, tv_DJWDTitle, tv_FZBTitle, tv_YHLNDTitle;
	private ImageView iv_wifi, ivShare;
	private SlideShowView bannerView;
	private int[] NormPotS = { 0, 0, 0, 0, 0, 0 }; // ��������������
	private int[] AeCnt = { 0, 0, 0, 0, 0, 0 }; // ����ЧӦ����
	private double[] AvgVSum = { 0, 0, 0, 0, 0, 0 }; // ����ƽ����ѹ�ܺ�
	private int[] DJWDSum = { 0, 0, 0, 0, 0, 0 }; // ��������¶��ܺ�
	private double[] FZBSum = { 0, 0, 0, 0, 0, 0 }; // �������ӱ��ܺ�
	private double[] YHLNDSum = { 0, 0, 0, 0, 0, 0 }; // ����������Ũ���ܺ�

	private String AeCnt_url = ":8000/scgy/android/odbcPhP/AeCnt_area_date.php"; // ЧӦ����
	protected String AvgVArea_url = ":8000/scgy/android/odbcPhP/GetAvgV_dayTable.php";
	protected String DJWD_url = ":8000/scgy/android/odbcPhP/GetDJWD_MeasueTable.php";
	private List<AeRecord> listBean_AeCnt = null; // ЧӦ�����б�
	private List<AvgV> listBean_AvgV = null; // �ձ������б�
	private List<DJWD> listBean_DJWD = null; // ����¶��б�

	private String get_NormPots1_url = ":8000/scgy/android/odbcPhP/GetNormPots1.php";
	private String get_NormPots2_url = ":8000/scgy/android/odbcPhP/GetNormPots2.php";

	private List<PotCtrl> NormPotsList1 = null; // һ������״̬ �б�
	private List<PotCtrl> NormPotsList2 = null; // ��������״̬ �б�

	private BarChart mBarChart_AE, mBarChart_V, mBarChart_DJWD, mBarChart_FZB, mBarChart_YHLND;
	private Handler handler = new Handler(Looper.getMainLooper());
	private int GetDateCnt = 0, GetJXCnt = 0;
	private View mView;

	private boolean CalcPots1 = false, CalcPots2 = false;
	private TextView tvPotTotal, tvPot1, tvPot11, tvPot12, tvPot13, tvPot2, tvPot21, tvPot22, tvPot23;

	private ImageView iv_Fresh_Pots, iv_Fresh_Ae, iv_Fresh_AvgV, iv_Fresh_DJWD, iv_Fresh_FZB, iv_Fresh_YHLND;
	protected MyProgressDialog dialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.dismiss();
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_home, container, false);
		Log.i("fragment", "create home fragment view");
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		init(); // ��ʼ�����ؼ�
		NetDetector netDetector = new NetDetector(mContext);
		if (netDetector.isConnectingToInternetNoShow() == 1) {
			iv_wifi.setVisibility(View.GONE);
			bannerView.setVisibility(View.VISIBLE);// wifi
		} else {
			iv_wifi.setVisibility(View.VISIBLE);
			bannerView.setVisibility(View.GONE); // no wifi
		}
		if (NetStatus() != 0) {
			if (!initdate(mContext)) { // ȡԶ�̷�������ַ�Ͷ˿�
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);// û������Զ�̷�����ip�Ͷ˿�

			} else {
				get_dateTable_url = "http://" + ip + get_dateTable_url;
				get_JXName_url = "http://" + ip + get_JXName_url;
				get_NormPots1_url = "http://" + ip + get_NormPots1_url;
				get_NormPots2_url = "http://" + ip + get_NormPots2_url;
				AeCnt_url = "http://" + ip + AeCnt_url;
				AvgVArea_url = "http://" + ip + AvgVArea_url;
				DJWD_url= "http://" + ip + DJWD_url;
				checkUpDate(); // ���汾����
				init_GetDate(); // ��ȡ����
				init_GetJXRecord(); // ��ȡ������¼
				GetAllData_ShowChart(true); // ��ȡ���в����ݺ󣬲���ִ�С��ĵ�һ�ߡ��������ݣ�����ʾ5��ͼ��

			}

		} else {
			Toast.makeText(mContext, "�����쳣��", Toast.LENGTH_LONG).show();
		}

	}

	private void initDATA_YHLND() {

	}

	private void initDATA_FZB() {

	}

	private void initDATA_DJWD() {
		ExecutorService exec_DJWD = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				System.out.println("��ȡ��������¶�����OK����ʼ��ʾ����ͼ������happyȥ");
				DJWDSum_Clear();
				CalcDJWDSUM(listBean_DJWD);
				ShowBar_DJWD();// ��ʾ��������¶���״ͼ
			}
		});

		// ��ȡ����¶�
		exec_DJWD.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd mm:ss");
				final String todayValue = sdf.format(dt);
				final String today=sdfLong.format(dt);
				/*
				 * TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				 * Calendar cal = Calendar.getInstance(); cal.add(Calendar.DATE,
				 * -1); SimpleDateFormat sdf = new
				 * SimpleDateFormat("yyyy-MM-dd"); final String yesterdayValue =
				 * sdf.format(cal.getTime());
				 */
				handler.post(new Runnable() {
					@Override
					public void run() {
						tv_DJWDTitle.setText("����¶ȣ�" + today);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // ȫ���ۺ�
				mparams.add(new BasicNameValuePair("BeginDate", todayValue));
				mparams.add(new BasicNameValuePair("EndDate", todayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(DJWD_url, "POST", mparams);
				if (json != null) {
					// Log.d("����������¶�", json.toString());// �ӷ���������������
					System.out.println("��ȡ��������¶�OK������������");
					listBean_DJWD = new ArrayList<DJWD>(); // ��ʼ������¶�������
					listBean_DJWD = JsonToBean_Area_Date.JsonArrayToDJWDBean(json.toString());

				} else {
					// �ٴ�get����¶�����
					json = jsonParser.makeHttpRequest(DJWD_url, "POST", mparams);
					if (json != null) {
						// Log.d("����������¶�", json.toString());// �ӷ���������������
						listBean_DJWD = new ArrayList<DJWD>(); // ��ʼ������¶�������
						listBean_DJWD = JsonToBean_Area_Date.JsonArrayToDJWDBean(json.toString());
					} else {
						Log.i("����������¶�", "��PHP�����������ݷ��أ�");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// "δ��ȡЧӦ����,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
								Toast.makeText(mContext, "δ��ȡ������¶���Ϣ�����ܻ�û�����룡", Toast.LENGTH_SHORT).show();

							}
						});
					}
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

		exec_DJWD.shutdown();
	}

	// ��ʾ����¶�ͼ��
	protected void ShowBar_DJWD() {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�����¶�
		ArrayList<String> xVals = new ArrayList<>();// X������
		int DJWDTotal1, DJWDTotal2, DJWDTotal; // һ�������������������ܵ���¶Ⱥ�
		int PotS1, PotS2, PotS;
		DJWDTotal1 = DJWDSum[0] + DJWDSum[1] + DJWDSum[2];// һ��������¶��ܺ�
		DJWDTotal2 = DJWDSum[3] + DJWDSum[4] + DJWDSum[5];// ����������¶��ܺ�
		DJWDTotal = DJWDTotal1 + DJWDTotal2;// ����¶��ܺ�
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ
		if (NormPotS[0] != 0) {
			xVals.add("һ��1��");
			yVals.add(new BarEntry((float) DJWDSum[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("һ��2��");
			yVals.add(new BarEntry((float) DJWDSum[1] / NormPotS[1], 1));
		}
		if (NormPotS[2] != 0) {
			xVals.add("һ��3��");
			yVals.add(new BarEntry((float) DJWDSum[2] / NormPotS[2], 2));
		}
		if (PotS1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) DJWDTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("����1��");
			yVals.add(new BarEntry((float) DJWDSum[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("����2��");
			yVals.add(new BarEntry((float) DJWDSum[4] / NormPotS[4], 5));
		}
		if (NormPotS[5] != 0) {
			xVals.add("����3��");
			yVals.add(new BarEntry((float) DJWDSum[5] / NormPotS[5], 6));
		}
		if (PotS2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) DJWDTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) DJWDTotal / PotS, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "����¶�");
		barDataSet.setColor(Color.rgb(255,215, 0));// ����ǳ�ƺ�ɫ��ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(10f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.0");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChart_DJWD.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChart_DJWD.invalidate();
			}
		});

	}

	protected void CalcDJWDSUM(List<DJWD> listBean) {
		if (listBean != null && listBean.size() != 0) {
			DJWD mTable = new DJWD();
			for (int i = 0; i < listBean.size(); i++) {
				mTable = listBean.get(i);
				if (mTable != null) {
					String mPot = mTable.getPotNo();
					String mDjwd = mTable.getDJWD();
					int potno = 0;
					int Djwd = 0;
					if (!(mPot == null || mPot.length() <= 0)) {
						potno = Integer.parseInt(mTable.getPotNo()); // �ۺ�
					}
					if (!(mDjwd == null || mDjwd.length() <= 0)) {
						Djwd = Integer.parseInt(mDjwd); // ����¶�
					}
					if (potno >= 1101 && potno <= 1136) {
						DJWDSum[0] = DJWDSum[0] + Djwd; // һ����һ������¶��ܺ�
					} else if (potno >= 1201 && potno <= 1237) {
						DJWDSum[1] = DJWDSum[1] + Djwd;
					} else if (potno >= 1301 && potno <= 1337) {
						DJWDSum[2] = DJWDSum[2] + Djwd;
					} else if (potno >= 2101 && potno <= 2136) {
						DJWDSum[3] = DJWDSum[3] + Djwd; // ������һ������¶��ܺ�
					} else if (potno >= 2201 && potno <= 2237) {
						DJWDSum[4] = DJWDSum[4] + Djwd;
					} else if (potno >= 2301 && potno <= 2337) {
						DJWDSum[5] = DJWDSum[5] + Djwd;
					}

				} else {
					System.out.println("�� " + i + " �� ����¶ȣ�Ϊ�գ�");
				}
			}
		}

	}

	protected void DJWDSum_Clear() {
		for (int i = 0; i < DJWDSum.length; i++) {
			DJWDSum[i] = 0;
		}

	}

	private void initDATA_AvgV() {
		ExecutorService exec_AvgV = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				System.out.println("��ȡ����ƽ����ѹ����OK����ʼ��ʾ����ͼ������happyȥ");
				AvgVSum_Clear();
				CalcAvgVSUM(listBean_AvgV);
				ShowBar_AvgV();// ��ʾ����ƽ����ѹ��״ͼ
			}
		});

		// ��ȡƽ����ѹ
		exec_AvgV.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String yesterdayValue = sdf.format(cal.getTime());
				handler.post(new Runnable() {
					@Override
					public void run() {
						tv_avgVTitle.setText("ƽ����ѹ��" + yesterdayValue);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // ȫ���ۺ�
				mparams.add(new BasicNameValuePair("BeginDate", yesterdayValue));
				mparams.add(new BasicNameValuePair("EndDate", yesterdayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(AvgVArea_url, "POST", mparams);
				if (json != null) {
					Log.d("������ƽ����ѹ", json.toString());// �ӷ���������������
					System.out.println("��ȡ����ƽ����ѹOK������������");
					listBean_AvgV = new ArrayList<AvgV>(); // ��ʼ��ЧӦ����������
					listBean_AvgV = JsonToBean_Area_Date.JsonArrayToAvgVDayTableBean(json.toString());

				} else {
					// �ٴ�getƽ����ѹ����
					json = jsonParser.makeHttpRequest(AvgVArea_url, "POST", mparams);
					if (json != null) {
						Log.d("������ƽ����ѹ", json.toString());// �ӷ���������������
						listBean_AvgV = new ArrayList<AvgV>(); // ��ʼ��ЧӦ����������
						listBean_AvgV = JsonToBean_Area_Date.JsonArrayToAvgVDayTableBean(json.toString());
					} else {
						Log.i("������ƽ����ѹ ---", "��PHP�����������ݷ��أ�");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// "δ��ȡЧӦ����,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
								Toast.makeText(mContext, "δ��ȡ������ƽ����ѹ������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT).show();

							}
						});
					}
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

		exec_AvgV.shutdown();
	}

	protected void AvgVSum_Clear() {
		for (int i = 0; i < AvgVSum.length; i++) {
			AvgVSum[i] = 0;
		}

	}

	protected void ShowBar_AvgV() {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�ЧӦϵ��
		ArrayList<String> xVals = new ArrayList<>();// X������
		double AvgVTotal1, AvgVTotal2, AvgVTotal; // һ��������������������ƽ����ѹ��
		int PotS1, PotS2, PotS;
		AvgVTotal1 = AvgVSum[0] + AvgVSum[1] + AvgVSum[2];// һ����ƽ����ѹ�ܺ�
		AvgVTotal2 = AvgVSum[3] + AvgVSum[4] + AvgVSum[5];// ������ƽ����ѹ�ܺ�
		AvgVTotal = AvgVTotal1 + AvgVTotal2;// ����ƽ����ѹ�ܺ�
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ
		if (NormPotS[0] != 0) {
			xVals.add("һ��1��");
			yVals.add(new BarEntry((float) AvgVSum[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("һ��2��");
			yVals.add(new BarEntry((float) AvgVSum[1] / NormPotS[1], 1));
		}
		if (NormPotS[2] != 0) {
			xVals.add("һ��3��");
			yVals.add(new BarEntry((float) AvgVSum[2] / NormPotS[2], 2));
		}
		if (PotS1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) AvgVTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("����1��");
			yVals.add(new BarEntry((float) AvgVSum[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("����2��");
			yVals.add(new BarEntry((float) AvgVSum[4] / NormPotS[4], 5));
		}
		if (NormPotS[5] != 0) {
			xVals.add("����3��");
			yVals.add(new BarEntry((float) AvgVSum[5] / NormPotS[5], 6));
		}
		if (PotS2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AvgVTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AvgVTotal / PotS, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "��ƽ����ѹ");
		barDataSet.setColor(Color.rgb(75, 92, 196));// �������ݱ�����ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.000");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChart_V.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChart_V.invalidate();
			}
		});

	}

	protected void CalcAvgVSUM(List<AvgV> listBean) {
		if (listBean != null && listBean.size() != 0) {
			AvgV mTable = new AvgV();
			for (int i = 0; i < listBean.size(); i++) {
				mTable = listBean.get(i);
				if (mTable != null) {
					int potno = mTable.getPotNo(); // �ۺ�
					double avgV = mTable.getAverageV();// ƽ����ѹ
					if (potno >= 1101 && potno <= 1136) {
						AvgVSum[0] = AvgVSum[0] + avgV; // һ����һ��ƽ����ѹ�ܺ�
					} else if (potno >= 1201 && potno <= 1237) {
						AvgVSum[1] = AvgVSum[1] + avgV;
					} else if (potno >= 1301 && potno <= 1337) {
						AvgVSum[2] = AvgVSum[2] + avgV;
					} else if (potno >= 2101 && potno <= 2136) {
						AvgVSum[3] = AvgVSum[3] + avgV; // ������һ��ƽ����ѹ�ܺ�
					} else if (potno >= 2201 && potno <= 2237) {
						AvgVSum[4] = AvgVSum[4] + avgV;
					} else if (potno >= 2301 && potno <= 2337) {
						AvgVSum[5] = AvgVSum[5] + avgV;
					}

				} else {
					System.out.println("�� " + i + " �� ƽ����ѹ��Ϊ�գ�");
				}
			}
		}

	}

	/*
	 * private void init_GetCommData() { // ִ�д�Զ�̻���������� if (date_table == null)
	 * { mhttpgetdata_date = (AsyTask_HttpGetDate) new
	 * AsyTask_HttpGetDate(get_dateTable_url, this, mContext) .execute(); } if
	 * (JXList == null) { mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new
	 * AsyTask_HttpGetJXRecord(get_JXName_url, this, mContext).execute(); //
	 * ִ�д�Զ�̻�ý�����¼���� } }
	 */

	private void SetNormalPot() {
		tvPot11.setText(NormPotS[0] + "");
		tvPot12.setText(NormPotS[1] + "");
		tvPot13.setText(NormPotS[2] + "");
		tvPot1.setText(NormPotS[0] + NormPotS[1] + NormPotS[2] + "");
		tvPot21.setText(NormPotS[3] + "");
		tvPot22.setText(NormPotS[4] + "");
		tvPot23.setText(NormPotS[5] + "");
		tvPot2.setText(NormPotS[3] + NormPotS[4] + NormPotS[5] + "");
		tvPotTotal.setText(NormPotS[0] + NormPotS[1] + NormPotS[2] + NormPotS[3] + NormPotS[4] + NormPotS[5] + "̨");
	}

	private void initDATA_AE() {
		ExecutorService exec = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				System.out.println("��ȡ����ЧӦϵ������OK����ʼ��ʾ����ͼ������happyȥ");
				AeCnt_Clear();
				CalcAeCnt(listBean_AeCnt);
				ShowBar_AE();// ��ʾ����ЧӦ��״ͼ
			}
		});
		// ��ȡЧӦ����
		exec.execute(new Runnable() {
			@Override
			public void run() {
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
					System.out.println("��ȡ����ЧӦ����OK������������");
					listBean_AeCnt = new ArrayList<AeRecord>(); // ��ʼ��ЧӦ����������
					listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());

				} else {
					// �ٴ�getЧӦ��������
					json = jsonParser.makeHttpRequest(AeCnt_url, "POST", mparams);
					if (json != null) {
						Log.d("������ЧӦ����", json.toString());// �ӷ���������������
						listBean_AeCnt = new ArrayList<AeRecord>(); // ��ʼ��ЧӦ����������
						listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());
					} else {
						Log.i("������ЧӦ���� ---", "��PHP�����������ݷ��أ�");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// tv_title.setTextSize(14);
								// tv_title.setText("����վ:" +
								// "δ��ȡЧӦ����,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
								Toast.makeText(mContext, "û�л�ȡ������ЧӦ����������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT).show();

							}
						});
					}
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

	protected void AeCnt_Clear() {
		for (int i = 0; i < AeCnt.length; i++) {
			AeCnt[i] = 0;
		}

	}

	// ��ȡ����������������
	protected Boolean GetNormalPots2() {
		HttpPost_JsonArray jsonParser2 = new HttpPost_JsonArray();
		JSONArray json2 = jsonParser2.makeHttpRequest(get_NormPots2_url, "POST");
		if (json2 != null) {
			// Log.d("������������������", json2.toString());// �ӷ���������������
			NormPotsList2 = new ArrayList<PotCtrl>();
			NormPotsList2 = JsonToBean_GetPublicData.JsonArrayToNormPots(json2.toString());
			return true;
		} else {
			return false;
		}
	}

	// ��ȡһ��������������
	protected Boolean GetNormalPots1() {
		HttpPost_JsonArray jsonParser1 = new HttpPost_JsonArray();
		JSONArray json1 = jsonParser1.makeHttpRequest(get_NormPots1_url, "POST");
		if (json1 != null) {
			// Log.d("һ����������������", json1.toString());// �ӷ���������������
			NormPotsList1 = new ArrayList<PotCtrl>();
			NormPotsList1 = JsonToBean_GetPublicData.JsonArrayToNormPots(json1.toString());
			return true;
		} else {
			return false;
		}
	}

	protected void ShowBar_AE() {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�ЧӦϵ��
		ArrayList<String> xVals = new ArrayList<>();// X������
		int AeTotal1, AeTotal2, AeTotal, PotS1, PotS2, PotS;
		AeTotal1 = AeCnt[0] + AeCnt[1] + AeCnt[2];// һ����ЧӦ����
		AeTotal2 = AeCnt[3] + AeCnt[4] + AeCnt[5];// ������ЧӦ����
		AeTotal = AeTotal1 + AeTotal2;// ����ЧӦ����
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ

		if (NormPotS[0] != 0) {
			xVals.add("һ��1��");
			yVals.add(new BarEntry((float) AeCnt[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("һ��2��");
			yVals.add(new BarEntry((float) AeCnt[1] / NormPotS[1], 1));
		}
		if (NormPotS[2] != 0) {
			xVals.add("һ��3��");
			yVals.add(new BarEntry((float) AeCnt[2] / NormPotS[2], 2));
		}
		if (PotS1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) AeTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("����1��");
			yVals.add(new BarEntry((float) AeCnt[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("����2��");
			yVals.add(new BarEntry((float) AeCnt[4] / NormPotS[4], 5));
		}
		if (NormPotS[5] != 0) {
			xVals.add("����3��");
			yVals.add(new BarEntry((float) AeCnt[5] / NormPotS[5], 6));
		}
		if (PotS2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AeTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AeTotal / PotS, 8));
		}
		String lable = "������  " + "<һ��1��" + NormPotS[0] + "> <һ��2��" + NormPotS[1] + "> <һ��3��" + NormPotS[2];
		lable = lable + "> <һ�� " + PotS1 + "> <����1��" + NormPotS[3] + "> <����2��" + NormPotS[4] + "> <����3��" + NormPotS[5]
				+ "> <���� " + PotS2 + ">  <���� " + PotS + ">";
		BarDataSet barDataSet = new BarDataSet(yVals, lable);
		barDataSet.setColor(Color.rgb(190, 0, 47));// ����������ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(13f);
		barDataSet.setBarSpacePercent(55f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		mBarChart_AE.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChart_AE.invalidate();
			}
		});

	}

	// ����ЧӦ�����ܺ�
	protected void CalcAeCnt(List<AeRecord> listBean_AeCnt) {
		if (listBean_AeCnt != null && listBean_AeCnt.size() != 0) {
			AeRecord aeCnt = new AeRecord();
			for (int i = 0; i < listBean_AeCnt.size(); i++) {
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
	}

	// ͳ������������
	protected void CalcPotsNorm(List<PotCtrl> normPotsList, int Room) {
		if (normPotsList != null && (normPotsList.size() != 0)) {
			for (int i = 0; i < normPotsList.size(); i++) {
				PotCtrl mPotCtrl = new PotCtrl();
				mPotCtrl = normPotsList.get(i);
				if (mPotCtrl != null) {
					int potno = mPotCtrl.getPotNo(); // �ۺ�
					int mCtrl = mPotCtrl.getCtrls(); // ��״̬��
					// �����λ�������ƣ�00���� ������01 ����Ԥ�ȣ�10����������11����ͣ��
					if ((mCtrl & 0x03) == 0) {
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
					System.out.println("�ۿ���������ΪNULL!");
				}
			}
			if (Room == 1) {
				CalcPots1 = true;
			}
			if (Room == 2) {
				CalcPots2 = true;
			}
		}

	}

	private void NormPots_clear() {
		for (int i = 0; i < NormPotS.length; i++) {
			NormPotS[i] = 0;
		}
	}

	private void checkUpDate() {
		UpdateManager manager = new UpdateManager(mContext, false);
		manager.checkUpdate();
	}

	private int NetStatus() {
		NetDetector netDetector = new NetDetector(mContext);
		return netDetector.isConnectingToInternet();

	}

	private void init_GetDate() {
		GetDateCnt++;
		if (date_table == null) {
			// ִ�д�Զ�̻����������
			AsyTask_HttpGetDate mhttpgetdata_date = (AsyTask_HttpGetDate) new AsyTask_HttpGetDate(get_dateTable_url,
					this, mContext).execute();
		}

	}

	private void init_GetJXRecord() {
		GetJXCnt++;
		if (JXList == null) {
			AsyTask_HttpGetJXRecord mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new AsyTask_HttpGetJXRecord(
					get_JXName_url, this, mContext).execute(); // ִ�д�Զ�̻�ý�����¼����
		}

	}

	private void init() {
		dialog = MyProgressDialog.createDialog(mContext);
		init_normalPot(); // ��ʼ�������������ؼ�
		init_button();// ��ʼ����ť�ؼ�
		ivShare = (ImageView) mView.findViewById(R.id.iv_share);
		ivShare.setOnClickListener(this);
		titlePopup = new TitlePopup(mContext, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Popup_initData();
		btnMore = (Button) mView.findViewById(R.id.btn_more);
		btnMore.setVisibility(View.VISIBLE);
		btnMore.setOnClickListener(this);
		tv_title = (TextView) mView.findViewById(R.id.tv_title);
		iv_wifi = (ImageView) mView.findViewById(R.id.iv_NoWiFi);
		bannerView = (com.hewaiming.ALWorkInfo.banner.SlideShowView) mView.findViewById(R.id.bannerwView);
		// 5��ͼ���ʼ��
		mBarChart_AE = (BarChart) mView.findViewById(R.id.Ae_chart);
		mBarChart_V = (BarChart) mView.findViewById(R.id.AvgV_chart);
		mBarChart_DJWD = (BarChart) mView.findViewById(R.id.DJWD_chart);
		mBarChart_FZB = (BarChart) mView.findViewById(R.id.FZB_chart);
		mBarChart_YHLND = (BarChart) mView.findViewById(R.id.YHLND_chart);
		// ͼ������ʼ��
		tv_aeTitle = (TextView) mView.findViewById(R.id.tv_AeTitle);
		tv_avgVTitle = (TextView) mView.findViewById(R.id.tv_AvgV);
		tv_DJWDTitle = (TextView) mView.findViewById(R.id.tv_DJWD);
		tv_FZBTitle = (TextView) mView.findViewById(R.id.tv_FZB);
		tv_YHLNDTitle = (TextView) mView.findViewById(R.id.tv_YHLND);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String todayValue = sdf.format(dt);
		tv_aeTitle.setText("ЧӦϵ����" + todayValue);
		init_BarCHART(mBarChart_AE, 0);
		init_BarCHART(mBarChart_V, 1);
		init_BarCHART(mBarChart_DJWD, 2);
		init_BarCHART(mBarChart_FZB, 3);
		init_BarCHART(mBarChart_YHLND, 4);
	}

	private void init_button() {
		iv_Fresh_Pots = (ImageView) mView.findViewById(R.id.iv_refresh_pots);
		iv_Fresh_Pots.setOnClickListener(this);
		// iv_Fresh_Pots.setVisibility(View.GONE);
		iv_Fresh_Ae = (ImageView) mView.findViewById(R.id.iv_refresh_ae);
		iv_Fresh_Ae.setOnClickListener(this);
		iv_Fresh_AvgV = (ImageView) mView.findViewById(R.id.iv_refresh_avgv);
		iv_Fresh_AvgV.setOnClickListener(this);
		iv_Fresh_DJWD = (ImageView) mView.findViewById(R.id.iv_refresh_djwd);
		iv_Fresh_DJWD.setOnClickListener(this);
		iv_Fresh_FZB = (ImageView) mView.findViewById(R.id.iv_refresh_fzb);
		iv_Fresh_FZB.setOnClickListener(this);
		iv_Fresh_YHLND = (ImageView) mView.findViewById(R.id.iv_refresh_yhlnd);
		iv_Fresh_YHLND.setOnClickListener(this);

	}

	// ��ȡ�����������󣬲��ܽ��л�ȡ�ĵ�һ�߹��ղ������ݣ�����ͼ����ʽ��ʾ
	private void GetAllData_ShowChart(boolean All_Charts) {
		NormPots_clear(); //
		final CountDownLatch latch = new CountDownLatch(2);// �������˵�Э��
		// ��ȡһ��������������
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				boolean FoundData1 = false;
				do {
					if (GetNormalPots1()) {
						FoundData1 = true;
						break;
					}
					i++;
				} while (i < 3);
				if (!FoundData1) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "δ��ȡ��һ�������������������粻����������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				latch.countDown();
			}
		}).start();
		// ��ȡ����������������
		new Thread(new Runnable() {
			public void run() {
				int j = 0;
				boolean FoundData2 = false;
				do {
					if (GetNormalPots2()) {
						FoundData2 = true;
						break;
					}
					j++;
				} while (j < 3);
				if (!FoundData2) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "δ��ȡ�����������������������粻����������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				latch.countDown();
			}
		}).start();

		try {
			latch.await();// �ȴ����й�����ɹ���

			CalcPotsNorm(NormPotsList1, 1); // �ֱ�ͳ��һ������������
			CalcPotsNorm(NormPotsList2, 2);// �ֱ�ͳ�ƶ�������������
			if (CalcPots1 && CalcPots2) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						SetNormalPot(); // ��ʾ����������
						iv_Fresh_Pots.setVisibility(View.GONE);
					}
				});
			}
			if (All_Charts) {
				initDATA_AE();// ��ʾ��ǰ����ЧӦϵ��
				initDATA_AvgV();// ��ʾ��ǰ����ƽ����ѹ
				initDATA_DJWD();// ��ʾ��ǰ��������¶�
				initDATA_FZB();// ��ʾ��ǰ�������ӱ�
				initDATA_YHLND();// ��ʾ��ǰ����������Ũ��
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void init_normalPot() {
		tvPotTotal = (TextView) mView.findViewById(R.id.tv_potTotal);
		tvPot1 = (TextView) mView.findViewById(R.id.tv_Room1_sum);
		tvPot11 = (TextView) mView.findViewById(R.id.tv_Room11_sum);
		tvPot12 = (TextView) mView.findViewById(R.id.tv_Room12_sum);
		tvPot13 = (TextView) mView.findViewById(R.id.tv_Room13_sum);
		tvPot2 = (TextView) mView.findViewById(R.id.tv_Room2_sum);
		tvPot21 = (TextView) mView.findViewById(R.id.tv_Room21_sum);
		tvPot22 = (TextView) mView.findViewById(R.id.tv_Room22_sum);
		tvPot23 = (TextView) mView.findViewById(R.id.tv_Room23_sum);

	}

	private void init_BarCHART(BarChart mChart, int TYPE) {
		// ͼ����ʾ����
		// mBarChart.setTouchEnabled();
		mChart.setScaleEnabled(false); // ���Ŵ�

		mChart.getLegend().setEnabled(false);
		mChart.getLegend().setPosition(LegendPosition.BELOW_CHART_LEFT);// ����ע���λ�������Ϸ�
		mChart.getLegend().setForm(LegendForm.CIRCLE);// ���������ʾСͼ�����״
		mChart.getLegend().setWordWrapEnabled(true);
		mChart.getLegend().setTextSize(2.8f);

		mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// ����X���λ��
		mChart.getXAxis().setDrawGridLines(false);// ����ʾ����
		mChart.getXAxis().setDrawAxisLine(false);
		mChart.getXAxis().setTextSize(8f);
		// mChart.getXAxis().setLabelRotationAngle(90f);
		mChart.getXAxis().setTextColor(Color.DKGRAY);

		mChart.getAxisRight().setEnabled(false);// �Ҳ಻��ʾY��
		mChart.getAxisLeft().setEnabled(false);
		mChart.getAxisLeft().setDrawLabels(false); // ���Y���겻��ʾ���ݿ̶�
		mChart.getAxisLeft().setAxisMinValue(0.0f);// ����Y����ʾ��Сֵ����Ȼ0������п�϶
		// mBarChart.getAxisLeft().setAxisMaxValue(2.0f);// ����Y����ʾ���ֵ
		mChart.getAxisLeft().setDrawGridLines(true);// ������Y������

		mChart.setNoDataTextDescription("û�л�ȡ�����ͼ������");
		mChart.setDescription("");

		if (TYPE == 0) {
			mChart.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					if (checkGlobalData()) {
						Intent aemost_intent = new Intent(getActivity(), AeMostActivity.class);
						Bundle aemostBundle = new Bundle();
						if (date_record != null) {
							aemostBundle.putStringArrayList("date_record", (ArrayList<String>) date_record);
						}
						if (JXList != null) {
							aemostBundle.putSerializable("JXList", (Serializable) JXList);
						}
						if (date_record != null && JXList != null) {
							aemostBundle.putString("ip", ip);
							aemostBundle.putInt("port", port);
							aemost_intent.putExtras(aemostBundle);
							startActivity(aemost_intent);
							// ЧӦ��
						}
					}
					return true;
				}
			});
		}
		if (TYPE == 1) {

		}
		if (TYPE == 2) {

		}
		if (TYPE == 3) {

		}
		if (TYPE == 4) {

		}
	}

	protected Boolean checkGlobalData() {
		if (GetJXCnt > 3) {
			tv_title.setTextSize(14);
			tv_title.setText("����վ:" + "����̫��������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			return false;
		} else {
			Toast.makeText(mContext, "��" + GetJXCnt + " �γ��Ի�ȡ������¼����", Toast.LENGTH_SHORT).show();
			init_GetJXRecord();
		}

		if (GetDateCnt > 3) {
			tv_title.setTextSize(14);
			tv_title.setText("����վ:" + "����̫��������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			return false;
		} else {
			Toast.makeText(mContext, "��" + GetDateCnt + " �γ��Ի�ȡ��������", Toast.LENGTH_SHORT).show();
			init_GetDate();
		}
		return true;
	}

	private void Popup_initData() {
		// �������������������
		titlePopup.addAction(new ActionItem(mContext, "����Զ�̷�����", R.drawable.settings));
		titlePopup.addAction(new ActionItem(mContext, "����", R.drawable.mm_title_btn_keyboard_normal));
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
			// Toast.makeText(getApplicationContext(),
			// "û�л�ȡ��[����]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();
			// tv_title.setTextSize(14);
			// tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");

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
			// tv_title.setTextSize(14);
			// tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			// Toast.makeText(getApplicationContext(),
			// "û�л�ȡ��[������]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();

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
		case R.id.iv_share:
			MyConst.showShare(mContext); // һ������
			break;
		case R.id.iv_refresh_pots:
			dialog.setMessage("��������������в�����...");
			if (!dialog.isShowing()) {
				dialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			GetAllData_ShowChart(false);
			break;
		case R.id.iv_refresh_ae:
			dialog.setMessage("�����������ЧӦϵ��...");
			if (!dialog.isShowing()) {
				dialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}

			initDATA_AE();// ��ʾ��ǰ����ЧӦϵ��
			break;
		case R.id.iv_refresh_avgv:
			dialog.setMessage("�����������ƽ����ѹ...");
			if (!dialog.isShowing()) {
				dialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_AvgV();// ��ʾ��ǰ����ƽ����ѹ
			break;
		case R.id.iv_refresh_djwd:
			dialog.setMessage("���������������¶�...");
			if (!dialog.isShowing()) {
				dialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_DJWD();// ��ʾ��ǰ��������¶�
			break;	
		}
	}

}
