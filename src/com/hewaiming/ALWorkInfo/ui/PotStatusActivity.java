package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_DayTableAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_PotStatusAdapter;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.DemoBase;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.socket.SocketTransceiver;
import com.hewaiming.ALWorkInfo.socket.TcpClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import bean.PotStatus;
import bean.RealTime;
import bean.RequestAction;

public class PotStatusActivity extends DemoBase implements OnScrollListener, OnClickListener {
	private String ip;
	private int port;
	private Context ctx;
	private SharedPreferences sp;
	private Spinner spinner_area;
	private Button backBtn;
	// private ImageButton isShowingBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter;

	private String PotNo, BeginDate, EndDate;
	private List<PotStatus> listBean = new ArrayList<PotStatus>();

	private RelativeLayout mHead;
	private ListView lv_PotStatus;
	private LinearLayout showArea = null;
	// private View layout_PotStatus;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private Context mContext;
	protected HSView_PotStatusAdapter PotStatus_Adapter = null;
	private Timer timer = null;
	private TimerTask timerTask = null;

	private Handler handler = new Handler(Looper.getMainLooper());

	private TcpClient client = new TcpClient() {

		@Override
		public void onConnect(SocketTransceiver transceiver) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					tv_title.setText("��״̬��������Զ�̷������ɹ���");					
				}
			});

		}

		@Override
		public void onConnectFailed() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					tv_title.setText("��״̬��������Զ�̷�����ʧ�ܣ�");
					Toast.makeText(PotStatusActivity.this, "����SOCKETʧ�ܣ�������Զ�̷�����IP�����߼�������Ƿ�������", Toast.LENGTH_SHORT).show();
				}
			});

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, String s) {

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, RealTime realTime) {

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, final ArrayList<PotStatus> potStatus) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					listBean = new ArrayList<PotStatus>(potStatus);
					if (listBean != null) {
						if (listBean.size() > 0) {
							// listBean.clear(); // ���LISTVIEW ��ǰ������
							PotStatus_Adapter.onDateChange(listBean);
						}
					}
				}
			});
		}

		@Override
		public void onDisconnect(SocketTransceiver transceiver) {

		}

	};

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_potstatus);
		ctx = this;
		initdate();// ��ȡ������ip
		// dateBean = getIntent().getStringArrayListExtra("date_table");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayValue = sdf.format(dt);
		BeginDate = todayValue;
		EndDate = todayValue;
		mContext = this;
		init_area();
		init_title();
		init_HSView();
		init_listview();
		PotStatus_Adapter = new HSView_PotStatusAdapter(mContext, R.layout.item_hsview_potstatus, listBean, mHead);
		lv_PotStatus.setAdapter(PotStatus_Adapter);
		connect();
		timer = new Timer();
		SendActionToServer();
	}

	public void initdate() {
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		ip = sp.getString("ipstr", ip);
		port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
		if(ip==""){
			Toast.makeText(ctx, "������Զ�̷�����IP", 1).show();
		}
		if(sp.getString("port", String.valueOf(port)) == null){
			Toast.makeText(ctx, "������Զ�̷������˿�", 1).show();
		}
		// MyLog.i(TAG, "��ȡ��ip�˿�:" + ip + ";" + port);
	}

	private void init_listview() {
		lv_PotStatus = (ListView) findViewById(R.id.lv_PotStatus);
		lv_PotStatus.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_PotStatus.setCacheColorHint(0);
		lv_PotStatus.setOnScrollListener(this);
		lv_PotStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PotNo = String.valueOf(listBean.get(position).getPotNo());
				// Toast.makeText(getApplicationContext(), PotNo, 1).show();
				Intent potv_intent = new Intent(PotStatusActivity.this, ShowPotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putString("PotNo", PotNo);
				potv_bundle.putString("Begin_Date", BeginDate);
				potv_bundle.putString("End_Date", EndDate);
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // ��ѹ����ͼ

			}
		});

	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head); // ��ͷ����
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

	}

	private void init_title() {
		// layout_daytable = findViewById(R.id.Layout_daytable);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("��״̬��");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
		// isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		// showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		// isShowingBtn.setOnClickListener(this);

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// ���������б��ķ��
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ���ӵ�spinner��
		spinner_area.setAdapter(Area_adapter);
		spinner_area.setVisibility(View.VISIBLE);
		spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (timerTask != null) {
					timerTask.cancel();
				}
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
				SendActionToServer();//
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			if (timer != null) {
				timer.cancel();
			}
			client.disconnect();
			finish();
			break;
		/*
		 * case R.id.btn_isSHOW: if (showArea.getVisibility() == View.GONE) {
		 * showArea.setVisibility(View.VISIBLE);
		 * isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.
		 * btn_up)); } else { showArea.setVisibility(View.GONE);
		 * isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.
		 * btn_down)); } break;
		 */
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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

	private void connect() {
		if (client.isConnected()) {
			// �Ͽ�����
			client.disconnect();
		} else {
			try {
				client.connect(ip, port);
			} catch (NumberFormatException e) {
				Toast.makeText(this, "�˿ڴ���", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
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
						sendPotStatusAction();
					}
				});

			}
		};
		timer.schedule(timerTask, 0, 3000);

	}

	private void sendPotStatusAction() {
		try {
			RequestAction action = new RequestAction();
			action.setActionId(2);
			action.setPotNo_Area(String.valueOf(areaId));
			client.getTransceiver().send(action);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}